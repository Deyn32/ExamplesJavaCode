package ru.nvacenter.bis.npa.services.dto;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_STRUCTURE;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPANode;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPA_AUD_ContentNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oshesternikova on 28.09.2017.
 * Сервис перевода из линейной структруры в рекурсивную
 */

@Service
@PreAuthorize("isAuthenticated()")
public class NPALinearRecursiveService {

    public NPANode ToRecursive(List<NVA_SPR_AUD_NPA_STRUCTURE> strs){
        NPANode root = new NPANode();
        List<NPANode> res = new ArrayList<>();
        int curLevel = 0;
        List<NPANode> curList = res;
        Map<Integer, List<NPANode>> lastLists = new HashMap<>();
        lastLists.put(0, res);
        for(NVA_SPR_AUD_NPA_STRUCTURE str : strs){
            if (curLevel != str.getLevel()) {
                if (curLevel < str.getLevel()) {
                    curLevel = str.getLevel();
                    NPANode s = curList.get(curList.size() - 1);
                    curList = s.getChildren();
                    lastLists.put(curLevel, curList);
                } else {
                    curLevel = str.getLevel();
                    curList = lastLists.get(curLevel);
                }
            }
            curList.add(createContNode(str));
        }
        for (NPANode ch: res) {
            root.addChild(ch);
        }

        return root;
    }

    private NPANode createContNode(NVA_SPR_AUD_NPA_STRUCTURE str){
        NPANode res = new NPANode();
        res.getData().setName(str.getText());
        if (str.getElementOriginalType() == null)
            res.getData().setType(str.getElementType());
        else
            res.getData().setType(str.getElementOriginalType());
        res.getData().setNum(str.getElementNumber());
        return res;
    }

   public List<NPA_AUD_ContentNode> ToRecursiveView(List<NVA_SPR_AUD_NPA_STRUCTURE> strs){
       List<NPA_AUD_ContentNode> res = new ArrayList<>();
       int curLevel = 0;
       List<NPA_AUD_ContentNode> curList = res;
       Map<Integer, List<NPA_AUD_ContentNode>> lastLists = new HashMap<>();
       lastLists.put(0, res);
       for(NVA_SPR_AUD_NPA_STRUCTURE str : strs){
           if (curLevel != str.getLevel()) {
               if (curLevel < str.getLevel()) {
                   curLevel = str.getLevel();
                   NPA_AUD_ContentNode s = curList.get(curList.size() - 1);
                   curList = s.getChildren();
                   lastLists.put(curLevel, curList);
               } else {
                   curLevel = str.getLevel();
                   curList = lastLists.get(curLevel);
               }
           }
           curList.add(createCont(str));
       }
       return res;
   }

   private NPA_AUD_ContentNode createCont(NVA_SPR_AUD_NPA_STRUCTURE str){
        return new NPA_AUD_ContentNode(str, new ArrayList<NPA_AUD_ContentNode>());
    }
}
