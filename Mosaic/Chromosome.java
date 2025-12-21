package Mosaic;

import java.util.Random;

public class Chromosome {
    private int[] gene;
    public Random MyRand; // random generator dikirim dari luar untuk membuat invididu acak
    private static final int FILLED = 1; // penanda grid ditandai kotak hitam
    private static final int EMPTY = 0; // penanda grid tidak ditandai kotak hitam
    private static final int BOUND = 2; // bound untuk random

    public Chromosome(Random MyRand) {
        // this.gene = gene;
        this.MyRand = MyRand;
    }

    // ambil suatu cell dari chromosome 
    public int getCell(int x, int y, int n){
        return gene[x * n + y];
    }

    public int[] getGene() {
        return gene;
    }

    public void setGene(int[] gene) {
        this.gene = gene;
    }
    

    public void generateRandom(int n){
        this.gene = new int[n*n];
        for (int i = 0; i < n * n; i++) {
            int status = MyRand.nextInt(BOUND);
            gene[i] = (status == 0) ? EMPTY : FILLED;
        }
    }

    // melakukan mutasi dengan metode flip bit
    public void doMutation() {
        for (int i = 0; i < this.gene.length; i++) {
            this.gene[i] = this.gene[i] == 0 ? 1:0;
        }
    }

    // single point (sementara)
    public Chromosome[] doCrossover(Chromosome other){
        // Tentukan titik potong
        int potongan = this.gene.length / 2;

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);
        
        int[] child1Gene = new int[this.gene.length];
        int[] child2Gene = new int[this.gene.length];

        int[] otherGene = other.getGene();
        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < this.gene.length; i++) {
            if (i <= potongan) {
                // Anak 1 mengambil dari Parent 1 (this)
                child1Gene[i] = this.gene[i];
                // Anak 2 mengambil dari Parent 2 (other)
                child2Gene[i] = otherGene[i];
            } else {
                // Anak 1 mengambil dari Parent 2 (other)
                child1Gene[i] = otherGene[i];
                // Anak 2 mengambil dari Parent 1 (this)
                child2Gene[i] = this.gene[i];
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);
        
        return new Chromosome[] {child1, child2};
    }

    @Override
    public String toString() {
        String res = "";
        int count = 0;
        int length = (int) Math.sqrt(gene.length);
        // System.out.println(gene.length);
        for (int i = 0; i < gene.length; i++) {
            res += gene[i] + "";
            count++;
            if (count == length) {
                count = 0;
                res += "\n";
            }
        }
        return res;
    }
}
