package Mosaic2D;

import java.util.List;
import java.util.Random;
/**
 * Class Chromosome mempresentasikan struktur awal dari algo GA yang
 * mempresentasikan grid 2 dimensi
 * yang setiap gen nya akan bernilai 1 jika dia merupakan kotak hitam dan 0 jika
 * putih/kosong
 * 
 * Sumber: ...
 * 
 * @author Axel, Davin, Keane
 * 
 */
public class Chromosome {
    private int[][] gene;
    public Random MyRand; // random generator
    private static final int FILLED = 1; // penanda grid ditandai kotak hitam
    private static final int EMPTY = 0; // penanda grid ditandai kotak putih
    private static final int BOUND = 2; // batas untuk random
    private Crossover crossover;
    private Mutation mutation;

    /**
     * Konstraktor untuk chromosome
     * 
     * @param MyRand Generator untuk angka acak
     */
    public Chromosome(Random MyRand) {
        // this.gene = gene;
        this.MyRand = MyRand;
        this.crossover = new Crossover(MyRand);
        this.mutation = new Mutation(MyRand);
    }

    /**
     * ambil nilai gen pada grid(baris, kolom) tertentu dan di mapping dari
     * koordinat 2D ke array 1D
     *
     * @param x Baris
     * @param y Kolom
     * @param n Ukuran sisi grid
     * @return Nilai gen (1 atau 0)
     */
    public int getCell(int x, int y) {
        return gene[x][y];
    }

    /**
     * Membalik nilai sel pada koordinat tertentu (Flip Bit)
     * Jika 0 menjadi 1 dan sebaliknya
     * @param x Baris
     * @param y Kolom
     * @param n Ukuran sisi grid
     */
    public void flipCell(int x, int y) {
        gene[x][y] = gene[x][y] == 0 ? 1 : 0;
    }

    /**
     * ambil seluruh array gene
     * 
     * @return Array integer gene
     */
    public int[][] getGene() {
        return gene;
    }

    /**
     * mengatur seluruh nilai gene
     * 
     * @param gene Array integer gene baru
     */
    public void setGene(int[][] gene) {
        this.gene = gene;
    }

    /**
     * method untuk generate random yang berfungsi mengisi gene awal dengan acak
     * digunakan hanya pada generasi pertama
     */
    public void generateRandom(int n) {
        this.gene = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int status = MyRand.nextInt(BOUND);
                gene[i][j] = (status == 0) ? EMPTY : FILLED;
            }
        }
    }

    /**
     * mutasi dengan flip 1 bit random
     */
    public void flipOneBitMutation() {
        setGene(this.mutation.flipOneBit(this));
    }

    /**
     * mutasi dengan flip seluruh bit
     */
    public void flipAllBitMutation() {
        setGene(this.mutation.flipAllBit(this));
    }

    /**
     * mutasi dengan flip bit random yang terpilih dari probability
     * 
     * @param probability peluang mutasi per generasi nya
     */
    public void probabiltyFlipBitMutation(int probability) {
        setGene(this.mutation.probabiltyFlip(this, probability));
    }

    /**
     * Melakukan mutasi untuk memperbaiki kromosom dengan membandingkan nilai pada angka dengan jumlah kotak hitam aktual.
     * Jika tidak sesuai, maka akan membalik sel hitam di sekitar angka tersebut sampai sama dengan jumlah kotak hitam yang diharapkan
     * 
     * @param numberLocation Daftar koordinat angka pada puzzle
     * @param actual Array yang berisi jumlah kotak hitam aktual di sekitar setiap clue
     * @param n Ukuran sisi grid
     */
    public void randomAdjustmentMutation(List<Coordinate> numberLocation, int[] actual, int n) {
        setGene(this.mutation.randomAdjustment(this, numberLocation, actual, n));
    }

    /**
     * Melakukan Uniform Crossover
     * Setiap gen anak akan dipilih secara acak dari salah satu orang tua
     *
     * @param other Parent kedua (pasangan)
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] uniformCrossover(Chromosome other) {
        return crossover.uniformCrossover(this, other);
    }

    /**
     * Melakukan Single Point Crossover
     * Memotong kromosom di satu titik acak, lalu menukar bagian ekornya
     *
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] singlePointCrossover(Chromosome other) {
        return crossover.singlePointCrossover(this, other);
    }

    /**
     * Melakukan Two Point Crossover dengan memilih dua titik potong,
     * lalu menukar segmen di antara kedua titik tersebut
     *
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] twoPointCrossover(Chromosome other) {
        return crossover.twoPointCrossover(this, other);
    }

    /**
     * Representasi String dari Chromosome untuk visualisasi
     * Mengubah 1 menjadi "B" (Black) dan 0 menjadi "W" (White)
     *
     * @return String grid puzzle
     */
    @Override
    public String toString() {
        String res = "";

        for (int i = 0; i < gene.length; i++) {
            for (int j = 0; j < gene[i].length; j++) {
                if (gene[i][j] == 1)
                    res += "B ";
                else
                    res += "W ";
            }
            res += "\n";
        }
        return res;
    }
}
