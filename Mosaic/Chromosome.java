package Mosaic;

import java.util.Random;

public class Chromosome {
    private int[] gene;
    public Random MyRand; // random generator dikirim dari luar untuk membuat invididu acak
    private static final int FILLED = 1; // penanda grid ditandai kotak hitam
    private static final int EMPTY = 0; // penanda grid ditandai kotak putih
    private static final int BOUND = 2; // bound untuk random

    public Chromosome(Random MyRand) {
        // this.gene = gene;
        this.MyRand = MyRand;
    }

    // ambil suatu cell dari chromosome
    public int getCell(int x, int y, int n) {
        return gene[x * n + y];
    }

    public int[] getGene() {
        return gene;
    }

    public void setGene(int[] gene) {
        this.gene = gene;
    }

    public void generateRandom(int n) {
        this.gene = new int[n * n];
        for (int i = 0; i < n * n; i++) {
            int status = MyRand.nextInt(BOUND);
            gene[i] = (status == 0) ? EMPTY : FILLED;
        }
    }

    // melakukan mutasi dengan metode flip bit
    public void doMutation(double probability) {
        // ==========================================
        // mutasi flip bit semua index
        // ==========================================
//         for (int i = 0; i < this.gene.length; i++) {
//         this.gene[i] = this.gene[i] == 0 ? 1:0;
        // =========================================

        // ==========================================
        // mutasi flip bit 1 index random saja
        // ==========================================
//        int idx = MyRand.nextInt(gene.length);
//        int idxArr[] = new int[idx];
//
//        for (int index : idxArr) {
//            index = MyRand.nextInt(gene.length);
//        }
//        this.gene[idx] = this.gene[idx] == 0 ? 1 : 0;

        // ==========================================
        // mutasi Probabilistic
        // ==========================================

        for (int i = 0; i < this.gene.length; i++) {
            if (MyRand.nextDouble() < probability) {
                this.gene[i] = (this.gene[i] == 0) ? 1 : 0;
            }
        }
    }

    // public void doMutation(double mutationRate) {
    // for (int i = 0; i < this.gene.length; i++) {
    // if (MyRand.nextDouble() < mutationRate) {
    // this.gene[i] = (this.gene[i] == 0) ? 1 : 0; // Flip bit
    // }
    // }
    // }

    //uniform crossover
    public Chromosome[] uniformCrossover(Chromosome other) {
        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[] child1Gene = new int[this.gene.length];
        int[] child2Gene = new int[this.gene.length];
        int[] otherGene = other.getGene();

        for (int i = 0; i < this.gene.length; i++) {
            child1Gene[i] = MyRand.nextBoolean() ? this.gene[i] : other.gene[i];
            child2Gene[i] = MyRand.nextBoolean() ? other.gene[i] : this.gene[i];
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    // single point (sementara)
    public Chromosome[] singlePointCrossover(Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int potongan = MyRand.nextInt(this.gene.length);

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

        return new Chromosome[] { child1, child2 };
    }

    // Two Point Crossover
    public Chromosome[] twoPointCrossover(Chromosome other){
        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[] child1Gene = new int[this.gene.length];
        int[] child2Gene = new int[this.gene.length];
        int[] otherGene = other.getGene();

        int point1 = MyRand.nextInt(this.gene.length);
        int point2 = MyRand.nextInt(this.gene.length);

        int start = Math.min(point1,point2);
        int end = Math.max(point1,point2);
        
        for (int i = 0; i < this.gene.length; i++) {
            // kalau ada di luar titik potong, pertahankan seperti awal
            if (i < start || i > end) {
                // Anak 1 mengambil dari Parent 1 (this)
                child1Gene[i] = this.gene[i];
                // Anak 2 mengambil dari Parent 2 (other)
                child2Gene[i] = otherGene[i];
            } else {
                // Kalau di dalam titik potong, tuker
                // Anak 1 mengambil dari Parent 2 (other)
                child1Gene[i] = otherGene[i];
                // Anak 2 mengambil dari Parent 1 (this)
                child2Gene[i] = this.gene[i];
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    @Override
    public String toString() {
        String res = "";
        int count = 0;
        int length = (int) Math.sqrt(gene.length);
        // System.out.println(gene.length);
        for (int i = 0; i < gene.length; i++) {
            res += gene[i] + " ";
            count++;
            if (count == length) {
                count = 0;
                res += "\n";
            }
        }
        return res;
    }
}
