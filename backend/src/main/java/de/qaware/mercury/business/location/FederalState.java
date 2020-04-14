package de.qaware.mercury.business.location;

import lombok.Getter;

public enum FederalState {
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

    FederalState(String code) {
        this.code = code;
    }

    public static FederalState parse(String input) {
        for (FederalState federalState : values()) {
            if (federalState.getCode().equalsIgnoreCase(input)) {
                return federalState;
            }
        }

        throw new InvalidFederalStateException(input);
    }
}
