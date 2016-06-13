package main;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.genetics.Genome;
import main.genetics.NNGeneticAlgorithm;
import main.mars.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable{

    public static double g = -3.711;
    private int i;
    private ArrayList<Pair> points;
    private Player player;
    private ArrayList<Genome> population;
    private ArrayList<SpaceShip> brains;
    private NNGeneticAlgorithm gen;
    private int generations;
    private ArrayList<Double> averageFitness;
    private ArrayList<HashMap<Integer, Integer>> best;
    private ArrayList<Double> bestFitness; //per gen
    private int numticks;
    private State initialState;

    @FXML
    private Stage mainStage;

    @FXML
    private Text gen_, bfitness;

    @FXML
    private NumberAxis xAxis, yAxis;

    @FXML
    private LineChart lineChart;

    @FXML
    private Button startButton, runGeneration;



    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        xAxis.setLowerBound(0);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1);
        yAxis.setTickUnit(0.1);


        this.population = new ArrayList<>();
        this.brains = new ArrayList<>();
        this.points = new ArrayList<>();
        this.averageFitness = new ArrayList<>();
        this.bestFitness = new ArrayList<>();
        this.generations = 0;
        this.numticks = 100;
        this.initializeDir("src/main/mars/tc1.txt");
        this.startButton.setOnAction(this::handleStartButtonAction);
        this.runGeneration.setOnAction(this::handleRunButtonAction);

        addTerrain();


        /*Engine engine = new Engine("src/main/mars/tc1.txt");

        ArrayList<HashMap<Integer, Integer>> best = engine.getBest();
        ArrayList<Pair> terrain = engine.getTerrain();

        XYChart.Series series = new XYChart.Series();
        series.setName("Attempt 1");

        for(int i = 0; i< best.size(); i++){
            for(Map.Entry e: best.get(i).entrySet()){
                series.getData().add(new XYChart.Data((int)e.getKey(), e.getValue()));
            }
        }


  */
    }

    public void runGeneration(){
        int u = 0;


        System.out.println("entering round: " + u);
        for (int i = 0; i < brains.size(); i++) {

            while(!brains.get(i).getEnded()){


                boolean finished = brains.get(i).hasFinished();
                if (!brains.get(i).update()) {
                    System.out.println("ERRROR");
                    break;
                }

                double fitness = 0;
//                    fitness -=  (double) brains.get(i).getRelativeDistance().getValue();
                System.out.println("Y: " + brains.get(i).getPlayer().getCurrentState().getY() + " landed: " + brains.get(i).hasLanded(brains.get(i).getPlayer().getCurrentState()));
                double getDiffVV = Math.abs(Double.valueOf(40) - brains.get(i).getPlayer().getCurrentState().getvSpeed());
                double getDiffHV = Math.abs(Double.valueOf(20) - brains.get(i).getPlayer().getCurrentState().gethSpeed());
                double penalty = (getDiffHV + getDiffVV);

                if(brains.get(i).hasLanded(brains.get(i).getPlayer().getCurrentState())){
                    int pts = getLandingScore(brains.get(i));

                    System.out.println("POINTS: " + pts);
                    System.out.println("ATERRRROUUUUU");
                    fitness += (1000 + Math.random()) / penalty;
                    fitness += pts;
                }
                else {

                    System.out.println("penalty: " + penalty);
                    fitness += (brains.get(i).getPlayer().getCurrentState().getFuel() + Math.random()) / (brains.get(i).getDistance() + penalty);
//                    fitness += pts/4;
                }
                /*if(!brains.get(i).getPlayer().getCurrentState().isOutOfBounds() || !brains.get(i).hasCrashed()) {
                    System.out.println("OR");
                    fitness += brains.get(i).getPlayer().getCurrentState().getFuel() + Math.random();
                }
                else if(!brains.get(i).getPlayer().getCurrentState().isOutOfBounds() && !brains.get(i).hasCrashed()) {
                    fitness += brains.get(i).getPlayer().getCurrentState().getFuel() + Math.random();
                    System.out.println("AND");
                }
                else {
                    System.out.println("CRASHED OR OUT");
                    fitness = 0;
                }*/
                System.out.println("UPDATING FITNESS: " + fitness);
                gen.getPopulation().get(i).setFitness(fitness);

                brains.get(i).setFitness(fitness);
                population.get(i).setFitness(fitness);

            }
            System.out.println("FINISHED " + i + " ship - " + brains.get(i).getPlayer().getCurrentState());
        }

        System.out.println("ENDED GENERATION " + generations);
        gen_.setText((Integer.toString(generations)));
        for (int v = 0; v < brains.size(); v++) {
            System.out.println("brain: " + v + " - " + brains.get(v).getPlayer().getCurrentState());
            System.out.println("fitness: " + brains.get(v).getFitness());
            System.out.println("CRASHED: " + brains.get(v).hasCrashed());

            int finalV = v;
            Task task = new Task<Void>() {
                @Override public Void call() {

                    XYChart.Series series = new XYChart.Series();
                    series.setName("Attempt " + Integer.toString(finalV+1));

                    for(Pair p: brains.get(finalV).getPath()){

                        series.getData().add(new XYChart.Data((int)p.getKey(), p.getValue()));

                    }

                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            lineChart.getData().add(series);
                        }
                    });

//                            lineChart.getData().setAll(series);


                    return null;
                }
            };

            new Thread(task).start();

        }

        averageFitness.add(gen.getAverageFitness());
        bestFitness.add(gen.getBestFitness());

        System.out.println(gen.getBestFitness());


        System.out.println("BEST FITNESS: " + bestFitness);

        generations++;

        population = gen.Epoch();
        bfitness.setText(Double.toString(gen.getBestFitness()));
        for(int t = 0; t < brains.size(); t++){
            brains.get(t).putWeights(population.get(t).getWeights());
            brains.get(t).reset(initialState);
        }

        u = 0;

    }

    public void addTerrain(){
        ArrayList<Pair> terrain = getTerrain();
        System.out.println(terrain);

        Task addTerrain = new Task<Void>() {
            @Override public Void call() {
                XYChart.Series terrainserie = new XYChart.Series();
                terrainserie.setName("Terrain");
                for(int i = 0; i< terrain.size(); i++){

                    terrainserie.getData().add(new XYChart.Data((int)terrain.get(i).getKey(), terrain.get(i).getValue()));
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        lineChart.getData().add(terrainserie);
                    }
                });

                return null;
            }
        };
        new Thread(addTerrain).start();
    }

    private void handleStartButtonAction(ActionEvent actionEvent) {
        int u = 0;
        int x = 0;
        while(x++ != 20) {

            runGeneration();


        }
    }

    private void handleRunButtonAction(ActionEvent actionEvent) {
        int u = 0;
        int x = 0;
        lineChart.getData().clear();
        addTerrain();
        runGeneration();
    }

    public void initializeDir(String dir){

        Scanner in = null;

        try {
            File file = new File(dir);
            in = new Scanner(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int surfaceN = in.nextInt(); // the number of points used to draw the surface of Mars.
        for (int i = 0; i < surfaceN; i++) {

            int landX = in.nextInt(); // X coordinate of a surface point. (0 to 6999)
            int landY = in.nextInt(); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.
            Pair p = new Pair(landX, landY);
            points.add(p);
        }

        this.player = new Player(points);
        this.initialState = new State(in.nextInt(), in.nextInt(), in.nextDouble(), in.nextDouble(), in.nextInt(), in.nextDouble(), in.nextDouble());
        this.player.setCurrentState(initialState);
        System.out.println("landing zone: " + this.player.getLandingZone());

        for(int i = 0; i < 10; i++){
            Player p = new Player(points);
            p.setCurrentState(initialState);
            brains.add(new SpaceShip(p));
        }
        this.gen = new NNGeneticAlgorithm(16, brains.get(0).getNumberOfWeights());

        population = gen.getPopulation();

        for(int x = 0; x < brains.size(); x++){
            brains.get(i).putWeights(population.get(i).getWeights());
        }

        System.out.println(surfaceN + " - " + points);
    }


    public ArrayList<HashMap<Integer,Integer>> getBest(){
        return best;
    }

    public ArrayList<Pair> getTerrain(){
        return points;
    }

    public void start(){


    }

    public void setStage(Stage stage)
    {
        this.mainStage = stage;
    }

    public int getLandingScore(SpaceShip sc) {
        int i = 0;
        if(sc.isLandingHSpeed()) {
            System.out.println("HSPEED");
            i++;
        }
        if(sc.isLandingVSpeed()) {
            System.out.println("VSPEED");
            i++;
        }
        if(sc.isLandingRotation()) {
            System.out.println("ROTATION");
            i++;
        }

        return i;
    }
}
