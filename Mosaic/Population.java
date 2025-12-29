package Mosaic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    public Population(Random MyRand, int maxPopulationSize, double elitismPct) {
        // skala 0-1
        this.MyRand = MyRand; // menggunakan random generator dari luar
        this.maxPopulationSize = maxPopulationSize;
        this.population = new ArrayList<Individual>();
        this.elitismPct = elitismPct;
        // this.maxCapacity = maxCapacity;
        for (int i = 1; i <= this.maxPopulationSize; i++)
            this.sumRank = this.sumRank + i;

    }

    public Individual getBestIdv() {
        return this.population.get(0);
    }

    // Untuk handle elitism nya
    public Population handleElitism(){
        Population newPopulation = new Population(MyRand, maxPopulationSize, elitismPct);
        
        int count = (int) (maxPopulationSize * elitismPct);
        for (int i = 0; i < count; i++) {
            // ambil dari populasi terus masukin ke newPop
            newPopulation.addIndividual(population.get(i).clone());
        }

        return newPopulation;
    }

    public void randomPopulation() {
        for (int i = 0; i < this.maxPopulationSize; i++) {
            this.addIndividual(new Individual(this.MyRand));
        }
        // this.populationSize = maxPopulationSize;
    }

    public boolean isFilled() {
        return this.maxPopulationSize == this.populationSize;
    }

    public boolean addIndividual(Individual newIdv) { // masukkan individu baru
        if (this.populationSize >= this.maxPopulationSize)
            return false;
        this.population.add(newIdv);
        this.populationSize++;
        return true;
    }

    public void computeAllFitnesses() { // hitung fitness seluruh individu dalam populasi kemudian urutkan
        for (Individual individu : population) {
            individu.setFitness();
        }
        Collections.sort(this.population);
    }

    // Untuk pilih ortu
    public Individual[] selectParents(){
        Individual[] parents = new Individual[2];
        parents[0] = tournamentSelection();
        parents[1] = tournamentSelection();
        return parents;
    }

    // Pilih 16 orang random untuk di tandingin
    private Individual tournamentSelection(){
        Individual best = null;
        int loop = (int) (this.populationSize * 0.1);
        loop = Math.max(2, loop); //minimal 2 individu
        
        loop = 16;
        for (int i = 0; i < loop; i++) {
            int idx = MyRand.nextInt(this.populationSize);
            Individual calon = population.get(idx);

            if (best == null || calon.fitness > best.fitness) {
                    best = calon;
            }
        }
        return best;
    }

    // pemilihan 2 parent dengan teknik roulette wheel
    public Individual[] selectParent() {
        Individual[] parents = new Individual[2];
        // this.population.sort((idv1,idv2) -> idv1.compareTo(idv2));

        // int top = this.population.size() + 1;
        double sumfitness = 0;
        for (int i = 0; i < this.population.size(); i++) {
            sumfitness = sumfitness + this.population.get(i).fitness;
        }
        // System.out.println(sumfitness);
        for (int i = 0; i < this.population.size(); i++) {
            ((Individual) this.population.get(i)).parentProbability = 1
                    - ((1.0 * this.population.get(i).fitness) / sumfitness);
        }

        // Kode untuk roulette
        for (int n = 0; n < 2; n++) {
            int i = -1;
            double prob = this.MyRand.nextDouble();
            double sum = 0.0;
            do {
                i++;
                sum = sum + this.population.get(i).parentProbability;
            } while (sum < prob);
            parents[n] = this.population.get(i);
        }
        return parents;
    }

    public Individual[] selectParentByRank() {
        Individual[] parents = new Individual[2];
        int N = this.population.size();

        // sort populasi berdasarkan fitnessnya
        Collections.sort(this.population);

        // tetapkan probabilitas berdasarkan Peringkat (Rank).
        // individu terbaik (index 0) mendapat peringkat N.
        // individu terburuk (index N-1) mendapat peringkat 1.
        if (this.sumRank == 0) { // Pengaman jika sumRank tidak terinisialisasi
            for (int i = 1; i <= N; i++)
                this.sumRank += i;
        }

        for (int i = 0; i < N; i++) {
            // i = 0 adalah yang terbaik, i = N-1 adalah yang terburuk.
            int rank = N - i; // Peringkat terbalik (terbaik = N, terburuk = 1)
            this.population.get(i).parentProbability = (double) rank / this.sumRank;
        }

        // 3. lakukan "Roulette Wheel" pada Peringkat (Rank).
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
            // kita pilih individu terakhir (i akan bernilai N-1).
            parents[n] = this.population.get(i);
        }

        return parents;
    }
}
