package Mosaic2D;

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

/**
 * Class main bertindak sebagai class utama untuk program Mosaic ini, Class main
 * dimulai dari membaca file input
 * yang berisi ukuran grid dan lokasi angka. Kemudian menginisialisasi data
 * kedalam individu dan membaca parameter GA yang digunakan
 * terkahir main akan menampilkan visualisasi berbasis CLI untuk solusi dan
 * detail hasil apakah berhasil atau gagal
 * 
 * Sumber: ...
 * 
 * @author Axel, Davin, Keane
 */
public class Main {
    /**
     * Method utama yang dieksekusi oleh Java
     *
     * @param args Argumen baris perintah
     */
    public static void main(String[] args) {
        Scanner sc;
        int m = 0; // banyak angka
        int n = 0; // Ukuran papan
        // Try Catch untuk input File dan error prevention jika tidak ada file yang
        // cocok
        try {
            // ambil file dengan nama "input.txt"
            sc = new Scanner(new File("Mosaic2D/Inputs/20-Hard.txt"));
            n = sc.nextInt();

            // Array 2 dimensi untuk referensi map
            int[][] mosaic = new int[n][n];

            // List untuk menyimpan koordinat
            List<Coordinate> numberLocation = new ArrayList<>();

            // buat papan kosong dengna isi -1
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    mosaic[i][j] = -1;
                }
            }

            m = sc.nextInt(); // Membaca jumlah angka yang ada

            // Luup untuk membaca setiap baris di input
            for (int i = 0; i < m; i++) {
                int x = sc.nextInt() - 1;
                int y = sc.nextInt() - 1;
                int value = sc.nextInt();

                mosaic[x][y] = value;

                // Masukan ke list koordinat
                numberLocation.add(new Coordinate(x, y, value));
            }

            // Set ke class individual
            Individual.setMap(mosaic);
            Individual.setNumberLocation(numberLocation);

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int loop = Integer.parseInt(args[0]);// berapa kali algogen dijalankan
        Random init = new Random(); // random generator untuk membuat seeds
        double bestFitness = Double.MIN_VALUE;

        double top5 = 0;
        double top10 = 0;

        Individual bestState = null;
        long seed = 67; // simpan seed sebagai seed untuk random generator
        Random gen = new Random(seed); // random generator untuk algogen-nya
        Hyperparam parameter = null;
        // Loop eksekusi GA
        for (int ct = 1; ct <= loop; ct++) {
            // System.out.println("===================\nRun: "+ct);
            int totalGeneration = 0, maxPopulationSize = 0;
            double crossoverRate = 0.0, mutationRate = 0.0, elitismPct = 0.0;

            // baca data parameter GA
            try {
                sc = new Scanner(new File("Mosaic2D/param.txt"));
                parameter = new Hyperparam(
                        sc.nextInt(),
                        sc.nextInt(),
                        sc.nextDouble(),
                        sc.nextDouble(),
                        sc.nextDouble());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // gen (random generator) dikirim ke algogen, jadi hanya menggunakan satu
            // generator untuk keseluruhan algo

            // Inisialisasi GA
            MosaicGA mosaicGA = new MosaicGA(gen, n, parameter);

            // Jalankan GA dan ambil indibidu terbaik run tersebut
            Individual current = mosaicGA.run();

            // Print hasil run saat ini (5 angka belakang koma)
            System.out.printf("Current Fitness:  %.5f\n", current.fitness);
            // System.out.println(best);

            // Update best fitness jika ad yang lebih baik
            if (current.fitness > bestFitness) {
                bestFitness = current.fitness;
                bestState = current;
            }

            if (ct == 5)
                top5 = bestFitness;
            if (ct == 10)
                top10 = bestFitness;
        }
        // Print laporan hasil run algo
        System.out.println("\n========================================");
        System.out.println("Seed: " + seed);
        System.out.printf("Best in 5: %.5f\n", top5);
        System.out.printf("Best in 10: %.5f\n", top10);
        System.out.printf("Best Fitness: %.5f\n", bestFitness);
        System.out.println("========================================\n");
        System.out.println("=========== HASIL BEST STATE ===========");
        System.out.print(bestState);
        System.out.println("========================================");
        if (bestFitness < 1) {
            bestState.printSavedErrors();
        } else {
            System.out.println("========== 100% Jawaban Benar ==========");
            System.out.println("========================================");
        }

    }
}

// Jawaban ujicoba awal
/*
 * 1 0 1 0 0
 * 1 0 0 0 1
 * 1 0 1 1 1
 * 0 1 1 1 0
 * 0 1 1 1 0
 */