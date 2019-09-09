import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.scene.input.KeyCode.*;

public class Sprite extends Rectangle{

    boolean dead;
    final String type;
    boolean front;
    int hitpoints = 6;

    Sprite(int x, int y, int w, int h, String type, Color color) {
        super(w, h, color);
        this.front = false;
        this.dead = false;
        this.type = type;
        setTranslateX(x);
        setTranslateY(y);
    }


    public void moveLeft() {
        setTranslateX(getTranslateX() - 5);
    }

    public void moveRight() {
        setTranslateX(getTranslateX() + 5);
    }

    public void moveUp() {
        setTranslateY(getTranslateY() - 7);
    }

    public void moveDown() {
        setTranslateY(getTranslateY() + 6);
    }
}
