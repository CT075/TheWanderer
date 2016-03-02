package io.camdar.eng.wanderer.view;

import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
// I hate to have to import this because this potentially masks jfx Color
import java.awt.Color;
import javax.imageio.ImageIO;

import io.camdar.eng.wanderer.model.nav.Floor;
import io.camdar.eng.wanderer.model.nav.Tile;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;

// XXX - This entire class is rather janky in that we combine awt and fx core
// classes to provide a service that javafx doesn't.
public class FloorViewBuilder extends ImageView {
    // There doesn't appear to be a way to tile together images in memory
    // without having it display the entire thing in javafx, so we instead
    // draw our "master floor image" onto an awt BufferedImage and show
    // parts of that on the screen.
    public static Image constructImage(Floor f) {
        BufferedImage[] tileImages = new BufferedImage[Tile.NUM_TYPES];
        try {
            for (int i = 0 ; i < tileImages.length ; i++) {
                tileImages[i] = ImageIO.read(new File(
                        String.format("res/%d.png", i)
                ));
            }
        }
        catch (IOException ioe) {
            System.err.println("Unable to load tile images. Aborting.");
            System.exit(1);
        }
        // XXX - Properly this should be inside the try-catch block
        // This looks like a job for python's try-catch-else construct!
        BufferedImage result = new BufferedImage(
                (f.getWidth()+ViewConstants.EDGE_BLANKS)
                    * ViewConstants.TILE_DIMENSIONS,
                (f.getHeight()+ViewConstants.EDGE_BLANKS)
                    * ViewConstants.TILE_DIMENSIONS,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = result.createGraphics();
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, result.getWidth(), result.getHeight());
        for (int i = 0 ; i < f.getHeight() ; i++) {
            for (int j = 0 ; j < f.getWidth() ; j++) {
                g.drawImage(
                        tileImages[f.getTile(i, j).getID()], null,
                        (i+ViewConstants.TOP_LEFT_BLANKS)
                            * ViewConstants.TILE_DIMENSIONS,
                        (j+ViewConstants.BOT_RIGHT_BLANKS)
                            * ViewConstants.TILE_DIMENSIONS
                );
            }
        }
        return SwingFXUtils.toFXImage(result, null);
    }
}
