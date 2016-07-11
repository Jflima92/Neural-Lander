package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Mars Lander Simulation");
        this.primaryStage.setResizable(false);
        this.primaryStage.sizeToScene();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mars.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        controller.setStage(this.primaryStage);

        /*final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X");
        xAxis.setLowerBound(0);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1);
        yAxis.setTickUnit(0.1);
        final Label label = new Label("Generation");
        label.setText("1");


        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Mars Lander Simulation");


        Engine engine = new Engine("src/main/mars/tc1.txt");

        ArrayList<HashMap<Integer, Integer>> best = engine.getBest();
        ArrayList<Pair> terrain = engine.getTerrain();

        XYChart.Series series = new XYChart.Series();
        series.setName("Attempt 1");

        for(int i = 0; i< best.size(); i++){
            for(Map.Entry e: best.get(i).entrySet()){
                series.getData().add(new XYChart.Data((int)e.getKey(), e.getValue()));
            }
        }


        XYChart.Series terrainserie = new XYChart.Series();
        terrainserie.setName("Terrain");

        System.out.println(terrain);
        for(int i = 0; i< terrain.size(); i++){

                terrainserie.getData().add(new XYChart.Data((int)terrain.get(i).getKey(), terrain.get(i).getValue()));
}


        Scene scene  = new Scene(lineChart,800,600);

        lineChart.getData().addAll(series, terrainserie);

        primaryStage.setScene(scene);*/
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
