package io.camdar.eng.wanderer.items;

public class Badge extends Item {
    
    public Badge() {
        ItemID = "Badge";
    }
    
    public String getDescription() {
        return "Badge of honor. +2 defense.";
    }

    public int attackIncrease() {
        return 0;
    }

    public int defenseIncrease() {
        return 2;
    }
    
    public int healthIncrease () {
        return 0;
    }
}
