package main.neural;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jorgelima on 3/14/16.
 */
public class Neuron {

    // Number of inputs the neuron receives

    protected int numInputs;
    public ArrayList<Double> inputsWeight = new ArrayList<>();

    public Neuron(int numInputs){
        this.numInputs = numInputs;
        Random rand = new Random();
        for(int i = 0; i < numInputs + 1; i++){
            // Set up the weights with an initial random value
            inputsWeight.add(rand.nextDouble());
        }
    }

    @Override
    public String toString() {
        return "Neuron{" +
                "numInputs=" + numInputs +
                ", inputsWeight=" + inputsWeight +
                '}';
    }
}
