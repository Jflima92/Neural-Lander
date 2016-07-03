package main.neural;

import java.util.ArrayList;

/**
 * Created by jorgelima on 3/14/16.
 */
public class NeuralNetwork {

    private int numInputs;
    private int numOutputs;
    private int numHiddenLayers;
    private int neuronsPerLayer;
    private ArrayList<Layer> neuronLayers = new ArrayList<>();

    public NeuralNetwork(){
        //Initialize parameters, default for starters
        this.numInputs = 6;
        this.numHiddenLayers = 5;
        this.neuronsPerLayer = 10;
        this.numOutputs = 2;
        this.CreateNetwork();
    }

    private void CreateNetwork(){

        // Creation of the Network Layers
        if(numHiddenLayers > 0){

            // First layer creation
            neuronLayers.add(new Layer(neuronsPerLayer, numInputs));
            // If needed new Layers are created
            for(int i = 0; i < numHiddenLayers-1; i++){
                neuronLayers.add(new Layer(neuronsPerLayer, neuronsPerLayer));
            }

            // Output layer creation
            neuronLayers.add(new Layer(numOutputs, neuronsPerLayer));
        }
        else
        {
            // Output layer creation
            neuronLayers.add(new Layer(numOutputs, numInputs));
        }
    }

    public ArrayList<Double> getWeights(){

        ArrayList<Double> weights = new ArrayList<>();

        // For each layer
        for(int i = 0; i < numHiddenLayers + 1; i++){
            Layer l = neuronLayers.get(i);
            // For each neuron
            for(int j = 0; j < l.getNumNeurons(); j++){
                Neuron n = l.neuronsInLayer.get(j);
                // For each weight
                for(int k = 0; k < n.numInputs; k++){
                    weights.add(n.inputsWeight.get(k));
                }
            }
        }

        return weights;
    }

    public void putWeights(ArrayList<Double> _weights){
        int w = 0;

        // For each layer
        for(int i = 0; i < numHiddenLayers + 1; i++){
            Layer l = neuronLayers.get(i);
            // For each neuron
            for(int j = 0; j < l.getNumNeurons(); j++){
                Neuron n = l.neuronsInLayer.get(j);
                // For each weight
                for(int k = 0; k < n.numInputs; k++){
                    neuronLayers.get(i).neuronsInLayer.get(j).inputsWeight.set(k, _weights.get(w++));
                }
            }
        }
    }

    public int getNumberOfWeights(){
        int weights = 0;

        // For each layer
        for(int i = 0; i < numHiddenLayers + 1; i++){
            Layer l = neuronLayers.get(i);
            // For each neuron
            for(int j = 0; j < l.getNumNeurons(); j++){
                Neuron n = l.neuronsInLayer.get(j);
                // For each weight
                for(int k = 0; k < n.numInputs; k++){
                    weights++;
                }
            }
        }

        return weights;
    }

    public ArrayList<Double> update(ArrayList<Double> inputs){
        ArrayList<Double> outputs = new ArrayList<>();
        int cWeight = 0;

        // Check that the amount of inputs is correct
        if( inputs.size() != numInputs){
            return outputs;
        }

        // For each layer
        for(int i = 0; i < numHiddenLayers + 1; i++){
            if (i > 0){
                inputs.clear();
                inputs.addAll(outputs);
            }

            outputs.clear();

            cWeight = 0;

            // For each neuron sum the inputs * weights. Send the total to the sigmoid function to the output
            for(int j = 0; j < neuronLayers.get(i).getNumNeurons(); j++){
                double netInput = 0;

                int numInputs = neuronLayers.get(i).neuronsInLayer.get(j).numInputs;
                // For each weight
                for(int k = 0; k < numInputs; k++){
                    // Sum the weights x inputs

                    netInput += neuronLayers.get(i).neuronsInLayer.get(j).inputsWeight.get(k) * inputs.get(cWeight++);
                }

                netInput += neuronLayers.get(i).neuronsInLayer.get(j).inputsWeight.get(numInputs-1) * -1; //BIAS
                outputs.add(applySigmoid(netInput, 1));

                cWeight = 0;
            }
        }
        return outputs;
    }

    public double applySigmoid(double inp, double response){
        return ( 1 / ( 1 + Math.exp(-inp / response)));
    }

}
