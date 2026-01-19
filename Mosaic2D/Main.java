package Mosaic2D;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/*
Input File nanti akan memiliki Format:
n (Ukuran Kotak n x n)
m (Banyak Angka dalam map)
Baris Kolom Value (misal : 1 4 6)
Baris Kolom Value (misal : 2 1 0)
Baris Kolom Value (misal : 2 4 1)
*/

/**
 * Class main bertindak sebagai class utama untuk program Mosaic ini, Class main
 * dimulai dari membaca file input
 * yang berisi ukuran grid dan lokasi angka. Kemudian menginisialisasi data
 * kedalam individu dan membaca parameter GA yang digunakan
 * terakhir main akan menampilkan visualisasi berbasis CLI untuk solusi dan
 * detail hasil apakah berhasil atau gagal
 * 
 * Sumber: Mandiri dan referensi tugas Fire Station dan file Knapsack Teams
 * 
 * - Ide input file yang dijalankan dengan looping dari teman seangkatan (Kenneth Nathanael)
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
        String[] fileList = { //list file yang ingin dijalankan dalam 1 kali run program
                "20-Easy_1", "20-Easy_2", "20-Easy_3", "20-Easy_4", "20-Easy_5",
                "20-Hard_1", "20-Hard_2", "20-Hard_3", "20-Hard_4", "20-Hard_5"
        };

        int loop = Integer.parseInt(args[0]);// berapa kali algogen dijalankan
        double count = 0; //kumulatif best fitness tiap fileList
        double best = 0; //best fitness dari seluruh file yang ada di fileList

        for (String fileName : fileList) { //loop menjalankan semua file pada fileList
            Scanner sc;
            int m = 0; // banyak angka
            int n = 0; // Ukuran papan
            // Try Catch untuk input File dan error prevention 
            // jika tidak ada file yang cocok
            try {
                // ambil file input
                sc = new Scanner(new File("Mosaic2D/Inputs/"+ fileName + ".txt"));
                n = sc.nextInt(); // ukuran papan

                // Array 2 dimensi untuk referensi map
                int[][] mosaic = new int[n][n];

                // List untuk menyimpan koordinat
                List<Coordinate> numberLocation = new ArrayList<>();

                // buat papan kosong dengan isi -1
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        mosaic[i][j] = -1;
                    }
                }

                m = sc.nextInt(); // Membaca jumlah angka yang ada

                // Loop untuk membaca setiap baris di input
                for (int i = 0; i < m; i++) {
                    int x = sc.nextInt() - 1; //baris
                    int y = sc.nextInt() - 1; //kolom
                    int value = sc.nextInt(); //clues angka

                    mosaic[x][y] = value;

                    // Masukan ke list koordinat
                    numberLocation.add(new Coordinate(x, y, value));
                }

                // Set ke class individual
                Individual.setMap(mosaic);
                Individual.setNumberLocation(numberLocation);

                sc.close(); //tutup scanner
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            Random init = new Random(); // random generator untuk membuat seeds
            double bestFitness = Double.MIN_VALUE; //fitness terbaik di 1 file

            Individual bestState = null; //state terbaik di 1 file
            long seed = 67; // simpan seed sebagai seed untuk random generator
            Random gen = new Random(seed); // random generator untuk algogen-nya
            Hyperparam parameter = null;
            // Loop eksekusi GA
            for (int ct = 1; ct <= loop; ct++) {
                // System.out.println("===================\nRun: "+ct);
                // baca data parameter GA
                try {
                    sc = new Scanner(new File("Mosaic2D/param.txt"));
                    parameter = new Hyperparam(
                            sc.nextInt(), //banyak generation
                            sc.nextInt(), //banyak population
                            sc.nextDouble(), //crossover rate nilainya antara 0 - 1
                            sc.nextDouble(), //mutation rate nilainya antara 0 - 1
                            sc.nextDouble()); //elitism rate nilainya antara 0 - 1
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
                System.out.printf("#%d Fitness:  %.5f\n",ct, current.fitness);
                // System.out.println(best);

                // Update best fitness jika ad yang lebih baik
                if (current.fitness > bestFitness) {
                    bestFitness = current.fitness;
                    bestState = current;

                    if(bestFitness == 1) break;
                }
            }

            count += bestFitness;
            best = Math.max(best, bestFitness);

            // Print laporan hasil run algo
            System.out.println("\n========================================");
            System.out.println("File Name : " +  fileName);
            System.out.println("Seed: " + seed);
            // System.out.printf("Best in 5: %.5f\n", top5);
            // System.out.printf("Best in 10: %.5f\n", top10);
            System.out.printf("Best Fitness: %.10f\n", bestFitness);
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
        System.out.printf("Avg dari 10 input: %.5f\n", count/10.0);
        System.out.printf("Best: %.5f\n", best);
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