package io.camdar.eng.wanderer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import io.camdar.eng.wanderer.control.KeyController;
import io.camdar.eng.wanderer.items.Item;
import io.camdar.eng.wanderer.model.Game;
import io.camdar.eng.wanderer.view.GameViewer;
import io.camdar.eng.wanderer.view.ShopViewer;

// The "master" class - exists outside of MVC. Coordinates the three and handles
// file IO for the various FXML (view) files.
// Cam: I put a lot of things that involve spawning windows (etc) in this file
// because I would prefer to keep all the IO in one place
public class Wanderer extends Application {
    // JFX-related stage/scene things
    private Stage primaryStage;
    private BorderPane layoutRoot;
    
    // Controller/coordination things
    private GameViewer viewer;
    private KeyController ctrlr;
    private boolean over = false;
    
    // model things
    private Game state;
    private Timeline tl;
    
    @Override
    public void start(Stage s) throws Exception {
        this.primaryStage = s;
        this.primaryStage.setTitle("The Wanderer");
        startGame();
        initRoot();
        initContainer();
    }
    
    // Initializes the game state to a "base" condition, with a single floor
    // and enemies loaded. Also sets up our KeyListener to interface with
    // this state object.
    public void startGame() {
        state = Game.init(true);
        ctrlr = new KeyController(state);
    }
    
    // Initialize the root container (Scene) to a "base" window.
    public void initRoot() {
        try {
            FXMLLoader loader = new FXMLLoader(Wanderer.class.getResource(
                    "view/RootFrame.fxml"
            ));
            layoutRoot = loader.load();
            this.primaryStage.setScene(new Scene(layoutRoot));
            this.primaryStage.setResizable(false);
            this.primaryStage.sizeToScene();
        } catch (IOException e) {
            System.err.println("Unable to load root layout. Aborting");
            System.exit(-1);
        }
    }
    
    // Sets up the inventory display that shows everything you're holding.
    public void displayInventory(List<Item> items) {
        // This is where java's supposed new "functional programming" stuff gets
        // pretty nasty. This entire block of code is essentially map();
        Stream<String> strings = items.stream().map(
                i -> String.format(
                        "%s\t\t%s",
                        i.getItemID(), i.getDescription()
                )
        );
        // This is because JFX requires you to use their own janky list format
        // to get listview to display them properly.
        ObservableList<String> ol = FXCollections.observableList(
                strings.collect(Collectors.toList())
        );
        ListView<String> lv = new ListView<>(ol);
        try {
            FXMLLoader loader = new FXMLLoader(Wanderer.class.getResource(
                    "view/InventoryFrame.fxml"
            ));
            BorderPane dialog = loader.load();
            dialog.setCenter(lv);
            Stage s = new Stage();
            s.setTitle("Inventory");
            s.initModality(Modality.WINDOW_MODAL);
            s.initOwner(primaryStage);
            s.setScene(new Scene(dialog));
            s.showAndWait();
        }
        catch(IOException e) {
            System.err.println("Unable to load inventory display. Skipping.");
            return;
        }
    }
    
    // Display the control dialog
    public void displayControls() {
        try {
            FXMLLoader loader = new FXMLLoader(Wanderer.class.getResource(
                    "view/ControlsDialog.fxml"
            ));
            AnchorPane dialog = loader.load();
            Stage s = new Stage();
            s.setTitle("Controls");
            s.initModality(Modality.WINDOW_MODAL);
            s.initOwner(primaryStage);
            s.setScene(new Scene(dialog));
            s.showAndWait();
        }
        catch(IOException e) {
            System.err.println("Unable to load controls display. Skipping.");
            return;
        }
    }
    
    // Create and display the minimap dialog.
    public void displayMinimap(ImageView map) {
        // This block of code basically just takes the map image that our
        // GameViewer instance uses, shrinks it, and uses it as a minimap.
        Image mapImage = map.getImage();
        ImageView minimapView = new ImageView(mapImage);
        minimapView.setPreserveRatio(true);
        minimapView.setFitWidth(300);
        try {
            FXMLLoader loader = new FXMLLoader(Wanderer.class.getResource(
                    "view/MinimapDialog.fxml"
            ));
            BorderPane dialog = loader.load();
            ScrollPane sp = new ScrollPane();
            sp.setContent(minimapView);
            dialog.setCenter(sp);
            Stage s = new Stage();
            s.setTitle("Map");
            s.initModality(Modality.WINDOW_MODAL);
            s.initOwner(primaryStage);
            s.setScene(new Scene(dialog));
            s.showAndWait();
        }
        catch(IOException e) {
            System.err.println("Unable to load minimap display. Skipping.");
            return;
        }
    }
    
    // Display the shop dialog.
    public void displayShop(){
        try {
            // Load layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Wanderer.class.getResource(
                    "view/ShopFrame.fxml"
            ));
            AnchorPane shopLayout = (AnchorPane) loader.load();
            
            ShopViewer v = loader.getController();
            v.setOwner(state);
            v.setRunner(this);

            // Show the scene containing the shop layout.
            Scene scene = new Scene(shopLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            v.refresh();
        } catch (IOException e) {
            System.err.println("Unable to load shop screen. Aborting.");
            System.exit(-8);
        }

    }
    

    // Loads and initializes the "container" - the "meat" of the scene. Places
    // the container in the middle of the BorderPane and attaches relevant
    // key and mouse listeners.
    public void initContainer() {
        try {
            FXMLLoader loader = new FXMLLoader(Wanderer.class.getResource(
                    "view/GameContainer.fxml"
            ));
            AnchorPane gameContainer = loader.load();
            // Let's get this party started
            viewer = loader.getController();
            viewer.setOwner(state);
            viewer.setRunner(this);
            
            // "ctrlr::handleKeyInput" is a "method object"; this line is
            // essentially equivalent to (e) -> { ctrlr.handleKeyInput(); }.
            layoutRoot.setOnKeyPressed(ctrlr::handleKeyInput);
            
            // Setup the timeline that handles animation (etc)
            // Cam: I have a super major fundamental disagreement with putting
            // the timeline the model, so I'd rather put the timeline outside
            // of the MVC architecture altogether. The update() method just
            // calls the respective update() methods of all sprites onscreen.
            tl = new Timeline(new KeyFrame(
                    Duration.millis(150), (e) -> { this.refresh(); }
            ));
            tl.setCycleCount(Timeline.INDEFINITE);
            tl.play();
            // Now that we're all set up, we can show our window.
            GameViewer gv = loader.getController();
            gv.setOwner(state);
            gv.setupFloorView();
            layoutRoot.setCenter(gameContainer);
            this.primaryStage.show();

        } catch (IOException e) {
            System.err.println("Unable to load game layout. Aborting");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    // Change the scene to display a game over
    public void displayGameOver() {
        // This prevents the issue with the endlayout getting loaded 60 times/s
        if (over) { return; }
        try {
            FXMLLoader loader = new FXMLLoader(Wanderer.class.getResource(
                    "view/EndContainer.fxml"
            ));
            AnchorPane endContainer = loader.load();
            this.primaryStage.setScene(new Scene(endContainer));
            this.primaryStage.setResizable(false);
            this.primaryStage.sizeToScene();
            over = true;
        }
        catch (IOException e) {
            System.err.println("Couldn't load gameover layout. Aborting.");
            System.exit(-3);
        }
    }
    
    // Refresh the screen framecounter
    private void refresh() {
        if (state.updated) {
            this.step();
            state.updated = false;
        }
        viewer.updateFrame();
    }
    
    // Updates all sprites onscreen to their current frames and positions.
    public void step() {
        if (state.gameOver) {
            displayGameOver();
        }
        else {
            if (state.showShop){
                displayShop();
                tl.pause();
            }
            else { tl.play(); }
            viewer.refreshHUD();
            viewer.refreshMapview();
        }
    }

    public static void main(String... args) {
        launch(args);
    }
}