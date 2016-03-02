package io.camdar.eng.wanderer.model.nav;

import io.camdar.eng.wanderer.model.unit.GameEntity;

public class Room {    
    // The area that every Room will attempt to generate.
    public final static int PRIORITY_AREA = 40;
    
    // The coordinates for the room's top corner.
    private int topLeftTileX;
    private int topLeftTileY;
    
    // The location of the "Beacon," used for floor generation.
    private int beaconXLocation;
    private int beaconYLocation;

    
    // The height and width of the room.
    private int width;
    private int height;
    
    // Determines if the room is connected to the main body of rooms,
    // used for floor generation.
    private boolean isConnected; 

    // Generates the room.
    public Room(int topLeftTileX, int topLeftTileY) {
        this.topLeftTileX = topLeftTileX;
        this.topLeftTileY = topLeftTileY;
        generateRoom();        
        isConnected = false;
    }
    
    // Returns true if GE is within the given room.
    public boolean isInRoom(GameEntity ge) {
        
        if (ge.getXLoc() >= topLeftTileX 
                && ge.getXLoc() < topLeftTileX + width
                && ge.getYLoc() >= topLeftTileY
                && ge.getYLoc() < topLeftTileY + height) {
            return true;
        }
        return false;
    }
    
    // Generates the width and height, taking the Priority Area into account.
    private void generateRoom() {
        width = (int) (Math.random() * 9 + 4);
        height = (int) (1 + 1.0 * PRIORITY_AREA / width);
    }
    
    
    public void setBeaconX(int x) {
        beaconXLocation = x;
    }
    
    public void setBeaconY(int y) {
        beaconYLocation = y;
    }
    
    public int getBeaconX() {
        return beaconXLocation;
    }
    
    public int getBeaconY() {
        return beaconYLocation;
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public void connectRoom() {
        isConnected = true;
    }
    
    public int getTLTX() {
        return topLeftTileX;
    }
    
    public int getTLTY() {
        return topLeftTileY;
    }
    
    public int getTRTX() {
        return topLeftTileX + width;
    }
    
    public int getTRTY() {
        return topLeftTileY;
    }
    
    public int getBLTX() {
        return topLeftTileX;
    }
    
    public int getBLTY() {
        return topLeftTileY + height;
    }
    
    public int getBRTX() {
        return topLeftTileX + width;
    }
    
    public int getBRTY() {
        return topLeftTileY + width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
}
