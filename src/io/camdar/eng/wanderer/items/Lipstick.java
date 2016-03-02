package io.camdar.eng.wanderer.items;

public class Lipstick extends Item {
    
    public Lipstick() {
        ItemID = "Lipstick";
    }
    
    public String getDescription() {
        return "Boys go crazy at your red lips. +6 defense";
    }

    public int attackIncrease() {
        return 0;
    }

    public int defenseIncrease() {
        return 6;
    }
    
    public int healthIncrease () {
        return 0;
    }
}
