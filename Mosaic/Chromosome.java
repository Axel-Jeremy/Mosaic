package Mosaic;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
/**
 * Class Chromosome mempresentasikan struktur awal dari algo GA yang
 * mempresentasikan grid 2 dimensi
 * yang setiap gen nya akan bernilai 1 jika dia merupakan kotak hitam dan 0 jika
 * putih/kosong
 * 
 * Sumber: ...
 * 
 * @author Axel, Davin, Keane
 * 
 */
public class Chromosome {
    private int[] gene;
    public Random MyRand; // random generator
    private static final int FILLED = 1; // penanda grid ditandai kotak hitam
    private static final int EMPTY = 0; // penanda grid ditandai kotak putih
    private static final int BOUND = 2; // batas untuk random

    /**
     * Konstraktor untuk chromosome
     * 
     * @param MyRand Generator untuk angka acak
     */
    public Chromosome(Random MyRand) {
        // this.gene = gene;
        this.MyRand = MyRand;
    }

    /**
     * ambil nilai gen pada grid(baris, kolom) tertentu dan di mapping dari
     * koordinat 2D ke array 1D
     *
     * @param x Baris
     * @param y Kolom
     * @param n Ukuran sisi grid
     * @return Nilai gen (1 atau 0)
     */
    public int getCell(int x, int y, int n) {
        return gene[x * n + y];
    }

    /**
     * Membalik nilai sel pada koordinat tertentu (Flip Bit)
     * Jika 0 menjadi 1 dan sebaliknya
     * @param x Baris
     * @param y Kolom
     * @param n Ukuran sisi grid
     */
    public void flipCell(int x, int y, int n) {
        gene[x * n + y] = gene[x * n + y] == 0 ? 1 : 0;
    }

    /**
     * ambil seluruh array gene
     * 
     * @return Array integer gene
     */
    public int[] getGene() {
        return gene;
    }

    /**
     * mengatur seluruh nilai gene
     * 
     * @param gene Array integer gene baru
     */
    public void setGene(int[] gene) {
        this.gene = gene;
    }

    /**
     * method untuk generate random yang berfungsi mengisi gene awal dengan acak
     * digunakan hanya pada generasi pertama
     */
    public void generateRandom(int n) {
        this.gene = new int[n * n];
        for (int i = 0; i < n * n; i++) {
            int status = MyRand.nextInt(BOUND);
            gene[i] = (status == 0) ? EMPTY : FILLED;
        }
    }

    /**
     * mutasi genetik dengan flip bit yang terpilih, dimana setiap gen memiliki
     * probability
     * untuk bermutasi (flip), ada beberapa teknik dalam melakuakn flip bit
     * 1. Semua bit di ubah
     * 2. 1 Index random saja
     * 3. Probabilistic
     *
     * @param probability Nilai mutasi
     */
    public void doMutation(double probability) {

        // ==========================================
        // mutasi flip bit semua index
        // ==========================================
        // for (int i = 0; i < this.gene.length; i++) {
        // this.gene[i] = this.gene[i] == 0 ? 1:0;

        // ==========================================
        // mutasi flip bit 1 index random saja
        // ==========================================
        // int idx = MyRand.nextInt(gene.length);
        // int idxArr[] = new int[idx];
        //
        // for (int index : idxArr) {
        // index = MyRand.nextInt(gene.length);
        // }
        // this.gene[idx] = this.gene[idx] == 0 ? 1 : 0;

        // ==========================================
        // mutasi Probabilistic
        // ==========================================
        // jika angka acak kurang dari rate mutasi, lakukan flip
        for (int i = 0; i < this.gene.length; i++) {
            if (MyRand.nextDouble() < probability) {
                this.gene[i] = (this.gene[i] == 0) ? 1 : 0;
            }
        }
    }

    /**
     * Melakukan mutasi untuk memperbaiki kromosom dengan membandingkan nilai pada angka dengan jumlah kotak hitam aktual.
     * Jika tidak sesuai, maka akan membalik sel hitam di sekitar angka tersebut sampai sama dengan jumlah kotak hitam yang diharapkan
     * 
     * @param numberLocation Daftar koordinat angka pada puzzle
     * @param actual Array yang berisi jumlah kotak hitam aktual di sekitar setiap clue
     * @param n Ukuran sisi grid
     */
    public void doMutation(List<Coordinate> numberLocation, int[] actual, int n) {
        // ==========================================
        // mutasi cek expected sama actual
        // ==========================================
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

                            // if(c.getValue() > actual[k]){
                            // if(getCell(i, j, n) == 0){
                            // flipCell(i, j, n);
                            // count++;
                            // }
                            // }
                            // else if(c.getValue() < actual[k]){
                            // if(getCell(i, j, n) == 1) {
                            // flipCell(i, j, n);
                            // count++;
                            // }
                            // }
                        }

                        // if(count == selisih) break;
                    }
                }

                // acak index
                Collections.shuffle(indexes, this.MyRand);

                // benerin kotak yang salah
                for (Integer[] index : indexes) {
                    // item lebih banyak dari aslinya, 
                    if (c.getValue() > actual[k]) {
                        if (getCell(index[0], index[1], n) == 0) {
                            //tuker kotak item jadi putih
                            flipCell(index[0], index[1], n);
                            count++;
                        }
                    } 
                    // Kalau kotak putih lebih banyak
                    else if (c.getValue() < actual[k]) {
                        if (getCell(index[0], index[1], n) == 1) {
                            // puter jadi kotak item
                            flipCell(index[0], index[1], n);
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
    }

    /**
     * Melakukan Uniform Crossover
     * Setiap gen anak akan dipilih secara acak dari salah satu orang tua
     *
     * @param other Parent kedua (pasangan)
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] uniformCrossover(Chromosome other) {
        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[] child1Gene = new int[this.gene.length];
        int[] child2Gene = new int[this.gene.length];
        int[] otherGene = other.getGene();

        for (int i = 0; i < this.gene.length; i++) {
            // Acak pengambilan gen untuk Anak 1
            child1Gene[i] = MyRand.nextBoolean() ? this.gene[i] : other.gene[i];
            // Acak pengambilan gen untuk Anak 2
            child2Gene[i] = MyRand.nextBoolean() ? other.gene[i] : this.gene[i];
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    /**
     * Melakukan Single Point Crossover
     * Memotong kromosom di satu titik acak, lalu menukar bagian ekornya
     *
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] singlePointCrossover(Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int potongan = MyRand.nextInt(this.gene.length);

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[] child1Gene = new int[this.gene.length];
        int[] child2Gene = new int[this.gene.length];
        int[] otherGene = other.getGene();

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < this.gene.length; i++) {
            if (i <= potongan) {
                // Anak 1 mengambil dari Parent 1 (this)
                child1Gene[i] = this.gene[i];
                // Anak 2 mengambil dari Parent 2 (other)
                child2Gene[i] = otherGene[i];
            } else {
                // Anak 1 mengambil dari Parent 2 (other)
                child1Gene[i] = otherGene[i];
                // Anak 2 mengambil dari Parent 1 (this)
                child2Gene[i] = this.gene[i];
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    /**
     * Melakukan Two Point Crossover dengan memilih dua titik potong,
     * lalu menukar segmen di antara kedua titik tersebut
     *
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] twoPointCrossover(Chromosome other) {
        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[] child1Gene = new int[this.gene.length];
        int[] child2Gene = new int[this.gene.length];
        int[] otherGene = other.getGene();

        // tentukan titik potong 1 dan 2
        int point1 = MyRand.nextInt(this.gene.length);
        int point2 = MyRand.nextInt(this.gene.length);

        // Buat agar titik potong start lebih kecil dari end
        int start = Math.min(point1, point2);
        int end = Math.max(point1, point2);

        for (int i = 0; i < this.gene.length; i++) {
            // kalau ada di luar titik potong, pertahankan seperti awal
            if (i < start || i > end) {
                // Anak 1 mengambil dari Parent 1 (this)
                child1Gene[i] = this.gene[i];
                // Anak 2 mengambil dari Parent 2 (other)
                child2Gene[i] = otherGene[i];
            } else {
                // Kalau di dalam titik potong, tuker
                // Anak 1 mengambil dari Parent 2 (other)
                child1Gene[i] = otherGene[i];
                // Anak 2 mengambil dari Parent 1 (this)
                child2Gene[i] = this.gene[i];
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    /**
     * Representasi String dari Chromosome untuk visualisasi
     * Mengubah 1 menjadi "B" (Black) dan 0 menjadi "W" (White)
     *
     * @return String grid puzzle
     */
    @Override
    public String toString() {
        String res = "";
        int count = 0;
        int length = (int) Math.sqrt(gene.length);
        // System.out.println(gene.length);
        for (int i = 0; i < gene.length; i++) {
            if (gene[i] == 1)
                res += "B ";
            else
                res += "W ";
            count++;
            // Ganti baris setiap mencapai lebar grid
            if (count == length) {
                count = 0;
                res += "\n";
            }
        }
        return res;
    }
}
