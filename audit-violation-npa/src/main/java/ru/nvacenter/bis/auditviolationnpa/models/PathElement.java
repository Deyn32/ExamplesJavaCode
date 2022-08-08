package ru.nvacenter.bis.auditviolationnpa.models;

import org.apache.commons.lang3.StringUtils;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oshesternikova on 21.02.2018.
 * Элемент пути
 */
public class PathElement {

    private String name;
    private String num;
    private Long order;
    private String text;

    private static final String PARAGRAPH = "Абзац";
    private static final String HASH_FUNCTION_NAME = "SHA-256";

    public PathElement(NVA_SPR_AUD_NPA_STRUCTURE str) {
        this.text = str.getText();
        this.name = str.getElementType();
        this.num = str.getElementNumber();
        this.order = str.getElementOrder();
    }


    public boolean Match(NVA_SPR_AUD_NPA_STRUCTURE str){
        if (StringUtils.isBlank(str.getElementType()) &&
                StringUtils.isBlank(str.getElementNumber()) &&
                StringUtils.isBlank(name) &&
                StringUtils.isBlank(num)) /*return order == str.getElementOrder()*/ return  compareByTextHash(text, str.getText());
        return StringUtils.equals(normilizeType(str.getElementType()), normilizeType(name)) && StringUtils.equals(normilizeNumber(str.getElementNumber()), normilizeNumber(num));
    }

    private static String normilizeType(String s){
        return StringUtils.equals(s, PARAGRAPH) ? StringUtils.EMPTY : s;
    }
    private static String normilizeNumber(String s){
        return s.endsWith(".") ? s.substring(0, s.length() - 1) : s;
    }
    private static boolean compareByTextHash(String txt1, String txt2){
        try {
            MessageDigest messageDigest1 = MessageDigest.getInstance(HASH_FUNCTION_NAME);
            messageDigest1.update(txt1.getBytes());
            MessageDigest messageDigest2 = MessageDigest.getInstance(HASH_FUNCTION_NAME);
            messageDigest2.update(txt2.getBytes());
            return MessageDigest.isEqual(messageDigest1.digest(), messageDigest2.digest());
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(num)){
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(HASH_FUNCTION_NAME);
                messageDigest.update(text.getBytes());
                String encryptedString = new String(messageDigest.digest());
                return encryptedString;
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }
        return String.format("%s %s", name, num);
    }
}
