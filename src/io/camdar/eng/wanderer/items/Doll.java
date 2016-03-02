package io.camdar.eng.wanderer.items;

public class Doll extends Item{
    
    public Doll() {
        ItemID = "Doll";
    }

    public String getDescription() {
        return "The aura of the doll protects you. +10 defense";
    }

    public int attackIncrease() {
        return 0;
    }

    public int defenseIncrease() {
        return 10;
    }
    
    public int healthIncrease () {
        return 0;
    }
}
