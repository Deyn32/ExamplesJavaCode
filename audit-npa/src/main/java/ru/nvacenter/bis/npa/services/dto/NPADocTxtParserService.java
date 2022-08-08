package ru.nvacenter.bis.npa.services.dto;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.npa.antlr.*;

import ru.nvacenter.bis.npa.domain.dto.NPAParseResult;
import ru.nvacenter.bis.npa.domain.dto.NPATypeNumberMap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by oshesternikova on 30.05.2017.
 * Сервис обработки НПА
 */

@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class NPADocTxtParserService {
    public NPAParseResult parseStream(InputStream stream, String charset) throws IOException {
        //Добавляем в начале переход на новую строку, иначе теряем первую строку (из-за грамматики)
        String newLine = " \r\n";
        InputStream is = new ByteArrayInputStream( newLine.getBytes(charset));
        SequenceInputStream inp = new SequenceInputStream(is, stream);

        InputStreamReader isr = new InputStreamReader(inp, charset);
        Reader read = new BufferedReader(isr);

        ANTLRInputStream inputStream = new ANTLRInputStream(read);
        FZLexer speakLexer = new FZLexer(inputStream);
        speakLexer.removeErrorListeners();
        CommonTokenStream commonTokenStream = new CommonTokenStream(speakLexer);
        FZParser speakParser = new FZParser(commonTokenStream);

        FZParser.DocContext chatContext = speakParser.doc();
        FZSimpleVisitor visitor = new FZSimpleVisitor();
        visitor.visit(chatContext);
        NPAParseResult res = new NPAParseResult();
        res.setTree(visitor.getInner());
        List<NPATypeNumberMap> errs = new ArrayList<>();
        for(Map.Entry<String, String> entry : visitor.getUncertainties().entrySet()){
            NPATypeNumberMap map = new NPATypeNumberMap();
            map.setHeader(entry.getValue());
            map.setName(entry.getKey());
            errs.add(map);
        }
        res.setUncertainties(errs);
        return res;
    }
}
