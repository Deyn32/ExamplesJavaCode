package ru.nvacenter.bis.auditviolationnpa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.inject.Named;

@Controller
@Named("JspNPAViolationController")
public class JspNPAViolationController {
    @RequestMapping(value = {"/audit-violation-npa.html"},
            method = RequestMethod.GET)
    public String getIndexView() {
        return "audit-violation-npa";
    }

}
