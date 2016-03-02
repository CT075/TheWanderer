package io.camdar.eng.wanderer.view;

import javafx.scene.image.Image;
import io.camdar.eng.wanderer.model.unit.Enemy;
import io.camdar.eng.wanderer.model.unit.GameEntity;
import io.camdar.eng.wanderer.model.unit.ItemEntity;
import io.camdar.eng.wanderer.model.unit.Player;

// An EntitySprite is a Sprite attached to a specific GameEntity. This is to
// allow each Sprite to be created at runtime in a "behind-the-scenes" fashion,
// akin to the factory pattern. Additionally, it means that each sprite can be
// updated by itself, instead of having to scan through the units array of a
// given Floor to update Sprite positions.
public class EntitySprite extends Sprite {
    public static final Sprite PLAYER_SPRITE = new Sprite(
            new Image("file:res/char.png"), 60, 70
    );
    public static final Sprite ENEMY_SPRITE = new Sprite(
            new Image("file:res/enemy.png"), 60, 70
    );
    public static final Sprite ITEM_SPRITE = new Sprite(
            new Image("file:res/item.png"), 70, 70
    );
    public static final Sprite RECIPE_SPRITE = new Sprite(
            new Image("file:res/recipe.png"), 64, 64
    );
    public static final Sprite LIPSTICK_SPRITE = new Sprite(
            new Image("file:res/lipistick.png"), 64, 64
    );
    public static final Sprite MASCARA_SPRITE = new Sprite(
            new Image("file:res/mascara.png"), 64, 64
    );
    public static final Sprite BADGE_SPRITE = new Sprite(
            new Image("file:res/badge.png"), 64, 64
    );
    
    // This is a convenience "factory" method that gets the right Sprite for
    // the relevant gameEntity.
    private static Sprite determineSprite(GameEntity ge) {
        if (ge instanceof Player) { return PLAYER_SPRITE; }
        if (ge instanceof Enemy) { return ENEMY_SPRITE; }
        if (ge instanceof ItemEntity) {
            return ITEM_SPRITE; 
        }
        return null;
    }
    
    private GameEntity entity;
    
    public EntitySprite(GameEntity ge) {
        super(determineSprite(ge));
        this.entity = ge;
    }
    
    public boolean isInMap() {
        return this.entity.isInMap();
    }
    
    // Changes the position of this Sprite to be in the correct position
    // relative to the player's location.
    public void updatePosition() {
        // If the entity we're displaying is a Player, we don't need to check
        // anything, since we're always in the center of the screen.
        if (this.entity instanceof Player) {
            this.setTileLocation(
                    ViewConstants.TOP_LEFT_BLANKS,
                    ViewConstants.TOP_LEFT_BLANKS
            );
        }
        else {
            int xDist = entity.distToPlayerX();
            int yDist = entity.distToPlayerY();
            // The +-1 is because TOP_LEFT_BLANKS are for a 1-origin coordinate
            // system, but the map uses a 0-origin coordinate system
            if (xDist > -ViewConstants.TOP_LEFT_BLANKS-1 &&
                xDist < ViewConstants.BOT_RIGHT_BLANKS+1 &&
                yDist > -ViewConstants.TOP_LEFT_BLANKS-1 &&
                yDist < ViewConstants.BOT_RIGHT_BLANKS+1
            ) {
                int mapX = ViewConstants.TOP_LEFT_BLANKS + xDist;
                int mapY = ViewConstants.TOP_LEFT_BLANKS + yDist;
                this.setX(mapX * ViewConstants.TILE_DIMENSIONS);
                this.setY(mapY * ViewConstants.TILE_DIMENSIONS);
                this.setVisible(true);
            }
            else {
                this.setVisible(false);
            }
        }
    }
}
