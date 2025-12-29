package Mosaic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/* TODO : 
    
*/

public class Individual implements Comparable<Individual> {
    static int n;
    public Chromosome chromosome; // map warna dengan bit, 1 = kotak hitam, 0 = kotak putih
    public double fitness;
    public Random MyRand; // random generator dikirim dari luar untuk membuat invididu acak
    public double parentProbability; // probabilitas individu ini terpilih sbg parent
    static int[][] map;
    static List<Coordinate> numberLocation;

    private static final int FILLED = 1;
    private static final int EMPTY = 0;

    // membuat individu acak
    public Individual(Random MyRand) {
        this.MyRand = MyRand;
        this.chromosome = generateRandomChromosome();

        // System.out.println("=================");
        // for(int i = 0; i < chromosome.length; i++){
        // System.out.println(chromosome[i]);
        // }

        // this.fitness = setFitness(chromosome);
        this.parentProbability = 0;
    }

    // membuat individu baru berdasarkan kromosom dari luar
    public Individual(Random MyRand, Chromosome chromosome) {
        this.MyRand = MyRand;
        this.chromosome = chromosome;
        // this.numberLocation = numberLocation;
        this.setFitness();
        this.parentProbability = 0;
    }

    static boolean isValid(int row, int col) {
        return row >= 0 && row < map.length &&
                col >= 0 && col < map[0].length;
    }

    // https://stackoverflow.com/questions/19320183/1d-array-to-2d-array-mapping
    // mengambil index dari chromosome yang dimapping dari array 2d
    public int getCell(int row, int col) {
        return chromosome.getCell(row, col, n);
    }

    public void setFitness() {
        // ================================================================
        // eksperimen fitness pertama, menghitung banyak kotak yang salah
        // ================================================================
        // int totalError = 0;
        // for (Coordinate c : numberLocation) {
        // int x = c.getX();
        // int y = c.getY();
        // int value = c.getValue();

        // int blackCtn = countBlackCell(x, y);

        // totalError += Math.abs(blackCtn - value);
        // }
        // this.fitness = 1.0 / (1 + totalError);

        // ================================================================
        // eksperimen fitness 2,
        // ================================================================
        int totalError = 0;
        int correctClues = 0;
        // int maxBlack = 9;

        for (Coordinate c : numberLocation) {
            int black = countBlackCell(c.getX(), c.getY());
            int diff = Math.abs(black - c.getValue());
            totalError += diff;
            if (diff == 0) // itung banyak angka yang udah bener
                correctClues++;
        }

        int maxPossibleError = 0;
        for(Coordinate number : numberLocation){
            maxPossibleError += number.findSumNeighbor(map.length) - number.getValue();
        }

        double errorScore = (double) totalError / maxPossibleError; //max valuenya 1

        // Base fitness dari constraint (soft penalti)
        double base = 1.0 - errorScore;

        // Reward: makin banyak clue yang benar, makin naik
        double reward = (double) correctClues / numberLocation.size(); //ini max valuenya juga 1

        this.fitness = (base + reward) / 2.0;
    }

    

    public int countBlackCell(int x, int y) {
        int count = 0;
        for (int i = x - 1; i < x + 2 && i < n && i >= 0; i++) {
            for (int j = y - 1; j < y + 2 && j < n && j >= 0; j++) {
                count += getCell(i, j);
            }
        }
        return count;
    }

    public Chromosome generateRandomChromosome() {
        Chromosome randomChromosome = new Chromosome(MyRand);
        randomChromosome.generateRandom(n);
        return randomChromosome;
    }

    // single point crossover
    public Individual[] doCrossover(Individual other) {
        //
        Chromosome[] child = this.chromosome.uniformCrossover(other.chromosome);

        // Buat objek Individual dengan kromosom baru
        Individual child1 = new Individual(this.MyRand, child[0]);
        Individual child2 = new Individual(this.MyRand, child[1]);

        return new Individual[] { child1, child2 };
    }

    public static void setMap(int[][] map) {
        Individual.map = map;
        Individual.n = map.length;
    }

    public static void setNumberLocation(List<Coordinate> numberLocation) {
        Individual.numberLocation = numberLocation;
    }

    public void doMutation() {
        this.chromosome.doMutation();
    }

    @Override
    public Individual clone() {
        Chromosome clonedChromosome = new Chromosome(this.MyRand);
        clonedChromosome.setGene(Arrays.copyOf(this.chromosome.getGene(), this.chromosome.getGene().length));
        return new Individual(this.MyRand, clonedChromosome);
    }

    @Override
    public int compareTo(Individual other) {
        if (this.fitness > other.fitness)
            return -1;
        else if (this.fitness < other.fitness)
            return 1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return this.chromosome.toString();
    }
}
