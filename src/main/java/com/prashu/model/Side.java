package com.prashu.model;

public enum Side {
    Buy ('B'),
    Sell ('S');

    private final Character side;

    Side(Character side){
        this.side = side;
    }

    Character getSide(){
        return this.side;
    }

}
