package io.camdar.eng.wanderer.model.unit;

import java.util.ArrayList;

import io.camdar.eng.wanderer.items.Item;
import io.camdar.eng.wanderer.model.nav.Room;

// LivingGameEntity is the representation of an Enemy or a Player. Each LGE has
// combat statistics and methods specific to movement.
public class LivingGameEntity extends GameEntity {
    
    // The LivingGameEntity's stats.
    private int curHealth;
    private int maxHealth;
    protected int defense;
    protected int attack;
    private int baseAttack=0;
    private int baseDefense=0;
    private int attackRange;
    
 // The room the Enemy is in. Used to track the player.
     protected Room room;
     
    // The inventory is the list of all of the items the LivingGameEntity is
    // carrying.
    public ArrayList<Integer> inventory;
    
    // Stores the previous location of the GameEntity. This is needed so that
    // movement can be reverted in cases of unit collision or wall collision.
    private int lastXLocation;
    private int lastYLocation;

    // GameEntity that can move initializer
    // @param xLoc
    // @param yLoc
    public LivingGameEntity(int xLoc, int yLoc) {
        xLocation = xLoc;
        yLocation = yLoc;
        inventory = new ArrayList<Integer>();
    }
    
    // ------- METHODS -------
    
    // Moves the Entity one unit in the specified direction
    // 1 is up
    // 2 is right
    // 3 is down
    // 4 is left
    // True is returned to signify the movement has been successful, and
    // false is returned to signify the movement has failed.
    public boolean move(int direction) {
        lastXLocation = getXLoc();
        lastYLocation = getYLoc();
        
        // Moves down.
        if (direction == 1) {
            setYLoc(getYLoc() - 1);
        // Moves right.
        } else if (direction == 2) {
            setXLoc(getXLoc() + 1);
        // Moves down.
        } else if (direction == 3) {
            setYLoc(getYLoc() + 1);
        // Moves left.
        } else if (direction == 4) {
            setXLoc(getXLoc() - 1);
        // Throws an error if the direction is invalid.
        } else {
            throw new IllegalArgumentException(
                    "Invalid direction for LGE movement");
        }

        //If the LivingGameEntity has moved outside of the map, revert movement.
        if (!isInMap()) {
            revertMovement();
            return false;
        }
        
        // If the unit has moved to an illegal location, revert movement.
        if (!curFloor.unitHasMoved(this, xLocation, yLocation)) {
            revertMovement();
            return false;
        }
        
        // Return true to indicate the movement was successful.
        return true;
    }

    // Takes a certain amount away from the LivingGameEntity's current health.
    public void takeDamage(int dmg) {
        if (dmg <= 0) { return; }
        curHealth -= dmg;
        if (curHealth <= 0) { remove(); }
    }

    // Returns true if this LivingGameEntity is in range to attack
    // LivingGameEntity other.
    public boolean canAttack(LivingGameEntity other) {
        // Calculates the distance between the two LivingGameEntities.
        int xDistance = xLocation - other.getXLoc();
        int yDistance = yLocation - other.getYLoc();
        double distance = Math.sqrt(xDistance*xDistance + yDistance*yDistance);

        // If in range, return true.
        if (1.0 * attackRange >= distance) {
            return true;
        }
        
        return false;
    }

    // Adds Item item to the LivingGameEntity's inventory.
    public boolean addItem(int item) {
        inventory.add(item);
        return true;
    }

    // If two LivingGameEntities try to occupy the same tile, the one who
    // has moved damages this one.
    public void dealWithCollision(LivingGameEntity other) {
        takeDamage(calculateDamage(other, this));
        if (curHealth > 0) {
            other.revertMovement();
        }
    }
    
    // Moves the LivingGameEntity back to its previous locatoin.
    public void revertMovement() {
        setXLoc(lastXLocation);
        setYLoc(lastYLocation);
    }
    
    // Updates the LivingGameEntity's stats based off of its equipped items.
    public void update() { }
    
    // ------- STATIC METHODS -------

    // Returns damage based off of two LivingGameEntity's stats.
    public static int calculateDamage(
            LivingGameEntity attacker, LivingGameEntity defender) {
        return attacker.getBaseAttack() + 
                attacker.getAttack() - 
                defender.getDefense();
    }

    // ------- SET AND GET METHODS -------

    // Sets maxHealth, and fills curHealth to max.
    public void initializeMaxHealth(int hp) {
        maxHealth = hp;
        setCurHealth(hp);
    }
    
    public void incHealthStat(int i){
        maxHealth+=i;
        curHealth+=i;
    }
    
    public void heal(int i){
        for (int k=0;k<i;k++){
            if (curHealth<maxHealth){
                curHealth++;
            }
        }
    }

    public void setMaxHealth(int hp) {
        maxHealth = hp;
    }

    public void setCurHealth(int hp) {
        curHealth = hp;
    }

    public void setDefense(int df) {
        defense = df;
    }

    public void setAttack(int att) {
        attack = att;
    }

    public void incBaseAttack(int att) {
        baseAttack+= att;
    }

    public void incBaseDefense(int def) {
        baseDefense+= def;
    }

    public void setAttackRange(int attRange) {
        attackRange = attRange;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurHealth() {
        return curHealth;
    }

    public int getDefense() {
        return defense;
    }

    public int getAttack() {
        return attack;
    }
    
    public int getBaseAttack() {
        return baseAttack;
    }

    public int getAttackRange() {
        return attackRange;
    }
    
    public ArrayList<Integer> getInventory() {
        return inventory;
    }
    
    public int getLastXLocation() {
        return lastXLocation;
    }
    
    public int getLastYLocation() {
        return lastYLocation;
    }
    
    public void setRoom(Room room) {
        this.room = room;
    }
    // This is here to make the abstract method stop complaining.
    public Item getItem() {
        return null;
    }
}
