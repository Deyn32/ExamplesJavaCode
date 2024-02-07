package ru.nvacenter.bis.npa.services.dto;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_STRUCTURE;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oshesternikova on 12.02.2018.
 * Генерация pdf из данных базы
 */
@Service
@PreAuthorize("isAuthenticated()")
public class PdfNPAViewService extends BaseNPAViewService {
    @Override
    public byte[] create(List<NVA_SPR_AUD_NPA_STRUCTURE> str, NVA_SPR_AUD_NPA_Revision rev, NVA_SPR_AUD_NPA doc){

        ByteArrayOutputStream st = new ByteArrayOutputStream();

        com.lowagie.text.Document d = new com.lowagie.text.Document();
        try {
            Font font = com.lowagie.text.FontFactory.getFont("META-INF/fonts/arial.ttf", "Cp1251", BaseFont.EMBEDDED);
            Font fontBold = new Font(font.getBaseFont(), font.getSize(), Font.BOLD);
            d.open();
            //Заголовок
            addHeading(rev, doc, font, d);
            //Содержание
            addContent(str, fontBold, font, d);
            d.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return st.toByteArray();
    }

    private void addContent(List<NVA_SPR_AUD_NPA_STRUCTURE> str, Font fontBold, Font font, Document d) throws DocumentException {
        for (NVA_SPR_AUD_NPA_STRUCTURE nstr: str) {
            List<Chunk> chunks = new ArrayList<>();
            if (hasHeader(nstr)) chunks.add(new Chunk(nstr.formattedPath(true), fontBold));
            else chunks.add(new Chunk(nstr.formattedPath(true), font));
            chunks.add(new Chunk(nstr.getText(), font));
            Paragraph p = new Paragraph();
            for (Chunk ch : chunks) p.add(ch);
            d.add(p);
        }
    }

    private void addHeading(NVA_SPR_AUD_NPA_Revision rev, NVA_SPR_AUD_NPA doc, Font font, Document d) throws DocumentException {
        Paragraph phead = new Paragraph(createDocHeader(doc), font);
        phead.setAlignment(Element.ALIGN_CENTER);
        d.add(phead);
        Paragraph prev = new Paragraph(createRevHeader(rev), font);
        prev.setAlignment(Element.ALIGN_CENTER);
        d.add(prev);
        Paragraph pname = new Paragraph(doc.getName(), font);
        pname.setAlignment(Element.ALIGN_CENTER);
        d.add(pname);
    }
}
