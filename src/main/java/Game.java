import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends Application{
    //Integer[][] a = new Integer[][]{{2,4,8,16},{32,64,128,256},{512,1028,2056,2},{4,8,16,32}};
    Integer[][] a = new Integer[4][4];
    Integer randomA;
    Integer randomB;
    Boolean up, right, down, left;
    GridPane gridPane;

    public void setA() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                a[i][j] = 0;
            }
        }
    }

    public void random() {
        randomA = ThreadLocalRandom.current().nextInt(0, 4);
        randomB = ThreadLocalRandom.current().nextInt(0, 4);
    }

    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(800, 800);
        VBox vBox = new VBox();
        gridPane = new GridPane();
        gridPane.setPrefSize(600, 600);
        up = false;
        right = false;
        down = false;
        left = false;

        setA();
        random();
        a[randomA][randomB] = 2;
        paint(gridPane);
        /*while (!up && !right && !down && !left) {

        }*/

        Button run = new Button();
        vBox.getChildren().add(run);
        borderPane.setCenter(gridPane);
        borderPane.setRight(vBox);

        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                System.out.println(event.getCode());
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
            }
        });
        /*scene.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                System.out.println(event.getCode());
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
            }
        });*/
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
                test.setText(Integer.toString(a[i][j]));
                gridPane.add(test, j, i);
            }
        }
    }
    public void update(){
        if(up){
            if(a[0][0]==0&&a[1][0]==0&&a[2][0]==0&&a[3][0]==0){
                for(int i = 0; i<4;i++){
                    for(int j = 0; j<3;j++){
                        a[i][j]=a[i][j+1];
                    }
                }
            }
            paint(gridPane);
            printA();
        }
        if(right){
            if(a[3][0]==0&&a[3][1]==0&&a[3][2]==0&&a[3][3]==0){
                for(int i = 3; i>0;i--){
                    for(int j = 0;j<4;j++){
                        a[i][j]=a[i-1][j];
                    }
                }
            }
            paint(gridPane);
            printA();
        }
        if(down){
            if(a[0][3]==0&&a[1][3]==0&&a[2][3]==0&&a[3][3]==0){
                for(int i =0;i<4;i++){
                    for(int j = 3; j>0;j--){
                        a[i][j]=a[i][j-1];
                    }
                }
            }
            paint(gridPane);
            printA();
        }
        if(left){
            if(a[0][0]==0&&a[0][1]==0&&a[0][2]==0&&a[0][3]==0){
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        a[i][j]=a[i+1][j];
                    }
                }
            }
            paint(gridPane);
            printA();
        }
    }
    public void printA(){
        for(int i = 0;i<4;i++){
            System.out.println("\n");
            for (int j = 0; j <4 ; j++) {
                System.out.println(a[j][i]);
            }
        }
    }
    public static void main(String[] args) {
        launch(Game.class, args);
    }
}
