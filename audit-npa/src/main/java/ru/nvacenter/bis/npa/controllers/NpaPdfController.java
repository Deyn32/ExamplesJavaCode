package ru.nvacenter.bis.npa.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/directory")
public interface NpaPdfController {

    @GetMapping(path = {"fzDownloadNPA"})
    @ResponseStatus(value = HttpStatus.OK)
    //Добавление в справичник
    public void download(HttpServletResponse response,
                         @RequestParam("docId") Long docId,
                         @RequestParam("revId") Optional<Long> revId) throws Exception;
}
