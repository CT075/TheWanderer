package io.camdar.eng.wanderer.items;

public class CootieShot extends Item {
    
    public CootieShot() {
        ItemID = "CootieShot";
    }
    
    public String getDescription() {
        return "Protects against cooties! +2 health.";
    }

    public int attackIncrease() {
        return 0;
    }

    public int defenseIncrease() {
        return 0;
    }
    
    public int healthIncrease () {
        return 2;
    }
}
