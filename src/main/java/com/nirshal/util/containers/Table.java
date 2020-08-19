package com.nirshal.util.containers;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.HashMap;

@Data
@AllArgsConstructor
public class Table<T> extends HashMap<Index, T> {

    private T emptyCell;

    public int getMaxRow() {
        return this.isEmpty() ?
                -1
                :
                this.keySet().stream().map(Index::getX).reduce(0, Math::max);
    }
    public int getMaxColumn() {
        return this.isEmpty() ?
                -1
                :
                this.keySet().stream().map(Index::getY).reduce(0, Math::max);
    }

    public int getRowsSize(){
        return getMaxRow() +1;
    }
    public int getColumnsSize(){
        return getMaxColumn() +1;
    }

    public T get(Integer row, Integer column) {
        return this.getOrDefault(new Index(row, column), emptyCell);
    }

    public Table<T> put(Integer row, Integer column, T value){
        this.put(new Index(row,column), value);
        return this;
    }

    public Table<T> append(Integer row, T value){
        this.put(new Index(row, getColumnsSize()), value);
        return this;
    }
}
