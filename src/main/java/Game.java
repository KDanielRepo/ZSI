import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ThreadLocalRandom;

public class Game extends Application{
    Integer[][] a = new Integer[4][4];
    Integer randomA;
    Integer randomB;
    Boolean up, right, down, left;
    GridPane gridPane;
    Scene scene;
    TextArea score;

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
                if(a[i][j]>0){
                    test++;
                }
            }
        }
        if(test==16){
            System.out.println("nie");
            return;
        }
        if(a[randomA][randomB]!=0){
            System.out.println("tez nie");
            random();
        }
        a[randomA][randomB] = 2;
    }
    public void calculateScore(){
        int scoree = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                scoree+=a[i][j];
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
        up = false;
        right = false;
        down = false;
        left = false;

        setA();
        randomA = ThreadLocalRandom.current().nextInt(0, 4);
        randomB = ThreadLocalRandom.current().nextInt(0, 4);
        a[randomA][randomB] = 2;
        paint(gridPane);

        scene = new Scene(borderPane);
        scene.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()){
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
                update();
                scene.getRoot().requestFocus();
            }
        });
        scene.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()){
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
                update();
                scene.getRoot().requestFocus();
            }
        });

        score = new TextArea();
        Button run = new Button();
        vBox.getChildren().add(run);
        vBox.getChildren().add(score);
        borderPane.setCenter(gridPane);
        borderPane.setRight(vBox);

        primaryStage.setTitle("ZSI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void paint(GridPane gridPane){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                TextArea test = new TextArea();
                test.setPrefSize(150, 150);
                test.setEditable(false);
                if(a[i][j]==0){
                    test.setText("");
                }else {
                    test.setText(Integer.toString(a[i][j]));
                }
                gridPane.add(test, i, j);
            }
        }
    }
    public void update(){
        if(left){
            for(int k = 0; k<4;k++) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        if(a[i][j].equals(a[i+1][j])){
                            a[i][j]=a[i][j]*2;
                            a[i+1][j]=0;
                        }
                        if (a[i][j] == 0) {
                            if (a[i + 1][j] != 0) {
                                a[i][j] = a[i + 1][j];
                                a[i + 1][j] = 0;
                            }
                        }
                    }
                }
            }
            random();
            paint(gridPane);
            calculateScore();
        }
        if(right){
            for(int k = 0; k<4;k++) {
                for (int i = 3; i > 0; i--) {
                    for (int j = 0; j < 4; j++) {
                        if(a[i][j].equals(a[i-1][j])){
                            a[i][j]=a[i][j]*2;
                            a[i-1][j]=0;
                        }
                        if (a[i][j] == 0) {
                            if (a[i - 1][j] != 0) {
                                a[i][j] = a[i - 1][j];
                                a[i - 1][j] = 0;
                            }
                        }
                    }
                }
            }
            random();
            paint(gridPane);
            calculateScore();
        }
        if(up){
            for(int k = 0; k<4;k++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 3; j++) {
                        if(a[i][j].equals(a[i][j+1])){
                            a[i][j]=a[i][j]*2;
                            a[i][j+1]=0;
                        }
                        if (a[i][j] == 0) {
                            if (a[i][j + 1] != 0) {
                                a[i][j] = a[i][j + 1];
                                a[i][j + 1] = 0;
                            }
                        }
                    }
                }
            }
            random();
            paint(gridPane);
            calculateScore();
        }
        if(down){
            for(int k = 0; k<4;k++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 3; j > 0; j--) {
                        if(a[i][j].equals(a[i][j-1])){
                            a[i][j]=a[i][j]*2;
                            a[i][j-1]=0;
                        }
                        if (a[i][j] == 0) {
                            if (a[i][j - 1] != 0) {
                                a[i][j] = a[i][j - 1];
                                a[i][j - 1] = 0;
                            }
                        }
                    }
                }
            }
            random();
            paint(gridPane);
            calculateScore();
        }
    }

    public static void main(String[] args) {
        launch(Game.class, args);
    }
}
