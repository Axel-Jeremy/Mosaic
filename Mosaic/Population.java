package Mosaic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    // int maxCapacity;
    int sumRank = 0;
    Random MyRand;
    public int n; // banyak firestation yang di deklarasi
    static int[][] map;

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
        Individual[] parents = new Individual[2];
        parents[0] = tournamentSelection();
        parents[1] = tournamentSelection();
        return parents;
    }

    /**
     * Melakukan seleksi individu menggunakan metode Tournament Selection.
     * Cara kerja: Memilih kandidat secara acak sebanyak tournament akan dilakukan,
     * lalu mengambil yang terbaik (kami pakai roubd of 16)
     *
     * @return 1 individu pemenang turnamen
     */
    private Individual tournamentSelection() {
        Individual best = null;
        int loop = (int) (this.populationSize * 0.1);
        loop = Math.max(2, loop); // minimal 2 individu

        // Menentukan ukuran turnamen (tournament size)
        loop = 16; // Sementara pakai 16

        // Loop untuk melakukan turney nya
        for (int i = 0; i < loop; i++) {
            int idx = MyRand.nextInt(this.populationSize);
            Individual calon = population.get(idx);

            // bandingin kandidat terbaik yang ada sejauh ini
            if (best == null || calon.fitness > best.fitness) {
                best = calon;
            }
        }
        return best;
    }

    /**
     * (Untuk Experimen) memilih parent menggunakan teknik Roulette Wheel Selection
     *
     * @return Array berisi 2 Individu terpilih.
     */
    public Individual[] selectParent() {
        Individual[] parents = new Individual[2];

        // this.population.sort((idv1,idv2) -> idv1.compareTo(idv2));
        // int top = this.population.size() + 1;

        double sumfitness = 0;
        // itung total fitness populasi
        for (int i = 0; i < this.population.size(); i++) {
            sumfitness = sumfitness + this.population.get(i).fitness;
        }
        // System.out.println(sumfitness);

        // itung probabilitasnya
        for (int i = 0; i < this.population.size(); i++) {
            ((Individual) this.population.get(i)).parentProbability = 1
                    - ((1.0 * this.population.get(i).fitness) / sumfitness);
        }

        // Kode untuk roulette
        // putar roda roulette untuk memilih 2 parent
        for (int n = 0; n < 2; n++) {
            int i = -1;
            double prob = this.MyRand.nextDouble();
            double sum = 0.0;

            // cari individu yang range probabilitasnya terkena random
            do {
                i++;
                sum = sum + this.population.get(i).parentProbability;
            } while (sum < prob);
            parents[n] = this.population.get(i);
        }
        return parents;
    }

    /**
     * (Untuk Eksperimen) memilih perent menggunakan Rank Selection,
     *
     * @return Array berisi 2 Individu terpilih.
     */
    public Individual[] selectParentByRank() {
        Individual[] parents = new Individual[2];
        int N = this.population.size();

        // sort populasi berdasarkan fitnessnya (terbaik = index 0)
        Collections.sort(this.population);

        // tetapkan probabilitas berdasarkan Peringkat (Rank).
        // individu terbaik (index 0) mendapat peringkat N.
        // individu terburuk (index N-1) mendapat peringkat 1.
        // Pengaman jika sumRank tidak terinisialisasi
        if (this.sumRank == 0) {
            for (int i = 1; i <= N; i++)
                this.sumRank += i;
        }

        // set probabilitas berdasarkan rank yang didapat
        for (int i = 0; i < N; i++) {
            // i = 0 adalah yang terbaik, i = N-1 adalah yang terburuk.
            int rank = N - i; // Peringkat terbalik (terbaik = N, terburuk = 1)
            this.population.get(i).parentProbability = (double) rank / this.sumRank;
        }

        // seleksi kaya Roulette Wheel menggunakan nilai probilitas rank
        // loop ini memilih 2 parent.
        for (int n = 0; n < 2; n++) {
            double prob = this.MyRand.nextDouble();
            double cumulativeSum = 0.0;
            int i = 0; // mulai dari individu terbaik

            // loop untuk menemukan parent berdasarkan prob
            while (i < N - 1) { // berhenti di N-1 untuk menghindari out of bounds
                cumulativeSum += this.population.get(i).parentProbability;
                if (cumulativeSum >= prob) {
                    break;
                }
                i++;
            }
            // Jika loop selesai (karena prob sangat tinggi atau error floating point),
            // pilih individu terakhir (i akan bernilai N-1).
            parents[n] = this.population.get(i);
        }
        return parents;
    }
}
