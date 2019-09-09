import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class Barrier extends Sprite {
    Barrier(int x, int y, int w, int h, Color color) {
        super(x, y, w, h, "barrier", color);
        this.hitpoints = 5;
        setTranslateX(x);
        setTranslateY(y);
    }
}
