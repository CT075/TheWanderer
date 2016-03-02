package io.camdar.eng.wanderer.items;

public class Sash extends Item{
    
    public Sash() {
        ItemID = "Sash";
    }
    
    public String getDescription() {
        return "Gain 1 defense when equipped.";
    }

    public int attackIncrease() {
        return 0;
    }

    public int defenseIncrease() {
        return 1;
    }
    
    public int healthIncrease () {
        return 0;
    }
}
