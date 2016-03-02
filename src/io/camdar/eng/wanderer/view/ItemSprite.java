package io.camdar.eng.wanderer.view;

import io.camdar.eng.wanderer.items.*;
import javafx.scene.image.Image;

public class ItemSprite extends Sprite {
    Item i;
    
    public ItemSprite(Image i, int frameWidth, int frameHeight) {
        super(i, frameWidth, frameHeight);
    }
}
