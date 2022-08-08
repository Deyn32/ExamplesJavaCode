package ru.nvacenter.bis.auditviolationnpa.services.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPA;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPAStructure;
import ru.nvacenter.bis.auditviolationcommon.server.domain.ViolationData;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.UnloadedNPA;
import ru.nvacenter.bis.auditviolationnpa.services.dao.ViolationsService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmihaylov on 02.04.2018.
 */
@Service
public class CreateListUnloadedNPAService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FZDocumentService.class);
    private static final String ERROR_MESSAGE = "Ошибка при выполнении запроса '%s'";

    @Inject
    ViolationsService violationsService;

    public List<UnloadedNPA> create() {
        List<UnloadedNPA> list = new ArrayList<>();
        List<NVA_SPR_FKAU_ViolationNPAStructure> listStruct = violationsService.findAllUnloadNPA();
        for(NVA_SPR_FKAU_ViolationNPAStructure l : listStruct){
            NVA_SPR_FKAU_ViolationNPA npa = violationsService.findLinkNpa(l.getIdNPA());
            ViolationData vd = violationsService.find(npa.getIdViolation());
            UnloadedNPA unloadedNPA = createUnloadedNPA(npa, l, vd);
            list.add(unloadedNPA);
        }
        return list;
    }

    private UnloadedNPA createUnloadedNPA(NVA_SPR_FKAU_ViolationNPA npa, NVA_SPR_FKAU_ViolationNPAStructure vnpas, ViolationData vd) {
        UnloadedNPA uNPA = new UnloadedNPA();
        uNPA.setId(vnpas.getId());
        uNPA.setViolationText(vd.getViolationText());
        uNPA.setCaption(npa.getCaption());
        uNPA.setDate(npa.getDate());
        uNPA.setNum(npa.getNumber());
        uNPA.setIdNpa(vnpas.getIdNPA());
        uNPA.setText(vnpas.getText());
        return uNPA;
    }
}
