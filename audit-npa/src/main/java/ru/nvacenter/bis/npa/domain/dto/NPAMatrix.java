package ru.nvacenter.bis.npa.domain.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oshesternikova on 25.09.2017.
 * Класс для матрицы (хз, есть ли готовый, проще свой наваять)
 */
public class NPAMatrix<TEl, TIn> {
    private List<TEl> rows;
    private List<TEl> cols;
    private TIn defValue;
    private List<List<TIn>> inner;

    public NPAMatrix(List<TEl> rows, List<TEl> cols, TIn defValue) {
        this.rows = rows;
        this.cols = cols;
        this.defValue = defValue;
        inner = new ArrayList<>(rows.size());
        for(int j = 0; j < rows.size();j++){
            ArrayList<TIn> col = new ArrayList<TIn>(cols.size());
            for(int i =0; i < cols.size(); i++){
                col.add(defValue);
            }
            inner.add(col);
        }
    }

    public void setValue(TEl row, TEl col, TIn val){
        int ind1 = rows.indexOf(row);
        int ind2 = cols.indexOf(col);
        setValue(ind1, ind2, val);
    }

    public void setValue(int indexRow, int indexCol, TIn val){
        this.inner.get(indexRow).set(indexCol, val);
    }

    public TIn getValue(TEl row, TEl col){
        int ind1 = rows.indexOf(row);
        int ind2 = cols.indexOf(col);
        return getValue(ind1, ind2);
    }

    public TIn getValue(int indexRow, int indexCol){
        return this.inner.get(indexRow).get(indexCol);
    }

    public List<TIn> getColumn(TEl row){
        int ind1 = rows.indexOf(row);
        return getColumn(ind1);
    }

    public List<TIn> getColumn(int row){
        return  this.inner.get(row);
    }
}
