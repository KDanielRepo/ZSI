import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends Application implements Runnable{
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
    TextArea results;
    int iteration = 0;
    int movePlayed = 0;
    int generation = 0;
    boolean groupSet;
    boolean paused;
    private String l = "left";
    private String u = "up";
    private String r = "right";
    private String d = "down";
    XYChart.Series series;
    TableViewHelper t;
    List<XYChart.Series> seriesList = new ArrayList<>();
    BarChart<String,Number> barChart;
    List<Being> testBeings = new ArrayList<>();

    public void setA() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                a[i][j] = 0;
            }
        }
    }

    public void random() {
        int iteration = 0;
        randomA = ThreadLocalRandom.current().nextInt(0, 4);
        randomB = ThreadLocalRandom.current().nextInt(0, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (a[i][j] > 0) {
                    iteration++;
                }
            }
        }
        if (iteration == 16) {
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
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<String, Number>(xAxis,yAxis);
        barChart.setTitle("wykres");
        xAxis.setLabel("kierunek ruchu");
        yAxis.setLabel("ilość ruchu");

        series = new XYChart.Series();
        series.setName("test");
        barChart.getData().addAll(series);

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
        groupSet = false;
        paused = true;

        setA();
        randomA = ThreadLocalRandom.current().nextInt(0, 4);
        randomB = ThreadLocalRandom.current().nextInt(0, 4);
        a[randomA][randomB] = 2;
        setGrid(gridPane);
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
                update();
                //scene.getRoot().requestFocus();
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
                update();
                //scene.getRoot().requestFocus();
            }
        });

        score = new TextArea();
        Button run = new Button("reset");
        run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reset();
            }
        });
        Button pause = new Button("pause");
        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                paused = !paused;
                System.out.println(paused);
                scene.getRoot().requestFocus();
                update();
            }
        });

        results = new TextArea();
        results.setEditable(false);
        t = new TableViewHelper();

        Button show = new Button("show selected result");
        show.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(t!=null&&seriesList!=null&&t.getSelectionModel().getFocusedIndex()>-1){
                    barChart.getData().setAll(seriesList.get(t.getSelectionModel().getFocusedIndex()));
                }
            }
        });

        Button graph = new Button("Show time graph");
        graph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage graphStage = new Stage();
                BorderPane graphPane = new BorderPane();
                int test = t.getSelectionModel().getFocusedIndex();
                //final NumberAxis xAxis = new NumberAxis(-1, 4, 1);
                //final NumberAxis yAxis = new NumberAxis(-5, testBeings.get(test).getMoves().size()+5, 100);
                final NumberAxis xAxis = new NumberAxis();
                xAxis.setAutoRanging(false);
                xAxis.setLowerBound(0);
                xAxis.setUpperBound(testBeings.get(test).getMoves().size()+5);
                final NumberAxis yAxis = new NumberAxis();
                //final ScatterChart<Number,Number> sc = new ScatterChart<Number,Number>(xAxis,yAxis);
                final LineChart<Number,Number> lineChart =
                        new LineChart<Number,Number>(xAxis,yAxis);
                xAxis.setLabel("Typ ruchu");
                yAxis.setLabel("Numer ruchu");
                lineChart.setTitle("Time graph");

                XYChart.Series series = new XYChart.Series();
                for (int i = 0; i <  testBeings.get(test).getMoves().size(); i++) {
                    series.getData().add(new XYChart.Data(i,testBeings.get(test).getMoves().get(i)));
                }
                lineChart.getData().add(series);
                graphPane.setCenter(lineChart);
                Scene graphScene = new Scene(graphPane);
                graphStage.setTitle("Time graph");
                graphStage.setScene(graphScene);
                graphStage.show();
            }
        });

        vBox.getChildren().add(run);
        vBox.getChildren().add(pause);
        vBox.getChildren().add(score);
        vBox.getChildren().add(graph);
        vBox.getChildren().add(show);
        vBox.getChildren().add(t);
        vBox.getChildren().add(barChart);
        borderPane.setCenter(gridPane);
        borderPane.setRight(vBox);

        primaryStage.setTitle("ZSI");
        primaryStage.setScene(scene);
        primaryStage.show();
        //being.generateMove();
    }

    public void setGrid(GridPane gridPane) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                TextArea textArea = new TextArea();
                textArea.setPrefSize(150, 150);
                textArea.setEditable(false);
                if (a[i][j] == 0) {
                    textArea.setText("");
                } else {
                    textArea.setText(Integer.toString(a[i][j]));
                }
                gridPane.add(textArea, i, j);
            }
        }
    }

    public void paint(GridPane gridPane) {
        TextArea textArea;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (a[i][j] == 0) {
                    textArea = (TextArea) gridPane.getChildren().get(j + (i * 4));
                    textArea.setText("");
                } else {
                    textArea = (TextArea) gridPane.getChildren().get(j + (i * 4));
                    textArea.setText(Integer.toString(a[i][j]));
                }
            }
        }
    }

    public void update() {
        if (!paused) {
            if (game) {
                if (!groupSet) {
                    being.generateMove();
                } else {
                    if (movePlayed < being.getMoves().size()) {
                        being.playMove(movePlayed);
                    } else {
                        being.generateMove();
                    }
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
                    if(groupSet&&being==algorithm.getBest()){
                        game = false;
                    }else {
                        being.setScore(Integer.parseInt(score.getText()));
                        being.setGen(generation);
                        being.setLp(iteration);
                        being.setAmount(being.getMoves().size());
                        moved = false;
                    }
                }
                checkGameOver();
            } else {
                testBeings.add(being);
                series.getData().add(new XYChart.Data(u,being.getUpMoves()));
                series.getData().add(new XYChart.Data(r,being.getRightMoves()));
                series.getData().add(new XYChart.Data(d,being.getDownMoves()));
                series.getData().add(new XYChart.Data(l,being.getLeftMoves()));
                seriesList.add(series);
                series = new XYChart.Series();
                t.addRow(being);
                //paused=true;
                //napraw to
                if (!groupSet && iteration < 300) {
                    iteration++;
                    being.setMoved(moved);
                    results.setText(results.getText() + "\n" + iteration + ": " + "score: " + being.getScore() + ", moves: " + being.getMoves().size());
                    algorithm.getGenePool().add(being);
                    being = new Being();
                    reset();
                    //paused=true;
                }else if (groupSet && iteration < 300) {
                    //miedzy tu
                    movePlayed = 0;
                    being.setMoved(moved);
                    results.setText(results.getText() + "\n" + iteration + ": " + "score: " + being.getScore() + ", moves: " + being.getMoves().size());
                    being = algorithm.getGenePool().get(iteration);
                    iteration++;
                    reset();
                    //paused=true;
                    //a tu
                }
                if (iteration == 300) {
                    groupSet = true;
                    algorithm.getAverageFitness();
                    algorithm.calculateGlobalFitness();
                    algorithm.calculateRFitness();
                    algorithm.getFittest();
                    algorithm.createOffspring();
                    algorithm.mutate();
                    algorithm.resetPcPool();
                    System.out.println("best: "+algorithm.getBest().getScore());
                    iteration = 0;
                    generation++;
                    if(generation==1){
                        paused=true;
                    }
                }
            }
        }
    }

    public void reset() {
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
        if (!groupSet) {
            being.generateMove();
        } else {
            being.playMove(0);
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
        //launch(Test.class,args);
    }

    @Override
    public void run() {

    }
}
