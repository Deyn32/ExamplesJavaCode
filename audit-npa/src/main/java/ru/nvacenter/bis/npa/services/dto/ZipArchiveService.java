package ru.nvacenter.bis.npa.services.dto;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by dmihaylov on 01.06.2018.
 */

@Service
public class ZipArchiveService {
    @Inject
    XmlSerializeService xmlSerializeService;

    public byte[] zip(Map<String, String> xmlMap) throws Exception{
        byte[] bytes = new byte[]{};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(baos));
        try{
            for(Map.Entry<String, String> map: xmlMap.entrySet()){
                try(ByteArrayInputStream bais = new ByteArrayInputStream(map.getValue().getBytes())){
                    zip.putNextEntry(new ZipEntry(map.getKey()));
                    write(bais, zip, map.getValue().getBytes().length);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            zip.finish();
            zip.close();
            bytes = baos.toByteArray();
            baos.close();
        }
        return bytes;
    }

    public Map<String, String> unZip(InputStream is) throws Exception {
        Map<String, String> xmlMaps = new HashMap<>();
        ZipInputStream zis = new ZipInputStream(is);
        ByteArrayOutputStream baos = null;
        try{
            ZipEntry entry;
            String name;
            while((entry = zis.getNextEntry())!=null){
                name = entry.getName();
                baos = new ByteArrayOutputStream();
                for (int i = zis.read(); i != -1; i = zis.read()) {
                    baos.write(i);
                }
                xmlMaps.put(name, baos.toString());
                baos.flush();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            zis.closeEntry();
            baos.close();
        }
        return xmlMaps;
    }

    private static void write(InputStream in , OutputStream out, int len) throws IOException {
        byte[] buffer = new byte[len];
        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);
        in.close();
    }
}
