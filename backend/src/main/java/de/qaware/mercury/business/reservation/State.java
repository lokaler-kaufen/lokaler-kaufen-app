package de.qaware.mercury.business.reservation;

import lombok.Getter;

public enum State {
    BREMEN("hb"),
    HAMBURG("hh"),
    BERLIN("be"),
    SAARLAND("sl"),
    SCHLESWIG_HOLSTEIN("sh"),
    THURINGIA("th"),
    SAXONY("sn"),
    RHINELAND_PALATINATE("rp"),
    SAXONY_ANHALT("st"),
    HESSE("he"),
    MECKLENBURG_WESTERN_POMERANIA("mv"),
    BRANDENBURG("bb"),
    NORTHRHINE_WESTPHALIA("nw"),
    BADEN_WUERTTEMBERG("bw"),
    LOWER_SAXONY("ni"),
    BAVARIA("by");

    @Getter
    private final String code;

    State(String code) {
        this.code = code;
    }
}
