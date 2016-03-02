package io.camdar.eng.wanderer.control;

import io.camdar.eng.wanderer.model.Game;
import javafx.scene.input.KeyEvent;

// Feeds key input into the Game object.
public class KeyController {
    private Game owner;
    
    // TODO: Make it an enum
    @SuppressWarnings("incomplete-switch")
    public void handleKeyInput(KeyEvent e) {
        // turns the input into usable int
        switch (e.getCode()) {
        case DOWN:
            owner.update(3);
            break;
        case UP:
            owner.update(1);
            break;
        case LEFT:
            owner.update(4);
            break;
        case RIGHT:
            owner.update(2);
        }
    }
    
    public KeyController(Game owner) { this.owner = owner; }
}
