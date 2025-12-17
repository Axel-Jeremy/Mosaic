package Mosaic;

import java.util.List;
import java.util.Random;

/* TODO : 
    - bikin setter buat map dimana map adalah array 2d yang merepresentasikan warna (0 = white cell, 1 = black cell) 
*/

public class Individual {
    int n;
    public int[] chromosome; // map warna dengan bit, 1 = kotak hitam, 0 = kotak putih
    public int fitness;
    public Random MyRand; // random generator dikirim dari luar untuk membuat invididu acak
    public double parentProbability; // probabilitas individu ini terpilih sbg parent
    static int[][] map;

    // static final int[][] direction = {
    // {-1,-1},{-1,0},{-1,1},
    // {0,-1}, {0,0}, {0,1},
    // {1,-1},{1,0},{1,1}
    // };

    // membuat individu acak
    public Individual(Random MyRand) {
        this.MyRand = MyRand;
        // this.banyakFireStation = banyakFireStation;
        // this.chromosome = generateRandomCoordinates();

        // System.out.println("=================");
        // for(int i = 0; i < chromosome.length; i++){
        // System.out.println(chromosome[i]);
        // }

        // this.fitness = setFitness(chromosome);
        this.parentProbability = 0;

    }

    static boolean isValid(int row, int col) {
        return row >= 0 && row < map.length &&
                col >= 0 && col < map[0].length;
    }

    // https://stackoverflow.com/questions/19320183/1d-array-to-2d-array-mapping
    // mengambil index dari chromosome yang dimapping dari array 2d
    public int getCell(int row, int col) {
        return chromosome[row * n + col];
    }

    public double setFitness(List<Coordinate> numberLocation, int n) {
        int totalError = 0;
        for (Coordinate c : numberLocation) {
            int x = c.getX();
            int y = c.getY();
            int value = c.getValue();

            int blackCtn = countBlackCell(x, y);

            totalError += Math.abs(blackCtn - value);
        }

        return 1.0 / (1 + totalError);
    }

    public int countBlackCell(int x, int y) {
        int count = 0;
        for (int i = x - 1; i < x + 2 && i < n && i >= 0; i++) {
            for (int j = y - 1; j < y + 2 && j < n && j >= 0; j++) {
                count += getCell(i, j);
            }
        }
        return count;
    }


    public int[] generateRandomChromosome() {
        int[] randomChromosome = new int[n * n];
        for (int i = 0; i < n * n; i++) {
            randomChromosome[i] = MyRand.nextInt(2);
        }
        return randomChromosome;
    }
}
