package io.camdar.eng.wanderer.model.unit.tests;

import io.camdar.eng.wanderer.model.nav.Floor;
import io.camdar.eng.wanderer.model.unit.Enemy;
import io.camdar.eng.wanderer.model.unit.ItemEntity;
import io.camdar.eng.wanderer.model.unit.Player;

import java.util.Scanner;

public class EntityTester {
    
    private static Floor floor;
    
    public static void main(String[] args) {
        floor = new Floor(40, 40);
        //floor.createFloor(40, 40);
        
        Player player = new Player(8, 8);
        Enemy enemy1 = new Enemy(3, 3);
        Enemy enemy2 = new Enemy(8, 7);
        ItemEntity badge = new ItemEntity(8, 9, "Sash");
        
        
        floor.addGameEntity(player);
        floor.addGameEntity(enemy1);
        floor.addGameEntity(enemy2);
        floor.addGameEntity(badge);
        
        player.setFloor(floor);
        enemy1.setFloor(floor);
        enemy2.setFloor(floor);
        badge.setFloor(floor);
        
        Scanner kbrd = new Scanner(System.in);
        for (int i = 0; i < 50; i++) {
            displayMap();
            int x = kbrd.nextInt();
            player.move(x);
            
            floor.step();
            
            System.out.println("HP: " + player.getCurHealth());
            
        }
        kbrd.close();
    }
    
    public static void displayMap() {
        for (int i = 0; i < floor.getHeight(); i++) {
            for (int j = 0; j < floor.getWidth(); j++) {
                
                    //System.out.print(Floor.floorTilesCreator[i][j] + "\t");
                
            }
            System.out.println();            
        }
    }
}