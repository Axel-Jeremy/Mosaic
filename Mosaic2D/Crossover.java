package Mosaic2D;

import java.util.Random;

/**
 * Class crossover bertugas untuk melakukan kombinasi dari 2 parent yang
 * sbeleumnya terpilih
 * yang akan menghasilkan anak nantinya
 * 
 * Metode:
 * 1. Uniform Crossover
 * 2. Single Point crossover
 * 3. Two point crossover
 * 4. Horizontal Crossover
 * 5. Vertical Crossover
 * 6. Right Diagonal Crossover
 * 7. Left Diagonal Crossover
 * 8. Center Point Plus Crossover
 * 9. Random Point Plus Crossover
 * 10. Double Diagonal Crossover
 * 
 * Sumber: Sendiri dengan refrensi Tugas Fire Station dan debugging LLM
 * 
 * @author Axel, Davin, Keane
 * 
 */
public class Crossover {
    Random MyRand;

    /**
     * Konstraktor untuk crossover
     * 
     * @param MyRand Generator untuk angka acak
     */
    public Crossover(Random MyRand) {
        this.MyRand = MyRand;
    }

    /**
     * Melakukan Uniform Crossover
     * Setiap gen anak akan dipilih secara acak dari salah satu orang tua
     *
     * @param other Parent kedua (pasangan)
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] uniformCrossover(Chromosome current, Chromosome other) {
        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        int m = currentGene[0].length;

        int[][] child1Gene = new int[n][m];
        int[][] child2Gene = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // Acak pengambilan gen untuk Anak 1
                child1Gene[i][j] = MyRand.nextBoolean() ? currentGene[i][j] : otherGene[i][j];
                // Acak pengambilan gen untuk Anak 2 (kebalikannya atau acak ulang, di sini
                // logic aslinya acak ulang)
                child2Gene[i][j] = MyRand.nextBoolean() ? otherGene[i][j] : currentGene[i][j];
            }
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
    public Chromosome[] singlePointCrossover(Chromosome current, Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        // int totalCells = n * n;

        int potongan = MyRand.nextInt((n*n) -1);

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];

        int count = 0;

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (count <= potongan) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
                count++;
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    /**
     * Memotong grid secara vertikal (kolom), dimana bagian kiri akan menjadi 
     * parent 1 dan yang kanan akan menjadi parent 2
     * 
     * @param current Parent pertama
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] verticalCrossover(Chromosome current, Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        // int totalCells = n * n;

        int potongan = MyRand.nextInt(n -1);

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];

        int count = 0;

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < n; i++) {
            count = 0;
            for (int j = 0; j < n; j++) {
                if (count <= potongan) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
                count++;
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    /**
     * Memotong grid secara horizontal (baris), dimana bagian kiri akan menjadi 
     * parent 1 dan yang kanan akan menjadi parent 2
     * 
     * @param current Parent pertama
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] horizontalCrossover(Chromosome current, Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        // int totalCells = n * n;

        int potongan = MyRand.nextInt(n -1);

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];

        int count = 0;

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (count <= potongan) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
            }
            count++;
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    /**
     * Melakukan diagonal crossover (kiri atas ke kanan bawah) (\)
     * bagian kiri dari parent 1 kanan diambil dari parent 2 (untuk anak 1)
     * bagian kiri dari parent 2 kanan diambil dari parent 1 (untuk anak 2)
     * 
     * @param current Parent pertama
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] rightDiagonalCrossover(Chromosome current, Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        // int totalCells = n * n;

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];

        int potongan = 0;

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j <= potongan) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
            }
            potongan++;
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }


    /**
     * Melakukan diagonal crossover (kanan atas ke kiri bawah) (/)
     * Memotong grid secara diagonal dan bagian kanan atas ke kiri bawah
     * bagian kiri dari parent 1 bagian kanan parent 2 (untuk anak 1)
     * bagian kiri dari parent 2 bagian kanan parent 1 (untuk anak 2)
     * 
     * 
     * @param current Parent pertama
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] leftDiagonalCrossover(Chromosome current, Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        // int totalCells = n * n;

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];

        int potongan = n-1;

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j <= potongan) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
            }
            potongan--;
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }


    /**
     * Melakukan pembagian seperti kuadran 
     *  2 1
     *  3 4
     * Anak 1 : kuadran 2 dan 4 dari parent 1, kuadran 1 dan 3 dari parent 2
     * Anak 2 : kuadran 2 dan 4 dari parent 2, kuadran 1 dan 3 dari parent 1
     * 
     * @param current Parent pertama
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] centerPointPlusCrossover(Chromosome current, Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        // int totalCells = n * n;

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];

        int potongan = n/2;

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((i <= potongan && j <= potongan) || (i > potongan && j > potongan)) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    /**
     * Melakukan quadrant crossover dengan titik random, pada dasarnya bakal mirip dengan
     * center point crossover tapi titik temu antar 2 garisnya random
     * 
     * @param current Parent pertama
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] randomPointPlusCrossover(Chromosome current, Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        // int totalCells = n * n;

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];

        int potongan = MyRand.nextInt(n-1);

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((i <= potongan && j <= potongan) || (i > potongan && j > potongan)) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }

    /**
     * melakukan diagobal crossover tapi bentuk x dengan membagi grid jadi 2 garis menyilang seperti bentuk x
     * Anak 1: Segitiga atas bawah parent 1 dan Segitiga kanan dan kiri parent 2
     * Anak 2: Segitiga atas bawah parent 2 dan Segitiga kanan dan kiri parent 1
     * 
     * Ilustrasi: 
     * .\../.
     * ..\/..
     * ../\..
     * ./..\.
     * 
     * @param current Parent pertama
     * @param other Parent kedua
     * @return Array berisi 2 Chromosome anak
     */
    public Chromosome[] doubleDiagonalCrossover(Chromosome current, Chromosome other) {
        // Tentukan titik potong
        // int potongan = this.gene.length / 2;
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        // int totalCells = n * n;

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];


        int left = 0;
        int right = n-1;

        // Lakukan crossover dengan menyalin gen
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((i >= left && j <= right) || (i >= right && j <= left)) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
            }
            left++;
            right--;
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
    public Chromosome[] twoPointCrossover(Chromosome current, Chromosome other) {
        int[][] currentGene = current.getGene();
        int[][] otherGene = other.getGene();

        int n = currentGene.length;
        int totalCells = n * n;

        Chromosome child1 = new Chromosome(MyRand);
        Chromosome child2 = new Chromosome(MyRand);

        int[][] child1Gene = new int[n][n];
        int[][] child2Gene = new int[n][n];

        // tentukan titik potong 1 dan 2
        int point1 = MyRand.nextInt(totalCells -1);
        int point2 = MyRand.nextInt(totalCells -1);

        // Buat agar titik potong start lebih kecil dari end
        int start = Math.min(point1, point2);
        int end = Math.max(point1, point2);

        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // kalau ada di luar titik potong, pertahankan seperti awal
                if (count < start || count > end) {
                    // Anak 1 mengambil dari Parent 1 (current)
                    child1Gene[i][j] = currentGene[i][j];
                    // Anak 2 mengambil dari Parent 2 (other)
                    child2Gene[i][j] = otherGene[i][j];
                } else {
                    // Kalau di dalam titik potong, tuker
                    // Anak 1 mengambil dari Parent 2 (other)
                    child1Gene[i][j] = otherGene[i][j];
                    // Anak 2 mengambil dari Parent 1 (current)
                    child2Gene[i][j] = currentGene[i][j];
                }
                count++;
            }
        }

        child1.setGene(child1Gene);
        child2.setGene(child2Gene);

        return new Chromosome[] { child1, child2 };
    }
}