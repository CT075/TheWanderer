package io.camdar.eng.wanderer.items;

public class Purse extends Item {
    
    public Purse() {
        ItemID = "Purse";
    }
    
    public String getDescription() {
        return "Gain 1 attack when equipped.";
    }

    public int attackIncrease() {
        return 1;
    }

    public int defenseIncrease() {
        return 0;
    }
    
    public int healthIncrease () {
        return 0;
    }

}
