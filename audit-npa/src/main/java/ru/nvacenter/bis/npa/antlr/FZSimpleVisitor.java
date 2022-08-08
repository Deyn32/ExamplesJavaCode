package ru.nvacenter.bis.npa.antlr;

import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringUtils;
import ru.nvacenter.bis.audit.npa.domain.dto.NPANode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oshesternikova on 30.05.2017.
 */
public class FZSimpleVisitor extends FZBaseVisitor<Void> {

    private NPANode inner;
    private NPANode root;
    private NPANode current;
    private Map<String, String> uncertainties;

    private static final String RootName = "Документ";
    public static boolean CheckIfRoot(NPANode node){
        return node.getData().getType() == RootName;
    }

    public FZSimpleVisitor() {
        this.inner = new NPANode();
        this.inner.getData().setType(RootName);
        this.inner.getData().setOriginalType(RootName);
        this.root = inner;
        this.uncertainties = new HashMap<>();
    }

    int st = 0;
    int fin = 0;
    @Override
    public Void visitLine(FZParser.LineContext ctx) {

        if (current != null)
        {
            fin = ctx.getStart().getStartIndex() - 1;
            if (fin > st)
            {
                Interval inter = Interval.of(st, fin);
                String strs = ctx.getStart().getInputStream().getText(inter).trim() ;
                //Проверка, что ключевое слово может быть просто началом предложения (пр. Приложение формируется получателем)
                boolean isContext = false;
                if (current.getData().getType() != null && current.getData().getNum() == null ){
                    for (int i = 0; i < strs.length(); i++) {
                        char ch = strs.charAt(i);
                        if (!Character.isSpaceChar(ch))
                        {
                            if (Character.isUpperCase(ch)){
                                isContext = true;
                                break;
                            }
                        }
                        if (Character.isDigit(ch)) break;
                        if (Character.isLowerCase(ch)) break;
                        if (ch == '.') break;
                    }

                    if (!isContext){
                        String tt = current.getData().getType();
                        current.getData().setType(null);
                        current.getData().setOriginalType(null);
                        strs = tt + " "+strs;
                    }
                }

                current.getData().setName(strs);
            }

            String tstr = current.getData().getType();
            NPANode relNode = root.getLastNode();
            if (relNode == null) relNode = root;

            NPANode prev = findParent(tstr, relNode);
            if (prev == null)
                root = findNonEmpty(relNode);
            else
                root = prev;
            saveCurrent();
        }

        current = new NPANode();
        st = ctx.getStart().getStopIndex() + 1;

        return super.visitLine(ctx);
    }

    @Override
    public Void visitHeaderfull(FZParser.HeaderfullContext ctx) {
        //Основной алгоритм анализа заголовков элементов
        String lineSt = ctx.getStart().getInputStream().getText(Interval.of(st, ctx.getStart().getStartIndex() - 1));
        if (StringUtils.isBlank(lineSt))
        {
            st = ctx.getStop().getStopIndex() + 1;
            //Полный заголовок
            FZParser.HeaderContext hdrcontext = ctx.getRuleContext(FZParser.HeaderContext.class, 0);
            int finalTypeIndex = 0;
            if (hdrcontext != null)
            {
                //Тип элемента - часть, пункт и пр.
                List<TerminalNode> nodes = hdrcontext.getTokens(FZLexer.HEADERNAMES);
                if (nodes.size() > 0)
                {
                    String tstr2 = nodes.get(0).getText();
                    finalTypeIndex = nodes.get(0).getSymbol().getStopIndex() + 1;
                    st = finalTypeIndex;
                    current.getData().setType(tstr2);
                    current.getData().setOriginalType(tstr2);
                }
            }

            //Номер элемента заголовка
            boolean hasNumber = false;
            FZParser.NumbersContext numcontext = ctx.getRuleContext(FZParser.NumbersContext.class, 0);
            if (numcontext != null)
            {
                String tstr = numcontext.getText();
                int startNumIndex = numcontext.getStart().getStartIndex() - 1;
                //Проверка, что номер в начале
                String space = ctx.getStart().getInputStream().getText(Interval.of(finalTypeIndex, startNumIndex));
                space = space.trim();
                if (finalTypeIndex == 0 || space.length() < 3)
                {
                    hasNumber = true;
                    current.getData().setNum(tstr);
                    //st = ctx.getStop().getStopIndex() + 1;
                    st = numcontext.getStop().getStopIndex() + 1;
                    //Если есть номер, но нет названия - даем фиктивное исходя из типа нумерации (арабская, римская) и уровня (считаем по количеству .)
                    if (current.getData().getType() == null)
                    {
                        TerminalNode n = numcontext.getChild(TerminalNode.class, 0);
                        String[] vals = tstr.split("\\.");
                        int count = 0;
                        for (String st: vals) {
                            if (!StringUtils.isEmpty(st))
                                count++;
                        }
                        String tempName = "_" + FZLexer.VOCABULARY.getSymbolicName(n.getSymbol().getType()) + count;
                        current.getData().setType(tempName);
                        current.getData().setOriginalType(tempName);
                        if (!uncertainties.containsKey(tempName))
                            uncertainties.put(tempName, tstr);
                    }
                }
            }

        }

        return super.visitHeaderfull(ctx);
    }

    @Override
    public Void visitAnytext(FZParser.AnytextContext ctx) {
        return super.visitAnytext(ctx);
    }

    @Override
    public Void visitHeader(FZParser.HeaderContext ctx) {
        return super.visitHeader(ctx);
    }

    @Override
    public Void visitNumbers(FZParser.NumbersContext ctx) {
        return super.visitNumbers(ctx);
    }

    @Override
    public Void visitAnytextline(FZParser.AnytextlineContext ctx) {
        return super.visitAnytextline(ctx);
    }

    @Override
    public Void visitDoc(FZParser.DocContext ctx) {
        return super.visitDoc(ctx);
    }

    @Override
    public Void visitNl(FZParser.NlContext ctx) {
        return super.visitNl(ctx);
    }

    private NPANode findParent(String name, NPANode el)
    {
        if (StringUtils.equals(el.getData().getType(), name)){
            return el.getParent();
        }
        if (el.getParent() == null) return null;
        if (checkIfUpperLevel(name)){
            return findRoot(el);
        }
        return findParent(name, el.getParent());
    }

    private NPANode findNonEmpty(NPANode el)
    {
        if (el.getParent() == null) return el;

        String t = el.getData().getType();
        if (t == null)
            return findNonEmpty(el.getParent());
        return el;
    }

    private NPANode findRoot(NPANode el)
    {
        if (el.getParent() == null) return el;
        return findRoot(el.getParent());
    }

    private boolean checkIfUpperLevel(String elType){
        if (elType != null && elType.equals("Приложение")){
         return true;
        }
        return false;
    }

    private void saveCurrent()
    {
        if (current.getData().getType() != null || !StringUtils.isBlank(current.getData().getName()))
        {
            current.setParent(root);
            root.addChild(current);
            if (current.getData().getType() != null)
                root = current;
        }
    }

    public NPANode getInner() {
        return inner;
    }

    public Map<String, String> getUncertainties() {
        return uncertainties;
    }

}
