package main.neural;

import java.util.ArrayList;

/**
 * Created by jorgelima on 3/14/16.
 */
public class Layer {

    public int numNeurons;
    public ArrayList<Neuron> neuronsInLayer = new ArrayList<>();

    public Layer(int numNeurons, int numInputsNeuron){
        this.numNeurons = numNeurons;

        for(int i = 0; i < numNeurons; i++){
            neuronsInLayer.add(new Neuron(numInputsNeuron));
        }
    }

    public int getNumNeurons(){
        return numNeurons;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "numNeurons=" + numNeurons +
                ", neuronsLayer=" + neuronsInLayer +
                '}';
    }
}
