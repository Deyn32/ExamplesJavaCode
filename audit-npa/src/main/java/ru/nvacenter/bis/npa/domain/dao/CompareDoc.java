package ru.nvacenter.bis.npa.domain.dao;
import java.util.List;

/**
 * Created by dmihaylov on 20.10.2017.
 */
public class CompareDoc
{
    private List<String> listOne;
    private List<String> listTwo;

    public List<String> getListOne(){
        return listOne;
    }

    public  List<String> getListTwo(){
        return listTwo;
    }

    public void setListOne(List<String> str){
        listOne = str;
    }

    public void setListTwo(List<String> str){
        listTwo = str;
    }
}
