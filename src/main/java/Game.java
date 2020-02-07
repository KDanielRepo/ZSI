import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends Application{
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
    TextArea generationNumber;
    int iteration = 0;
    int movePlayed = 0;
    int generation = 0;
    boolean groupSet;
    boolean paused;
    boolean automatic;
    private String l = "left";
    private String u = "up";
    private String r = "right";
    private String d = "down";
    XYChart.Series series;
    TableViewHelper t;
    List<XYChart.Series> seriesList = new ArrayList<>();
    BarChart<String,Number> barChart;
    List<Being> testBeings = new ArrayList<>();
    ProgressBar currentGenProgress;
    ProgressBar overallProgress;

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
        int twoOrFour = ThreadLocalRandom.current().nextInt(0,4);
        if(twoOrFour==3){
            a[randomA][randomB] = 4;
        }else{
            a[randomA][randomB] = 2;
        }
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
        //bar chart
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<String, Number>(xAxis,yAxis);
        barChart.setTitle("Chart");
        xAxis.setLabel("Direction of move");
        yAxis.setLabel("Ammount of moves");

        series = new XYChart.Series();
        barChart.getData().addAll(series);

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1000, 800);
        VBox vBox = new VBox();
        gridPane = new GridPane();
        gridPane.setPrefSize(600, 600);
        being = new Being();
        algorithm = new Algorithm();

        paused = true;
        setA();
        groupSet = false;
        setGrid(gridPane);
        reset();

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
            }
        });

        score = new TextArea();
        score.setEditable(false);
        HBox hBox = new HBox();
        Label label = new Label("Set Number of generations: ");
        generationNumber = new TextArea();
        Button run = new Button("Reset");
        run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reset();
            }
        });
        Button auto = new Button("Automatic");
        auto.setOnAction(event -> {
            automatic = !automatic;
            if(automatic){
                auto.setText("Automatic");
            }else{
                auto.setText("Manual");
            }
        });
        final Button pause = new Button("Start");
        hBox.getChildren().addAll(run,pause,auto);
        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                paused = !paused;
                if(paused){
                    pause.setText("Start");
                }else{
                    pause.setText("Pause");
                }
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
                graphStage.setMinWidth(1920);
                BorderPane graphPane = new BorderPane();
                int test = t.getSelectionModel().getFocusedIndex();
                final NumberAxis xAxis = new NumberAxis();
                xAxis.setAutoRanging(false);
                xAxis.setLowerBound(0);
                xAxis.setUpperBound(testBeings.get(test).getMoves().size()+5);
                final NumberAxis yAxis = new NumberAxis();
                final LineChart<Number,Number> lineChart =
                        new LineChart<Number,Number>(xAxis,yAxis);
                xAxis.setLabel("Type of move");
                yAxis.setLabel("Number of move");
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

        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(graph,show);
        vBox.getChildren().addAll(hBox,label,generationNumber,score,hBox1,t,barChart);
        Label cgpLabel = new Label("Current Generation: ");
        Label overallLabel = new Label("Overall progress: ");
        ProgressBar currentGenProgress = new ProgressBar();
        ProgressBar overallProgress = new ProgressBar();
        VBox gridAndProgress = new VBox();
        HBox progressess = new HBox();
        progressess.getChildren().addAll(cgpLabel,currentGenProgress,overallLabel,overallProgress);
        gridAndProgress.getChildren().addAll(gridPane,progressess);
        borderPane.setLeft(gridAndProgress);
        //borderPane.setCenter(gridPane);
        borderPane.setRight(vBox);

        primaryStage.setTitle("ZSI");
        primaryStage.setScene(scene);
        primaryStage.show();
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
                if(automatic) {
                    if (!groupSet) {
                        being.generateMove();
                    } else {
                        if (movePlayed < being.getMoves().size()) {
                            being.playMove(movePlayed);
                        } else {
                            being.generateMove();
                        }
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
                currentGenProgress.setProgress((double)iteration/300);
                testBeings.add(being);
                series.getData().add(new XYChart.Data(u,being.getUpMoves()));
                series.getData().add(new XYChart.Data(r,being.getRightMoves()));
                series.getData().add(new XYChart.Data(d,being.getDownMoves()));
                series.getData().add(new XYChart.Data(l,being.getLeftMoves()));
                seriesList.add(series);
                series = new XYChart.Series();
                t.addRow(being);
                //napraw to
                if (!groupSet && iteration < 300) {
                    iteration++;
                    being.setMoved(moved);
                    results.setText(results.getText() + "\n" + iteration + ": " + "score: " + being.getScore() + ", moves: " + being.getMoves().size());
                    algorithm.getGenePool().add(being);
                    being = new Being();
                    reset();
                }else if (groupSet && iteration < 300) {
                    //miedzy tu
                    movePlayed = 0;
                    being.setMoved(moved);
                    results.setText(results.getText() + "\n" + iteration + ": " + "score: " + being.getScore() + ", moves: " + being.getMoves().size());
                    being = algorithm.getGenePool().get(iteration);
                    iteration++;
                    reset();
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
                    //System.out.println("best: "+algorithm.getBest().getScore());
                    iteration = 0;
                    generation++;
                    generationNumber.setText(Integer.toString(Integer.parseInt(generationNumber.getText())-1));
                    //overallProgress.setProgress(Integer.parseInt(generationNumber.getText())); DODAJ PROGRES GLOBALNY DSAJDOKJASFLKJASLKDJASLKDJLAKSDJLKASJDLKAJSDLKAJSLDKJALSKDJLKSAJDLASJDJASLKDJALSDJLASDJKLASDLJAKSDJLASKDJALSKDJALSK
                    if(algorithm.getConvergence()>=15){
                        paused=true;
                    }
                    if(Integer.parseInt(generationNumber.getText())==0){
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
        random();
        random();
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

    public static void main(String[] args) {
        launch(Game.class, args);
    }
}
