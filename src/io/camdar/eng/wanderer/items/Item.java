package io.camdar.eng.wanderer.items;

// An item gives a LivingGameEntity stats when it is in the LGE's inventory.
public abstract class Item {
    // The item's name.
    protected String ItemID;
    
    // Check if the given String is the same as the ItemID.
    public boolean checkIfIsItem(String str) {
        return (ItemID.equals(str));
    }
    
    // Returns the description of the item.
    public abstract String getDescription();
    
    // Returns the attack, defense, and health increase granted by the item.
    public abstract int attackIncrease();
    public abstract int defenseIncrease();
    public abstract int healthIncrease();

    public void setItemID(String id) {
        ItemID = id;
    }
    
    public String getItemID() {
        return ItemID;
    }
}
