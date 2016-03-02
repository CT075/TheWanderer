package io.camdar.eng.wanderer.model.nav;

// Represents a single tile
public class Tile {
    public static final int NUM_TYPES = 5;
    
    private final int id;

    public Tile(int x) {
        id = x;
    }

    public Tile() {
        id = 0;
    }

    public int getID() {
        try{
        return id;
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            return 0;
        }
    }
    
    // TODO: terrain
    
    public static Tile[] convertInts(int[] nums) {
        Tile[] result = new Tile[nums.length];
        for (int i = 0 ; i < nums.length ; i++) {
            result[i] = new Tile(nums[i]);
        }
        return result;
    }
}
