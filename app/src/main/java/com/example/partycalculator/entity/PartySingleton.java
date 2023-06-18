package com.example.partycalculator.entity;

public class PartySingleton {
    private static PartySingleton instance;
    private Party party;

    private PartySingleton() {
    }

    public static PartySingleton getInstance() {
        if (instance == null) {
            instance = new PartySingleton();
        }
        return instance;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
