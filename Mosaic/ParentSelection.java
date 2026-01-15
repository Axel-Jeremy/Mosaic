package Mosaic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ParentSelection {
    Random MyRand;

    public ParentSelection(Random myRand) {
        MyRand = myRand;
    }

    /**
     * Melakukan seleksi individu menggunakan metode Tournament Selection.
     * Cara kerja: Memilih kandidat secara acak sebanyak tournament akan dilakukan,
     * lalu mengambil yang terbaik (kami pakai roubd of 16)
     *
     * @return 1 individu pemenang turnamen
     */
    public Individual[] tournamentSelection(Population population) {
        Individual top1 = null;
        Individual top2 = null;
        int populationSize = population.getPopulationSize();

        int loop = (int) (populationSize * 0.1);
        loop = Math.max(2, loop); // minimal 2 individu

        // Menentukan ukuran turnamen (tournament size)
        // loop = 16; // Sementara pakai 16

        // Loop untuk melakukan turney nya
        for (int i = 0; i < loop; i++) {
            int idx = MyRand.nextInt(populationSize);
            Individual calon = population.getSpecificIndividual(idx);

            // bandingin kandidat terbaik yang ada sejauh ini
            if (top1 == null || calon.fitness > top1.fitness) {
                if(top1 != null) top2 = top1;
                
                top1 = calon;
            }
        }
        return new Individual[]{top1, top2};
    }

    /**
     * memilih parent menggunakan teknik Roulette Wheel Selection
     *
     * @return Array berisi 2 Individu terpilih.
     */
    public Individual[] selectParent(Population population) {
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

        // itung probabilitasnya
        for (int i = 0; i < populationSize; i++) {
            ((Individual) population.getSpecificIndividual(i)).parentProbability = 1
                    - ((1.0 * population.getSpecificIndividual(i).fitness) / sumfitness);
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
                sum = sum + population.getSpecificIndividual(i).parentProbability;
            } while (sum < prob);
            parents[n] = population.getSpecificIndividual(i);
        }
        return parents;
    }

    /**
     * memilih perent menggunakan Rank Selection,
     *
     * @return Array berisi 2 Individu terpilih.
     */
    public Individual[] selectParentByRank(Population population) {
        int populationSize = population.getPopulationSize();
        Individual[] parents = new Individual[2];
        int N = populationSize;

        ArrayList<Individual> populations = population.getPopulations();
        // sort populasi berdasarkan fitnessnya (terbaik = index 0)
        Collections.sort(populations);

        // tetapkan probabilitas berdasarkan Peringkat (Rank).
        // individu terbaik (index 0) mendapat peringkat N.
        // individu terburuk (index N-1) mendapat peringkat 1.
        // Pengaman jika sumRank tidak terinisialisasi
        if (population.sumRank == 0) {
            for (int i = 1; i <= N; i++)
                population.sumRank += i;
        }

        // set probabilitas berdasarkan rank yang didapat
        for (int i = 0; i < N; i++) {
            // i = 0 adalah yang terbaik, i = N-1 adalah yang terburuk.
            int rank = N - i; // Peringkat terbalik (terbaik = N, terburuk = 1)
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
                cumulativeSum += population.getSpecificIndividual(i).parentProbability;
                if (cumulativeSum >= prob) {
                    break;
                }
                i++;
            }
            // Jika loop selesai (karena prob sangat tinggi atau error floating point),
            // pilih individu terakhir (i akan bernilai N-1).
            parents[n] = population.getSpecificIndividual(i);
        }
        return parents;
    }
}
