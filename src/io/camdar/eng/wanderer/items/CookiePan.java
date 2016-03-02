package io.camdar.eng.wanderer.items;

public class CookiePan extends Item{
    
    public CookiePan() {
        ItemID = "CookiePan";
    }
    
    public String getDescription() {
        return "WHAM!! +6 attack.";
    }

    public int attackIncrease() {
        return 6;
    }

    public int defenseIncrease() {
        return 0;
    }
    
    public int healthIncrease () {
        return 0;
    }
}
