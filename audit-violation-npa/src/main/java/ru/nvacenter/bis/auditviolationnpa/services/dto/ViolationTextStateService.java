package ru.nvacenter.bis.auditviolationnpa.services.dto;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.dto.NPANode;
import ru.nvacenter.bis.audit.npa.domain.dto.NPA_AUD_ContentNode;
import ru.nvacenter.bis.audit.npa.domain.dto.compare.NPARevisionCompareNode;
import ru.nvacenter.bis.audit.npa.services.dto.NPARevisionCompareService;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPA;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPAStructure;
import ru.nvacenter.bis.auditviolationcommon.server.domain.ViolationData;
import ru.nvacenter.bis.auditviolationnpa.services.dao.ViolationsService;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by oshesternikova on 10.05.2018.
 * Сервис сравнения текста формулировки нарушения и текста положения
 */
@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="jpaTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class ViolationTextStateService {
    @Inject
    NPARevisionCompareService npaRevisionCompareService;
    @Inject
    ViolationsTreeService violationsTreeService;
    @Inject
    private ViolationsService violationsService;

    public NPARevisionCompareNode compare(Long idNpaLink, Long idRev){
        NVA_SPR_FKAU_ViolationNPAStructure str = violationsService.findLink(idNpaLink);
        NVA_SPR_FKAU_ViolationNPA lnpa = violationsService.findLinkNpa(str.getIdNPA());
        ViolationData vdata = violationsService.find(lnpa.getIdViolation());

        List<NPA_AUD_ContentNode> abs = violationsTreeService.structureAbstractByLink(idNpaLink, idRev);
        //Имитируем дерево для текста нарушения
        NPANode rootViol = new NPANode();
        NPANode v1 = new NPANode();
        v1.getData().setName(vdata.getViolationText());
        rootViol.getChildren().add(v1);
        //Имитируем дерево для текста положения
        NPANode rootState = new NPANode();
        NPANode v2 = new NPANode();
        v2.getData().setName(createTextRecursive(abs));
        rootState.getChildren().add(v2);
        return npaRevisionCompareService.mergeRoot(rootViol, rootState, true);
    }

    private String createTextRecursive(List<NPA_AUD_ContentNode> abs){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < abs.size(); i++){
            NPA_AUD_ContentNode ct = abs.get(i);
            sb.append(ct.getData().getLabel());
            if (ct.getChildren().size() > 0){
                sb.append(System.getProperty("line.separator"));
                sb.append(createTextRecursive(ct.getChildren()));
            }
            if (i < abs.size() - 1)
                sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
}
