package main.genetics;

import java.util.Random;

/**
 * Created by jorgelima on 3/14/16.
 */
public class Chromosome {

    public static int CHROMO_LEN = 5;
    private Random rand = new Random();
    private char[] ltable = {'0','1','2','3','4','5','6','7','8','9','+','-','*','/'};
    private double crossRate = .7;
    private double mutRate = .001;

    //Binary enconding of the chromosome - > 0011 1011 1101 0100 0011 CHROMO_LEN * 4
    protected StringBuffer chromosome = new StringBuffer(CHROMO_LEN * 4); // Size of each part of chromossome a number
    private StringBuffer decodedChromosome = new StringBuffer(CHROMO_LEN * 4);
    public double score;
    public int total;

    public Chromosome(StringBuffer chromo) { this.chromosome = chromo; }

    // Random Chromosome Generator
    public Chromosome(int target){

        //Create the Buffer
        for(int i = 0; i < CHROMO_LEN; i++){
            //Current Length
            int position = chromosome.length();

            //Random binary integer
            String binString = Integer.toBinaryString(rand.nextInt(ltable.length));

            int fillLen = 4 - binString.length();

            //Fill to 4
            for(int u = 0; u < fillLen; u++){
                chromosome.append('0');
            }
            //Append the chromossome
            chromosome.append(binString);
        }

        scoreChromosome(target);

    }

    public String decodeChromo() {

        // Create a buffer
        decodedChromosome.setLength(0);

        // Loop throught the chromo
        for (int x=0;x<chromosome.length();x+=4) {
            // Get the
            int idx = Integer.parseInt(chromosome.substring(x,x+4), 2);
            if (idx<ltable.length) decodedChromosome.append(ltable[idx]);
        }

        // Return the string
        return decodedChromosome.toString();
    }

    public void crossOver(Chromosome other) {

        // Should we cross over?
        if (rand.nextDouble() > crossRate) return;

        // Generate a random position
        int pos = rand.nextInt(chromosome.length());

        // Swap all chars after that position
        for (int x=pos;x<chromosome.length();x++) {
            // Get our character
            char tmp = chromosome.charAt(x);

            // Swap the chars
            chromosome.setCharAt(x, other.chromosome.charAt(x));
            other.chromosome.setCharAt(x, tmp);
        }
    }

    // Mutation
    public void mutate() {
        for (int x=0;x<chromosome.length();x++) {
            if (rand.nextDouble()<=mutRate)
                chromosome.setCharAt(x, (chromosome.charAt(x)=='0' ? '1' : '0'));
        }
    }

    public void scoreChromosome(int target) {
        total = addUp();
        if (total == target) score = 0;
        score = (double)1 / (target - total);
    }

    private int addUp() {
        String decodedString = decodeChromo();

        // Total
        int tot = 0;

        // Find the first number
        int ptr = 0;
        while (ptr<decodedString.length()) {
            char ch = decodedString.charAt(ptr);
            if (Character.isDigit(ch)) {
                tot=ch-'0';
                ptr++;
                break;
            } else {
                ptr++;
            }
        }

        // If no numbers found, return
        if (ptr==decodedString.length()) return 0;

        // Loop processing the rest
        boolean num = false;
        char oper=' ';
        while (ptr<decodedString.length()) {
            // Get the character
            char ch = decodedString.charAt(ptr);

            // Is it what we expect, if not - skip
            if (num && !Character.isDigit(ch)) {ptr++;continue;}
            if (!num && Character.isDigit(ch)) {ptr++;continue;}

            // Is it a number
            if (num) {
                switch (oper) {
                    case '+' : { tot+=(ch-'0'); break; }
                    case '-' : { tot-=(ch-'0'); break; }
                    case '*' : { tot*=(ch-'0'); break; }
                    case '/' : { if (ch!='0') tot/=(ch-'0'); break; }
                }
            } else {
                oper = ch;
            }

            // Go to next character
            ptr++;
            num=!num;
        }

        return tot;
    }

    public boolean isValid() {

        // Decode our chromosome
        String decodedString = decodeChromo();

        boolean num = true;
        for (int x=0;x<decodedString.length();x++) {
            char ch = decodedString.charAt(x);

            // Did we follow the num-oper-num-oper-num patter
            if (num == !Character.isDigit(ch)) return false;

            // Don't allow divide by zero
            if (x>0 && ch=='0' && decodedString.charAt(x-1)=='/') return false;

            num = !num;
        }

        // Can't end in an operator
        if (!Character.isDigit(decodedString.charAt(decodedString.length()-1))) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "chromosome=" + chromosome +
                ", decodedChromosome=" + decodedChromosome +
                '}';
    }
}
