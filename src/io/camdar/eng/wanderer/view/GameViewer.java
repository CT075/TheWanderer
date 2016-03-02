package io.camdar.eng.wanderer.view;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Rectangle2D;
import io.camdar.eng.wanderer.Wanderer;
import io.camdar.eng.wanderer.model.Game;
import io.camdar.eng.wanderer.model.unit.GameEntity;
import io.camdar.eng.wanderer.view.FloorViewBuilder;

// What JFX calls a "controller" for the game panel. Handles refreshing of
// view elements (etc)
public class GameViewer {
    public static final int MAPVIEW_WIDTH = 400;
    public static final int MAPVIEW_HEIGHT = 400;
    
    // Holds reference to the runner and the Game
    // We need a reference to the runner because the runner handles all of the
    // nasty I/O operations involving loading FXML files.
    private Wanderer runner;
    private Game owner;
    
    private int displayedFloor = 1;
    
    // The list of sprites currently needed on the floor
    private ArrayList<EntitySprite> sprites = new ArrayList<>();
    
    // All of the FXML things
    @FXML
    private Label hpLabel;
    @FXML
    private Label atkLabel;
    @FXML
    private Label defLabel;
    @FXML
    private Label cookieLabel;
    @FXML
    private Label floorLabel;
    @FXML
    private ImageView mapView;
    @FXML
    private AnchorPane spriteView;
    
    @FXML
    public void displayMinimap() { runner.displayMinimap(this.mapView); }
    @FXML
    public void displayControls() { runner.displayControls(); }
    @FXML
    public void displayInventory() {
        runner.displayInventory(owner.getInventory());
    }
    
    // Constructs the image "floorview" of the current floor, and attaches
    // each GameEntity to an EntitySprite so we can easily update everything at
    // once.
    public void setupFloorView() {
        Image floorView = FloorViewBuilder.constructImage(owner.currentFloor());
        sprites = new ArrayList<EntitySprite>();
        spriteView.getChildren().clear();
        for (GameEntity[] row : owner.getEntities()) {
            for (GameEntity ge : row) {
                if (ge != null) {
                    EntitySprite es = new EntitySprite(ge);
                    spriteView.getChildren().add(es);
                    sprites.add(es);
                }
            }
        }
        mapView.setImage(floorView);
        refreshHUD();
        refreshMapview();
        refreshEntities();
    }
    
    // Change the map to display correctly relative to the player.
    public void refreshMapview() {
        int tlX = owner.getPlayerXLoc() * ViewConstants.TILE_DIMENSIONS;
        int tlY = (owner.getPlayerYLoc()-1) * ViewConstants.TILE_DIMENSIONS;
        refreshEntities();
        mapView.setViewport(new Rectangle2D(
                tlX, tlY, MAPVIEW_WIDTH, MAPVIEW_HEIGHT
        ));
    }
    
    public void refreshEntities() {
        Iterator<EntitySprite> it = this.sprites.iterator();
        while (it.hasNext()) {
            EntitySprite s = it.next();
            if (!s.isInMap()) {
                s.setVisible(false);
                it.remove();
                continue;
            }
            s.updatePosition();
        }
    }
    
    // Update everything as per a game "step"
    public void updateFrame() {
        for (EntitySprite s : this.sprites) { s.refresh(); }
        if (this.displayedFloor != owner.getCurrentLevel()) {
            this.displayedFloor = owner.getCurrentLevel();
            this.setupFloorView();
        }
    }
    
    // Make sure the HUD is always current with the model state.
    public void refreshHUD() {
        hpLabel.setText(owner.formatPlayerHP());
        atkLabel.setText(owner.formatPlayerAtk());
        defLabel.setText(owner.formatPlayerDef());
        cookieLabel.setText(owner.formatCookieCount());
        // It's kind of abusing type coersion, but it looks cleaner than any
        // alternative I could think of.
        floorLabel.setText("" + this.displayedFloor);
    }
    
    public void setRunner(Wanderer r) {
        this.runner = r;
    }
    
    public void setOwner(Game g) {
        this.owner = g;
    }
}
