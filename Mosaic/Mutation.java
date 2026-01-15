package Mosaic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Mutation {
    Random MyRand;

    public Mutation(Random MyRand) {
        this.MyRand = MyRand;
    }

    public int[] flipOneBit(Chromosome current) {
        int[] currentGene = current.getGene();
        for (int i = 0; i < currentGene.length; i++) {
            currentGene[i] = currentGene[i] == 0 ? 1:0;
        }
        return currentGene;
    }

    public int[] flipAllBit(Chromosome current) {
        int[] currentGene = current.getGene();
        int idx = MyRand.nextInt(currentGene.length);
        int idxArr[] = new int[idx];
        
        for (int index : idxArr) {
        index = MyRand.nextInt(currentGene.length);
        }
        currentGene[idx] = currentGene[idx] == 0 ? 1 : 0;

        return currentGene;
    }

    public int[] probabiltyFlip(Chromosome current, double probability) {
        int[] currentGene = current.getGene();

        for (int i = 0; i < currentGene.length; i++) {
            if (MyRand.nextDouble() < probability) {
                currentGene[i] = (currentGene[i] == 0) ? 1 : 0;
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
    public int[] randomAdjustment(Chromosome current, List<Coordinate> numberLocation, int[] actual, int n) {
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
                    // item lebih banyak dari aslinya, 
                    if (c.getValue() > actual[k]) {
                        if (current.getCell(index[0], index[1], n) == 0) {
                            //tuker kotak item jadi putih
                            current.flipCell(index[0], index[1], n);
                            count++;
                        }
                    } 
                    // Kalau kotak putih lebih banyak
                    else if (c.getValue() < actual[k]) {
                        if (current.getCell(index[0], index[1], n) == 1) {
                            // puter jadi kotak item
                            current.flipCell(index[0], index[1], n);
                            count++;
                        }
                    }
                    // kalau udh sama kek sleisih, berenti
                    if (count == selisih)
                        break;
                }
            }
            k++;
        }
        return current.getGene();
    }
}
