package io.camdar.eng.wanderer.items;

public class Heel extends Item{
    
    public Heel() {
        ItemID = "Heel";
    }
    
    public String getDescription() {
        return "Gains 8 extra attack when equipped.";
    }

    public int attackIncrease() {
        return 8;
    }

    public int defenseIncrease() {
        return 0;
    }
    
    public int healthIncrease () {
        return 0;
    }
}
