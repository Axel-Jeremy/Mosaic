package Mosaic;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/*
Input File nanti akan memiliki Format:
n (Ukuran Kotak n x n)
m (Banyak Angka dalam map)
SbX SbY Value (misal : 1 4 6)
SbX SbY Value (misal : 2 1 0)
SbX SbY Value (misal : 2 4 1)
*/

public class Main {
    public static void main(String[] args) {
        Scanner sc;
        int m = 0; // banyak angka
        int n = 0;
        // Try Catch untuk input File dan error prevention jika tidak ada file yang
        // cocok
        try {
            sc = new Scanner(new File("Mosaic/inputlarge.txt")); // ambil file dengan nama "input.txt"
            n = sc.nextInt();
            int[][] mosaic = new int[n][n];

            List<Coordinate> numberLocation = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    mosaic[i][j] = -1;
                }
            }

            m = sc.nextInt();
            for (int i = 0; i < m; i++) {
                int x = sc.nextInt() - 1;
                int y = sc.nextInt() - 1;
                int value = sc.nextInt();

                mosaic[x][y] = value;
                numberLocation.add(new Coordinate(x, y, value));
            }

            Individual.setMap(mosaic);
            Individual.setNumberLocation(numberLocation);

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int loop = Integer.parseInt(args[0]);// berapa kali algogen dijalankan
        double total = 0;
        Random init = new Random(); // random generator untuk membuat seeds
        double bestFitness = Double.MIN_VALUE;
        Individual bestState = null;
        long seed = init.nextLong() % 1000; // simpan seed sebagai seed untuk random generator
        Random gen = new Random(seed); // random generator untuk algogen-nya

        for (int ct = 1; ct <= loop; ct++) {
            // System.out.println("===================\nRun: "+ct);
            int maxCapacity = m, totalGeneration = 0, maxPopulationSize = 0;
            double crossoverRate = 0.0, mutationRate = 0.0, elitismPct = 0.0;

            try { // baca data parameter genetik
                sc = new Scanner(new File("Mosaic/param.txt"));
                totalGeneration = sc.nextInt();
                maxPopulationSize = sc.nextInt();
                crossoverRate = sc.nextDouble(); // skala 0-1
                mutationRate = sc.nextDouble(); // skala 0-1
                elitismPct = sc.nextDouble(); // skala 0-1
            } catch (Exception e) {
                e.printStackTrace();
            }
            // gen (random generator) dikirim ke algogen, jadi hanya menggunakan satu
            // generator untuk keseluruhan algo

            MosaicGA mosaicGA = new MosaicGA(gen, n, totalGeneration, maxPopulationSize, elitismPct,
                    crossoverRate, mutationRate);
            Individual current = mosaicGA.run();
            System.out.printf("Current Fitness:  %.5f\n", current.fitness);
            // System.out.println(best);
            if (current.fitness > bestFitness) {
                bestFitness = current.fitness;
                bestState = current;
            }
        }
        System.out.println("\n========================================");
        System.out.println("Seed: " + seed);
        System.out.printf("Best Fitness: %.5f\n", bestState.fitness);
        System.out.println("========================================\n");

        System.out.println("=========== HASIL BEST STATE ===========");

        System.out.print(bestState);

        System.out.println("========================================");

        if (bestState.fitness < 1) {
            bestState.printSavedErrors();
        }
        else{
        System.out.println("========== 100% Jawaban Benar ==========");
        System.out.println("========================================");
        }

    }
}

/*
 * 1 0 1 0 0
 * 1 0 0 0 1
 * 1 0 1 1 1
 * 0 1 1 1 0
 * 0 1 1 1 0
 */