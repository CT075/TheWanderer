package io.camdar.eng.wanderer.view;

import io.camdar.eng.wanderer.Wanderer;
import io.camdar.eng.wanderer.model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

// javafx controller for shop view
public class ShopViewer {
    
    // holds reference to the runner and the Game
    // sets price for items in the store
    private Wanderer runner;
    private Game owner;
    private static final int PRICE=50;
    
    // Labels. price: price of the stats, cookies: your money/how many cookies
    // you have to spend.
    @FXML
    private Label price;
    @FXML
    private Label cookies;

    // the exit shop button
    @FXML
    private void handleExit(){
        owner.exitShop();
        runner.initRoot();
        runner.initContainer();
    }

    // purchase attack stat (tied to a button)
    @FXML 
    private void buyAttack(){
        if (owner.canBuy(PRICE)){
            owner.addAttack(1);
            owner.spendCookies(PRICE);
            refresh();
        }
    }

    // purchase defense stat (tied to a button)
    @FXML 
    private void buyDefense(){
        if (owner.canBuy(PRICE)){
            owner.addDefense(1);
            owner.spendCookies(PRICE);
            refresh();
        }
    }

    // purchase health stat (tied to a button)
    @FXML 
    private void buyHealth(){
        if (owner.canBuy(PRICE)){
            owner.addHealth(3);
            owner.spendCookies(PRICE);
            refresh();
        }
    }
    
    // initialize changes the price label
    @FXML
    private void initialize(){
        price.setText(Integer.toString(PRICE));
    }
    
    // used in the exit to set scene back to the game
    public void setRunner(Wanderer r) {
        this.runner = r;
    }
    
    // refreshes the number fo cookies (called after purchase)
    public void refresh(){
        cookies.setText(owner.formatCookieCount());
    }
    
    // used to change number of cookies player has
    public void setOwner(Game g) {
        this.owner = g;
    }

}
