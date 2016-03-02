package io.camdar.eng.wanderer.tests;

import static org.junit.Assert.*;
import org.junit.Test;

import io.camdar.eng.wanderer.model.Game;
import io.camdar.eng.wanderer.model.unit.Player;

// unit tests
public class GSCQTest {    
    private Player pc;
    private Game g;
    private int xPlayerLocation;
    private int yPlayerLocation;
    private boolean gameState;
    
    // So all of these tests are for things you guys coded/rewrote, 
    // (for example I think you totally changed item equipment?), 
    // but there's really only six different tests here since there are 
    // so many duplicated things we need to test 
    // (different cases of the same functionality).
    
    @Test 
    public void everyTileConnectsToAtLeastOneOtherTile() {
        // To-do: I don't know how floor generation works
    }
    
    @Test
    public void playerCannotGoThroughWall() {
        // To-do: I don't know how you guys did this either
    }
    
    @Test
    public void playerCannotBeOnTileImmediatelyBeyondTopWall() {
        // To-do
    }
    
    @Test
    public void playerCannotBeOnTileImmediatelyBeyondBottomWall() {
        // To-do
    }
    
    @Test
    public void playerCannotBeOnTileImmediatelyBeyondRightWall() {
        // To-do
    }
    
    @Test
    public void playerCannotBeOnTileImmediatelyBeyondLeftWall() {
        // To-do
    }
    
    @Test
    public void enemyCannotBeOnTileImmediatelyBeyondTopWall() {
        // To-do
    }
    
    @Test
    public void enemyCannotBeOnTileImmediatelyBeyondRightWall() {
        // To-do
    }
    
    @Test
    public void enemyCannotBeOnTileImmediatelyBeyondBottomWall() {
        // To-do
    }
    
    @Test
    public void enemyCannotBeOnTileImmediatelyBeyondLeftWall() {
        // To-do
    }
    
    @Test
    public void itemCannotBeOnTileImmediatelyBeyondTopWall() {
        // To-do
    }
    
    @Test
    public void itemCannotBeOnTileImmediatelyBeyondBottomWall() {
        // To-do
    }
    
    @Test
    public void itemCannotBeOnTileImmediatelyBeyondRightWall() {
        // To-do
    }
    
    @Test
    public void itemCannotBeOnTileImmediatelyBeyondLeftWall() {
        // To-do
    }
    
    @Test
    public void itemCannotRaisePlayerHealthHigherThanTen() {
        pc.setCurHealth(10);
        // don't know how you're using items right now,
        // but probably just use a cootie shot here
    }
    
    
    @Test
    public void everyLevelHasAnExit() {
        // To-do
    }
    
    @Test
    public void playerGainsCookieOnFirstStep() {
        // To-do
    }
    
    @Test
    public void playerExistsBeforeMovement() {
        // To-do
    }
    
    @Test
    public void playerDoesNotLoseDamageTwoAwayFromEnenmy() {
        // To-do
    }
    
    @Test
    public void playerDoesNotGainHPFromAttackingEnemyFromTop() {
        // To-do
    }
    
    @Test
    public void playerDoesNotGainHPFromAttackingEnemyFromBelow() {
        // To-do
    }
    
    @Test
    public void playerDoesNotGainHPFromAttackingEnemyFromLeft() {
        // To-do
    }
    
    @Test
    public void playerDoesNotGainHPFromAttackingEnemyFromRight() {
        // To-do
    }
    
    @Test
    public void playerLosesHPFromAttackingEnemyFromTop() {
        // To-do
    }
    
    @Test
    public void playerLosesHPFromAttackingEnemyFromBelow() {
        // To-do
    }
    
    @Test
    public void playerLosesHPFromAttackingEnemyFromRight() {
        // To-do
    }
    
    @Test
    public void playerLosesHPFromAttackingEnemyFromLeft() {
        // To-do
    }
    
    @Test
    public void enemyLosesHPFromAttackingPlayerFromTop() {
        // To-do
    }
    
    @Test
    public void enemyLosesHPFromAttackingPlayerFromBelow() {
        // To-do
    }
    
    @Test
    public void enemyLosesHPFromAttackingPlayerFromRight() {
        // To-do
    }
    
    @Test
    public void enemyLosesHPFromAttackingPlayerFromLeft() {
        // To-do
    }
    
    @Test
    public void playerCannotAttackEnemyTwiceInARow() {
        // To-do
    }
    
    @Test
    public void enemyCannotAttackPlayerTwiceInARow() {
        // To-do
    }
    
    @Test
    public void firstFloorHasAShop() {
        // To-do
    }
    
    @Test
    public void secondFloorHasAShop() {
        // To-do
    }
    
    @Test
    public void itemDisappearsFromInventoryOnceEquipped() {
        // To-do
    }
    
    @Test
    public void itemDisappearsAfterPlayerStepsOnItsTile() {
        // To-do
    }
    
    @Test
    public void effectOfFirstCookieStillLastsAfterSecondCookieIsFound() {
        // To-do
    }
    
    @Test
    public void mascaraDoesNotAffectAttack() {
        // To-do
    }
    
    
    
    
    // Taken care of these
    
    @Test
    public void playerDiesWhenHealthIsZero() {
        pc.setCurHealth(0);
        gameState = g.gameOver;
        assertTrue(gameState);
    }
    
    @Test
    public void playerDiesWhenHealthIsNegativeOne() {
        pc.setCurHealth(-1);
        gameState = g.gameOver;
        assertTrue(gameState);
    }
    
    @Test
    public void playerIsAliveWhenHealthIsPositiveOne() {
        pc.setCurHealth(1);
        gameState = g.gameOver;
        assertFalse(gameState);
    }
    
    
    @Test
    public void playerAttackCannotBeNegativeOne () {
        try {
            pc.setAttack(-1);
            fail("Player attack cannot be negative");
        } catch (IllegalArgumentException e) {
            // :)
        }
    }
    
    @Test
    public void playerDefenseCannotBeNegativeOne () {
        try {
            pc.setDefense(-1);
            fail("Player defense cannot be negative");
        } catch (IllegalArgumentException e) {
            // :)
        }
    }
    
    @Test
    public void playerHealthCannotBeEleven() {
        try {
            pc.setCurHealth(11);
            fail("Player health can only be ten");
        } catch (IllegalArgumentException e) {
            // :)
        }
    }
}


