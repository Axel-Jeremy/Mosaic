package Mosaic2D;

/**
 * Class Hyperparam memiliki fungsi untuk nyimpen parameter konfigurasi ketika
 * training
 * class ini akan mengatur mutasi, ukuran generasi, dll.
 * 
 * Sumber : Sendiri dengan refrensi Tugas Fire Station
 * 
 * @author Axel, Keane
 * 
 */
public class Hyperparam {
    int totalGeneration;
    int maxPopulationSize;
    double crossoverRate; // skala 0-1
    double mutationRate; // skala 0-1
    double elitismPct;

    /**
     * Konstruktor Hyperparam
     * 
     * @param totalGeneration   Jumlah maksimal generasi
     * @param maxPopulationSize Jumlah individu dalam populasi
     * @param crossoverRate     Peluang crossover
     * @param mutationRate      Peluang mutasi
     * @param elitismPct        Persentase elitism
     */
    public Hyperparam(int totalGeneration, int maxPopulationSize, double crossoverRate, double mutationRate,
            double elitismPct) {
        this.totalGeneration = totalGeneration;
        this.maxPopulationSize = maxPopulationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.elitismPct = elitismPct;
    }

    /**
     * ambil total nilai generasi
     * 
     * @return Jumlah generasi maksimal
     */
    public int getTotalGeneration() {
        return totalGeneration;
    }

    /**
     * Atur nilai total generasi
     * 
     * @param totalGeneration Jumlah generasi
     */
    public void setTotalGeneration(int totalGeneration) {
        this.totalGeneration = totalGeneration;
    }

    /**
     * ambil ukuran maks populasi
     * 
     * @return Jumlah maskimal populasi
     */
    public int getMaxPopulationSize() {
        return maxPopulationSize;
    }

    /**
     * atur nilai maks populasi
     * 
     * @param maxPopulationSize ukuran populasi
     */
    public void setMaxPopulationSize(int maxPopulationSize) {
        this.maxPopulationSize = maxPopulationSize;
    }

    /**
     * ambil ukuran crossover
     * 
     * @return nilai crossover
     */
    public double getCrossoverRate() {
        return crossoverRate;
    }

    /**
     * atur rate crossover
     * 
     * @param crossoverRate ukuran cross over
     */
    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    /**
     * ambil ukuran kemungkinan mutasi
     * 
     * @return nilai kemungkinan mutasi
     */
    public double getMutationRate() {
        return mutationRate;
    }

    /**
     * atur nilai mutation rate
     * 
     * @param maxPopulationSize ukuran mutation
     */
    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    /**
     * ambil nilai terjadi elitism
     * 
     * @return nilai eliti
     */
    public double getElitismPct() {
        return elitismPct;
    }

    /**
     * atur persentase elitism
     * 
     * @param maxPopulationSize ukuran elitism
     */
    public void setElitismPct(double elitismPct) {
        this.elitismPct = elitismPct;
    }
}
