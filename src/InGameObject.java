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
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.scene.input.KeyCode.*;


public class InGameObject extends Application {
    private Pane root = new Pane();

    boolean right, left;

    private double t = 0;

    public Sprite player = new Sprite(200, 750, 30, 30, "player", Color.RED);

    ArrayList<ArrayList<Sprite>> enemies = nextLevel();

    ArrayList<Barrier> barriers = placeBarriers();

    public Parent createContent() {
        root.setPrefSize(600, 800);
        root.getChildren().add(player);
        for (int i = 0; i < 5; i++) {
            for (Sprite s : enemies.get(i)) {
                root.getChildren().add(s);
            }
        }
        for (int i = 0; i < 4; i++){
                root.getChildren().add(barriers.get(i));
        }
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

        nextLevel();


        return root;
    }

    private ArrayList<Barrier> placeBarriers() {
        ArrayList<Barrier> barriers = new ArrayList<>();

        for (int i = 0; i < 4;i++){
            barriers.add(new Barrier( 20+ 150 * i, 600, 70, 30, Color.BISQUE));
        }
        return barriers;
    }

    private ArrayList<ArrayList<Sprite>> nextLevel() {
        ArrayList<ArrayList<Sprite>> enemies = new ArrayList<>();
        for(int j = 0; j <5; j++) {
            enemies.add(new ArrayList<Sprite>());
            for (int i = 0; i < 5; i++) {
                Sprite enemy = new Sprite(90 + i * 100, 25+50*j, 30, 30, "enemy", Color.GREEN);
                if (j == 4){
                    enemy.front = true;
                }
                enemies.get(j).add(enemy);
            }
        }
        return enemies;
    }

    private List<Sprite> sprites() {
        return root.getChildren().stream().map(n -> (Sprite)n).collect(Collectors.toList());
    }

    private void update() {
        t += 0.05;
        sprites().forEach(s -> {
            switch (s.type) {
                case "enemybullet":
                    s.moveDown();
                    if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        player.dead = true;
                        s.dead = true;
                    }
                    sprites().stream().filter(b -> b.type.equals("barrier")).forEach(barrier -> {
                        if (s.getBoundsInParent().intersects(barrier.getBoundsInParent())) {
                            s.dead = true;
                            barrier.hitpoints--;
                            if (barrier.hitpoints == 0) {
                                barrier.dead = true;
                            }
                        }
                    });
                    break;
                case "playerbullet":
                    s.moveUp();
                    sprites().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                        if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.dead = true;
                            s.dead = true;
                        }
                    });
                    sprites().stream().filter(b -> b.type.equals("barrier")).forEach(barrier -> {
                        if (s.getBoundsInParent().intersects(barrier.getBoundsInParent())) {
                            s.dead = true;
                        }
                    });
                    break;
                case "enemy":
                    if (t>2) {
                        if ((Math.random() < .1) && s.front){
                            shoot(s);
                        }
                    }
                    break;
            }
        });
        root.getChildren().removeIf(n -> {
            Sprite s = (Sprite) n;
            return s.dead;
        });

        if (t > 2) {
            t = 0;
        }
    }


    public void shoot(Sprite shooter) {
        if (!player.dead) {
            Sprite s = new Sprite((int) shooter.getTranslateX() + 20, (int) shooter.getTranslateY(), 5, 20, shooter.type + "bullet", Color.BLACK);
            root.getChildren().add(s);
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case A: left = true; break;
                    case D: right = true; break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case A: left = false; break;
                    case D: right = false; break;
                    case SPACE: shoot(player); break;
                }
            }
        });

        stage.setScene(scene);
        stage.show();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (right) player.moveRight();
                if (left) player.moveLeft();
            }
        };
        timer.start();
    }
/*
    public class EnemyMove extends TimerTask {
        String direction = "right";

        public void run() {
            if (direction == "left" && enemies.get(0).get(0).getTranslateX() == 0) {
                enemies.forEach(row -> row.forEach(enemy -> enemy.moveDown()));
                direction = "right";
            }
            if (direction == "right" && enemies.get(0).get(4).getTranslateX() == 570) {
                enemies.forEach(row -> row.forEach(enemy -> enemy.moveDown()));
                direction = "left";
            }
            else if (direction == "left") {
                enemies.forEach(row -> row.forEach(enemy -> enemy.moveLeft()));
            }
            else if (direction == "right") {
                enemies.forEach(row -> row.forEach(enemy -> enemy.moveRight()));

            }
        }
    }
*/
    public static void main(String[] args) {
        launch(args);
    }
}
