import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Gemini {

    // --- VARIABEL GLOBAL DINAMIS ---
    static int ROWS;
    static int COLS;
    static int[][] CLUES;
    static final int EMPTY = -1;

    // --- PARAMETER GENETIK ---
    static final int POPULATION_SIZE = 150;
    static final double MUTATION_RATE = 0.05;
    static final double CROSSOVER_RATE = 0.9;
    static final int ELITISM_COUNT = 5;
    static final int MAX_GENERATIONS = 2000;
    static final int TOURNAMENT_SIZE = 5;

    // Heuristik: Menyimpan status sel yang sudah pasti (True=Hitam, False=Putih, Null=Belum Tahu)
    static Boolean[] fixedGenes;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== SETUP MOSAIC PUZZLE ===");
        System.out.println("Masukkan ukuran papan (Baris Kolom), contoh: 5 5");
        System.out.print(">> ");
        try {
            ROWS = sc.nextInt();
            COLS = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Input salah. Harap masukkan dua angka integer.");
            return;
        }

        // Inisialisasi Papan Clue
        CLUES = new int[ROWS][COLS];
        for (int[] row : CLUES) Arrays.fill(row, EMPTY);

        System.out.println("Masukkan Clue dengan format: baris,kolom nilai");
        System.out.println("Contoh: 1,4 6");
        System.out.println("Ketik 'mulai' atau 'run' jika sudah selesai memasukkan semua clue.");

        while (true) {
            System.out.print(">> ");
            String input = sc.next();

            if (input.equalsIgnoreCase("mulai") || input.equalsIgnoreCase("run") || input.equalsIgnoreCase("done")) {
                break;
            }

            try {
                // Parsing format "x,y"
                String[] parts = input.split(",");
                int r = Integer.parseInt(parts[0]);
                int c = Integer.parseInt(parts[1]);
                int val = sc.nextInt(); // Baca nilai clue setelah spasi

                if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
                    CLUES[r][c] = val;
                } else {
                    System.out.println("Koordinat di luar batas papan!");
                }
            } catch (Exception e) {
                System.out.println("Format salah! Gunakan format: baris,kolom nilai (contoh: 1,4 6)");
                sc.nextLine(); // Clear buffer
            }
        }

        System.out.println("\n=== MEMULAI ALGORITMA GENETIKA ===");
        solveGA();
    }

    static void solveGA() {
        // 1. Inisialisasi Heuristik
        fixedGenes = new Boolean[ROWS * COLS];
        applyHeuristics();

        // 2. Inisialisasi Populasi
        Individual[] population = new Individual[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new Individual();
            population[i].randomize();
        }

        Individual bestSolution = population[0];
        int generation = 0;
        boolean solved = false;

        // 3. Loop Evolusi
        while (generation < MAX_GENERATIONS && !solved) {
            Arrays.sort(population); // Sort berdasarkan fitness terbaik (error terkecil)
            bestSolution = population[0];

            if (bestSolution.fitness == 0) {
                solved = true;
                break;
            }

            if (generation % 100 == 0) {
                System.out.printf("Gen %d | Best Error: %d\n", generation, -bestSolution.fitness);
            }

            Individual[] newPopulation = new Individual[POPULATION_SIZE];

            // Elitism
            for (int i = 0; i < ELITISM_COUNT; i++) {
                newPopulation[i] = population[i];
            }

            // Crossover & Mutation
            for (int i = ELITISM_COUNT; i < POPULATION_SIZE; i++) {
                Individual parent1 = tournamentSelection(population);
                Individual parent2 = tournamentSelection(population);

                Individual child;
                if (Math.random() < CROSSOVER_RATE) {
                    child = parent1.uniformCrossover(parent2);
                } else {
                    child = parent1.copy();
                }

                child.mutate();
                newPopulation[i] = child;
            }

            population = newPopulation;
            generation++;
        }

        System.out.println("\n=== HASIL AKHIR ===");
        System.out.println("Generasi: " + generation);
        System.out.println("Total Error: " + (-bestSolution.fitness));
        bestSolution.printBoard();
    }

    // --- LOGIC HEURISTIK & GA SAMA SEPERTI SEBELUMNYA, TAPI DINAMIS ---

    static void applyHeuristics() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int clue = CLUES[r][c];
                if (clue == EMPTY) continue;

                int validNeighbors = countValidNeighbors(r, c);

                // Jika clue == total tetangga, semua tetangga pasti HITAM
                if (clue == validNeighbors) {
                    lockNeighbors(r, c, true);
                }
                // Jika clue == 0, semua tetangga pasti PUTIH
                else if (clue == 0) {
                    lockNeighbors(r, c, false);
                }
            }
        }
    }

    static int countValidNeighbors(int r, int c) {
        int count = 0;
        for (int nr = r - 1; nr <= r + 1; nr++) {
            for (int nc = c - 1; nc <= c + 1; nc++) {
                if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS) {
                    count++;
                }
            }
        }
        return count;
    }

    static void lockNeighbors(int r, int c, boolean val) {
        for (int nr = r - 1; nr <= r + 1; nr++) {
            for (int nc = c - 1; nc <= c + 1; nc++) {
                if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS) {
                    fixedGenes[nr * COLS + nc] = val;
                }
            }
        }
    }

    static Individual tournamentSelection(Individual[] pop) {
        Random rand = new Random();
        Individual best = null;
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            Individual ind = pop[rand.nextInt(pop.length)];
            if (best == null || ind.fitness > best.fitness) {
                best = ind;
            }
        }
        return best;
    }

    static class Individual implements Comparable<Individual> {
        boolean[] genes = new boolean[ROWS * COLS];
        int fitness = Integer.MIN_VALUE;
        boolean fitnessCalculated = false;

        public void randomize() {
            Random rand = new Random();
            for (int i = 0; i < genes.length; i++) {
                if (fixedGenes[i] != null) {
                    genes[i] = fixedGenes[i];
                } else {
                    genes[i] = rand.nextBoolean();
                }
            }
        }

        public Individual copy() {
            Individual newInd = new Individual();
            System.arraycopy(this.genes, 0, newInd.genes, 0, this.genes.length);
            return newInd;
        }

        public void calculateFitness() {
            int totalError = 0;
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    int clue = CLUES[r][c];
                    if (clue != EMPTY) {
                        int actualBlacks = countBlackNeighbors(r, c);
                        totalError += Math.abs(actualBlacks - clue);
                    }
                }
            }
            this.fitness = -totalError;
            this.fitnessCalculated = true;
        }

        private int countBlackNeighbors(int r, int c) {
            int count = 0;
            for (int nr = r - 1; nr <= r + 1; nr++) {
                for (int nc = c - 1; nc <= c + 1; nc++) {
                    if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS) {
                        if (genes[nr * COLS + nc]) count++;
                    }
                }
            }
            return count;
        }

        public int getFitness() {
            if (!fitnessCalculated) calculateFitness();
            return fitness;
        }

        public Individual uniformCrossover(Individual partner) {
            Individual child = new Individual();
            Random rand = new Random();
            for (int i = 0; i < genes.length; i++) {
                if (rand.nextBoolean()) child.genes[i] = this.genes[i];
                else child.genes[i] = partner.genes[i];
            }
            return child;
        }

        public void mutate() {
            Random rand = new Random();
            for (int i = 0; i < genes.length; i++) {
                if (fixedGenes[i] != null) continue; // Jangan ubah gene yang dikunci heuristik
                if (rand.nextDouble() < MUTATION_RATE) {
                    genes[i] = !genes[i];
                }
            }
            fitnessCalculated = false;
        }

        @Override
        public int compareTo(Individual other) {
            return Integer.compare(other.getFitness(), this.getFitness());
        }

        public void printBoard() {
            System.out.println("Solusi (█ = Hitam, . = Putih):");
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    int idx = r * COLS + c;
                    System.out.print((genes[idx] ? "█ " : ". ") + " ");
                }
                System.out.println();
            }
        }
    }
}