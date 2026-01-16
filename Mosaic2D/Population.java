package Mosaic2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class population mempresentasikan kumpulan dari individu dalam GA, dimana
 * class ini bertanggung jawab untuk
 * mengelola pemilihan orang tua, elitism, dan siklus dari seluruh individu di
 * algoritma ini
 * 
 * Sumber: -
 * 
 * @author Axel, Davin, Keane
 * 
 */
public class Population {
    public ArrayList<Individual> population;
    private int maxPopulationSize;
    private int populationSize = 0;
    public double elitismPct;
    int sumRank = 0;
    Random MyRand;
    static int[][] map;
    private ParentSelection parentSelection;

    /**
     * Konstraktor untuk membuat populasi baru
     * Meng-inisialisasi parameter yang diguanakn untuk setiap individu
     * 
     * @param MyRand            Generator untuk angka acak
     * @param maxPopulationSize Jumlah maksmial individu dalam 1 generasi
     * @param elitsimPct        Persentase dari elistism yang digunakan
     */
    public Population(Random MyRand, int maxPopulationSize, double elitismPct) {
        // skala 0-1
        this.MyRand = MyRand;
        this.maxPopulationSize = maxPopulationSize;
        this.population = new ArrayList<Individual>();
        this.elitismPct = elitismPct;
        // this.maxCapacity = maxCapacity;

        this.parentSelection = new ParentSelection(MyRand);

        // Menghitung jumlah rank untuk rank selection
        for (int i = 1; i <= this.maxPopulationSize; i++)
            this.sumRank = this.sumRank + i;
    }

    /**
     * mengambil individu terbaik (fitness tertinggi) dari populasi yang sudah di
     * sorting
     *
     * @return Individu pada indeks ke-0 (terbaik)
     */
    public Individual getBestIdv() {
        return this.population.get(0);
    }

    /**
     * mengambil maksimal populasi
     *
     * @return jumlah populasi maksimal
     */
    public int getMaxPopulationSize() {
        return maxPopulationSize;
    }

    /**
     * mengambil jumlah individu dalam populasi
     *
     * @return jumlah individu saat ini
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * mengambil daftar semua individu di populasi
     *
     * @return arraylist individu
     */
    public ArrayList<Individual> getPopulations() {
        return population;
    }

    /**
     * ambil satu individu spesifik berdasarkan indeks dari list populasi
     *
     * @param idx Indeks individu yang diambil
     * @return Individual yang diambil
     */
    public Individual getSpecificIndividual(int idx) {
        return population.get(idx);
    }

    /**
     * menangani proses Elitism
     * Elistim: Mengambil populasi terbaik dari generasi sebelumnya untuk dimasukan
     * ke generasi baru
     * tanpa melalui proses crossover dan mutasi
     *
     * @return objek Population baru yang isinya individu terbaik dari gen
     *         sebelumnya
     */
    public Population handleElitism() {
        // membuat wadah untuk populasi baru
        Population newPopulation = new Population(MyRand, maxPopulationSize, elitismPct);

        // menghitung count individu yang di anggap terbaik
        int count = (int) (maxPopulationSize * elitismPct);

        for (int i = 0; i < count; i++) {
            // ambil dari populasi terus masukin ke newPop
            newPopulation.addIndividual(population.get(i).clone());
        }
        return newPopulation;
    }

    /**
     * method untuk generate random yang berfungsi mengisi populasi awal dengan
     * individu-individu acak
     * digunakan hanya pada generasi pertama
     */
    public void randomPopulation() {
        // Loop sebanyak populasi maksimal dari suatu generasi
        for (int i = 0; i < this.maxPopulationSize; i++) {
            // Membuat individu baru secara acak dan massukin ke list
            this.addIndividual(new Individual(this.MyRand));
        }
        // this.populationSize = maxPopulationSize;
    }

    /**
     * memeriksa batas populasi sudah maksimal atau belum
     *
     * @return true jika penuh, false jika masih ada slot
     */
    public boolean isFilled() {
        return this.maxPopulationSize == this.populationSize;
    }

    /**
     * Menambahkan satu individu baru ke dalam populasi dan
     * melakukan pengecekan kapasitas terlebih dulu sebelum menambahkan
     *
     * @param newIdv Individu yang akan ditambahkan.
     * @return true jika berhasil, false jika populasi sudah penuh.
     */
    public boolean addIndividual(Individual newIdv) {
        // Cek apakah populasi sudah terisi penuh atau belum
        if (this.populationSize >= this.maxPopulationSize)
            return false;

        this.population.add(newIdv);
        this.populationSize++;
        return true;
    }

    /**
     * Menghitung nilai fitness untuk setiap individu dalam populasi kemudian sort
     */
    public void computeAllFitnesses() {
        // Loop untuk setiap individu dan dihitung fitnessnya
        for (Individual individu : population) {
            individu.setFitness();
        }
        // Sort list
        Collections.sort(this.population);
    }

    /**
     * method untuk memilih parent
     * ada beberapa jenis teknik memilih:
     * 1. Tournament Selection
     * 2. Roulete Wheel
     * 3. Rank
     *
     * @return Array berisi 2 Individu terpilih
     */
    public Individual[] selectParents() {
        Individual[] parents = parentSelection.tournamentSelection(this);
        return parents;
    }
}