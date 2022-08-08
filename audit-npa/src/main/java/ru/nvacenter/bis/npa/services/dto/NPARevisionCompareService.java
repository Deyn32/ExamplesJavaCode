package ru.nvacenter.bis.npa.services.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Diff;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.npa.domain.dto.NPAMatrix;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPANode;
import ru.nvacenter.bis.npa.domain.dto.NPARevisionCompareNode;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPA_AUD_ContentNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oshesternikova on 25.09.2017.
 * Сервис сравнения редакций
 */

@Service
@PreAuthorize("isAuthenticated()")
public class NPARevisionCompareService {
    /**
     * Переменная для анализа разницы текстов
     */
    private final static DiffMatchPatch diff = new DiffMatchPatch();
    /**
     * Порог для различения сходства
     */
    private final static double CUT_OFF = 0.8;
    /**
     * Функция объединения документов
     * @param root1
     * @param root2
     * @return
     */
    public NPARevisionCompareNode mergeRoot(NPANode root1, NPANode root2){
        List<NPARevisionCompareNode> chs = mergeLevel(root1.getChildren(), root2.getChildren());
        NPARevisionCompareNode root = new NPARevisionCompareNode(root1, root2, chs, null);
        return root;
    }
    /**
     * Функция объединения одного уровня
     * @param l1
     * @param l2
     * @return
     */
    private List<NPARevisionCompareNode> mergeLevel(List<NPANode> l1, List<NPANode> l2){
        NPAMatrix<NPANode, Double> matrix = new NPAMatrix<NPANode, Double>(l1, l2, 1.0);
        for (NPANode n1 : l1) {
            for (NPANode n2: l2) {
                matrix.setValue(n1, n2, distance(n1, n2));
            }
        }
        List<NPARevisionCompareNode> res = new ArrayList<>();
        int c = 0;
        for(int i = 0; i < l1.size(); i++){
            List<Double> vals = matrix.getColumn(i);
            double distance = 1.0;
            int near = -1;
            for(int k = c; k < vals.size();k++){//ищем максимально близкий
                double cur = vals.get(k);
                if (cur < CUT_OFF && cur < distance){
                    near = k;
                    distance = cur;
                }
            }

            if (near >= 0){
                //Если есть "совпадающий"
                for(int j = c; j < near; j++){
                    NPANode n = l2.get(j);
                    List<NPARevisionCompareNode> childs = createChildrenForRight(n);
                    res.add(new NPARevisionCompareNode(null, n, childs, null));
                }

                NPANode nn1 = l1.get(i);
                NPANode nn2 = l2.get(near);
                List<NPARevisionCompareNode> chs = mergeLevel(nn1.getChildren(), nn2.getChildren());
                List<DiffMatchPatch.Diff> diffs = findDifference(nn1.getData().getName(), nn2.getData().getName(), true);
                res.add(new NPARevisionCompareNode(nn1, nn2, chs, diffs.size() == 1 ? null : diffs));
                c = near + 1;
            }else{
                //Если нету
                NPANode n = l1.get(i);
                List<NPARevisionCompareNode> childs = createChildrenForLeft(n);
                res.add(new NPARevisionCompareNode(n, null, childs, null));
            }
        }
        //Остаток от правого
        for(int i = c; i < l2.size(); i++){
            NPANode n = l2.get(i);
            List<NPARevisionCompareNode> childs = createChildrenForRight(n);
            res.add(new NPARevisionCompareNode(null, n, childs, null));
        }


        return res;
    }

    /**
     * Функция нахождения расстояния между двумя узлами
     * @param n1 Узел 1
     * @param n2 Узел 2
     * @return Значение от 0 до 1
     */
    private double distance(NPANode n1, NPANode n2){
        boolean headerMatch = compare(n1.getData().getType(), n2.getData().getType())
               && compare(numWithoutLastSym(n1.getData().getNum()), numWithoutLastSym(n2.getData().getNum()));
        if (!headerMatch) return 1;
        if (compare(n1.getData().getName(), n2.getData().getName())) return 0;
       return similarity(n1.getData().getName(), n2.getData().getName(), true);
    }

    /**
     * Так как в базе есть НПА без симолов . в конце
     * @param num Номер
     * @return
     */
    private static String numWithoutLastSym(String num){
        if (StringUtils.isEmpty(num)) return num;
        char last = num.charAt(num.length() - 1);
        if (Character.isLetterOrDigit(last))  return num;
        return  num.substring(0, num.length() - 1);
    }

    /**
     * Функция нахождения сходства между двумя текстами
     * @param text1 Текст 1
     * @param text2 Текст 2
     * @param wordMode Сравнение строк пословно (true) или посимвольно (false)
     * @return Значение от 0 до 1
     */
    private double similarity(String text1, String text2, boolean wordMode){
        if (wordMode){
            List<String> l1 = lineToWords(text1);
            List<String> l2 = lineToWords(text2);
            String str1 = listWordsToString(l1);
            String str2 = listWordsToString(l2);
            LinkedList<DiffMatchPatch.Diff> diffs1 =  diff.diffMain(str1, str2);
            int index1 = diff.diffLevenshtein(diffs1);
            return (double)index1 / Math.max(str1.length(), str2.length());
        }
        LinkedList<DiffMatchPatch.Diff> diffs = diff.diffMain(text1, text2);
        int index = diff.diffLevenshtein(diffs);
        return (double)index / Math.max(text1.length(), text2.length());
    }

    /**
     * Нахождение разности с учетом пословного сравнения
     * @param text1 Первая строка
     * @param text2 Вторая строка
     * @param wordMode Сравнение строк пословно (true) или посимвольно (false)
     * @return
     */
    private List<DiffMatchPatch.Diff> findDifference(String text1, String text2, boolean wordMode){
        if (wordMode){
            List<String> l1 = lineToWords(text1);
            List<String> l2 = lineToWords(text2);
            String str1 = listWordsToString(l1);
            String str2 = listWordsToString(l2);
            LinkedList<DiffMatchPatch.Diff> diffs1 =  diff.diffMain(str1, str2);
            List<DiffMatchPatch.Diff> result = new ArrayList<>();
            int left = 0;
            int right = 0;

            for(int i = 0; i < diffs1.size();i++){
                String finalText = "";
                int len = diffs1.get(i).text.length();
                switch (diffs1.get(i).operation){
                    case INSERT:{
                        StringBuilder sb = new StringBuilder();
                        for(int j = 0; j < len; j++)
                            sb.append(l2.get(j+right));
                        finalText = sb.toString();
                        right+=len;
                        break;
                    }
                    case DELETE:{
                        StringBuilder sb = new StringBuilder();
                        for(int j = 0; j < len; j++)
                            sb.append(l1.get(j+left));
                        finalText = sb.toString();
                        left+=len;
                        break;
                    }
                    case EQUAL:{
                        StringBuilder sb = new StringBuilder();
                        for(int j = 0; j < len; j++)
                            sb.append(l1.get(j + left));
                        finalText = sb.toString();
                        right+=len;
                        left+=len;
                        break;
                    }
                }
                DiffMatchPatch.Diff d = new DiffMatchPatch.Diff(diffs1.get(i).operation, finalText);
                result.add(d);
            }
            return result;
        }
        return diff.diffMain(text1, text2);
    }

    /**
     * Сравнение двух строк с учетом null
     * @param str1 Первая строка
     * @param str2 Вторая строка
     * @return
     */
    private static boolean compare(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

    private List<NPARevisionCompareNode> createChildrenForRight(NPANode n){
        List<NPARevisionCompareNode> res = new ArrayList<>();
        for (NPANode ch: n.getChildren()) {
            List<NPARevisionCompareNode> lst = createChildrenForRight(ch);
            NPARevisionCompareNode nn = new NPARevisionCompareNode(null, ch, lst, null);
            res.add(nn);
        }
        return res;
    }

    private List<NPARevisionCompareNode> createChildrenForLeft(NPANode n){
        List<NPARevisionCompareNode> res = new ArrayList<>();
        for (NPANode ch: n.getChildren()) {
            List<NPARevisionCompareNode> lst = createChildrenForLeft(ch);
            NPARevisionCompareNode nn = new NPARevisionCompareNode(ch, null, lst, null);
            res.add(nn);
        }
        return res;
    }

    /**
     * Функция разбиения строки на слова
     * @param text Строка для разбиения
     **/
    private static List<String> lineToWords(String text){
        List<String> res = new ArrayList<>();
        List<Character> current = new ArrayList<>();
        WordTypes wtCurrent = WordTypes.NONE;
        for(int i = 0; i < text.length();i++){
            Character ch = text.charAt(i);
            WordTypes wt = WordTypes.NONE;
            if (Character.isLetterOrDigit(ch))
                wt = WordTypes.WORD;
            else if (Character.isWhitespace(ch))
                wt = WordTypes.EMPTY;
            else wt = WordTypes.PUNCTUATION;
            if (wt != wtCurrent && wtCurrent != WordTypes.NONE)
                if (current.size() > 0){
                    String newString = createString(current);
                    res.add(newString);
                    current.clear();
                }

            current.add(ch);
            wtCurrent = wt;
        }

        if (current.size() > 0) {
            String newString = createString(current);
            res.add(newString);
        }
        return res;
    }

    /**
     * Преобразование строки в символ (на основе hash функции)
     * @param word Слово
     * @return
     */
    private static Character wordToCharacter(String word){
        return  (char)word.hashCode();
    }

    private static String createString(List<Character> lst){
        char[] arrChars = new char[lst.size()];
        for(int j = 0; j < lst.size();j++)
            arrChars[j] = lst.get(j);
        String newString = new String(arrChars);
        return newString;
    }

    /**
     * Преобразовать набор слов в одно слово
     * @param words Список слов
     * @return
     */
    private static String listWordsToString(List<String> words){
        List<Character> chs = words.stream().map(l -> wordToCharacter(l)).collect(Collectors.toList());
        return createString(chs);
    }

    public enum WordTypes {
        NONE, WORD, EMPTY, PUNCTUATION
    }

    /**
     * Отфильтровать получившееся дерево, убрав идентичные ветки
     * @param compareNode Смерженная структура двух деревьев
     */
    public NPARevisionCompareNode filterCompareNode(NPARevisionCompareNode compareNode) {
        for (int i = 0; i < compareNode.getChildren().size();) {
            if (compareNode(compareNode.getChildren().get(i)) == null) {
                compareNode.getChildren().remove(i);
            }
            else {
                i++;
            }
        }
        return  compareNode;
    }

    private NPARevisionCompareNode compareNode(NPARevisionCompareNode node) {
        if (node.getNodeLeft() != null && node.getNodeRight() == null) {
            return node;
        }
        else if (node.getNodeRight() != null && node.getNodeLeft() == null) {
            return node;
        }
        else {
            if (node.getDiffs() != null) {
                return node;
            }
            else if (node.getChildren().size() == 0) {
                return null;
            }
            else {
                node.setChildren(compareNode(node.getChildren()));
                if (node.getChildren().size() == 0) {
                    return null;
                }
            }
        }
        return node;
    }

    private List<NPARevisionCompareNode> compareNode(List<NPARevisionCompareNode> children) {
        for (int i = 0; i < children.size();) {
            if (compareNode(children.get(i)) == null) {
                children.remove(i);
            }
            else {
                i++;
            }
        }
        return children;
    }
}
