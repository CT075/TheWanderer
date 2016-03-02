package io.camdar.eng.wanderer.model.unit;

// The player is user's representation in the game. Main difference from LGE is
// that the player collects cookies on movement.
public class Player extends LivingGameEntity {
    public Player(int xLoc, int yLoc) {
        super(xLoc, yLoc);
        
        // The inital stats for the player.
        initializeMaxHealth(10);
        setDefense(0);
        setAttack(0);
        incBaseAttack(6);
        
        // Sets this player as the player all other classes refer to.
        GameEntity.player = this;
    }
}
