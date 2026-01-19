package Mosaic2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Class Individual mempresentasikan 1 individu atau solusi yang potensial dalam
 * algo GA, Setiap individu
 * memiliki kromosom, dan nilai fitness, class ini bertanggung jawab untuk
 * mengukut seberapa bagus suatu individu
 * yang dimiliki dibandingkan dengan jawaban benarnya
 * 
 * Sumber:
 * Sendiri dengan bantuan LLM untuk debugging
 * 
 * @author Axel, Davin, Keane
 * 
 */
public class Individual implements Comparable<Individual> {
    static int n; // Ukuran grid
    public Chromosome chromosome; // map warna dengan bit, 1 = kotak hitam, 0 = kotak putih
    public double fitness; // nilai kualitas suatu solusi (0 - 1)
    public Random MyRand; // random generator dikirim dari luar untuk membuat invididu acak
    public double parentProbability; // probabilitas individu ini terpilih sbg parent
    static int[][] map; // Peta soal dari input
    static List<Coordinate> numberLocation; // grid yang memiliki angka
    public int[] actualValues; // Array untuk menyimpan jawaban sekarang
    public List<Coordinate> wrongNumber = new ArrayList<>(); // List buat nyimpen koordinat yang salah

    // Represntasi kotak
    private static final int FILLED = 1;
    private static final int EMPTY = 0;

    /**
     * Konstruktor untuk membuat Individu Acak untuk generasi awal
     * 
     * @param MyRand Random generator
     */
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

    /**
     * Konstruktor untuk membuat Individu dari Kromosom yang sudah ada
     * 
     * @param MyRand     Random generator
     * @param chromosome Kromosom
     */
    public Individual(Random MyRand, Chromosome chromosome) {
        this.MyRand = MyRand;
        this.chromosome = chromosome;
        // this.numberLocation = numberLocation;
        this.setFitness();
        this.parentProbability = 0;
    }

    /**
     * Mengecek apakah koordinat (baris dan kolom) valid berada di dalam grid
     * 
     * @param row Baris
     * @param col Kolom
     * @return true jika valid
     */
    static boolean isValid(int row, int col) {
        return row >= 0 && row < n &&
                col >= 0 && col < n;
    }

    /**
     * Mengambil nilai sel (Hitam/Putih) dari kromosom
     * 
     * @param row Baris.
     * @param col Kolom.
     * @return 1 (Hitam) atau 0 (Putih).
     */
    public int getCell(int row, int col) {
        return chromosome.getCell(row, col);
    }

    /**
     * Menghitung nilai fitneess setiap individu
     * kami menggunakan soft penalty untuk error dan reward untuk jawaban benar
     * 
     * Error Score: Total selisih antara jumlah kotak hitam aktual dan target
     * Reward: Persentase clue yang sudah benar sempurna (selisih 0)
     * rumus akhir: this.fitness = (base + reward) / 2.0;
     */
    public void setFitness() {
        int totalError = 0; // total kesalahan 1 map (selisih expected - actual)
        int correctClues = 0; // berapa banyak angka clue yang sudah memiliki kotak hitam sesuai dengan
                              // angkanya

        // Array untuk menyimpan hasil itungan
        if (this.actualValues == null || this.actualValues.length != numberLocation.size()) {
            this.actualValues = new int[numberLocation.size()];
        }

        // Reset
        this.wrongNumber.clear();

        int i = 0;
        // Loop untuk menghitung jumlah salah
        for (Coordinate c : numberLocation) {
            // Itung kotak item yang ada di sekitar target berdasarkan kromosom saat ini
            int black = countBlackCell(c.getX(), c.getY());

            // Simpan nilai aktual
            this.actualValues[i] = black;

            // Itung selisih
            int diff = Math.abs(black - c.getValue());
            totalError += diff;

            // itung banyak angka yang udah bener
            if (diff == 0)
                correctClues++; // Tambah poin kalau benar
            else
                wrongNumber.add(c); // Catat kesalahannya
            i++;
        }

        // Normalisasi error yang terjadi dengan menghitung jumlah maksimal possible
        // error
        int maxPossibleError = 0;
        for (Coordinate number : numberLocation) {
            // Asumsi error maksimal = jumlah tetangga
            maxPossibleError += number.findSumNeighbor(map.length) - number.getValue();
        }

        // Error score (0 = Bagus, 1 = Salah Besar)
        double errorScore = (double) totalError / maxPossibleError;

        // Base fitness dari constraint (soft penalti)
        double base = 1.0 - errorScore;

        // Reward: makin banyak yang bener, makin naik
        double reward = (double) correctClues / numberLocation.size(); // ini max valuenya juga 1

        // Rata rata dari base dan reward agar hasil akhir 1 jika benar
        this.fitness = (base + reward) / 2.0;
    }

    /**
     * Menghitung jumlah kotak hitam di sekitar koordinat terpilih (3 x 3)
     * 
     * @param x Baris pusat
     * @param y kolom pusat
     * @return jumlah kotak hitam
     */
    public int countBlackCell(int x, int y) {
        int count = 0;
        // Loop sebanyak grid 3x3 untuk mencari kotak hitam
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                // memastikan koordinat titik pencarian tidak keluar dari papan
                if (i >= 0 && i < n && j >= 0 && j < n) {
                    count += getCell(i, j);
                }
            }
        }
        return count;
    }

    /**
     * Membuat kromosom baru secara random
     * 
     * @return kromosom baru yang sudah memiliki isi random
     */
    public Chromosome generateRandomChromosome() {
        Chromosome randomChromosome = new Chromosome(MyRand);
        randomChromosome.generateRandom(n);
        return randomChromosome;
    }

    /**
     * Melakukan crossover dengan metode:
     * 1. Uniform Crossover
     * 2. Single Point crossover
     * 3. Two point crossover
     * 4. Horizontal Crossover
     * 5. Vertical Crossover
     * 6. Right Diagonal Crossover
     * 7. Left Diagonal Crossover
     * 8. Center Point Plus Crossover
     * 9. Random Point Plus Crossover
     * 10.Double Diagonal Crossover
     * 
     * @param other Individu parent
     * @return array berisikan 2 anak
     */
    public Individual[] doCrossover(Individual other) {
        // panggil method crossover
        Chromosome[] child = this.chromosome.uniformCrossover(other.chromosome);

        // Buat objek Individual dengan kromosom baru
        Individual child1 = new Individual(this.MyRand, child[0]);
        Individual child2 = new Individual(this.MyRand, child[1]);

        return new Individual[] { child1, child2 };
    }

    // SETTER
    /**
     * atur map
     * 
     * @param map pemetaan soal
     */
    public static void setMap(int[][] map) {
        Individual.map = map;
        Individual.n = map.length;
    }

    /**
     * atur posisi angka
     * 
     * @param numberLocation koordinat yang memiliki angka
     */
    public static void setNumberLocation(List<Coordinate> numberLocation) {
        Individual.numberLocation = numberLocation;
    }

    /**
     * melakukan mutasi pada kromosom di individu dengan rate mutasi sesuai input
     * * Metode mutasi:
     * 1. One bit flip (flip hanya 1 bit acak)
     * 2. Flip all bit (Flip seluruh bit)
     * 3. Probabilistic Bit (Flip bit sesuai dengan suatu peluang)
     * 4. Even Flip Bit (flip hanya indeks genap)
     * 5. Odd Flip Bit (flip hanya indeks ganjil)
     */
    public void doMutation() {
        // this.chromosome.probabilityFlipBitMutation(0.5);
        // this.chromosome.flipOneBitMutation();
        this.chromosome.randomAdjustmentMutation(numberLocation, actualValues, n);
    }

    /**
     * membuat salinan dari individu untuk proses elistim 
     * agar tidak menimpa yang sudah ada
     */
    @Override
    public Individual clone() {
        Chromosome clonedChromosome = new Chromosome(this.MyRand);
        // copy array gen
        clonedChromosome.setGene(Arrays.copyOf(this.chromosome.getGene(), this.chromosome.getGene().length));
        return new Individual(this.MyRand, clonedChromosome);
    }

    /**
     * membandingkan individu berdasarkan fitness dari besar ke kecil
     */
    @Override
    public int compareTo(Individual other) {
        if (this.fitness > other.fitness)
            return -1; // this yg lebih baik
        else if (this.fitness < other.fitness)
            return 1; // untuk other lebih baik
        else
            return 0; // jika sama
    }

    /**
     * ubah menjadi string dari grid puzzle yang ada (1 = B = Black, 0 = W = White)
     */
    @Override
    public String toString() {
        return this.chromosome.toString();
    }

    /**
     * print hasil jawaban apakah benar (berhasil ditemukan) atau salah (tidak
     * ditemukan)
     */
    public void printSavedErrors() {
        System.out.println("============ JAWABAN SALAH =============");

        // Cek jaga jaga aja
        if (this.actualValues == null) {
            System.out.println("Belum ada data fitness yang dihitung.");
            return;
        }

        int j = 1;
        // Loop untuk mencari nilai actual dan expected
        for (int i = 0; i < numberLocation.size(); i++) {
            // Ambil nilai yang disimpan pas setFitness
            int actual = this.actualValues[i];
            int expected = numberLocation.get(i).getValue();

            if (actual != expected) {
                Coordinate c = numberLocation.get(i);

                System.out.printf("%d. Expected : %d, Actual : %d at [%d, %d]\n",
                        j++, expected, actual, (c.getX() + 1), (c.getY() + 1));
            }
        }
        System.out.println("========================================");
    }
}