package org.slizaa.server.graphql.hierarchicalgraph;

public class Cell {

    private int row;

    private int column;

    private int value;

    public Cell(int column, int row, int value) {
        this.column = column;
        this.row = row;
        this.value = value;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getValue() {
        return value;
    }
}

