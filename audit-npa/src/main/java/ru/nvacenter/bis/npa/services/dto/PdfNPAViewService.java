package ru.nvacenter.bis.npa.services.dto;


import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import org.apache.commons.io.FilenameUtils;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;

import java.io.*;
import java.net.URL;
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
            URL fontURL = this.getClass().getClassLoader().getResource("META-INF/fonts/arial.ttf");
            com.lowagie.text.pdf.PdfWriter writer = com.lowagie.text.pdf.PdfWriter.getInstance(d, st);
            String path = FilenameUtils.getPath(fontURL.getPath());
            //com.lowagie.text.FontFactory.register(path);
            //BaseFont baseFont = BaseFont.createFont(fontURL.getPath(), BaseFont.IDENTITY_H, false);
            //Font fontNorm = new Font(baseFont, 10, Font.NORMAL);
            Font font = com.lowagie.text.FontFactory.getFont("META-INF/fonts/arial.ttf", "Cp1251", BaseFont.EMBEDDED);
            Font fontBold = new Font(font.getBaseFont(), font.getSize(), Font.BOLD);
            d.open();
            //Заголовок
            Paragraph phead = new Paragraph(createDocHeader(doc), font);
            phead.setAlignment(Element.ALIGN_CENTER);
            d.add(phead);
            Paragraph prev = new Paragraph(createRevHeader(rev), font);
            prev.setAlignment(Element.ALIGN_CENTER);
            d.add(prev);
            Paragraph pname = new Paragraph(doc.getName(), font);
            pname.setAlignment(Element.ALIGN_CENTER);
            d.add(pname);
            //Содержание
            for (NVA_SPR_AUD_NPA_STRUCTURE nstr:
                    str) {
                List<Chunk> chunks = new ArrayList<>();
                String innerStr = "";
                if (hasHeader(nstr)){
                    chunks.add(new Chunk(nstr.formattedPath(true), fontBold));
                }
                else
                    chunks.add(new Chunk(nstr.formattedPath(true), font));
                chunks.add(new Chunk(nstr.getText(), font));
                Paragraph p = new Paragraph();
                for (Chunk ch:
                     chunks) {
                    p.add(ch);

                }
               d.add(p);
            }
            d.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        /*
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = null;
        try {
            contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
            InputStream fontStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/fonts/arial.ttf");
            PDType0Font f = PDType0Font.load(document, fontStream);
            contentStream.setFont(f, 12);
            for (NVA_SPR_AUD_NPA_STRUCTURE nstr:
                 str) {
                contentStream.beginText();
                contentStream.showText(nstr.getText());
                contentStream.endText();
            }

            contentStream.close();

            document.save(st);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        return st.toByteArray();
    }
}
