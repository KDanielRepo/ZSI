import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends Application {
    Integer[][] a = new Integer[4][4];
    Integer randomA;
    Integer randomB;
    Boolean up, right, down, left, moved;
    Boolean noUp;
    Boolean noRight;
    Boolean noDown;
    Boolean noLeft;
    Boolean game;
    GridPane gridPane;
    Scene scene;
    TextArea score;
    Algorithm algorithm;
    Being being;
    int test = 0;
    int movePlayed = 0;

    public void setA() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                a[i][j] = 0;
            }
        }
    }

    public void random() {
        int test = 0;
        randomA = ThreadLocalRandom.current().nextInt(0, 4);
        randomB = ThreadLocalRandom.current().nextInt(0, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (a[i][j] > 0) {
                    test++;
                }
            }
        }
        if (test == 16) {
            return;
        }
        if (a[randomA][randomB] != 0) {
            random();
        }
        a[randomA][randomB] = 2;
    }

    public void calculateScore() {
        int scoree = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                scoree += a[i][j];
            }
        }
        score.setText(Integer.toString(scoree));
    }

    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1000, 800);
        VBox vBox = new VBox();
        gridPane = new GridPane();
        gridPane.setPrefSize(600, 600);
        being = new Being();
        algorithm = new Algorithm();
        up = false;
        right = false;
        down = false;
        left = false;
        moved = false;
        noUp = false;
        noRight = false;
        noDown = false;
        noLeft = false;
        game = true;

        setA();
        randomA = ThreadLocalRandom.current().nextInt(0, 4);
        randomB = ThreadLocalRandom.current().nextInt(0, 4);
        a[randomA][randomB] = 2;
        paint(gridPane);

        scene = new Scene(borderPane);
        scene.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        up = true;
                        break;
                    case RIGHT:
                        right = true;
                        break;
                    case DOWN:
                        down = true;
                        break;
                    case LEFT:
                        left = true;
                        break;
                }
                try {
                    update();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                scene.getRoot().requestFocus();
            }
        });
        scene.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        up = false;
                        break;
                    case RIGHT:
                        right = false;
                        break;
                    case DOWN:
                        down = false;
                        break;
                    case LEFT:
                        left = false;
                        break;
                }
                try {
                    update();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                scene.getRoot().requestFocus();
            }
        });

        score = new TextArea();
        Button run = new Button();
        run.setText("reset");
        run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reset();
            }
        });
        vBox.getChildren().add(run);
        vBox.getChildren().add(score);
        borderPane.setCenter(gridPane);
        borderPane.setRight(vBox);

        primaryStage.setTitle("ZSI");
        primaryStage.setScene(scene);
        primaryStage.show();
        being.generateMove();
    }

    public void paint(GridPane gridPane) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                TextArea test = new TextArea();
                test.setPrefSize(150, 150);
                test.setEditable(false);
                if (a[i][j] == 0) {
                    test.setText("");
                } else {
                    test.setText(Integer.toString(a[i][j]));
                }
                gridPane.add(test, i, j);
            }
        }
    }

    public void update() throws AWTException {
        if (game) {
            if(test<4){
                being.generateMove();
            }else{
                being.playMove(movePlayed);
            }
            if (left) {
                for (int k = 0; k < 4; k++) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (a[i][j].equals(a[i + 1][j]) && (a[i][j] != 0)) {
                                a[i][j] = a[i][j] * 2;
                                a[i + 1][j] = 0;
                                moved = true;
                                noLeft = false;
                            }
                            if (a[i][j] == 0) {
                                if (a[i + 1][j] != 0) {
                                    a[i][j] = a[i + 1][j];
                                    a[i + 1][j] = 0;
                                    moved = true;
                                    noLeft = false;
                                }
                            }
                        }
                    }
                }
            }
            if (right) {
                for (int k = 0; k < 4; k++) {
                    for (int i = 3; i > 0; i--) {
                        for (int j = 0; j < 4; j++) {
                            if (a[i][j].equals(a[i - 1][j]) && (a[i][j] != 0)) {
                                a[i][j] = a[i][j] * 2;
                                a[i - 1][j] = 0;
                                moved = true;
                                noRight = false;
                            }
                            if (a[i][j] == 0) {
                                if (a[i - 1][j] != 0) {
                                    a[i][j] = a[i - 1][j];
                                    a[i - 1][j] = 0;
                                    moved = true;
                                    noRight = false;
                                }
                            }
                        }
                    }
                }
            }
            if (up) {
                for (int k = 0; k < 4; k++) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (a[i][j].equals(a[i][j + 1]) && (a[i][j] != 0)) {
                                a[i][j] = a[i][j] * 2;
                                a[i][j + 1] = 0;
                                moved = true;
                                noUp = false;
                            }
                            if (a[i][j] == 0) {
                                if (a[i][j + 1] != 0) {
                                    a[i][j] = a[i][j + 1];
                                    a[i][j + 1] = 0;
                                    moved = true;
                                    noUp = false;
                                }
                            }
                        }
                    }
                }
            }
            if (down) {
                for (int k = 0; k < 4; k++) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 3; j > 0; j--) {
                            if (a[i][j].equals(a[i][j - 1]) && (a[i][j] != 0)) {
                                a[i][j] = a[i][j] * 2;
                                a[i][j - 1] = 0;
                                moved = true;
                                noDown = false;
                            }
                            if (a[i][j] == 0) {
                                if (a[i][j - 1] != 0) {
                                    a[i][j] = a[i][j - 1];
                                    a[i][j - 1] = 0;
                                    moved = true;
                                    noDown = false;
                                }
                            }
                        }
                    }
                }
            }
            movePlayed++;
            if (moved) {
                random();
                paint(gridPane);
                calculateScore();
                being.setMoved(moved);
                being.setScore(Integer.parseInt(score.getText()));
                moved = false;
            }
            checkGameOver();
        } else {
            if(test<3){
                test++;
                being.setMoved(moved);
                System.out.println(being.getScore());
                System.out.println("size to: "+being.getMoves().size());
                algorithm.getGenePool().add(being);
                being = new Being();
                reset();
            }else{
                if(test<5){
                    movePlayed = 0;
                    System.out.println(being.getScore());
                    System.out.println("size to: "+being.getMoves().size());
                    being = algorithm.createOffspring(algorithm.selectParent(algorithm.getGenePool()),algorithm.selectParent(algorithm.getGenePool()));
                    test++;
                    reset();
                }
            }
        }
    }

    public void reset(){
        a = new Integer[4][4];
        up = false;
        right = false;
        down = false;
        left = false;
        moved = false;
        noUp = false;
        noRight = false;
        noDown = false;
        noLeft = false;
        game = true;

        setA();
        randomA = ThreadLocalRandom.current().nextInt(0, 4);
        randomB = ThreadLocalRandom.current().nextInt(0, 4);
        a[randomA][randomB] = 2;
        paint(gridPane);
        try {
            if(test<4){
                being.generateMove();
            }else {
                being.playMove(0);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void checkGameOver() {
        int test = 0;
        int test2 = 0;
        int test3 = 0;
        int test4 = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (a[i][j] != a[i + 1][j] && (a[i][j] > 0) && (a[i + 1][j] > 0)) {
                    test++;
                }
            }
        }
        for (int i = 3; i > 0; i--) {
            for (int j = 0; j < 4; j++) {
                if (a[i][j] != a[i - 1][j] && (a[i][j] > 0) && (a[i - 1][j] > 0)) {
                    test2++;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (a[i][j] != a[i][j + 1] && (a[i][j] > 0) && (a[i][j + 1] > 0)) {
                    test3++;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 3; j > 0; j--) {
                if (a[i][j] != a[i][j - 1] && (a[i][j] > 0) && (a[i][j - 1] > 0)) {
                    test4++;
                }
            }
        }
        noUp = test == 12;
        noRight = test2 == 12;
        noDown = test3 == 12;
        noLeft = test4 == 12;
        if (noUp && noRight && noDown && noLeft) {
            game = false;
        }
    }

    public Boolean getGame() {
        return game;
    }

    public static void main(String[] args) {
        launch(Game.class, args);
    }
}
