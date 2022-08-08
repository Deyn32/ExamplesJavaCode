package ru.nvacenter.bis.auditviolationnpa.services.dto;

import org.springframework.stereotype.Service;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPAStructure;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.AnalyseAsyncData;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.ViolationRevData;
import ru.nvacenter.bis.auditviolationnpa.services.NpaViolationAnalyseService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.DBRepairService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.ViolationsService;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by dmihaylov on 16.07.2018.
 */
@Service
public class RegroupingService {
    private AnalyseAsyncData analyseData;

    @Inject
    ViolationsService violationsService;
    @Inject
    DBRepairService dbRepairService;
    @Inject
    NpaViolationAnalyseService npaViolationAnalyseService;
    @Inject
    AsyncDataService asyncDataService;
    @Inject
    ViolationsTreeService violationsTreeService;

    public List<ViolationRevData> regrouping(Long idNpaStruct, Long idNpa) {
        NVA_SPR_FKAU_ViolationNPAStructure str = violationsService.findLink(idNpaStruct);
        String idSource = str.getIdSource();
        List<Long> listIdUniqueStructure = dbRepairService.findIdsStructure(idSource, idNpa);
        if(listIdUniqueStructure.size() != 0){
            dbRepairService.deleteUniqueStrucrure(listIdUniqueStructure);
        }
        analyseData  = asyncDataService.create();
        List<NVA_SPR_FKAU_ViolationNPAStructure> allstrs = violationsService.findAllLinksStr(str);
        List<NVA_SPR_FKAU_ViolationNPAStructure> newstrs = npaViolationAnalyseService.createAndCheck(idNpa, allstrs, str.getIdNPA());
        violationsService.save(newstrs);
        return violationsTreeService.revisionsByLink(idNpaStruct);
    }
}
