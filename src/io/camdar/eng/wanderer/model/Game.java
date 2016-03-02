package io.camdar.eng.wanderer.model;

import java.util.ArrayList;

import io.camdar.eng.wanderer.items.CookieRecipe;
import io.camdar.eng.wanderer.items.Item;
import io.camdar.eng.wanderer.model.nav.Floor;
import io.camdar.eng.wanderer.model.nav.Room;
import io.camdar.eng.wanderer.model.nav.Tile;
import io.camdar.eng.wanderer.model.unit.Enemy;
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
    
    // This is a homebrewed implementation of the Observer pattern. This isn't
    // quite a threaded application so we don't need to really implement the
    // observer pattern 
    public boolean updated = false;
    public boolean showShop = false;
    
    // Updates the game state based on input from the player
    // This method should only be called when the player takes some action
    public void update(int input) {
        if (pc.move(input)) {
            if (currFloor.getGrid()
                    [GameEntity.player.getYLoc()]
                    [GameEntity.player.getXLoc()].getID() == 4
            ) {
                nextFloor();
            }
            if (currFloor.getTile(
                    GameEntity.player.getXLoc(),
                    GameEntity.player.getYLoc()
                ).getID()==3
            ) {
                showShop = true;
            }
            currFloor.step();
            if (pc.getCurHealth() <= 0) { gameOver = true; }
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
    
    // This displays the number of cookies that the player has
    // on the screen.
    public String formatCookieCount() {
        return String.format("%s", pc.getCookieCount());
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
    
    // This gets the player's inventory.
    public ArrayList<Item> getInventory() { 
        return pc.getInventory(); 
    }

    // This gets the player's list of cookies.
    public ArrayList<CookieRecipe> getCookieList() { 
        return pc.getCookieList(); 
    } 
    
    // Move to the next floor
    private void nextFloor() {
        int x = 40 + currentLevel * 10;
        currFloor.generateFloor(x, x);
        setupFloor();
        currentLevel += 1;
    }
    
    // Sets up the current floor, generating it and figuring out the spawn point
    // of the player. Also sets up initial enemy and item spawns.
    private void setupFloor() {
        int i = 0;
        boolean isItem;
        while (i < currFloor.getRoomsOnFloor().size()) {
            isItem = (Math.random() > .5);
            Room room = currFloor.getRoomsOnFloor().get(i);
            int spawnX = room.getTLTX() + 
                    ((int) (Math.random() * room.getWidth()));
            int spawnY = room.getTLTY() +
                    ((int) (Math.random() * room.getHeight()));
            
            if (!isItem) {
                Enemy enemy = new Enemy(spawnX, spawnY, 4 + currentLevel,
                        2 * currentLevel - 2, 2 * currentLevel - 2, 1);
                if (currFloor.addGameEntity(enemy)) {
                    enemy.setRoom(room);
                    i++;
            }
                
            } else if (isItem && currFloor.addGameEntity(
                    randomGenerateItem(spawnX, spawnY))) {
                i++;
            }
        }
    }
    
    private ItemEntity randomGenerateItem(int spawnX, int spawnY) {
        return new ItemEntity(spawnX, spawnY, (int) (Math.random() * 86));
    }
    
    // Initialize the first floor
    public static Game init(boolean genFloor) {
        new Player(0,0);
        if (genFloor) {
           return new Game(new Floor(40, 40));
        } 
        else {
            return new Game(new Floor (40, 40));
        }
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

    public void exitShop() {
        pc.move(3);
        showShop=false;
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
    
    public void spendCookies(int i){
        pc.decreaseCookieCount(i);
    }
    
    public boolean canBuy(int i){
        if (pc.getCookieCount()>i){ return true; }
        return false;
    }
}
