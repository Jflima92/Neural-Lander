package main.mars;


import main.genetics.Genome;
import main.genetics.NNGeneticAlgorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by jorgelima on 5/31/16.
 */
public class Engine {
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

    public Engine(String dir){
        this.population = new ArrayList<>();
        this.brains = new ArrayList<>();
        this.points = new ArrayList<>();
        this.averageFitness = new ArrayList<>();
        this.bestFitness = new ArrayList<>();
        this.generations = 0;
        this.numticks = 100;
        this.initialize(dir);

        this.start();
    }

    public Engine(int i, ArrayList<Pair> points){
        this.i = i;
        this.points = points;
    }

    public void initialize(String dir){

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
        int u = 0;
        int x = 0;
        while(x++ != 5) {
            while (u++ != 50) {
                System.out.println("entering round: " + u);
                for (int i = 0; i < brains.size(); i++) {
                    boolean finished = brains.get(i).hasFinished();
                    if (!brains.get(i).update()) {
                        System.out.println("ERRROR");
                        break;
                    }

                    double fitness = 0;
//                    fitness -=  (double) brains.get(i).getRelativeDistance().getValue();

                    if(brains.get(i).hasFinished())
                        fitness += brains.get(i).getPlayer().getCurrentState().getFuel();



                    System.out.println("UPDATING FITNESS: " + fitness);
                    gen.getPopulation().get(i).setFitness(fitness);

                    brains.get(i).setFitness(fitness);
                    population.get(i).setFitness(fitness);

                }
            }

            System.out.println("ENDED GENERATION " + generations);
            for (int v = 0; v < brains.size(); v++) {
                System.out.println("brain: " + v + " - " + brains.get(v).getPlayer().getCurrentState());
                System.out.println("fitness: " + brains.get(v).getFitness());
            }

            System.out.println("has crashed: " + brains.get(0).hasCrashed() + " - " + brains.get(i).getFitness());
            averageFitness.add(gen.getAverageFitness());
            bestFitness.add(gen.getBestFitness());

            System.out.println("BEST FITNESS: " + bestFitness);

            generations++;

            population = gen.Epoch();

            for(int t = 0; t < brains.size(); t++){
                brains.get(t).putWeights(population.get(t).getWeights());
                brains.get(t).reset(initialState);
            }

            u = 0;

        }



    }

    public ArrayList getOutput(){
        ArrayList moves = new ArrayList();

        moves.add(45.0);
        moves.add(3.0);

        return moves;
    }


}
