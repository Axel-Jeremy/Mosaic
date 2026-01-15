package Mosaic;

public class Hyperparam {
    int totalGeneration;
    int maxPopulationSize;
    double crossoverRate; // skala 0-1
    double mutationRate; // skala 0-1
    double elitismPct;
    
    public Hyperparam(int totalGeneration, int maxPopulationSize, double crossoverRate, double mutationRate,
            double elitismPct) {
        this.totalGeneration = totalGeneration;
        this.maxPopulationSize = maxPopulationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.elitismPct = elitismPct;
    }

    public int getTotalGeneration() {
        return totalGeneration;
    }

    public void setTotalGeneration(int totalGeneration) {
        this.totalGeneration = totalGeneration;
    }

    public int getMaxPopulationSize() {
        return maxPopulationSize;
    }

    public void setMaxPopulationSize(int maxPopulationSize) {
        this.maxPopulationSize = maxPopulationSize;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double getElitismPct() {
        return elitismPct;
    }

    public void setElitismPct(double elitismPct) {
        this.elitismPct = elitismPct;
    }
}
