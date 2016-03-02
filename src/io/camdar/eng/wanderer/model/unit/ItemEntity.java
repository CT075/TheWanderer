package io.camdar.eng.wanderer.model.unit;

import io.camdar.eng.wanderer.items.Item;

// ItemEntity is the representation of an item on the map.
public class ItemEntity extends GameEntity {
    private int id;
    
    public static ItemEntity getRandomIE(int x, int y) {
        int id = Item.getRandomItem();
        if (id == -1) {
            return null;
        }
        return new ItemEntity(x, y, id);
    }
    
    // Generates the item entity.
    public ItemEntity(int xLoc, int yLoc, int id) {
        xLocation = xLoc;
        yLocation = yLoc;
        this.id = id;
    }

    // If something moves onto this ItemEntity, adds the appropriate item
    // to the LivingGameEntity's inventory.
    public void dealWithCollision(LivingGameEntity other) {
        if (other.addItem(this.id)) {
            this.remove();
        }
    }

    // Allows for two items to share space on the map.
    public boolean addItem(int item) {
        return false;
    }
    
    // Does not need to revert movement, as movement is impossible!
    public void revertMovement() { }

    // Does nothing when updated, other than look pretty.
    public void update() { }
}
