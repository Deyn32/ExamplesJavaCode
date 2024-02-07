package ru.nvacenter.bis.npa.services.dao;

import org.hibernate.engine.jdbc.WrappedClob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.*;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.npa.domain.dao.*;
import ru.nvacenter.bis.npa.domain.dto.NPARevisionCompareNode;
import ru.nvacenter.bis.npa.domain.dto.doca.NPA_AUD_ForCopy;
import ru.nvacenter.bis.npa.domain.dto.doca.NPA_AUD_Res;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPANode;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPA_AUD_ContentNode;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPA_AUD_Structure;
import ru.nvacenter.bis.npa.domain.dto.revisions.NPA_AUD_RevNpaData;
import ru.nvacenter.bis.npa.domain.dto.revisions.NPA_AUD_Rev_Data;
import ru.nvacenter.bis.npa.services.dto.NPALinearRecursiveService;
import ru.nvacenter.bis.npa.services.dto.NPARevisionCompareService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by oshesternikova on 23.01.2017.
 * Для переноса НПА из "Доки"
 */

@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class FZDocumentCopyService {

    public FZDocumentCopyService(FZDocumentService fzDocumentService, DocumentService documentService,
                                 NPALinearRecursiveService npaLinearRecursiveService,
                                 NVA_SPR_AUD_NPAService nva_SPR_AUD_NPAService,
                                 NPARevisionCompareService npaRevisionCompareService) {

        this.fzDocumentService = fzDocumentService;
        this.documentService = documentService;
        this.npaLinearRecursiveService = npaLinearRecursiveService;
        this.nva_SPR_AUD_NPAService = nva_SPR_AUD_NPAService;
        this.npaRevisionCompareService = npaRevisionCompareService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FZDocumentCopyService.class);
    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;

    private final FZDocumentService fzDocumentService;

    private final DocumentService documentService;

    private final NPALinearRecursiveService npaLinearRecursiveService;

    private final NVA_SPR_AUD_NPAService nva_SPR_AUD_NPAService;

    private final NPARevisionCompareService npaRevisionCompareService;

    public NPARevisionCompareNode merged(Long docId, Optional<Long> revId1, Optional<Long> revId2) {
        List<NVA_SPR_AUD_NPA_STRUCTURE> str1 = fzDocumentService.getListStructure(docId.toString(), revId1, Optional.empty());
        List<NVA_SPR_AUD_NPA_STRUCTURE> str2 = fzDocumentService.getListStructure(docId.toString(), revId2, Optional.empty());
        NPANode rev1 = npaLinearRecursiveService.ToRecursive(str1);
        NPANode rev2 = npaLinearRecursiveService.ToRecursive(str2);
        return npaRevisionCompareService.mergeRoot(rev1, rev2, false);
    }

    public List<NVA_SPR_AUD_Element_Type> getElementTypes(){
        List<NVA_SPR_AUD_Element_Type> elems;
        TypedQuery<NVA_SPR_AUD_Element_Type> query = entityManager.createQuery(
                "from NVA_SPR_AUD_Element_Type i where i.isDeleted = false"
                , NVA_SPR_AUD_Element_Type.class);
        elems = query.getResultList();
        return  elems;
    }

    public void updateElem(String name){
        NVA_SPR_AUD_Element_Type elem = new NVA_SPR_AUD_Element_Type();
        elem.setName(name);
        elem.setDate_begin(LocalDate.now());
        elem.setDate_modify(LocalDate.now());
        elem.setIsDeleted(false);
        elem.setAux(false);
        elem.setDeclensions("");
        elem.setDuplicates("");
        entityManager.persist(elem);
        entityManager.flush();
    }

    private List<NVA_SPR_AUD_NPA_Revision> getAllNPARevisions(){
        TypedQuery<NVA_SPR_AUD_NPA_Revision> query = entityManager.createQuery(
                "from NVA_SPR_AUD_NPA_Revision i where i.isDeleted = false"
                , NVA_SPR_AUD_NPA_Revision.class);
        return query.getResultList();
    }

    /* Проверка на существование в БД документа по номеру и дате */
    public NPA_AUD_Res checkIfExists(String num, LocalDate dt){
        //Есть ли в базе НПА
        TypedQuery<NVA_SPR_AUD_NPA> allDocsQ = entityManager.createQuery("from NVA_SPR_AUD_NPA i where i.number = :qnum and i.date = :qdate", NVA_SPR_AUD_NPA.class);
        allDocsQ.setParameter("qnum", num);
        allDocsQ.setParameter("qdate", dt);
        List<NVA_SPR_AUD_NPA> allDocs = allDocsQ.getResultList();
        List<Document> docaDocs = new ArrayList<>();
        //если ничего не нашли, ищем в базе ДОКИ
        List<String> err = new ArrayList<>();
        if (allDocs.isEmpty()){
            List<Document> docs = documentService.findList(num, dt);
            if (docs.isEmpty()) {
                return new NPA_AUD_Res(new ArrayList<>(), new ArrayList<>(Arrays.asList("Нет документов для копирования")), new ArrayList<>());
            }
            else{
                return new NPA_AUD_Res(new ArrayList<>(), new ArrayList<>(Arrays.asList("Нет документов в справочнике НПА, но есть документы для копирования в БД ДОКа")), docs);
            }
        }
        else{
            //Проверяем, заполнены ли уже и привязан ли к доке
            List<NVA_SPR_AUD_NPA> removes = new ArrayList<>();
            for(NVA_SPR_AUD_NPA ad : allDocs){
                Query countStr = entityManager
                        .createQuery("select count(*) from NVA_SPR_AUD_NPA_STRUCTURE i where i.id_SprAudNPA = :nid");
                countStr.setParameter("nid", ad.getId());
                Long numRecs = (Long)countStr.getSingleResult();
                if (numRecs > 0){
                    removes.add(ad);
                    err.add(String.format("Документ %1$s от %2$s уже скопирован", ad.getNumber(), ad.getDate()));
                }
                else {
                    if (ad.getPrevId() == null) {
                        List<Document> d = documentService.findList(ad);
                        if (d.size() == 0){
                            err.add(String.format("Нет данных в базе ДОКА для %1$s %2$s", ad.getNumber(), ad.getDate()));
                        }
                        else {
                            docaDocs.addAll(d);
                        }
                    }
                }
            }
            for(NVA_SPR_AUD_NPA id : removes) {
                allDocs.remove(id);
            }
        }

        return new NPA_AUD_Res(allDocs, err, docaDocs);
    }

    /* Создать объект БД на основе документа из БД Дока */
    private NVA_SPR_AUD_NPA convert(Document doc, String num){
        NVA_SPR_AUD_NPA item = new NVA_SPR_AUD_NPA();
        item.setName(doc.getCaption());
        item.setDate(doc.getDateLawAsDate());
        item.setNumber(num);
        item.setType(tryFindDocumentType(doc));
        item.setPrevId(doc.getId());
        return  item;
    }

    /* Найти по номеру и сконвертировать, если есть что */
    public List<String> tryFindAndConvert(Long id, String idDoc, Optional<Long> idRev){

        NVA_SPR_AUD_NPA npa = nva_SPR_AUD_NPAService.find(id);

        Document doca = documentService.find(idDoc);
        if (doca == null){
            List<String> result = new ArrayList<>();
            result.add(String.format("Документ %1$s от %2$s не найден документ БД \"Дока\"", npa.getNumber(), npa.getDate()));
            return result;
        }

        npa.setPrevId(doca.getId());
        entityManager.flush();

        deletePrevData(npa, idRev);
        return convertList(npa, idRev);
    }

    public List<String> convertList(NVA_SPR_AUD_NPA doc, Optional<Long> idRev){
        List<String> result = new ArrayList<>();
        Document doca = documentService.find(doc.getPrevId());
        if (doca == null){
            result.add(String.format("Документ %1$s от %2$s не найден документ БД \"Дока\"", doc.getNumber(), doc.getDate()));
            return result;
        }
        List<DocumentStructure> dstr = documentService.findStructureList(doca);
        if (dstr.size() == 0){
            result.add("Нет данных в структуре документа");
            return result;
        }
        List<NVA_SPR_AUD_NPA_STRUCTURE> str = convert(dstr, doc.getId(), idRev);
        int index = 0;
        for(NVA_SPR_AUD_NPA_STRUCTURE str1 : str){
            result.addAll(validate(str1, dstr.get(index)));
            index++;
        }
        if (result.size() > 0) return result;
        for(NVA_SPR_AUD_NPA_STRUCTURE str1 : str){
            entityManager.persist(str1);
        }
        try {
            entityManager.flush();
        }
        catch (Exception ex){
            String baseMsg =String.format("Ошибка копирования структуры документа %1 (%2)", doc.getId(), doca.getId());
            result.add(baseMsg+" : "+ex.getMessage());
            LOGGER.error(baseMsg, ex);
        }
        return result;
    }

    private void deletePrevData(NVA_SPR_AUD_NPA doc, Optional<Long> idRev){
        Query q2 = null;
        if (idRev.isPresent()){
            q2 = entityManager.createQuery("update NVA_SPR_AUD_NPA_STRUCTURE n set n.deleted = :dt where n.id_SprAudNPAStr = :rev and n.id_SprAudNPA = :npa");
            q2.setParameter("rev", idRev.get());
        }
        else{
            q2 = entityManager.createQuery("update NVA_SPR_AUD_NPA_STRUCTURE n set n.deleted = :dt where n.id_SprAudNPAStr is null and n.id_SprAudNPA = :npa");
        }

        q2.setParameter("npa", doc.getId());
        q2.setParameter("dt", LocalDate.now());
        q2.executeUpdate();
    }

    private List<String> validate(NVA_SPR_AUD_NPA obj, Document doc){
        List<String> ex = new ArrayList<>();
        if (obj.getType() == null) ex.add(String.format("Не определен тип документа %1$s", doc.getCaption()));
        return ex;
    }

    private List<String> validate(NVA_SPR_AUD_NPA_STRUCTURE obj, DocumentStructure dstr){
        List<String> ex = new ArrayList<>();
        if (obj.getElementType() == null) ex.add(String.format("Не определен тип элемента %1$s", dstr.getElemType()));
        return ex;
    }

    /* Сконвертировать набор из элементов структуры */
    private List<NVA_SPR_AUD_NPA_STRUCTURE> convert(List<DocumentStructure> lstrs, Long parentId, Optional<Long> idRev){
        List<NVA_SPR_AUD_NPA_STRUCTURE> lst = new ArrayList<>(lstrs.size());
        List<NVA_SPR_AUD_Element_Type> elems = getElementTypes();
        for(DocumentStructure dstr : lstrs){
            NVA_SPR_AUD_NPA_STRUCTURE strs = convert(dstr, elems);
            strs.setId_SprAudNPA(parentId);
            if (idRev.isPresent())
                strs.setId_SprAudNPAStr(idRev.get());
            if (strs.getElementType() != null){

                lst.add(strs);
            }
        }
        return lst;
    }

    /* Конвертация элемента документа */
    private NVA_SPR_AUD_NPA_STRUCTURE convert(DocumentStructure dstr, List<NVA_SPR_AUD_Element_Type> elems){
        NVA_SPR_AUD_NPA_STRUCTURE item = new NVA_SPR_AUD_NPA_STRUCTURE();
        item.setId(dstr.getId());
        item.setFk_parentElementId(dstr.getFkParentElemId());
        item.setText(dstr.getText());
        item.setElementNumber(dstr.getElemNumber());
        item.setElementOrder(dstr.getElemOrder().longValue());
        item.setElementLevel(dstr.getElemLevel());
        String elemtype = tryFindElementName(dstr, elems);
        item.setElementType(elemtype);
        item.setElementOriginalType(elemtype);
        item.setVersion(0);
        item.setUpdated(LocalDate.now());
        return item;
    }

    /* Получить имя типа элемента */
    private String tryFindElementName(DocumentStructure dstr, List<NVA_SPR_AUD_Element_Type> elems){
        String dstrname = ToNormalizeString(dstr.getElemType());

        for(NVA_SPR_AUD_Element_Type elem : elems){
            String elemNameNorm = ToNormalizeString(elem.getName());
            if (CheckInclusion(dstrname, elemNameNorm)){
                if (CheckIfSpare(dstr, elem))
                    return null;
                return elem.getName();
            }

            String[] elemdupls = elem.getDuplicates();
            for(String elemdupl : elemdupls){
                if (CheckInclusion(dstrname, ToNormalizeString(elemdupl))){
                    if (CheckIfSpare(dstr, elem))
                        return null;
                    return elem.getName();
                }
            }
        }
        return null;
    }

    //Проверка, что не нужно копировать
    private Boolean CheckIfSpare(DocumentStructure dstr, NVA_SPR_AUD_Element_Type el){
        return dstr.getFkParentElemId() == null && el.isAux();
    }

    /* Получить тип документа */
    private NVA_SPR_AUD_Document_Type tryFindDocumentType(Document doc){
        //находим типы документов, к которым можем отнести наш импортируемый документ
        String slawName = ToNormalizeString(doc.getLawName());
        String scap = ToNormalizeString(doc.getCaption());

        List<NVA_SPR_AUD_Document_Type> doctypes = fzDocumentService.getListDocTypes();
        List<NVA_SPR_AUD_Document_Type> hypdoctypes = new ArrayList<>();
        //находим по включению ключевого наименования
        for(NVA_SPR_AUD_Document_Type dtype : doctypes) {
            String docTypeName = ToNormalizeString(dtype.getName());
            if (CheckInclusion(slawName, docTypeName))
                hypdoctypes.add(dtype);
            else if (CheckInclusion(scap, docTypeName))
                hypdoctypes.add(dtype);
        }
        //если несколько вариантов - ищем более частное (пр. Правила и Межотраслевые правила)
        if (hypdoctypes.size() > 1) {
            List<NVA_SPR_AUD_Document_Type> sparehypdoctypes = new ArrayList<>();
            for(NVA_SPR_AUD_Document_Type dtype : hypdoctypes){
                String dtypenorm = ToNormalizeString(dtype.getName());
                for(NVA_SPR_AUD_Document_Type dtypech : hypdoctypes){
                    String dtypechnorm = ToNormalizeString(dtypech.getName());
                    if (dtype != dtypech && CheckInclusion(dtypechnorm, dtypenorm)){
                        sparehypdoctypes.add(dtype);
                        break;
                    }
                }
            }
            for(NVA_SPR_AUD_Document_Type dtype : sparehypdoctypes){
                hypdoctypes.remove(dtype);
            }
        }
        else if (hypdoctypes.size() == 0){
            //Проверка на возможные дублирующие названия
            for(NVA_SPR_AUD_Document_Type dt : doctypes){
                if (dt.getDuplicates() != null){
                    String[] duples = dt.getDuplicates().split(";");
                    for(String duple : duples){
                        if (CheckInclusion(slawName, ToNormalizeString(duple))){
                            return dt;
                        }
                        if (CheckInclusion(scap, ToNormalizeString(duple))){
                            return dt;
                        }
                    }
                }
            }
        }
        //Берем первый
        if (hypdoctypes.size() > 0) return  hypdoctypes.get(0);
        return  null;
    }

    //Вынести в отдельный сервис?
    /* Нормализовать строку */
    private static String ToNormalizeString(String str){
        if (str == null) return null;
        String[] parts = str.trim().split("\\s+");
        return String.join(" ", parts).toUpperCase();
    }

    /* Проверить включение подстроки в строку */
    private static boolean CheckInclusion(String main, String sub){
        return main.indexOf(sub) >= 0;
    }

    /* Список НПА для копирования */
    public List<NPA_AUD_ForCopy> findListForCopy(){

        List<NPA_AUD_ForCopy> res = new ArrayList<>();
        String rawSql = "SELECT ID as id, Num as num, DATE_NPA as dt, Name_Full as name, ID_Document_Type, cc, IsEditing as edit FROM dbo.NVA_SPR_AUD_NPA\n" +
                "LEFT JOIN \n" +
                "(SELECT [Id_SprAudNPA] as idS, count(*) as cc from dbo.NVA_SPR_AUD_NPA_STRUCTURE\n" +
                "GROUP BY [Id_SprAudNPA]) as t1\n" +
                "ON ID = idS\n" +
                "WHERE Is_Deleted = 'false' AND Num is not null AND DATE_NPA is not null";
        Query query =  entityManager.createNativeQuery(rawSql);
        List res2 = query.getResultList();

        List<NVA_SPR_AUD_Document_Type> docTypes = fzDocumentService.getListDocTypes();
        for(Object resDoc : res2){
            Object[] arr = (Object[])resDoc;
            String name = null;
            if (arr[3] != null){
                if (arr[3] instanceof String){
                    name = (String)arr[3];
                }
                else {
                    try {
                        Clob clob = ((WrappedClob) arr[3]).getWrappedClob();
                        name = clob.getSubString(1, (int) clob.length());
                    } catch (SQLException ex) {
                        LOGGER.trace("Конвертация из БД: НПА (Full_Name)", ex);
                    }
                }
            }
            NVA_SPR_AUD_Document_Type elDocType = null;
            if (arr[4] != null){
                Long idDoc = ((BigInteger)arr[4]).longValue();
                elDocType = docTypes.stream()
                        .filter(dt -> dt.getId() == idDoc)
                        .findFirst()
                        .orElse(null);
            }

            res.add(new NPA_AUD_ForCopy(((BigInteger)arr[0]).longValue(), (String)arr[1], ((Timestamp)arr[2]).toLocalDateTime().toLocalDate(),  name, elDocType, arr[5] != null, (Boolean) arr[6]));
        }

        for(NPA_AUD_ForCopy npa : res){
            List<Document> docs = documentService.findList(npa.getNum(), npa.getDt());
            npa.setDocs(docs);
        }

        return res;
    }

    /* Список НПА с данными */
    public List<NPA_AUD_Rev_Data> findList(){
        List<NPA_AUD_Rev_Data> res = new ArrayList<>();

        String rawSql = "SELECT n.id, n.Num, n.DATE_NPA, n.Name_Full, n.ID_Out_NPA, n.id_Parent, n.Is_Deleted, n.ID_Document_Type FROM NVA_SPR_AUD_NPA n\n" +
                "LEFT JOIN \n" +
                "(SELECT nn.Id_SprAudNPA as idS, count(*) as cc from NVA_SPR_AUD_NPA_STRUCTURE nn\n" +
                "GROUP BY nn.Id_SprAudNPA) as t1\n" +
                "ON n.id = t1.idS\n" +
                "WHERE t1.cc is not NULL";
        Query query =  entityManager.createNativeQuery(rawSql, NVA_SPR_AUD_NPA.class);
        List<NVA_SPR_AUD_NPA> res2 = query.getResultList();
        List<NVA_SPR_AUD_NPA_Revision> allRevs = getAllNPARevisions();
        Map<Long, List<NVA_SPR_AUD_NPA_Revision>> groups = allRevs.stream().collect(Collectors.groupingBy(rev -> rev.getId_NPA()));

        for(NVA_SPR_AUD_NPA resDoc : res2){
            if (groups.containsKey(resDoc.getId()))
            {
                List<NVA_SPR_AUD_NPA_Revision> revsid = groups.get(resDoc.getId());
                res.add(new NPA_AUD_Rev_Data(resDoc, revsid.toArray(new NVA_SPR_AUD_NPA_Revision[revsid.size()])));
            }
            else
                res.add(new NPA_AUD_Rev_Data(resDoc));
        }

        return res;
    }

    public List<NPA_AUD_RevNpaData> findRevisions(Long id){
        TypedQuery<NVA_SPR_AUD_NPA_Revision> q = entityManager.createQuery("from NVA_SPR_AUD_NPA_Revision r where r.isDeleted = 0 and r.id_NPA = :npa", NVA_SPR_AUD_NPA_Revision.class);
        q.setParameter("npa", id);
        List<NVA_SPR_AUD_NPA_Revision> res = q.getResultList();
        Query q2 = entityManager.createQuery("select distinct s.id_SprAudNPAStr from NVA_SPR_AUD_NPA_STRUCTURE s where s.id_SprAudNPA=:npa");
        q2.setParameter("npa", id);
        List l = q2.getResultList();
        List<Long> usedRevs = new ArrayList<>();
        for(Object o : l){
            usedRevs.add((Long)o);
        }
        List<NPA_AUD_RevNpaData> revs = res.stream().map(r -> new NPA_AUD_RevNpaData(r, usedRevs.contains(r.getId()))).collect(Collectors.toList()) ;
        if (usedRevs.contains(null)){
            NVA_SPR_AUD_NPA_Revision rev = new NVA_SPR_AUD_NPA_Revision();
            revs.add(new NPA_AUD_RevNpaData(rev, true));
        }
        revs = revs.stream().sorted(new Comparator<NPA_AUD_RevNpaData>() {
            @Override
            public int compare(NPA_AUD_RevNpaData r1, NPA_AUD_RevNpaData r2) {
                if (r1.getRev().getDtRev() == null && r2.getRev().getDtRev() == null)
                    return 0;
                if (r1.getRev().getDtRev() == null) return 1;
                if (r2.getRev().getDtRev() == null) return -1;
                return r2.getRev().getDtRev().compareTo(r1.getRev().getDtRev());
            }
        }).collect(Collectors.toList());
        return revs;
    }

    public List<NPA_AUD_ContentNode> getTreeStructure(String idDocument, Optional<Long> revId, Optional<LocalDate> dtRev){
        List<NVA_SPR_AUD_NPA_STRUCTURE> strs = fzDocumentService.getListStructure(idDocument, revId, dtRev);
        return npaLinearRecursiveService.ToRecursiveView(strs);
    }

    /* Текст пункта */
    public String getTreeStructureText(String id, Long documentId, Optional<Long> revisionId){
        Query q = null;
        if (revisionId.isPresent()){
            q =entityManager.createQuery("select n.text from NVA_SPR_AUD_NPA_STRUCTURE n where n.id = :id and n.deleted is null and n.id_SprAudNPA = :npa and n.id_SprAudNPAStr = :rev");
            q.setParameter("rev", revisionId.get());
        }
        else{
            q =entityManager.createQuery("select n.text from NVA_SPR_AUD_NPA_STRUCTURE n where n.id = :id and n.deleted is null and n.id_SprAudNPA = :npa and n.id_SprAudNPAStr is null");
        }

        q.setParameter("id", id);
        q.setParameter("npa", documentId);
        List lst = q.getResultList();
        return (String)lst.get(0);
    }

    public void updateDocStructureDates(NPA_AUD_Structure str, String id, Long documentId, Optional<Long> revisionId){
        String baseQuery = "update NVA_SPR_AUD_NPA_STRUCTURE n set n.elementType = :typ, n.dateBegin = :dtBegin, n.dateEnd = :dtEnd where n.id = :id and n.id_SprAudNPA = :npa";
        if (revisionId.isPresent()){
            baseQuery += " and n.id_SprAudNPAStr = :rev";
        }else{
            baseQuery += " and n.id_SprAudNPAStr is null";
        }
        Query q = entityManager.createQuery(baseQuery);
        q.setParameter("id", str.getId());
        q.setParameter("typ", str.getType());
        q.setParameter("dtBegin", str.getDtBegin());
        q.setParameter("dtEnd", str.getDtEnd());
        q.setParameter("npa", documentId);
        if (revisionId.isPresent()){
            q.setParameter("rev", revisionId.get());
        }
        q.executeUpdate();
    }

    public HttpServletResponse initResponse(String fileName, HttpServletResponse resp) throws Exception {
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        resp.setContentType(mimeType);

        resp.addHeader("Content-Disposition",
                // если pdf то попробует открыть средствами просмотра браузера
                "inline; "
                        // в этом параметре только в кодировке ISO-8859-1!
                        + "filename=\"" + fileName + "\"; "
                        // для IE 8 (filename*=UTF-8'') не работает
                        // Современные браузеры должны поддерживать https://tools.ietf.org/html/rfc6266#section-4.3
                        + "filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8").replace("+", "%20"));
        return resp;

    }

}
