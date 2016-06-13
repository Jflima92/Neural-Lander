package main.genetics;



import main.mars.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jorgelima on 6/7/16.
 */
public class NNGeneticAlgorithm {

    private ArrayList<Genome> population;
    private int popSize;
    private int chromoLenght; //weights per chromo
    private double totalFitness;
    private double bestFitness;
    private double averageFitness;
    private double worstFitness;
    private int fittestGenome;
    public static double mutationRate = 0.2;
    public static double crossoverRate = 0.7;
    private static double maxPerturbation = 0.4;
    private static int numElite = 3;
    private static int numEliteCopies = 2;
    private int generation;
    private Random rand = new Random();

    public NNGeneticAlgorithm(int popSize, int chromoLenght){
        this.population = new ArrayList<>();
        this.popSize = popSize;
        this.chromoLenght = chromoLenght;
        this.totalFitness = 0;
        this.generation = 0;
        this.fittestGenome = 0;
        this.bestFitness = 0;
        this.worstFitness = 99999;
        this.averageFitness = 0;

        //Initialize with random weights and fitnesses 0

        for(int i = 0; i < popSize; i++){
            Genome genome = new Genome();
            for(int u = 0; u < chromoLenght; u++){
                genome.addWeight(getRandomClamped());
            }
            population.add(genome);
        }
    }

    public ArrayList<Double> mutate(ArrayList<Double> chromo){
        ArrayList<Double> ch = new ArrayList<>();
        for(int i = 0; i < chromo.size(); i++){
            if(ThreadLocalRandom.current().nextFloat() < mutationRate){
                double d = chromo.get(i);
                d += (getRandomClamped() * maxPerturbation);
                ch.add(i, d);
            }
            else
                ch.add(i, chromo.get(i));
        }

        return ch;
    }

    public Genome getChromosomeRoulette(){
        double slice = (double) (ThreadLocalRandom.current().nextFloat() * totalFitness);


        Genome genome = null;

        double fitnessSofar = 0;

        for(int i = 0; i < popSize; i++){
            fitnessSofar += population.get(i).getFitness();


            if(fitnessSofar >= slice){
                genome = population.get(i);
                break;
            }
        }
        return genome;
    }

    public Pair crossover(ArrayList<Double> mum, ArrayList<Double> dad){
        ArrayList<Double> baby1 = new ArrayList<>(mum.size());
        ArrayList<Double> baby2 = new ArrayList<>(mum.size());
        Pair toSend = null;

        if((ThreadLocalRandom.current().nextFloat() > crossoverRate) || (mum == dad)){
            baby1 = mum;
            baby2 = dad;
            toSend = new Pair(baby1, baby2);
            return toSend;
        }

        int crossoverPoint = ThreadLocalRandom.current().nextInt(0, chromoLenght-1);


        baby1.addAll(mum.subList(0, crossoverPoint));
        baby2.addAll(dad.subList(0, crossoverPoint));

        baby1.addAll(dad.subList(crossoverPoint, dad.size()));
        baby2.addAll(mum.subList(crossoverPoint, mum.size()));

        toSend = new Pair(baby1, baby2);

        return toSend;
    }

    public ArrayList<Genome> Epoch(){

        reset();


        Collections.sort(population, (s1, s2) -> {
            int fit1 = (int) s1.getFitness();
            int fit2 = (int) s2.getFitness();
            return fit1-fit2;
        });

        calculateBestWorstAvTot();

        for(int b = 0; b < population.size(); b++){
            System.out.println("epoching GEN: " + population.get(b).getFitness());
        }

        ArrayList<Genome> newPop = new ArrayList<>();


        //Now to add a little elitism we shall add in some copies of the
        //fittest genomes. Make sure we add an EVEN number or the roulette
        //wheel sampling will crash
        if (((numEliteCopies * numElite % 2) == 0)){
            newPop.addAll(grabNBest(numElite, numEliteCopies));

        }


        //GA LOOP

        while(newPop.size() < popSize){
            Genome mum = getChromosomeRoulette();
            Genome dad = getChromosomeRoulette();
            Pair offsprings = crossover(mum.getWeights(),dad.getWeights());
            ArrayList<Double> baby = (ArrayList<Double>)offsprings.getKey();

            baby = mutate(baby);
            newPop.add(new Genome(baby, 0));
            baby = (ArrayList<Double>)offsprings.getValue();
            baby = mutate(baby);
            newPop.add(new Genome(baby, 0));
        }
        population = newPop;
        System.out.println("RETURNING NEW POP : " + population.size() + " - " + popSize);
        return newPop;
    }

    private ArrayList<Genome> grabNBest(int NBest, int NumCopies)
    {
        //add the required amount of copies of the n most fittest
        //to the supplied vector
        ArrayList<Genome> pop = new ArrayList<>();
        while(NBest-- != 0)
        {

            for (int i=0; i<NumCopies; ++i)
            {

                pop.add(population.get((popSize - 1) - NBest));
            }
        }
        return pop;
    }

    public ArrayList<Genome> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<Genome> population) {
        this.population = population;
    }

    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    public int getChromoLenght() {
        return chromoLenght;
    }

    public void setChromoLenght(int chromoLenght) {
        this.chromoLenght = chromoLenght;
    }

    public double getTotalFitness() {
        return totalFitness;
    }

    public void setTotalFitness(double totalFitness) {
        this.totalFitness = totalFitness;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness(double bestFitness) {
        this.bestFitness = bestFitness;
    }

    public double getAverageFitness() {
        return averageFitness;
    }

    public void setAverageFitness(double averageFitness) {
        this.averageFitness = averageFitness;
    }

    public double getWorstFitness() {
        return worstFitness;
    }

    public void setWorstFitness(double worstFitness) {
        this.worstFitness = worstFitness;
    }

    public int getFittestGenome() {
        return fittestGenome;
    }

    public void setFittestGenome(int fittestGenome) {
        this.fittestGenome = fittestGenome;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    private float getRandomClamped(){
        float f = ThreadLocalRandom.current().nextFloat() * 2 - 1;
        return f;

    }

    private void calculateBestWorstAvTot()
    {
        totalFitness = 0;

        double HighestSoFar = 0;
        double LowestSoFar  = 9999999;

        for (int i=0; i<popSize; ++i)
        {
            //System.out.println("FITNESS FROM POP " + i + " - " + population.get(i).getFitness());
            //update fittest if necessary
            if (population.get(i).getFitness() > HighestSoFar)
            {
                HighestSoFar	 = population.get(i).getFitness();

                fittestGenome = i;

                bestFitness	 = HighestSoFar;
            }

            //update worst if necessary
            if (population.get(i).getFitness() < LowestSoFar)
            {
                LowestSoFar = population.get(i).getFitness();

                worstFitness = LowestSoFar;
            }

            totalFitness	+= population.get(i).getFitness();


        }//next chromo

        averageFitness = totalFitness / popSize;
    }

    private void reset()
    {
        totalFitness = 0;
        bestFitness		= 0;
        worstFitness		= 9999999;
        averageFitness	= 0;
    }


}
