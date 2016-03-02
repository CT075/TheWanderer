package io.camdar.eng.wanderer.model.nav;

import java.util.ArrayList;

import io.camdar.eng.wanderer.model.unit.*;

// Represents a "grid" of terrain types and units on the floor.
public class Floor {
    
    // Each tile represents one square on the map.
    public static final Tile WALL = new Tile(0);
    public static final Tile PATH = new Tile(1);
    public static final Tile ROOM = new Tile(2);
    public static final Tile SHOP = new Tile(3);
    public static final Tile EXIT = new Tile(4);
    
    // The grid that represents the map.
    private Tile[][] floorTiles;
    
    // A grid that represents each GameEntity's location
    // on the map.
    private GameEntity[][] units;

    protected static ArrayList<Room> roomsOnFloor;
    
    public Floor(int x, int y) {
        floorTiles = new Tile[y][x];
        units = new GameEntity[y][x];
        generateFloor(x, y);
    }
    
    // Returns the appropriate grid.
    public Tile[][] getGrid() { return floorTiles; }
    public GameEntity[][] getUnits() { return units; }

    // Returns the tile at the specified point. Returns a wall if the tile is
    // not on the map.
    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 ||
            x >= floorTiles[0].length || y >= floorTiles.length) {
            return WALL;
        }
        Tile result = floorTiles[y][x];
        return result;
    }
    
    // Returns the GameEntity at the given tile, or null if there is no Entity
    // or if the tile is not on the map.
    public GameEntity getEntity(int x, int y){
        if (x < 0 || y < 0 ||
                x >= floorTiles[0].length || y >= floorTiles.length) {
                return null;
            }
            GameEntity result = units[y][x];
            return result;
    }

    public int getWidth() {
        return floorTiles[0].length;
    }

    public int getHeight() {
        return floorTiles.length;
    }
    
    public ArrayList<Room> getRoomsOnFloor() {
        return roomsOnFloor;
    }
    
    // Control what happens for a single "turn" (or "step") of the game. Only
    // called when the player takes an action.
    public void step() {
        for (GameEntity[] row : units) {
            for (GameEntity unit : row) {
                if (unit != null) { unit.update(); }
            }
        }
    }
    
    public void setTile(int x, int y, Tile tile){
        floorTiles[y][x] = tile;
    }

    // Attempts to move the unit to the given tile. Returns false if the tile
    // is not valid for movement.
    public boolean unitHasMoved(LivingGameEntity lge, int xLoc, int yLoc) {
        // If the tile is a wall, the tile is ineligable for movement.
        if (floorTiles[yLoc][xLoc] == WALL) {
            return false;
        }
        // If there is a unit on the tile, have the entity 
        // deal with the collision.
        if (units[yLoc][xLoc] != null) {
            units[yLoc][xLoc].dealWithCollision(lge);
        } 
        // If the tile is now null, move the unit to the new tile, and clear the
        // old tile. Indicate to the LivingGameEntity if it has moved into or
        // out of a room.
        if (units[yLoc][xLoc] == null) {
            units[lge.getLastYLocation()][lge.getLastXLocation()] = null;
            units[yLoc][xLoc] = lge;
            if (floorTiles[yLoc][xLoc] == ROOM) {
                lge.setRoom(determineRoom(xLoc, yLoc));
            } else if (floorTiles[yLoc][xLoc] == PATH) {
                lge.setRoom(null);
            }
        }
        return true;
    }
    
    // Removes the gameEntity from the grid, then moves it off the map to avoid
    // View confusion.
    public void removeGameEntity(GameEntity ge) {
        if (units[ge.getYLoc()][ge.getXLoc()] != ge) {
            throw new IllegalArgumentException("This unit doesn't exist!");
        }
        units[ge.getYLoc()][ge.getXLoc()] = null;
        ge.setXLoc(-1);
        ge.setYLoc(-1);
    }

    // Adds the gameEntity to the map, if its coordiantes are available for
    // population.
    public boolean addGameEntity(GameEntity ge) {
        if (units[ge.getYLoc()][ge.getXLoc()] != null
                || floorTiles[ge.getYLoc()][ge.getXLoc()] == WALL) {
            return false;
        }
        units[ge.getYLoc()][ge.getXLoc()] = ge;
        ge.setFloor(this);
        return true;
    }
  
    // Generates a new floor.
    public void generateFloor(int width, int height) {
        
        // Creates a new map for this floor.
        units = new GameEntity[height][width];
        floorTiles = new Tile[height][width];
        int[][] floorTilesCreator;
        // Make a grid of ints for the map to cast from
        floorTilesCreator = FloorFactory.createFloor(width, height);
        
        // Takes the generated int grid and applies it to the map.
        for (int i = 0; i < floorTiles.length; i++) {
            for (int j = 0; j < floorTiles[0].length; j++) {
                if (floorTilesCreator[i][j] == 0) {
                    floorTiles[i][j] = WALL;
                } else if (floorTilesCreator[i][j] == 1) {
                    floorTiles[i][j] = PATH;
                } else if (floorTilesCreator[i][j] == 2) {
                    floorTiles[i][j] = ROOM;
                } else {
                    throw new IllegalArgumentException(
                            "Floor Generation messed up.");
                }
            }
        }
        
        // Determines which room the player spawns in, which
        // room the shop spawns in, and which room the exit
        // spawns in.
        Room playerRoom;
        Room shopRoom;
        Room exitRoom;
        
        int spawnX;
        int spawnY;
        
        // Chooses a room for the Player to spawn in.
        playerRoom = roomsOnFloor.get((int) (Math.random() * roomsOnFloor.size()));
        
        // Chooses a room for the shop to spawn in.
        do {
            shopRoom = roomsOnFloor.get((int) (Math.random() * roomsOnFloor.size()));
        } while (shopRoom == playerRoom);
        
        // Chooses a room for the exit to spawn in.
        do {
            exitRoom = roomsOnFloor.get((int) (Math.random() * roomsOnFloor.size()));
        } while (exitRoom == playerRoom || exitRoom == shopRoom);
        
        // Spawns the player in the specified room.
        spawnX = playerRoom.getTLTX() + 
                ((int) (Math.random() * playerRoom.getWidth()));
        spawnY = playerRoom.getTLTY() +
                ((int) (Math.random() * playerRoom.getHeight()));
        
        // Places the player.
        units[spawnY][spawnX] = GameEntity.player;
        GameEntity.player.setXLoc(spawnX);
        GameEntity.player.setYLoc(spawnY);
        
        // Spawns the exit in the specified room.
        spawnX = exitRoom.getTLTX() + 
                ((int) (Math.random() * exitRoom.getWidth()));
        spawnY = exitRoom.getTLTY() +
                ((int) (Math.random() * exitRoom.getHeight()));
        
        // Places the exit.
        floorTiles[spawnY][spawnX] = EXIT;
        
        // Spawns the shop in the specified room.
        spawnX = (shopRoom.getTLTX() + shopRoom.getTRTX()) / 2;
        spawnY = shopRoom.getTLTY();
        
        // Places the shop.
        floorTiles[spawnY][spawnX] = SHOP;
    }
    
    // Determines which room the given coordinates are in. 
    // Returns null otherwise.
    private Room determineRoom(int x, int y) {
        for (int i = 0; i < roomsOnFloor.size(); i++) {
            Room room = roomsOnFloor.get(i);
            if (x > room.getTLTX() && x < room.getTRTX() &&
                    y > room.getTRTY() && y < room.getBRTY()) {
                return room;
            }
        }
        return null;
    }

}
