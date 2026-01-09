package Mosaic;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;


/**
 * Class MosaicGA bertindak sebagai penggerak utama dari algo GA ini, dimana akan mengatur siklus seleksi, crossover,
 * dan mutasi dari individu individu di algo GA ini
 * 
 * Sumber: ...
 * 
 * @author Axel, Davin, Keane
 */
public class MosaicGA{
    Random MyRand;                  // Angka Generator
    public int totalGeneration;     // Total generasi maksimal
    public int maxPopulationSize;   // Maksimal populasi
    public double elitismPct;       // persentase orang yang langsung lanjut ke generasi selanjutnya
    public double crossoverRate;    // Peluang crossover
    public double mutationRate;     // Peluang terjadi mutasi
    private int n;                  // jumlah grid di papan nya n*n

/**
     * Konstruktor untuk menginisialisasi parameter Algoritma Genetika.
     *
     * @param MyRand Generator angka random
     * @param n Ukuran grid
     * @param totalGeneration Jumlah generasi maksimal
     * @param maxPopulationSize Ukuran populasi
     * @param elitismPct Persentase elitisme
     * @param crossoverRate Rate crossover
     * @param mutationRate Rate mutasi
     */
public MosaicGA(Random MyRand, int n, int totalGeneration, int maxPopulationSize, double elitismPct, double crossoverRate, double mutationRate) {
        this.MyRand = MyRand;
        this.n = n;
        this.totalGeneration = totalGeneration;
        this.maxPopulationSize = maxPopulationSize;
        this.elitismPct = elitismPct;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
    }

    /**
     * Menjalankan siklus evolusi GA
     *
     * @return Individu terbaik yang ditemukan
     */
    public Individual run() {
        int generation = 1;
        // buat populasi awal
        Population currentPop = new Population(MyRand, this.maxPopulationSize, this.elitismPct);
        currentPop.randomPopulation(); // populasi diisi individu random
        currentPop.computeAllFitnesses(); // hitung seluruh fitnessnya

        // algogen mulai di sini
        while (terminate(generation) == false) { // jika belum memenuhi kriteria terminasi
            // buat populasi awal dengan elitism, bbrp individu terbaik dari populasi
            // sebelumnya sudah masuk
            Population newPop = currentPop.handleElitism();

            // Isi sisa slot yang ada dengan populasi baru dari hasil crossover
            while (newPop.isFilled() == false) {

                // pilih parent
                Individual[] parents = currentPop.selectParents();

                // crossover?
                if (this.MyRand.nextDouble() < this.crossoverRate) {
                     // jika ya, crossover kedua parent
                    Individual[] child = parents[0].doCrossover(parents[1]);
                    
                    // Loop setiap anak yang baru masuk
                    for (int i = 0; i < child.length; i++) {
                        // apakah terjadi mutasi?
                        if (this.MyRand.nextDouble() < this.mutationRate) { 
                            child[i].doMutation();
                            // newPop.addIndividual(child[i]);
                            // System.out.println(this.mutationRate);
                        }
                        // System.out.println(i);
                    }
                    // Loop setiap anak 
                    for (int i = 0; i < child.length; i++) {
                        // masukkan anak ke dalam populasi
                        newPop.addIndividual(child[i]);
                    }
                }
            }
            generation++; // sudah ada generasi baru
            currentPop = newPop; // generasi baru menggantikan generasi sebelumnya
            currentPop.computeAllFitnesses(); // hitung fitness generasi baru
        }
        return currentPop.getBestIdv(); // return individu terbaik dari generasi terakhir
    }

    /**
     * Mengecek kondisi berhenti berdasarkan jumlah maksimal dari 1 generasi
     *
     * @param generation Generasi saat ini
     * @return true jika harus berhenti, false jika lanjut
     */
    private boolean terminate(int generation) {
        if (generation >= this.totalGeneration)
            return true;
        else
            return false;
    }
}