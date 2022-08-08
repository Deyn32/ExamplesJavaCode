package ru.nvacenter.bis.npa.services.dto;

/**import com.google.web.bindery.requestfactory.shared.Service;*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;

import ru.nvacenter.bis.npa.antlr.FZSimpleVisitor;
import ru.nvacenter.bis.audit.npa.domain.dto.NPANode;
import ru.nvacenter.bis.npa.domain.dto.NPAParseResult;
import ru.nvacenter.bis.npa.services.dao.FZDocumentCopyService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by dmihaylov on 19.07.2017.
 * Сервис конвертации НПА
 */
@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class NPADocConverterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FZDocumentCopyService.class);
    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;

    private NPAParseResult parseResult;
    public void setParseResult(NPAParseResult pars) {parseResult = pars;}

    private List<NPANode> convertResult(NPANode res, List<NPANode> listNode) {
        for(int i = 0 ; i < res.getChildren().size(); i++) {
            NPANode node;
            node = res.getChildren().get(i);
            node.setOrder((long)i + 1);
            listNode.add(node);
            convertResult(node, listNode);
        }
        return listNode;
    }

    private List<NPANode> convert(NPAParseResult parseResult) {
        List<NPANode> listNode = new ArrayList<>();
        return convertResult(parseResult.getTree(), listNode);
    }

    private void deleteData(Long id, Optional<Long> idRev) {
        Query q2 = null;
        if (idRev.isPresent()){
            q2 = entityManager.createQuery("update NVA_SPR_AUD_NPA_STRUCTURE n set n.deleted = :dt where n.id_SprAudNPAStr = :rev and n.id_SprAudNPA = :npa");
            q2.setParameter("rev", idRev.get());
        }
        else{
            q2 = entityManager.createQuery("update NVA_SPR_AUD_NPA_STRUCTURE n set n.deleted = :dt where n.id_SprAudNPAStr is null and n.id_SprAudNPA = :npa");
        }
        q2.setParameter("npa", id);
        q2.setParameter("dt", LocalDate.now());
        q2.executeUpdate();
    }

    public void tryFindAndConvertDoc(Long id, Optional<Long> idRev) {

        this.deleteData(id, idRev);

        List<NPANode> listNode = convert(parseResult);
        List<NVA_SPR_AUD_NPA_STRUCTURE> dstr = convert(listNode, id, idRev);

        for(int i = 0; i< dstr.size(); i++){
            NPANode node = listNode.get(i);
            NVA_SPR_AUD_NPA_STRUCTURE str = dstr.get(i);
            NPANode parent_node = node.getParent();
            if (parent_node != null && !FZSimpleVisitor.CheckIfRoot((parent_node))){
                int index =listNode.indexOf(parent_node);
                NVA_SPR_AUD_NPA_STRUCTURE parent = dstr.get(index);
                str.setFk_parentElementId(parent.getId());
            }
            entityManager.persist(str);
        }

        try{
            entityManager.flush();
        }
        catch (Exception ex) {
            String baseMsg =String.format("Ошибка копирования структуры документа");
            LOGGER.error(baseMsg, ex);
            return;
        }

    }

    private List<NVA_SPR_AUD_NPA_STRUCTURE> convert(List<NPANode> listNode, Long id, Optional<Long> idRev) {
        List<NVA_SPR_AUD_NPA_STRUCTURE> dstr = new ArrayList<>();
        for(NPANode node: listNode) {
            dstr.add(convert(node, id, idRev));
        }
        return dstr;
    }

    private NVA_SPR_AUD_NPA_STRUCTURE convert(NPANode node, Long id, Optional<Long> idRev) {
        NVA_SPR_AUD_NPA_STRUCTURE struct = new NVA_SPR_AUD_NPA_STRUCTURE();
        struct.setUpdated(LocalDate.now());
        struct.setElementOrder(node.getOrder());
        struct.setElementNumber(node.getData().getFormattedNum());
        struct.setText(node.getData().getFormattedName());
        String elemtype = node.getData().getFormattedType();
        struct.setElementType(elemtype);
        struct.setElementOriginalType(elemtype);
        struct.setVersion(0);
        struct.setId_SprAudNPA(id);
        if(idRev.isPresent())
            struct.setId_SprAudNPAStr(idRev.get());
        return struct;
    }
    
    /**Метод для определения кодировки текста*/
    public String definitionEncoding(NPAParseResult pars) {
        List<NPANode> listNode = convert(pars);
        String code = encoding(listNode);
        return code;
    }

    private String encoding(List<NPANode> nodes) {
        String codeText = "Cp1251";
        for (NPANode node: nodes) {
            String str = node.getData().getName();
            if (str != null)
            {
                char[] arrayCh = str.toCharArray();
                for (char ch: arrayCh) {
                    if (ch == 8218) {
                        codeText = "UTF-8";
                        return  codeText;
                    }
                }
            }
        }
        return codeText;
    }
}
