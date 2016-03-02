package io.camdar.eng.wanderer.view;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Sprite extends ImageView {
    private final int frameWidth, frameHeight;
    private int frameX = 0, frameY = 0;
    
    public void refresh() {
        frameX += this.frameWidth;
        frameY += this.frameHeight;
        // If our next frame viewport goes past the last frame on the row, take
        // the next row.
        if (frameX + this.frameWidth > this.getImage().getWidth()) {
            frameX = 0;
            frameY += this.frameHeight;
        }
        // Ditto for the vertical frames.
        if (frameY + this.frameHeight > this.getImage().getHeight()) {
            frameY = 0;
        }
        this.setViewport(
                new Rectangle2D(frameX, frameY, frameWidth, frameHeight)
        );
    }
    
    public void setTileLocation(int x, int y) {
        this.setX(x * ViewConstants.TILE_DIMENSIONS);
        this.setY(y * ViewConstants.TILE_DIMENSIONS);
    }
    
    // Clone constructor: should be private but it needs to be visible to
    // subclasses.
    protected Sprite(Sprite s) {
        this(s.getImage(), s.frameWidth, s.frameHeight);
    }
    
    // I don't really think there's a need for this constructor
    public Sprite(Image i, int frameWidth, int frameHeight) {
        super(i);
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
    }
}
