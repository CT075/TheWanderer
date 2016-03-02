package io.camdar.eng.wanderer.model.unit;

// Enemy is designed to attack the player.
public class Enemy extends LivingGameEntity{
    
    // All enemy's have a base attack of 3.
    private static final int BASE_ATTACK = 3;
    
    private int dirMoving = 1;

    public Enemy(int xLoc, int yLoc) {
        super(xLoc, yLoc);
        
        // Sets default stats for the enemy.
        initializeMaxHealth(5);
        setDefense(0);
        incBaseAttack(BASE_ATTACK);
        setAttackRange(1);
    }
    
    // Allows for a much more specialized enemy, with specific stats.
    public Enemy(int xLoc, int yLoc, int hp, int att, int def, int attRange) {
        super(xLoc, yLoc);
        initializeMaxHealth(hp);
        setDefense(def);
        incBaseAttack(BASE_ATTACK + att);
        setAttackRange(attRange);
    }
    
    // When updated, the enemy searches for the player, and attacks the player
    // if she is within attack range.
    public void update() {
        
        super.update();
        
        if (canAttack(player)) {
            attackPlayer();
            return;
        }
        
        //calculatePathing();
    }
    
    //  ------- PRIVATE METHODS -------
    
    // Attacks the player.
    private void attackPlayer() {
        player.takeDamage(calculateDamage(this, player));
    }
    
    // Searches for the player.
    @Deprecated //Deprecated because it was causing errors where the enemies
    // would teleport across a room, and move multiple times in a single
    // update. CalculatePathing() was originally designed for the enemies
    // to move towards the player.
    private void calculatePathing() {
        // If the enemy is in a hallway, continues moving down the hall.
        if (room == null) {
            if (dirMoving == 1) {
                if (move(1)) {
                    return;
                }
            } else if (dirMoving == 2) {
                if (move(2)) {
                    return;
                }
            } else if (dirMoving == 3) {
                if (move(3)) {
                    return;
                }
            } else if (dirMoving == 4) {
                if (move(4)) {
                    return;
                }
            }
            dirMoving = (int) (Math.random() * 4 + 1);
        // If the enemy and the player are in the same room, moves towards the
        // player.
        } else if (room.isInRoom(player)) {
            // Moves towards the player.
            if (player.getXLoc() > xLocation) {
                move(2);
            } else if (player.getXLoc() < xLocation) {
                move(4);
            } else if(player.getYLoc() > yLocation) {
                move(3);
            } else {
                move(1);
            }
        // If in a room other than the player's, moves out of it.
        } else {
            if (dirMoving == 1 || dirMoving == 3) {
                if (floorTiles[yLocation][xLocation + 1].getID() == 1) {
                    dirMoving = 2;
                    move(2);
                    return;
                } else if (floorTiles[yLocation][xLocation - 1].getID() == 1) {
                    dirMoving = 4;
                    move(4);
                    return;
                } else {
                    move(dirMoving);
                }
            } else {
                if (floorTiles[yLocation + 1][xLocation].getID() == 1) {
                    dirMoving = 3;
                    move(3);
                    return;
                } else if (floorTiles[yLocation - 1][xLocation].getID() == 1) {
                    dirMoving = 1;
                    move(1);
                    return;
                } else {
                    move(dirMoving);
                }
            }
        }
    }
    
    // ------- GET AND SET METHODS -------
    


}
