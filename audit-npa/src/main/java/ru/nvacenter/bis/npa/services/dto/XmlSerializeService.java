package ru.nvacenter.bis.npa.services.dto;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_Document_Type;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_Organization;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_OrganizationService;
import ru.nvacenter.bis.npa.domain.dto.DocumentNpa;

import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dmihaylov on 01.06.2018.
 */

@Service
public class XmlSerializeService {
    @Inject
    FZDocumentService fzDocumentService;
    @Inject
    NVA_SPR_AUD_OrganizationService nva_spr_aud_OrganizationService;
    @Inject
    CheckService checkService;

    @XStreamImplicit
    private List<NVA_SPR_AUD_Organization> orgs;

    public List<NVA_SPR_AUD_Organization> getOrgs() {
        return orgs;
    }

    public Map<String, String> serialize(String fileName, Object object) throws Exception{
        Map<String, String> xmlMaps = new HashMap<>();
        XStream xStream = new XStream();
        xstreamSettings(xStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(baos, "UTF-8");
        try {
            xStream.toXML(object, writer);
            xmlMaps.put(fileName, baos.toString());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            writer.close();
            baos.close();
        }
        return xmlMaps;
    }

    public List<DocumentNpa> deserializeFromXml(Map<String, String> xmlMaps) throws Exception {
        List<DocumentNpa> listDocs = new ArrayList<>();
        XStream xStream = new XStream(new DomDriver("UTF-8"));
        xstreamSettings(xStream);
        try {
            for(Map.Entry<String, String> xml : xmlMaps.entrySet()){
                boolean check = checkService.equalsName(xml.getKey(), "Организации.xml");
                if(check){
                   deserializeOrgs(xml.getValue(), xStream);
                }
                else {
                    DocumentNpa doc = new DocumentNpa();
                    doc = deserializeNpa(doc, xml.getValue(), xStream);
                    listDocs.add(doc);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return  listDocs;
    }

    private void deserializeOrgs(String file, XStream xStream){
        orgs = new ArrayList<>();
        try(ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes())){
            orgs = (List<NVA_SPR_AUD_Organization>) xStream.fromXML(encodingText(bais));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private DocumentNpa deserializeNpa(DocumentNpa obj, String file, XStream xStream) throws Exception{
        try(ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes())) {
            xStream.fromXML(encodingText(bais), obj);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return obj;
    }

    private Reader encodingText(InputStream is) throws Exception{
        Reader reader = new InputStreamReader(is,"UTF-8");
        InputSource inputSource = new InputSource(reader);
        inputSource.setEncoding("UTF-8");
        return reader;
    }

    private void xstreamSettings(XStream xStream){
        xStream.alias("DocumentNpa", DocumentNpa.class);
        xStream.alias("NVA_SPR_AUD_Organization", NVA_SPR_AUD_Organization.class);
        LocalDateConverter conv = new LocalDateConverter();
        xStream.registerConverter(conv);
    }
}
