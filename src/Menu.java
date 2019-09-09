import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.image.*;

import java.io.File;
import java.io.FileInputStream;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Boolean.TRUE;


public class Menu extends Application {
    private Stage window;
    private Image image;
    private InGameObject game;

    private Parent createEndScreen() {

        StackPane endLayout = new StackPane();
        endLayout.setPrefSize(500,600);
        Text exit = new Text(100,50, "EXIT TO HOME");
        exit.setFill(Color.LIGHTGREEN);
        exit.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        exit.setOnMouseClicked(event -> {
            Scene scene = new Scene(createMenuContent());
            window.setScene(scene);
        });
        Rectangle bg = makeBackground(endLayout);
        endLayout.getChildren().addAll(bg, exit);
        return endLayout;
    }

    private Parent createPlayContent() {
        game = new InGameObject();
        Parent playParent = game.createContent();
        return playParent;
    }


    private void playGame() {
        Scene playScene = new Scene(createPlayContent());
        window.setScene(playScene);
        playScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    game.player.moveLeft();
                    break;
                case D:
                    game.player.moveRight();
                    break;
                case SPACE:
                    game.shoot(game.player);
                    break;
            }
        });
    }

    private Parent createControlsContent() {
        StackPane controlLayout = new StackPane();
        controlLayout.setPrefSize(500,600);

        //making black background
        Rectangle background = makeBackground(controlLayout);
        ImageView joystickView = loadImage("./images/joystick-large.png");

        //creating control title
        Text controlTitle = new Text(50,100, "CONTROLS");
        controlTitle.setFill(Color.LIGHTGREEN);
        controlTitle.setTranslateY(30);
        controlTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 40));

        //making menu with Controls
        MenuItem a = new MenuItem("A - Move Left");
        MenuItem d = new MenuItem("D - Move Right");
        MenuItem space = new MenuItem("SPACE - SHOOT");
        MenuItem exit = new MenuItem("HOME");
        exit.setOnMouseClicked(event -> {
            Scene scene = new Scene(createMenuContent());
            window.setScene(scene);
                }
        );
        MenuBox controlMenu = new MenuBox(a,d,space, exit);
        controlMenu.setTranslateY(300);
        controlLayout.getChildren().addAll(background, joystickView, controlTitle, controlMenu);
        StackPane.setAlignment(controlTitle, Pos.TOP_CENTER);
        StackPane.setAlignment(controlMenu, Pos.CENTER);
        StackPane.setAlignment(joystickView, Pos.TOP_CENTER);
        return controlLayout;
    }

// creates a window for our menu
    private Parent createMenuContent() {
        StackPane layout = new StackPane();
        layout.setPrefSize(500, 600);

        // background for the game
        Rectangle bg = makeBackground(layout);

        // creating menu
        MenuBox startMenu = makeMainMenu();

        //Passing FileInputStream object as a parameter
        ImageView alienView = loadImage("./images/space-invader-title-dark.png");
        alienView.setTranslateY(100);

        //making the title
        Text title = new Text(50, 100, "SPACE INVADERS");
        title.setFill(Color.LIGHTGREEN);
        title.setTranslateY(30);
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 40));

        layout.getChildren().addAll(bg, title, startMenu, alienView);
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        StackPane.setAlignment(alienView, Pos.TOP_CENTER);
        StackPane.setAlignment(startMenu, Pos.CENTER);
        return layout;
    }

    public void start(Stage spaceStage) {
        window = spaceStage;
        window.setTitle("Space Invaders");
        Scene scene = new Scene(createMenuContent());
        window.setScene(scene);
        window.show();
    }

    // Credit to https://github.com/Siderim for the inspiration for the menu

    private static class MenuBox extends VBox {
        private MenuBox(MenuItem... items) {
            for (MenuItem item : items) {
                getChildren().addAll(item);
            }

        }
    }

    private static class MenuItem extends StackPane{
        private MenuItem(String name) {

            Rectangle background = new Rectangle(300, 40);
            background.setOpacity(.4);

            // LIGHT GREEN MONOSPACE MENU TEXT
            Text text = new Text(name);
            text.setFill(Color.LIGHTGREEN);
            text.setFont(Font.font("Monospace", FontWeight.SEMI_BOLD, 30));

            setAlignment(Pos.CENTER);
            getChildren().addAll(background, text);

            setOnMouseEntered(event -> {
                text.setFill(Color.WHITE);
            });

            setOnMouseExited(event -> {
                text.setFill(Color.LIGHTGREEN);
            });
            setOnMousePressed(event -> {
                background.setFill(Color.WHITE);
                text.setFill(Color.RED);
            });

            setOnMouseReleased(event -> {
                background.setFill(Color.BLACK);
                text.setFill(Color.WHITE);
            });
        }
    }

    private Rectangle makeBackground(StackPane layout) {
        Rectangle background = new Rectangle();
        background.setFill(Color.BLACK);
        background.widthProperty().bind(layout.widthProperty());
        background.heightProperty().bind(layout.heightProperty());
        return background;
    }

    private MenuBox makeMainMenu() {
        MenuItem exitButton = new MenuItem("EXIT");
        exitButton.setOnMousePressed( event -> {
            System.exit(0); });
        MenuItem play = new MenuItem("PLAY");
        play.setOnMouseClicked(event -> {
            playGame();
        });
        MenuItem controls = new MenuItem("CONTROLS");
        controls.setOnMouseClicked(event -> {
            Scene controlScene = new Scene(createControlsContent());
            window.setScene(controlScene);
        });
        MenuBox startMenu = new MenuBox(play, controls, exitButton);
        startMenu.setTranslateY(300);
        return startMenu;
    }

    private ImageView loadImage(String dir) {
        try {
            image = new Image(new FileInputStream(dir));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageView picture = new ImageView(image);
        return picture;
    }


    public static void main(String[] args) {
        launch(args);
    }
}


