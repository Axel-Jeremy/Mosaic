package Mosaic2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class Parent Selection berguna untuk mengatur pemilihan orang tua dari
 * popilasi ketika
 * crossover dilakukan
 * Metode:
 * 1. Tournament Selection
 * 2. Roulete Wheel
 * 3. Rank selection
 * 
 * Sumber: 
 *       - Tugas Fire Station yang dimodifikasi dengan bantuan debug LLM
 *       - https://stackoverflow.com/questions/177271/roulette-selection-in-genetic-algorithms
 * 
 * @author Axel, Davin, Keane
 * 
 */
public class ParentSelection {
    Random MyRand; //random generator

    /**
     * Konstraktor parentSelection
     * 
     * @param MyRand Generator untuk angka acak
     */
    public ParentSelection(Random myRand) {
        MyRand = myRand;
    }

    /**
     * Tournament selection menggunakan ide dasar dari tournament dan akan
     * menjalankan tournament sebanyak 2 kali
     * untuk memilih parent 1 dan parent 2 dari masing masing jaura
     * 
     * @param population populasi sekarang
     * @return 2 individu pemenang turnamen
     */
    public Individual[] tournamentSelection(Population population) {
        Individual parent1 = this.tournament(population);
        Individual parent2 = this.tournament(population);

        return new Individual[] { parent1, parent2 };
    }

    /**
     * Tournament selection menggunakan ide dasar dari tournament dan akan
     * menjalankan tournament
     * untuk memilih parent 1 dan parent 2 dari juara 1 dan juara 2 nya
     * 
     * @param population populasi
     * @return 2 individu teratas pemenang turnamen
     */
    // public Individual[] tournamentSelection(Population population) {
    // Individual top1 = null;
    // Individual top2 = null;
    // int populationSize = population.getPopulationSize();

    // int loop = (int) (populationSize * 0.002);
    // loop = Math.max(2, loop); // minimal 2 individu

    // // Menentukan ukuran turnamen (tournament size)
    // // loop = 16; // Sementara pakai 16

    // // Loop untuk melakukan turney nya
    // for (int i = 0; i < loop; i++) {
    // int idx = MyRand.nextInt(populationSize);
    // Individual calon = population.getSpecificIndividual(idx);

    // // bandingin kandidat terbaik yang ada sejauh ini
    // if (top1 == null || calon.fitness > top1.fitness) {
    // if(top1 != null) top2 = top1;

    // top1 = calon;
    // }
    // if(top2 == null && top1 != null && calon.fitness < top1.fitness){
    // top2 = calon;
    // }
    // }
    // return new Individual[]{top1, top2};
    // }

    /**
     * Melakukan seleksi individu menggunakan metode Tournament Selection.
     * Cara kerja: Memilih kandidat secara acak sebanyak tournament akan dilakukan,
     * lalu mengambil yang terbaik (kami pakai round of 16)
     *
     * @param population populasi sekarang
     * @return 1 individu pemenang turnamen
     */
    private Individual tournament(Population population) {
        int populationSize = population.getPopulationSize();

        Individual best = null;
        int loop = (int) (populationSize * 0.003);
        loop = Math.max(2, loop); // minimal 2 individu

        // Menentukan ukuran turnamen (tournament size)
        loop = 16; //pakai 16

        // Loop untuk melakukan turney nya
        for (int i = 0; i < loop; i++) {
            int idx = this.MyRand.nextInt(populationSize);
            Individual calon = population.getSpecificIndividual(idx);

            // bandingin kandidat terbaik yang ada sejauh ini
            if (best == null || calon.fitness > best.fitness) {
                best = calon;
            }
        }
        return best;
    }

    /**
     * memilih parent menggunakan teknik Roulette Wheel Selection
     *
     * @param population populasi sekarang
     * @return Array berisi 2 Individu terpilih.
     */
    public Individual[] rouletteWheelSelection(Population population) {
        int populationSize = population.getPopulationSize();
        Individual[] parents = new Individual[2];

        // this.population.sort((idv1,idv2) -> idv1.compareTo(idv2));
        // int top = this.population.size() + 1;

        double sumfitness = 0;
        // itung total fitness populasi
        for (int i = 0; i < populationSize; i++) {
            sumfitness = sumfitness + population.getSpecificIndividual(i).fitness;
        }
        // System.out.println(sumfitness);

        // itung probabilitas setiap individu di satu populasi untuk jadi parent
        for (int i = 0; i < populationSize; i++) {
            ((Individual) population.getSpecificIndividual(i)).parentProbability = 1
                    - ((1.0 * population.getSpecificIndividual(i).fitness) / sumfitness);
        }

        // kode untuk roulette
        // putar roda roulette untuk memilih 2 parent
        for (int n = 0; n < 2; n++) {
            int i = -1;
            double prob = this.MyRand.nextDouble();
            double sum = 0.0;

            // cari individu pertama yang kumulatif parent probabilitynya lebih tinggi dari probability random
            do {
                i++;
                 //itung parentprobability kumulatif
                sum = sum + population.getSpecificIndividual(i).parentProbability;
            } while (sum < prob); // putar roda roulette selama probability kumulatif < probability random 
            parents[n] = population.getSpecificIndividual(i); //ambil individu terakhir yang membuat kumulatif >= prob 
        }
        return parents;
    }

    /**
     * memilih perent menggunakan Rank Selection,
     *
     * @param population populasi sekarang
     * @return Array berisi 2 Individu terpilih.
     */
    public Individual[] rankSelection(Population population) {
        int populationSize = population.getPopulationSize();
        Individual[] parents = new Individual[2];
        int N = populationSize;

        ArrayList<Individual> populations = population.getPopulations();
        // sort populasi berdasarkan fitnessnya (terbaik = index 0)
        Collections.sort(populations);

        
        // hitung kumulatif rank di satu populasi jika sumRanknya masih 0 (jaga-jaga)
        if (population.sumRank == 0) {
            for (int i = 1; i <= N; i++)
                population.sumRank += i;
        }

        // tentukan probabilitas berdasarkan rank
        // individu terbaik (index 0) mendapat rank N
        // individu terburuk (index N-1) mendapat rank 1
        // makin tinggi rank individu = probability parent makin tinggi
        for (int i = 0; i < N; i++) {
            int rank = N - i;
            population.getSpecificIndividual(i).parentProbability = (double) rank / population.sumRank;
        }

        // seleksi kaya Roulette Wheel menggunakan nilai probilitas rank
        // loop ini memilih 2 parent.
        for (int n = 0; n < 2; n++) {
            double prob = this.MyRand.nextDouble();
            double cumulativeSum = 0.0;
            int i = 0; // mulai dari individu terbaik

            // loop untuk menemukan parent berdasarkan prob
            while (i < N - 1) { // berhenti di N-1 untuk menghindari out of bounds
                //itung parentprobability kumulatif
                cumulativeSum += population.getSpecificIndividual(i).parentProbability;
                if (cumulativeSum >= prob) {
                    break;
                }
                i++;
            }
            //ambil individu terakhir yang membuat kumulatif >= prob 
            parents[n] = population.getSpecificIndividual(i);
        }
        return parents;
    }
}
