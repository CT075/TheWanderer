package io.camdar.eng.wanderer.model;

import java.util.ArrayList;

import io.camdar.eng.wanderer.model.nav.Floor;
import io.camdar.eng.wanderer.model.nav.Room;
import io.camdar.eng.wanderer.model.nav.Tile;
import io.camdar.eng.wanderer.model.unit.GameEntity;
import io.camdar.eng.wanderer.model.unit.ItemEntity;
import io.camdar.eng.wanderer.model.unit.Player;

// A "container" class containing convenience "hooks" for a relevant view
// as well as encapsulating the entire game state at once.
public class Game {
    // Holds all of the relevant data for navigation
    private Floor currFloor;
    private int currentLevel;
    private Player pc;
    public boolean gameOver = false;
    public boolean won = false;
    
    private final int SPAWN_RATE = 10;
    private int steps = 0;
    
    // This is a homebrewed implementation of the Observer pattern. This isn't
    // quite a threaded application so we don't need to really implement the
    // observer pattern 
    public boolean updated = false;
    
    // Updates the game state based on input from the player
    // This method should only be called when the player takes some action
    public void update(int input) {
        if (pc.move(input)) {
            currFloor.step();
            if (!won) {
                this.steps += 1;
                if (this.steps >= SPAWN_RATE) {
                    this.steps = 0;
                    // attempt to spawn a random item in a random room
                    int r1 = (int) (
                            Math.random() * currFloor.getRoomsOnFloor().size()
                    );
                    Room room = currFloor.getRoomsOnFloor().get(r1);
                    int spawnX = room.getTLTX() + 
                            ((int) (Math.random() * room.getWidth()));
                    int spawnY = room.getTLTY() +
                            ((int) (Math.random() * room.getHeight()));
                    spawnItem(spawnX, spawnY);
                    // TODO: spawn a random enemy in a random room
                }
                if (pc.getCurHealth() <= 0) { gameOver = true; }
            }
        }
        pc.update();
        this.updated = true;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    // This returns which tile a game entity (an item is on.
    // getters and setters
    public Tile getTile(int x, int y){
        // This detects if the game entity is within walls.
        if (x < 0 || y < 0) { return Floor.WALL; }
        return currFloor.getTile(x, y);
    }
    
    // This returns which entity is on a specific tile.
    public GameEntity getEntity(int x, int y){
        if (x < 0 || y < 0) { return null; }
        return currFloor.getEntity(x, y);
    }
    
    public int getPlayerXLoc() { return pc.getXLoc(); }
    public int getPlayerYLoc() { return pc.getYLoc(); }
    public Floor currentFloor() { return currFloor; }
    // This gets all the game entities on the floor.
    public GameEntity[][] getEntities(){ return currFloor.getUnits(); }
    
    // This displays both the current health of the player
    // and the maximum health that the player can have.
    public String formatPlayerHP() {
        int c_hp = pc.getCurHealth();
        int m_hp = pc.getMaxHealth();
        return String.format("%d/%d", c_hp, m_hp);
    }
    
    // This displays the player attack on the screen.
    public String formatPlayerAtk() {
        return String.format("%s", pc.getAttack());
    }
    
    // This displays the player defense on the screen.
    public String formatPlayerDef() {
        return String.format("%s", pc.getDefense());
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
    
    // This gets the player's inventory.
    public ArrayList<Integer> getInventory() { 
        return pc.getInventory(); 
    }
    
    // Sets up the current floor, generating it and figuring out the spawn point
    // of the player. Also sets up initial enemy and item spawns.
    private void setupFloor() {
        int i = 0;
        while (i < currFloor.getRoomsOnFloor().size()) {
            Room room = currFloor.getRoomsOnFloor().get(i);
            int spawnX = room.getTLTX() + 
                    ((int) (Math.random() * room.getWidth()));
            int spawnY = room.getTLTY() +
                    ((int) (Math.random() * room.getHeight()));
            if (spawnItem(spawnX, spawnY)) {
                i += 1;
            };
        }
    }
    
    private boolean spawnItem(int x, int y) {
        ItemEntity ie = ItemEntity.getRandomIE(x, y);
        if (ie == null) {
            this.won = true;
            return true;
        }
        return currFloor.addGameEntity(ie);
    }
    
    // Initialize the first floor
    public static Game init(boolean genFloor) {
        new Player(0,0);
        return new Game(new Floor(40, 40));
    }
    
    // Creates entities for us to test
    // This sets up the floor.
    private Game(Floor f) {
        this.pc = GameEntity.player;
        pc.setFloor(f);
        this.currFloor = f;
        this.currentLevel = 1;
        setupFloor();
    }

    public void addAttack(int i) {
        pc.incBaseAttack(i);
    }
    
    public void addDefense(int i){
        pc.incBaseDefense(i);
    }
    
    public void addHealth(int i){
        pc.incHealthStat(i);
    }
}
