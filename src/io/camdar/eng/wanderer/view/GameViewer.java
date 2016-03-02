package io.camdar.eng.wanderer.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ListView;

import io.camdar.eng.wanderer.Wanderer;
import io.camdar.eng.wanderer.items.Item;
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
    private ImageView mapView;
    @FXML
    private AnchorPane spriteView;
    @FXML
    private ListView<String> inventory;
    
    @FXML
    public void displayDescription(MouseEvent arg) {
        String item = inventory.getSelectionModel().getSelectedItem();
        if (item != null) {
            int i = Integer.parseInt(item.split(" - ")[0]);
            runner.displayItemDesc(Item.getDescription(i), Item.getName(i));
        }
        spriteView.requestFocus();
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
        inventory.setFocusTraversable(false);
        // This is where java's supposed new "functional programming" stuff gets
        // pretty nasty. This entire block of code is essentially map();
        Stream<String> strings = owner.getInventory().stream().map(
                i -> String.format("%d - %s", i, Item.getName(i))
        );
        // This is because JFX requires you to use their own janky list format
        // to get listview to display them properly.
        ObservableList<String> ol = FXCollections.observableList(
                strings.collect(Collectors.toList())
        );
        inventory.setItems(ol);
    }
    
    public void setRunner(Wanderer r) {
        this.runner = r;
    }
    
    public void setOwner(Game g) {
        this.owner = g;
    }
}
