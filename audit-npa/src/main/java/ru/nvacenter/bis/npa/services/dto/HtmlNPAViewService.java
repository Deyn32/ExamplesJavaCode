package ru.nvacenter.bis.npa.services.dto;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;
import ru.nvacenter.bis.audit.npa.domain.dto.compare.NPARevisionCompareNode;
import ru.nvacenter.bis.audit.npa.domain.dto.NPANodeData;

import java.util.List;

/**
 * Created by oshesternikova on 31.07.2017.
 * Сервис представления объектов в html
 */
@Service
@PreAuthorize("isAuthenticated()")
public class HtmlNPAViewService extends BaseNPAViewService {


    private String createDocView(List<NVA_SPR_AUD_NPA_STRUCTURE> strs){
        StringBuilder sb = new StringBuilder();
        if (strs.size() !=  0) {
            sb.append("<div class='mergeLeftText'>");
            for (NVA_SPR_AUD_NPA_STRUCTURE str:strs) {
                Integer margin = 0;
                if (str.getLevel() != null)
                    margin = str.getLevel()*30;
                String innerStr = "";
                if (hasHeader(str)){
                    innerStr = "<span class='fontBold'>"+str.formattedPath(true)+"</span>";
                }
                else
                    innerStr =str.formattedPath(true);
                innerStr += str.getText();
                sb.append("<div class='margin-bottom:10px'>"+innerStr+"</div>");
            }
            sb.append("</div>");
        }
        return sb.toString();
    }


    public String createMergeView(NPARevisionCompareNode root){
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        sb.append(createTableCaption(root.hasAnyChanges()));
        //sb.append(createMergeTableHeader(rev1, rev2));
        sb.append(createMergeViewList(root.getChildren()));
        sb.append("</table>");
        return sb.toString();
    }


    private String createMergeTableHeader(NVA_SPR_AUD_NPA_Revision rev1, NVA_SPR_AUD_NPA_Revision rev2){
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append("<th>");
        sb.append(createRevHeader(rev1));
        sb.append("</th>");
        sb.append("<th>");
        sb.append(createRevHeader(rev2));
        sb.append("</th>");
        sb.append("</tr>");
        return sb.toString();
    }

    private String createTableCaption(boolean changes){
        StringBuilder sb = new StringBuilder();
        if (!changes){
            sb.append("<caption>");
            sb.append("В документах нет отличий");
            sb.append("</caption>");
        }
        return sb.toString();
    }

    private String createMergeViewList(List<NPARevisionCompareNode> lst){
        StringBuilder sb = new StringBuilder();
        for (NPARevisionCompareNode ch : lst) {
            sb.append("<tr>");
            boolean mark = ch.getNodeLeft() == null || ch.getNodeRight() == null;
            if (ch.getNodeLeft() == null)
                sb.append(createEmptyCell());
            else
                sb.append(createTextCell(ch.getNodeLeft().getData(), mark, ch.getDiffs(), true));

            if (ch.getNodeRight() == null)
                sb.append(createEmptyCell());
            else
                sb.append(createTextCell(ch.getNodeRight().getData(), mark, ch.getDiffs(),false));

            String str = createMergeViewList(ch.getChildren());
            sb.append(str);
            sb.append("</tr>");
        }
        return sb.toString();
    }

    private String createEmptyCell(){
        return "<td class='greyNode'></td>";
    }

    private String createTextCell(NPANodeData node, boolean needMark, List<DiffMatchPatch.Diff> diffs, boolean isLeft){
        StringBuilder sb = new StringBuilder();
        sb.append("<td");
        if (needMark){
            sb.append(" class='brownNode'");
        }
        sb.append(">");
        if(diffs == null){
            String res = node.getFormattedType() +
                    (!StringUtils.isBlank(node.getFormattedType()) ? " " : "") +
                    node.getFormattedNum() +
                    (!StringUtils.isBlank(node.getFormattedNum()) && Character.isDigit(node.getFormattedNum().charAt(node.getFormattedNum().length() - 1))  ? "." : "")+
                    " " + node.getFormattedName();
            sb.append(res);
        }
        else{
           sb.append(createDiffText(diffs, isLeft));
        }

        sb.append("</td>");
        return sb.toString();

    }

    private String createDiffText(List<DiffMatchPatch.Diff> diffs, boolean isLeft){
        StringBuilder sb = new StringBuilder();
        if (isLeft){
            for (DiffMatchPatch.Diff diff: diffs){
                switch (diff.operation){
                    case DELETE:
                        appendSelected(sb, diff.text);
                        break;
                    case EQUAL:
                        appendSimple(sb, diff.text);
                        break;
                }
            }
        }
        else{
            for (DiffMatchPatch.Diff diff: diffs){
                switch (diff.operation){
                    case INSERT:
                        appendSelected(sb, diff.text);
                        break;
                    case EQUAL:
                        appendSimple(sb, diff.text);
                        break;
                }
            }
        }
        return sb.toString();
    }
    private void appendSelected(StringBuilder sb, String text){
        sb.append("<span class='insertTextNode'>");
        sb.append(text);
        sb.append("</span>");
    }
    private void appendSimple(StringBuilder sb, String text){
        sb.append("<span>");
        sb.append(text);
        sb.append("</span>");
    }

    @Override
    public byte[] create(List<NVA_SPR_AUD_NPA_STRUCTURE> str, NVA_SPR_AUD_NPA_Revision rev, NVA_SPR_AUD_NPA doc) {
        String val = createDocView(str);
        return val.getBytes();
    }
}
