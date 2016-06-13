package main.genetics;

import java.util.ArrayList;

/**
 * Created by jorgelima on 6/7/16.
 */
public class Genome {
    private ArrayList<Double> weights;
    private double fitness;

    public Genome(){
        fitness = 0;
        weights = new ArrayList<>();
    }

    public Genome(ArrayList<Double> weights, double fitness){
        this.weights = weights;
        this.fitness = fitness;
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void addWeight(double w){
        weights.add(w);
    }

    @Override
    public String toString() {
        return "Genome{" +
                "weights=" + weights +
                ", fitness=" + fitness +
                '}';
    }


}
