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
            newPopulation.addIndividual(population.get(i));
        }

        return newPopulation;
    }

    public void randomPopulation() {
        for (int i = 0; i < this.maxPopulationSize; i++) {
            this.addIndividual(new Individual(this.MyRand));
        }
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

    // Pilih 8 orang random untuk di tandingin
    private Individual tournamentSelection(){
        Individual best = null;
        for (int i = 0; i < 8; i++) {
            int idx = MyRand.nextInt(maxPopulationSize);
            Individual calon = population.get(idx);

            if (best == null || calon.fitness > best.fitness) { //ini mau pake peluang ato engga?
                best = calon;
            }
        }
        return best;
    }
}
