package main.genetics;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jorgelima on 5/30/16.
 */
public class GeneticAlgorithm {
    private Random rand = new Random();
    private int poolSize = 40;

    public GeneticAlgorithm(){
        this.startAlgorithm(47);

    }

    private void startAlgorithm(int target) {
        int gen=0;

        // Create the pool
        ArrayList pool = new ArrayList(poolSize);
        ArrayList newPool = new ArrayList(pool.size());

        // Generate unique cromosomes in the pool
        for (int x=0;x<poolSize;x++) pool.add(new Chromosome(target));

        // Loop until solution is found
        while(true) {
            // Clear the new pool
            newPool.clear();

            // Add to the generations
            gen++;

            // Loop until the pool has been processed
            for(int x=pool.size()-1;x>=0;x-=2) {
                // Select two members
                Chromosome n1 = selectMember(pool);
                Chromosome n2 = selectMember(pool);


                // Cross over and mutate
                n1.crossOver(n2);
                n1.mutate();
                n2.mutate();

                // Rescore the nodes
                n1.scoreChromosome(target);
                n2.scoreChromosome(target);

                // Check to see if either is the solution
                if (n1.total == target && n1.isValid()) { System.out.println("Generations: " + gen + "  Solution: " + n1.decodeChromo()); return; }
                if (n2.total == target && n2.isValid()) { System.out.println("Generations: " + gen + "  Solution: " + n2.decodeChromo()); return; }

                // Add to the new pool
                newPool.add(n1);
                newPool.add(n2);
            }

            // Add the newPool back to the old pool
            pool.addAll(newPool);

        }
    }

    private Chromosome selectMember(ArrayList genotype) {

        // Get the total fitness
        double tot=0.0;
        for (int x=genotype.size()-1;x>=0;x--) {
            double score = ((Chromosome) genotype.get(x)).score;
            tot+=score;
        }
        double slice = tot*rand.nextDouble();

        // Loop to find the node
        double ttot=0.0;
        for (int x=genotype.size()-1;x>=0;x--) {
            Chromosome node = (Chromosome) genotype.get(x);
            ttot+=node.score;
            if (ttot>=slice) { genotype.remove(x); return node; }
        }

        return (Chromosome)genotype.remove(genotype.size()-1);
    }
}
