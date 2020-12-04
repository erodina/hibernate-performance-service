package ru.ibs.gasu.utils;

import java.io.Serializable;

public class SortInfo implements Serializable {

    private String field;
    private Direction direction = Direction.ASC;
    public SortInfo() {
    }
    public enum Direction {
        ASC,
        DESC
    }
    public SortInfo(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }
    public SortInfo(String field) {
        this.field = field;
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
