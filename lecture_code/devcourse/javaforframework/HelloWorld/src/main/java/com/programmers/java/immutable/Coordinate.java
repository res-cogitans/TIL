package com.programmers.java.immutable;

public final class Coordinate {

    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate findMidpointOf(Coordinate destination) {
        return new Coordinate(Math.abs((this.x - destination.x) / 2)
                , Math.abs((this.y - destination.y) / 2));
    }
}
