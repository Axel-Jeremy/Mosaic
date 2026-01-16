package Mosaic2D;

import java.util.Random;

/**
 * Class crossover bertugas untuk melakukan kombinasi dari 2 parent yang sbeleumnya terpilih
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

        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        int m = currentGene[0].length;

        int[][] child1Gene = new int[n][m];
        int[][] child2Gene = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // Acak pengambilan gen untuk Anak 1
                child1Gene[i][j] = MyRand.nextBoolean() ? currentGene[i][j] : otherGene[i][j];
                // Acak pengambilan gen untuk Anak 2 (kebalikannya atau acak ulang, di sini logic aslinya acak ulang)
                child2Gene[i][j] = MyRand.nextBoolean() ? otherGene[i][j] : currentGene[i][j];
            }
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
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        // int totalCells = n * n;

        int potongan = MyRand.nextInt(n);

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];

        int count = 0;

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < n; i++) {
            count = 0;
            for (int j = 0; j < n; j++) {
                if (count <= potongan) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
                count++;
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
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        int totalCells = n * n;


        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];

        // tentukan titik potong 1 dan 2
        int point1 = MyRand.nextInt(totalCells);
        int point2 = MyRand.nextInt(totalCells);

        // Buat agar titik potong start lebih kecil dari end
        int start = Math.min(point1, point2);
        int end = Math.max(point1, point2);

        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // kalau ada di luar titik potong, pertahankan seperti awal
                if (count < start || count > end) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Kalau di dalam titik potong, tuker
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
                count++;
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }
}
