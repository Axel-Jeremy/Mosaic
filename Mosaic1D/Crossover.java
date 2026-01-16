package Mosaic1D;

import java.util.Random;

/**
 * Class crossOver bertugas untuk melakukan kombinasi dari 2 parent yang sbeleumnya terpilih
 * yang akan menghasilkan anak nantinya
 * 
 * Metode:
 * 1. Uniform Crossover
 * 2. Single Point crossover
 * 3. Two point crossover
 * 
 * @author Axel, Davin, Keane
 * 
 */
public class Crossover {
    Random MyRand;

    /**
     * Konstraktor untuk crossover
     * 
     * @param MyRand Generator untuk angka acak
     */
    public Crossover(Random MyRand) {
        this.MyRand = MyRand;
    }
    /**
     * Melakukan Uniform Crossover
     * Setiap gen anak akan dipilih secara acak dari salah satu orang tua
     *
     * @param other Parent kedua (pasangan)
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] uniformCrossover(Chromosome current, Chromosome other) {
        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int geneLength = current.getGene().length;
        
        int[] currentGene = current.getGene();
        int[] otherGene = other.getGene();

        int[] child1Gene = new int[geneLength];
        int[] child2Gene = new int[geneLength];

        for (int i = 0; i < geneLength; i++) {
            // Acak pengambilan gen untuk Anak 1
            child1Gene[i] = MyRand.nextBoolean() ? currentGene[i] : otherGene[i];
            // Acak pengambilan gen untuk Anak 2
            child2Gene[i] = MyRand.nextBoolean() ? otherGene[i] : currentGene[i];
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    /**
     * Melakukan Single Point Crossover
     * Memotong kromosom di satu titik acak, lalu menukar bagian ekornya
     *
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] singlePointCrossover(Chromosome current, Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int geneLength = current.getGene().length;
        
        int potongan = MyRand.nextInt(geneLength);

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);
        
        int[] currentGene = current.getGene();
        int[] otherGene = other.getGene();

        int[] child1Gene = new int[geneLength];
        int[] child2Gene = new int[geneLength];

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < geneLength; i++) {
            if (i <= potongan) {
                // Anak 1 mengambil dari Parent 1 (this)
                child1Gene[i] = currentGene[i];
                // Anak 2 mengambil dari Parent 2 (other)
                child2Gene[i] = otherGene[i];
            } else {
                // Anak 1 mengambil dari Parent 2 (other)
                child1Gene[i] = otherGene[i];
                // Anak 2 mengambil dari Parent 1 (this)
                child2Gene[i] = currentGene[i];
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    /**
     * Melakukan Two Point Crossover dengan memilih dua titik potong,
     * lalu menukar segmen di antara kedua titik tersebut
     *
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] twoPointCrossover(Chromosome current, Chromosome other) {
        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int geneLength = current.getGene().length;
        
        int[] currentGene = current.getGene();
        int[] otherGene = other.getGene();

        int[] child1Gene = new int[geneLength];
        int[] child2Gene = new int[geneLength];

        // tentukan titik potong 1 dan 2
        int point1 = MyRand.nextInt(geneLength);
        int point2 = MyRand.nextInt(geneLength);

        // Buat agar titik potong start lebih kecil dari end
        int start = Math.min(point1, point2);
        int end = Math.max(point1, point2);

        for (int i = 0; i < geneLength; i++) {
            // kalau ada di luar titik potong, pertahankan seperti awal
            if (i < start || i > end) {
                // Anak 1 mengambil dari Parent 1 (this)
                child1Gene[i] = currentGene[i];
                // Anak 2 mengambil dari Parent 2 (other)
                child2Gene[i] = otherGene[i];
            } else {
                // Kalau di dalam titik potong, tuker
                // Anak 1 mengambil dari Parent 2 (other)
                child1Gene[i] = otherGene[i];
                // Anak 2 mengambil dari Parent 1 (this)
                child2Gene[i] = currentGene[i];
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }
}
