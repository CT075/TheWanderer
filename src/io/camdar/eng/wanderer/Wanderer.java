package io.camdar.eng.wanderer;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import javafx.geometry.Insets;

import io.camdar.eng.wanderer.control.KeyController;
import io.camdar.eng.wanderer.model.Game;
import io.camdar.eng.wanderer.view.GameViewer;

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
            layoutRoot.requestFocus();
            
            // Setup the timeline that handles animation (etc)
            // Cam: I have a super major fundamental disagreement with putting
            // the timeline the model, so I'd rather put the timeline outside
            // of the MVC architecture altogether. The update() method just
            // calls the respective update() methods of all sprites onscreen.
            tl = new Timeline(new KeyFrame(Duration.millis(150), (e) -> {
                    this.refresh();
            }));
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
    
    public void displayItemDesc(String desc, String title) {
        Label lb = new Label(desc);
        lb.setWrapText(true);
        lb.setPrefHeight(235);
        lb.setPrefWidth(310);
        lb.setFont(Font.font("Consolas", 20));
        lb.setContentDisplay(ContentDisplay.TOP);
        lb.setTextAlignment(TextAlignment.LEFT);
        ScrollPane sp = new ScrollPane();
        sp.setContent(lb);
        try {
            FXMLLoader loader = new FXMLLoader(Wanderer.class.getResource(
                    "view/ItemDialog.fxml"
            ));
            BorderPane dialog = loader.load();
            dialog.setCenter(sp);
            BorderPane.setMargin(sp, new Insets(15, 30, 15, 30));
            Stage s = new Stage();
            s.setTitle(title);
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
            tl.play();
            viewer.refreshHUD();
            viewer.refreshMapview();
        }
    }

    public static void main(String... args) {
        launch(args);
    }
}