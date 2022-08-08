package ru.nvacenter.bis.auditviolationnpa.services.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.UniqueRealStructureElement;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.UniqueStructureElement;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.FailedResult;
import ru.nvacenter.bis.auditviolationnpa.models.Path;
import ru.nvacenter.bis.auditviolationnpa.models.PathElement;
import ru.nvacenter.bis.auditviolationnpa.services.dao.UniqueStructureService;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by oshesternikova on 28.02.2018.
 * Сервис работы с уникальными ссылками
 */
@Service
public class UniqueCreateStructureService {

    @Inject
    FZDocumentService fzDocumentService;

    @Inject
    UniqueStructureService uniqueStructureService;
    /**
     * Найти уникальные ссылки и сопоставить им реальные
     * @param idStr ID любой структуры
     * @param idRev ID редакции
     * @param idNPA ID НПА
     *              @return Ошибки обработки
     */
    public List<FailedResult> createUniqueStructureElement(String idStr, Optional<Long> idRev, Long idNPA){

        List<FailedResult> res = new ArrayList<>();
        if (!idRev.isPresent()){
            Long revProb = uniqueStructureService.tryFindRevision(idNPA, idStr);
            if (revProb != null){
                idRev = Optional.of(revProb);
                res.add(new FailedResult(idNPA, revProb, "В ссылке нарушения неверно указана пустая редакция"));
            }

        }

        //Данные по редакции НПА
        List<NVA_SPR_AUD_NPA_STRUCTURE> str1 = fzDocumentService.getListStructure(idNPA.toString(), idRev.isPresent() ? idRev : Optional.of((long)0), Optional.empty());
        if (str1.size() == 0){
            return res;
        }
        //Сам элемент
        NVA_SPR_AUD_NPA_STRUCTURE str = str1.stream()
                .filter(s -> StringUtils.equals(s.getId(), idStr))
                .findFirst()
                .orElse(null);

        if (str == null){
            res.add(new FailedResult(idNPA, null, "Не найдена ссылка в редакции"));
            return res;
        }

        //Есть ли первоначальная редакция у которой нет ID редакции
        boolean hasRevEmpty = fzDocumentService.checkNullRevision(idNPA);
        //"Путь" к элементу структуры в дереве редакции
        Path path = findPath(idStr, str1);
        if (path.isFailed())//Вообще странный момент??? (Для совместимости с DEVELOP)
            return res;

        //Все редакции НПА
        List<NVA_SPR_AUD_NPA_Revision> revs = fzDocumentService.findRevisionByNpa(idNPA);
        List<Long> allRevIds = revs.stream().map(r -> r.getId()).collect(Collectors.toList());
        //Если есть пустая редакция
        if (hasRevEmpty /*&& (idRev.isPresent() || el != null )*/){
            allRevIds.add(null);
        }

        //Находим все "классы эквивалентности" (уже найденные одинаковые положения)
        Map<UniqueStructureElement, List<UniqueRealStructureElement>> eqsm = uniqueStructureService.findEquivalentClasses(idStr, idRev, idNPA);
        Map<UniqueStructureElement, NVA_SPR_AUD_NPA_STRUCTURE> examples = new HashMap<>();
        //Для каждого вытаскиваем пример (с которым будем сравнивать)
        for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> eq: eqsm.entrySet()) {
            UniqueRealStructureElement real = eq.getValue()
                    .stream()
                    .filter(f -> f.getIdRealStructure() != null)
                    .findFirst()
                    .orElse(null);
            if (real != null)
            {
                NVA_SPR_AUD_NPA_STRUCTURE strm = fzDocumentService.getStructureElement(idNPA, real.getIdRev(), real.getIdRealStructure());
                examples.put(eq.getKey(), strm);
            }
            else
                examples.put(eq.getKey(), null);
        }

        //Список добавляемых к существующим
        Map<UniqueStructureElement, List<NVA_SPR_AUD_NPA_STRUCTURE>> addNewStructures = new HashMap<>();
        //список совсем новых
        List<List<NVA_SPR_AUD_NPA_STRUCTURE>> newEqualsStructures = new ArrayList<>();


        //UniqueStructureElement el = uniqueStructureService.find(idStr, idRev, idNPA);
        //List<UniqueRealStructureElement> equivalents = uniqueStructureService.findEquivalentExamples(el);

        //Уже занесенные элементы
        //List<UniqueRealStructureElement> reals = new ArrayList<>();
        //List<Long> usedRevs = new ArrayList<>();
        //if (el != null){
        //    reals = uniqueStructureService.findLinks(el);
        //    usedRevs = reals.stream().map(r -> r.getIdRev()).collect(Collectors.toList());
        //}

        List<Long> usedRevs = new ArrayList<>();
        for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> eq: eqsm.entrySet()) {
            usedRevs.addAll(eq.getValue().stream()
                    .map(e -> e.getIdRev())
                    .collect(Collectors.toList()));
        }

        List<Long> failedRevs = new ArrayList<>();
        //Проверка по всем редакциям
        for(Long revId : allRevIds){
            boolean b = usedRevs.contains(revId);
            if (!b){
                //Если редакция ещё не обработана
                List<NVA_SPR_AUD_NPA_STRUCTURE> str2 = fzDocumentService.getListStructure(idNPA.toString(), revId == null ? Optional.of((long)0) : Optional.of(revId), Optional.empty());
                //Ищем такой же элемент (по пути в дереве)
                NVA_SPR_AUD_NPA_STRUCTURE eq = findByPath(path, str2);
                if (eq == null){
                    res.add(new FailedResult(idNPA, revId, "Не найден элемент"));
                    failedRevs.add(revId);
                    continue;
                }
                //Проверяем, что совпадает по тексту со всеми вариантами
                boolean hasAny = false;
                for(Map.Entry<UniqueStructureElement, NVA_SPR_AUD_NPA_STRUCTURE> m : examples.entrySet()){
                    NVA_SPR_AUD_NPA_STRUCTURE eq1 = m.getValue();
                    if (eq1 == null) continue;
                    List<NVA_SPR_AUD_NPA_STRUCTURE> str3 = fzDocumentService.getListStructure(idNPA.toString(), eq1.getId_SprAudNPAStr() == null ? Optional.of((long)0) : Optional.of(eq1.getId_SprAudNPAStr()), Optional.empty());

                    boolean eqs = compareRecursive(eq1, eq, str3, str2);
                    if (!eqs){
                        //res.add(new FailedResult(idNPA, revId, "Не совпадает текст"));
                        //failedRevs.add(revId);
                        continue;
                    }
                    //Есть ли уже такой же в добавленных
                    //UniqueRealStructureElement rs = m.getValue()
                    //        .stream()
                    //        .filter(r -> longEquals(r.getIdRev(), revId) && StringUtils.equals(r.getIdRealStructure(), eq.getId()) )
                    //        .findFirst()
                    //        .orElse(null);

                    //if (rs == null)

                    try
                    {
                        addNewStructures.get(m.getKey()).add(eq);
                    }
                    catch(NullPointerException ex)
                    {

                    }
                    hasAny = true;
                    break;

                }
                if (!hasAny){
                    for(List<NVA_SPR_AUD_NPA_STRUCTURE> l :  newEqualsStructures){
                        NVA_SPR_AUD_NPA_STRUCTURE eq1 = l.get(0);
                        List<NVA_SPR_AUD_NPA_STRUCTURE> str3 = fzDocumentService.getListStructure(idNPA.toString(), eq1.getId_SprAudNPAStr() == null ? Optional.of((long)0) : Optional.of(eq1.getId_SprAudNPAStr()), Optional.empty());
                        boolean eqs = compareRecursive(eq1, eq, str3, str2);
                        if (!eqs){
                            //res.add(new FailedResult(idNPA, revId, "Не совпадает текст"));
                            //failedRevs.add(revId);
                            continue;
                        }
                        l.add(eq);
                        hasAny = true;
                        break;
                    }
                    if (!hasAny){
                        List<NVA_SPR_AUD_NPA_STRUCTURE> newl = new ArrayList<>();
                        newl.add(eq);
                        newEqualsStructures.add(newl);
                    }
                }


            }
        }

        //Добавляем новые
        for(List<NVA_SPR_AUD_NPA_STRUCTURE> l :  newEqualsStructures){
            UniqueStructureElement el = uniqueStructureService.create(idNPA, path);
            addNewStructures.put(el, l);
        }

        for(Map.Entry<UniqueStructureElement, List<NVA_SPR_AUD_NPA_STRUCTURE>> m : addNewStructures.entrySet()){
            List<UniqueRealStructureElement> ls = uniqueStructureService.createLinks(m.getValue(), m.getKey());
            uniqueStructureService.saveLinks(ls);
        }

        //Добавляем случаи, когда не найдены положения
        if (failedRevs.size() > 0){
            UniqueStructureElement elFailed = null;
            for(Map.Entry<UniqueStructureElement, NVA_SPR_AUD_NPA_STRUCTURE> ex : examples.entrySet()){
                if (ex.getValue() == null){
                    elFailed = ex.getKey();
                    break;
                }
            }
            if (elFailed == null)
                elFailed = uniqueStructureService.create(idNPA, path);
            List<UniqueRealStructureElement> ls = uniqueStructureService.createEmptyLinks(failedRevs, elFailed);
            uniqueStructureService.saveLinks(ls);
        }


        return res;
    }


    /**
     * Найти в массиве по пути
     * @param path Список порядковых номеров на каждом уровне (путь)
     * @param strs Массив элементов, относящихся к редакции
     * @return
     */
    private NVA_SPR_AUD_NPA_STRUCTURE findByPath(Path path, List<NVA_SPR_AUD_NPA_STRUCTURE> strs) {
        return findByPathOrder(0, null, path, strs);
    }

    private NVA_SPR_AUD_NPA_STRUCTURE findByPathOrder(int index, String parentId, List<PathElement> path, List<NVA_SPR_AUD_NPA_STRUCTURE> strs){
        PathElement current = path.get(index);
        NVA_SPR_AUD_NPA_STRUCTURE leaf =  strs.stream()
                .filter(s ->  StringUtils.equals(s.getFk_parentElementId(), parentId) && current.Match(s))
                .findFirst()
                .orElse(null);
        if (index == path.size() - 1) return  leaf;
        if (leaf == null) return  null;
        return findByPathOrder(index + 1, leaf.getId(), path, strs);
    }

    /**
     * Составить путь элемента
     * @param idStr Элемент
     * @param strs Массив элементов, относящихся к редакции
     * @return
     */
    private Path findPath(String idStr, List<NVA_SPR_AUD_NPA_STRUCTURE> strs){
        Path path = new Path();
        findLevelOrder(idStr, strs, path);
        Collections.reverse(path);
        return path;
        /*
        int[] arr = new int[path.size()];
        for(int i = 0; i < path.size();i++){
            Long l = path.get(path.size() - i - 1);
            arr[i] = (int)l.longValue();
        }
        return arr;
        */
    }

    private void findLevelOrder(String idStr, List<NVA_SPR_AUD_NPA_STRUCTURE> strs, Path arr){
        NVA_SPR_AUD_NPA_STRUCTURE s = strs.stream().filter(ss -> ss.getId().equals(idStr))
                .findFirst()
                .orElse(null);
        if (s == null){
            arr.setFailed(true);
            return;
        }
        arr.add( new PathElement(s));
        if (s.getFk_parentElementId() != null)
            findLevelOrder(s.getFk_parentElementId(), strs, arr);
    }

    /**
     * Проверяем, совпдают ли найденные узлы по тексту и тексту своих дочерних узлов
     * @param el1
     * @param el2
     * @param str1
     * @param str2
     * @return
     */

    private boolean compareRecursive(NVA_SPR_AUD_NPA_STRUCTURE el1, NVA_SPR_AUD_NPA_STRUCTURE el2, List<NVA_SPR_AUD_NPA_STRUCTURE> str1, List<NVA_SPR_AUD_NPA_STRUCTURE> str2){
        boolean b = checkText(el1, el2);
        if (!b) return false;
        List<NVA_SPR_AUD_NPA_STRUCTURE> children1 = str1.stream()
                .filter(s -> StringUtils.equals(s.getFk_parentElementId(), el1.getId()))
                .sorted(customComparator)
                .collect(Collectors.toList());
        List<NVA_SPR_AUD_NPA_STRUCTURE> children2 = str2.stream()
                .filter(s -> StringUtils.equals(s.getFk_parentElementId(), el2.getId()))
                .sorted(customComparator)
                .collect(Collectors.toList());
        if (children1.size() != children2.size()) return false;
        if (children1.size() == 0) return true;
        for (int i = 0; i < children1.size(); i++){
            NVA_SPR_AUD_NPA_STRUCTURE ch1 = children1.get(i);
            NVA_SPR_AUD_NPA_STRUCTURE ch2 = children2.get(i);
            boolean b2 = compareRecursive(ch1, ch2, str1, str2);
            if (!b2) return false;
        }
        return true;
    }

    private boolean checkText(NVA_SPR_AUD_NPA_STRUCTURE el1, NVA_SPR_AUD_NPA_STRUCTURE el2){
        return StringUtils.equals(el1.getText(), el2.getText());
    }


    /**
     * Найти уникальные ссылки и сопоставить им реальные
     * @param idStr ID любой структуры
     * @param idRev ID редакции
     * @param idNPA ID НПА
     * @return Ошибки обработки
     */
    public List<FailedResult> createOrUpdateUniqueElements(String idStr, Long idRev, Long idNPA){

        List<FailedResult> res = new ArrayList<>();

        //Данные по редакции НПА
        List<NVA_SPR_AUD_NPA_STRUCTURE> str1 = fzDocumentService.getListStructure(idNPA.toString(), idRev == null ? Optional.of((long)0) : Optional.of(idRev), Optional.empty());
        if (str1.size() == 0){
            return res;
        }
        //Сам элемент
        NVA_SPR_AUD_NPA_STRUCTURE str = str1.stream()
                .filter(s -> StringUtils.equals(s.getId(), idStr))
                .findFirst()
                .orElse(null);

        if (str == null){
            res.add(new FailedResult(idNPA, null, "Не найдена ссылка в редакции"));
            return res;
        }

        //"Путь" к элементу структуры в дереве редакции
        Path path = findPath(idStr, str1);
        if (path.isFailed())//Вообще странный момент??? (Для совместимости с DEVELOP)
            return res;

        //Все редакции НПА
        List<NVA_SPR_AUD_NPA_Revision> revs = fzDocumentService.findRevisionByNpa(idNPA);
        List<Long> allCurrentRevIds = revs.stream().map(r -> r.getId()).collect(Collectors.toList());

        //Находим все "классы эквивалентности" (уже найденные одинаковые положения)
        Map<UniqueStructureElement, List<UniqueRealStructureElement>> eqsm = uniqueStructureService.findEquivalentClasses(idStr, Optional.of(idRev), idNPA);
        List<Long> allUsedRevIds = new ArrayList<>();
        for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> eq: eqsm.entrySet()) {
            allUsedRevIds.addAll(eq.getValue().stream().map(e -> e.getIdRev()).collect(Collectors.toList()));
        }

        //Новые (необработанные) редакции
        List<Long> newValues = findDifference(allCurrentRevIds, allUsedRevIds);
        //Удаленные редакции
        List<Long> removedValues = findDifference(allUsedRevIds, allCurrentRevIds);
        //Прежние редакции
        List<Long> oldValues = findDifference(allCurrentRevIds, newValues);

        //Проверяем, что не изменились оставшиеся редакции
        List<Long> removed = checkExistingRevisions(idNPA, oldValues, eqsm);
        newValues.addAll(removed);
        //Проверяем, что в тех редакциях, в которых ничего не найдено, ничего не найдено
        List<Long> changedNull = checkNullElemsRevisions(path, idNPA, eqsm);
        newValues.addAll(changedNull);
        //Добавить
        createForNewRevisions(path, idNPA, newValues, eqsm);
        //Удалить
        deleteForDeletedRevisions(idNPA, removedValues, eqsm);

        return res;
    }

    /**
     * Создать новые ссылки
     * @param path
     * @param idNPA
     * @param newRevisions
     * @param eqsm
     */
    private void createForNewRevisions(Path path, Long idNPA, List<Long> newRevisions, Map<UniqueStructureElement, List<UniqueRealStructureElement>> eqsm){

        //Список добавляемых к существующим
        Map<UniqueStructureElement, List<NVA_SPR_AUD_NPA_STRUCTURE>> addNewStructures = new HashMap<>();
        //Для каждого вытаскиваем пример (с которым будем сравнивать)
        Map<UniqueStructureElement, NVA_SPR_AUD_NPA_STRUCTURE> examples = new HashMap<>();

        for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> eq: eqsm.entrySet()) {
            addNewStructures.put(eq.getKey(), new ArrayList<>());
            Optional<UniqueRealStructureElement> real = eq.getValue()
                    .stream()
                    .filter(f -> f.getIdRealStructure() != null)
                    .findFirst();
            if (real.isPresent()){
                NVA_SPR_AUD_NPA_STRUCTURE strm = fzDocumentService.getStructureElement(eq.getKey().getNpa(), real.get().getIdRev(), real.get().getIdRealStructure());
                examples.put(eq.getKey(), strm);
            }
        }


        //Список для совсем новых
        List<List<NVA_SPR_AUD_NPA_STRUCTURE>> newEqualsStructures = new ArrayList<>();
        //Список редакций, где не найдены положения
        List<Long> revsNotFound = new ArrayList<>();

        for (Long revId :
                newRevisions) {
            //Если редакция ещё не обработана
            List<NVA_SPR_AUD_NPA_STRUCTURE> str2 = fzDocumentService.getListStructure(idNPA.toString(), revId == null ? Optional.of((long)0) : Optional.of(revId), Optional.empty());
            //Ищем такой же элемент (по пути в дереве)
            NVA_SPR_AUD_NPA_STRUCTURE eq = findByPath(path, str2);
            if (eq == null){
                revsNotFound.add(revId);
                continue;
            }
            //Проверяем, что совпадает по тексту со всеми вариантами
            boolean hasAny = false;
            for(Map.Entry<UniqueStructureElement, NVA_SPR_AUD_NPA_STRUCTURE> m : examples.entrySet()){
                NVA_SPR_AUD_NPA_STRUCTURE eq1 = m.getValue();
                if (eq1 == null) continue;
                List<NVA_SPR_AUD_NPA_STRUCTURE> str3 = fzDocumentService.getListStructure(idNPA.toString(), eq1.getId_SprAudNPAStr() == null ? Optional.of((long)0) : Optional.of(eq1.getId_SprAudNPAStr()), Optional.empty());
                boolean eqs = compareRecursive(eq1, eq, str3, str2);
                if (!eqs) continue;
                addNewStructures.get(m.getKey()).add(eq);
                hasAny = true;
                break;

            }
            if (!hasAny){
                for(List<NVA_SPR_AUD_NPA_STRUCTURE> l :  newEqualsStructures){
                    NVA_SPR_AUD_NPA_STRUCTURE eq1 = l.get(0);
                    List<NVA_SPR_AUD_NPA_STRUCTURE> str3 = fzDocumentService.getListStructure(idNPA.toString(), eq1.getId_SprAudNPAStr() == null ? Optional.of((long)0) : Optional.of(eq1.getId_SprAudNPAStr()), Optional.empty());
                    boolean eqs = compareRecursive(eq1, eq, str3, str2);
                    if (!eqs){
                        //res.add(new FailedResult(idNPA, revId, "Не совпадает текст"));
                        //failedRevs.add(revId);
                        continue;
                    }
                    l.add(eq);
                    hasAny = true;
                    break;
                }
                if (!hasAny){
                    List<NVA_SPR_AUD_NPA_STRUCTURE> newl = new ArrayList<>();
                    newl.add(eq);
                    newEqualsStructures.add(newl);
                }
            }
        }

        //Добавляем новые
        for(List<NVA_SPR_AUD_NPA_STRUCTURE> l :  newEqualsStructures){
            UniqueStructureElement el = uniqueStructureService.create(idNPA, path);
            addNewStructures.put(el, l);
            examples.put(el, l.get(0));
        }

        for(Map.Entry<UniqueStructureElement, List<NVA_SPR_AUD_NPA_STRUCTURE>> m : addNewStructures.entrySet()){
            List<UniqueRealStructureElement> ls = uniqueStructureService.createLinks(m.getValue(), m.getKey());
            uniqueStructureService.saveLinks(ls);
        }

        //Находим ключ, у записей, которые с пустыми положениями
        UniqueStructureElement elFailed = null;
        for(Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> eq: eqsm.entrySet()){
            boolean isEmpty = eq.getValue()
                    .stream()
                    .allMatch(e -> e.getIdRealStructure() == null);

            if (isEmpty){
                elFailed = eq.getKey();
                break;
            }
        }

        //Добавляем случаи, когда не найдены положения
        if (revsNotFound.size() > 0){

            if (elFailed == null)
                elFailed = uniqueStructureService.create(idNPA, path);
            List<UniqueRealStructureElement> ls = uniqueStructureService.createEmptyLinks(revsNotFound, elFailed);
            uniqueStructureService.saveLinks(ls);
        }

        //Проверяем предыдущие случаи, когда не найднены
        if (eqsm.containsKey(elFailed)){

        }

    }

    private void deleteForDeletedRevisions(Long idNPA, List<Long> deletedRevisions, Map<UniqueStructureElement, List<UniqueRealStructureElement>> eqsm){
        List<UniqueRealStructureElement> deleted = new ArrayList<>();
        for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> eq: eqsm.entrySet()) {
            deleted.addAll(eq.getValue()
                    .stream()
                    .filter(f -> deletedRevisions.contains(f.getIdRev()))
                    .collect(Collectors.toList()));
        }

        for (UniqueRealStructureElement del :
                deleted) {
            del.setDeleted(true);
        }

        uniqueStructureService.saveLinks(deleted);
    }

    private List<Long> checkExistingRevisions(Long idNPA, List<Long> prevRevisions, Map<UniqueStructureElement, List<UniqueRealStructureElement>> eqsm){

        List<UniqueRealStructureElement> rss = new ArrayList<>();
        for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> m :
                eqsm.entrySet()) {
            List<UniqueRealStructureElement> lst = m.getValue()
                    .stream()
                    .filter(e -> prevRevisions.contains(e.getIdRev()))
                    .collect(Collectors.toList());
            for (UniqueRealStructureElement el :
                    lst) {
                if (el.getIdRealStructure() != null){
                    boolean isDeleted = fzDocumentService.checkIfDeleted(el.getIdRealStructure(), el.getIdRev(), idNPA);
                    if (isDeleted){
                        rss.add(el);
                        m.getValue().remove(el);
                    }
                }
                else{
                }
            }
        }
        List<Long> removedRevs = new ArrayList<>();
        if (rss.size() > 0){
            uniqueStructureService.remove(rss);
            removedRevs.addAll(rss.stream().map(r -> r.getIdRev()).collect(Collectors.toList()));
        }

        return removedRevs;
    }

    private List<Long> checkNullElemsRevisions(Path path, Long idNPA, Map<UniqueStructureElement, List<UniqueRealStructureElement>> eqsm){
        UniqueStructureElement fEl = null;
        for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> m :
                eqsm.entrySet()) {
            boolean allEmpty = m.getValue()
                    .stream()
                    .allMatch(mm -> mm.getIdRealStructure() == null);
            if (allEmpty){
                fEl = m.getKey();
                break;
            }
        }
        List<Long> res = new ArrayList<>();
        if (fEl == null) return res;

        List<Long> nullRevisions = eqsm.get(fEl).stream().map(f -> f.getIdRev()).collect(Collectors.toList());
        for (Long revId :
                nullRevisions) {
            //Если редакция ещё не обработана
            List<NVA_SPR_AUD_NPA_STRUCTURE> str2 = fzDocumentService.getListStructure(idNPA.toString(), revId == null ? Optional.of((long)0) : Optional.of(revId), Optional.empty());
            //Ищем такой же элемент (по пути в дереве)
            NVA_SPR_AUD_NPA_STRUCTURE eq = findByPath(path, str2);
            if (eq != null) {
                eqsm.get(fEl).remove(revId);
                res.add(revId);
            }
        }
        return res;
    }

    /**
     * Разность двух множеств
     * @param arr1 Множество, из которого вычетаем
     * @param arr2 Множество, которое вычетаем
     * @return
     */
    private static List<Long> findDifference(List<Long> arr1, List<Long> arr2){
        List<Long> newValues = new ArrayList<>();
        for (Long l1 :
                arr1) {
            boolean b1 = false;
            for (Long l2 :
                    arr2) {
                if (longEquals(l1, l2)) {
                    b1 = true;
                    break;
                }
            }
            if (!b1) newValues.add(l1);
        }
        return newValues;
    }

    /**
     * Сравнение двух Long с учетом null
     * @param l1
     * @param l2
     * @return
     */
    private static boolean longEquals(Long l1, Long l2){
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        return Long.compare(l1, l2) == 0;
    }

    private final static  Comparator<NVA_SPR_AUD_NPA_STRUCTURE> customComparator = new Comparator<NVA_SPR_AUD_NPA_STRUCTURE>() {
        @Override
        public int compare(NVA_SPR_AUD_NPA_STRUCTURE o1, NVA_SPR_AUD_NPA_STRUCTURE o2) {
            return Long.compare(o1.getElementOrder(), o2.getElementOrder());
        }
    };

}

