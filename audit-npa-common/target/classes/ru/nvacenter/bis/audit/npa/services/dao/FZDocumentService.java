package ru.nvacenter.bis.audit.npa.services.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Оранизации
 * User: rgolovin
 */

@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class FZDocumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FZDocumentService.class);
    private static final String ERROR_MESSAGE = "Ошибка при выполнении запроса '%s'";

    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;

    @Autowired
    private NVA_SPR_AUD_RevisionService revisionService;

    public NVA_SPR_AUD_NPA find(Long id) {
        if (id == null)
            return null;

        NVA_SPR_AUD_NPA item = entityManager.find(NVA_SPR_AUD_NPA.class, id);
        return item;
    }

    public List<NVA_SPR_AUD_NPA> find(List<Long> ids){
        if (ids.size() == 0) return new ArrayList<>();
        TypedQuery<NVA_SPR_AUD_NPA> q = entityManager.createQuery("from NVA_SPR_AUD_NPA r where r.id in :ids", NVA_SPR_AUD_NPA.class);
        q.setParameter("ids", ids);
        return q.getResultList();
    }

    public List<Long> getHavingStructureIds() {
        String queryString = "SELECT npa.id from NVA_SPR_AUD_NPA npa WHERE npa.name IS NOT NULL AND npa.number IS NOT NULL AND npa.date IS NOT NULL";
        queryString += " AND EXISTS(SELECT 1 FROM NVA_SPR_AUD_NPA_STRUCTURE struct WHERE struct.id_SprAudNPA = npa.id)";

        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        try {
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(String.format(ERROR_MESSAGE, queryString));
            throw e;
        }
    }

    public List<NVA_SPR_AUD_NPA> getList(){
        List<NVA_SPR_AUD_NPA> docs = null;
        try {
            docs = entityManager.createQuery("from NVA_SPR_AUD_NPA i where i.name is not null and i.number is not null and i.date is not null", NVA_SPR_AUD_NPA.class).getResultList();
        }
        catch (Exception ex) {
            LOGGER.error(String.format(ERROR_MESSAGE, "from NVA_SPR_AUD_NPA i where i.name is not null and i.number is not null and i.date is not null"), ex);
            throw ex;
        }

        return  docs;
    }
    public List<NVA_SPR_AUD_NPA> getList(String docType){
        List<NVA_SPR_AUD_NPA> elems = getList();
        elems = elems.stream()
                .filter(t-> t.getType() != null && docType.indexOf(t.getType().getId().toString()) > -1)
                .map(t->t).collect(Collectors.toList());
        return elems;
    }
    public NVA_SPR_AUD_NPA getElement(Long idParent){
        NVA_SPR_AUD_NPA doc = null;
        try {
            doc = entityManager.createQuery(
                    "from NVA_SPR_AUD_NPA i " +
                            "where i.name is not null " +
                            "and i.number is not null " +
                            "and i.date is not null " +
                            "and i.id="+idParent
                    , NVA_SPR_AUD_NPA.class).getSingleResult();
        }
        catch (Exception ex) {
        }

        return  doc;
    }
    public List<NVA_SPR_AUD_NPA_STRUCTURE> getListStructure(String idDocument, Optional<Long> revId, Optional<LocalDate> dtRev){
//        String s = "with tree as (\n" +
//                "\tselect *\n" +
//                "\t, 0 as [level], cast('' as varchar(max)) as pathstr, ElementNumber\n" +
//                "\t   from [NVA_SPR_AUD_NPA_STRUCTURE] as dc\n" +
//                "\t   where id_SprAudNPA = " + idDocument + " and fk_parentElementId is null\n" +
//                "\tunion all\n" +
//                "\tselect dc.*, tree.[level] + 1, \n" +
//                "\tcast(tree.pathstr + right('00000000' + dc.ElementType + dc.ElementNumber, 8) as varchar(max)) as pathstr, dc.ElementNumber\n" +
//                "\t   from [NVA_SPR_AUD_NPA_STRUCTURE] as dc\n" +
//                "\t\t inner join tree on tree.documentElementId = dc.fk_parentElementId\n" +
//                ")\n" +
//                "\n" +
//                "select  documentElementId, id_SprAudNPA, elementType, [level], ElementNumber , [text], fk_parentElementId\n" +
//                "from tree\n" +
//                "order by pathstr, ElementType, right('00000' + ElementNumber,5) OPTION (MAXRECURSION 365);";
//
//        List<NVA_SPR_AUD_NPA_STRUCTURE> docs = entityManager.createNativeQuery(s, NVA_SPR_AUD_NPA_STRUCTURE.class).getResultList();
//
//        //"where [level] = 0\n" +
//        //, RIGHT('000000' + (cast([level] as varchar(max)) + elementNumber), 6) as [level]

        NVA_SPR_AUD_NPA_Revision rev = null;
        if (revId.isPresent()){
            if (revId.get() > 0){
                rev = revisionService.find(revId.get());
                if (rev == null) return new ArrayList<NVA_SPR_AUD_NPA_STRUCTURE>();
            }
        }
        else if (dtRev.isPresent())
            rev = findRevisionByDate(dtRev.get(), Long.parseLong(idDocument));
        else
            rev = findRevisionByDate(LocalDate.now(), Long.parseLong(idDocument));

        List<NVA_SPR_AUD_NPA_STRUCTURE> allDocs = entityManager.createQuery(
                "from NVA_SPR_AUD_NPA_STRUCTURE i " +
                        "where i.id_SprAudNPA=" + idDocument + " " +
                        (rev == null ? " and i.id_SprAudNPAStr is null " : " and i.id_SprAudNPAStr="+ rev.getId() +" ")+
                        " and i.deleted is null "+
                        "order by i.elementOrder", NVA_SPR_AUD_NPA_STRUCTURE.class).getResultList();

        List<NVA_SPR_AUD_NPA_STRUCTURE> docs = allDocs.stream()
                .filter(t -> t.getFk_parentElementId() == null).map(t -> t)
                .sorted((o1, o2) -> o1.getElementOrder().compareTo(o2.getElementOrder()))
                .collect(Collectors.toList());

        Boolean isFull = true;
        int i = 0;
        while (isFull)
        {
            docs = stepByStep(allDocs, docs, i);

            isFull = (docs.stream().filter(t->t.getLevel() == null).count()>0);
            i++;
        }
        return docs;
    }
    public List<NVA_SPR_AUD_Document_Type> getListDocTypes(){
        List<NVA_SPR_AUD_Document_Type> elems;
        try {
            TypedQuery<NVA_SPR_AUD_Document_Type> query = entityManager.createQuery(
                    "from NVA_SPR_AUD_Document_Type i order by i.id"
                    , NVA_SPR_AUD_Document_Type.class);

            elems = query.getResultList();
        }
        catch (Exception ex) {
            elems = new ArrayList<NVA_SPR_AUD_Document_Type>();
        }

        return  elems;
    }

    public List<NVA_SPR_AUD_NPA_STRUCTURE> stepByStep(List<NVA_SPR_AUD_NPA_STRUCTURE> allDocs,
                                                      List<NVA_SPR_AUD_NPA_STRUCTURE> docs, Integer level){
        String s0="", sPrev="";
        List<NVA_SPR_AUD_NPA_STRUCTURE> tmpDocs;
        for (int i = 0; i < docs.size(); i++) {
            if (docs.get(i).getLevel() == null) {
                docs.get(i).setLevel(level);

                s0 = docs.get(i).formattedPath(false);
                s0 = sPrev.concat(" ").concat(s0).trim();
                docs.get(i).setStructurePuth(s0);

                final int finalI = i;
                tmpDocs = allDocs.stream()
                        .filter(t -> t.getFk_parentElementId() != null && t.getFk_parentElementId().equals(docs.get(finalI).getId())).map(t -> t)
                        .sorted((o1, o2) -> o1.getElementOrder().compareTo(o2.getElementOrder()))
                        .collect(Collectors.toList());
                if (tmpDocs.size() > 0) {
                    docs.addAll(i + 1, tmpDocs);
                    i = i + tmpDocs.size();
                }
            }
            else {
                sPrev = docs.get(i).getStructurePuth();
            }
        }
        return docs;
    }


    public NVA_SPR_AUD_NPA_Revision findRevisionByDate(LocalDate dt, Long idNPA){
        TypedQuery<NVA_SPR_AUD_NPA_Revision> q= entityManager.createQuery(
                "from NVA_SPR_AUD_NPA_Revision r where r.dtBegin <= :dt and (r.dtEnd is null or r.dtEnd >= :dt) and r.id_NPA = :npa and r.isDeleted = 0",
                NVA_SPR_AUD_NPA_Revision.class);
        q.setParameter("dt", dt);
        q.setParameter("npa", idNPA);
        List<NVA_SPR_AUD_NPA_Revision> revs = q.getResultList();
        if (revs.size() == 0) return null;
        return revs.get(0);
    }

    /* Поиск редакции по ее ID (dmihaylov, 24.08.2017)*/
    public NVA_SPR_AUD_NPA_Revision findRevisionByID(Long id){
        TypedQuery<NVA_SPR_AUD_NPA_Revision> q= entityManager.createQuery("from NVA_SPR_AUD_NPA_Revision r where r.id =:idRev", NVA_SPR_AUD_NPA_Revision.class);
        q.setParameter("idRev", id);
        List<NVA_SPR_AUD_NPA_Revision> revs = q.getResultList();
        if (revs.size() == 0) return null;
        return revs.get(0);
    }

    /* Поиск всех редакций выбранного НПА (nburaeva, 21.04.2017) */
    public List<NVA_SPR_AUD_NPA_Revision> findRevisionByNpa(Long idNPA) {
        TypedQuery<NVA_SPR_AUD_NPA_Revision> q = entityManager.createQuery(
                "from NVA_SPR_AUD_NPA_Revision r " +
                "where r.id_NPA = :npa and r.isDeleted = 0 " +
                "order by r.dtBegin", NVA_SPR_AUD_NPA_Revision.class);
        q.setParameter("npa", idNPA);

        return q.getResultList();
    }

    /* Поиск всех редакций НПА (nburaeva, 02.05.2017) */
    public List<NVA_SPR_AUD_NPA_Revision> findRevisionAll() {
        TypedQuery<NVA_SPR_AUD_NPA_Revision> q = entityManager.createQuery(
                "from NVA_SPR_AUD_NPA_Revision r " +
                        "where r.isDeleted = 0 " +
                        "order by r.dtBegin", NVA_SPR_AUD_NPA_Revision.class);

        return q.getResultList();
    }

    /* Загрузка структуры документа (без последующей обработки); nburaeva, 20.02.2018 */
    public List<NVA_SPR_AUD_NPA_STRUCTURE> getStructureDocument(Long idNpa, Optional<Long> idRevision) {
        List<NVA_SPR_AUD_NPA_STRUCTURE> result = entityManager.createQuery(
                "from NVA_SPR_AUD_NPA_STRUCTURE i " +
                        "where i.id_SprAudNPA = " + idNpa + " " +
                        (idRevision.isPresent()
                                ? " and i.id_SprAudNPAStr = "+ idRevision.get()
                                : " and i.id_SprAudNPAStr is null " + " ") +
                        " and i.deleted is null ", NVA_SPR_AUD_NPA_STRUCTURE.class).getResultList();
        return result;
    }


    /*  oshesternikova: CRUD операции для NVA_SPR AUD_NPA*/

    /* Добавить в базу НПА новый */
    public void create(NVA_SPR_AUD_NPA npa){
        npa.setDeleted(false);
        entityManager.persist(npa);
        entityManager.flush();
    }

    /* Изменить */
    public void update(NVA_SPR_AUD_NPA npa, Long id){
        npa.setId(id);
        npa.setDeleted(false);
        entityManager.merge(npa);
        entityManager.flush();
    }

    /* Удалить */
    public void delete(Long id){
        Query q = entityManager.createQuery("update NVA_SPR_AUD_NPA n set n.isDeleted = 1 where n.id = :npa");
        q.setParameter("npa", id);
        q.executeUpdate();


        Query q2 = entityManager.createQuery("update NVA_SPR_AUD_NPA_STRUCTURE n set n.deleted = :dt where n.id_SprAudNPA = :npa");
        q2.setParameter("npa", id);
        q2.setParameter("dt", LocalDate.now());
        q2.executeUpdate();

    }

    /**
     * Проверка на данные, не привезянные к редакциям
     * @param docId НПА
     * @return
     */
    public boolean checkNullRevision(Long docId){
        Query q = entityManager.createQuery(
                "select count(*) from NVA_SPR_AUD_NPA_STRUCTURE r " +
                        "where r.id_SprAudNPAStr is null and r.id_SprAudNPA = :npa and r.deleted is null");
        q.setParameter("npa", docId);
        Object o = q.getSingleResult();
        Long count = (Long)o;
        return count > 0;

    }



}