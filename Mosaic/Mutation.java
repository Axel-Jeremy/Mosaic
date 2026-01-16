package Mosaic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class Mutation berperan untuk terjadinya mutasi acak pada kromosom. mutasi ini berfungsi untuk 
 * menjaga kerandoman dari populasi dan mencegah terjebak di local maksimum..5f
 * 
 * Metode mutasi:
 * 1. One bit flip (flip hanya 1 bit acak)
 * 2. Flip all bit (Flip seluruh bit)
 * 3. Probabilistic Bit (Flip bit sesuai dengan suatu peluang)
 * 
 * @author Axel, Davin, Keane
 * 
 */
public class Mutation {
    Random MyRand;

    /**
     * Konstraktor untuk mutation
     * 
     * @param MyRand Generator untuk angka acak
     */
    public Mutation(Random MyRand) {
        this.MyRand = MyRand;
    }

    /**
     * balik 1 bit random dari kromosom (item jadi putih atau sebaliknya)
     *
     * @param current Chromosome yang dimutasi
     * @return Array gen baru hasil mutasi
     */
    public int[][] flipOneBit(Chromosome current) {
        int[][] currentGene = current.getGene();
        int n = currentGene.length;

        int r = MyRand.nextInt(n);
        int c = MyRand.nextInt(n);

        currentGene[r][c] = (currentGene[r][c] == 0) ? 1 : 0;

        return currentGene;
    }

    /**
     * balik seluruh bit dari kromosom (item jadi putih atau sebaliknya)
     *
     * @param current Chromosome yang dimutasi
     * @return Array gen baru hasil mutasi
     */
    public int[][] flipAllBit(Chromosome current) {
        int[][] currentGene = current.getGene();

        for (int i = 0; i < currentGene.length; i++) {
            for (int j = 0; j < currentGene[i].length; j++) {
                currentGene[i][j] = (currentGene[i][j] == 0) ? 1 : 0;
            }
        }
        return currentGene;
    }

    /**
     * balik bit dari kromosom yang terpilih berdasarkan probability untuk dibalik (item jadi putih atau sebaliknya)
     *
     * @param current Chromosome yang dimutasi
     * @return Array gen baru hasil mutasi
     */
    public int[][] probabiltyFlip(Chromosome current, double probability) {
        int[][] currentGene = current.getGene();

        for (int i = 0; i < currentGene.length; i++) {
            for (int j = 0; j < currentGene[i].length; j++) {
                if (MyRand.nextDouble() < probability) {
                    currentGene[i][j] = (currentGene[i][j] == 0) ? 1 : 0;
                }
            }
        }
        return currentGene;
    }

    /**
     * Melakukan mutasi untuk memperbaiki kromosom dengan membandingkan nilai pada angka dengan jumlah kotak hitam aktual.
     * Jika tidak sesuai, maka akan membalik sel hitam di sekitar angka tersebut sampai sama dengan jumlah kotak hitam yang diharapkan
     * 
     * @param numberLocation Daftar koordinat angka pada puzzle
     * @param actual Array yang berisi jumlah kotak hitam aktual di sekitar setiap clue
     * @param n Ukuran sisi grid
     */
    public int[][] randomAdjustment(Chromosome current, List<Coordinate> numberLocation, int[] actual, int n) {
        int k = 0;

        // Loop untuk seluruh kondisi angka
        for (Coordinate c : numberLocation) {

            // Jika ada selisih maka jalankan
            if (c.getValue() != actual[k]) {
                int selisih = Math.abs(c.getValue() - actual[k]);
                int x = c.getX();
                int y = c.getY();
                int count = 0;

                List<Integer[]> indexes = new ArrayList<>();

                // loop 3*3 untuk mengecek kotak hitam sekitar yang tidak keluar dari grid
                for (int i = x - 1; i < x + 2; i++) {
                    for (int j = y - 1; j < y + 2; j++) {
                        // memastikan tetap valid (tidak keluar dari grid)
                        if (i >= 0 && i < n && j >= 0 && j < n) {
                            indexes.add(new Integer[] { i, j });
                        }

                    }
                }

                // acak index
                Collections.shuffle(indexes, this.MyRand);

                // benerin kotak yang salah
                for (Integer[] index : indexes) {
                    int row = index[0];
                    int col = index[1];

                    int cellValue = current.getCell(row, col);

                    // item lebih banyak dari aslinya
                    if (c.getValue() > actual[k]) {
                        if (cellValue == 0) {
                            current.flipCell(row, col);
                            count++;
                        }
                    }
                    // Kalau kotak putih lebih banyak (atau item kurang)
                    else if (c.getValue() < actual[k]) {
                        if (cellValue == 1) {
                            current.flipCell(row, col);
                            count++;
                        }
                    }
                    // kalau udh sama kek selisih, berenti
                    if (count == selisih)
                        break;
                }
            }
            k++;
        }
        return current.getGene();
    }
}
