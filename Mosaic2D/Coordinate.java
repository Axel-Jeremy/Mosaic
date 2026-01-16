package Mosaic2D;

/**
 * Class Coordinate mempresentasikan papan mosaic puzzle yang berisikan clue,
 * class ini akan menyimpan koordinat atau lokasi
 * dan angka pada titik itu yang menunjukan berapa banyak kotak hitam yang harus
 * ada di sekitarnya termasukdi titik tersebut
 * 
 * Sumber: ...
 * 
 * @author Axel
 * 
 */
public class Coordinate {
    private int x; // baris
    private int y; // kolom
    private int value; // berapa kotak item yang harus ada di neighbor

    /**
     * konstruktor untuk membuat objek Coordinate
     *
     * @param x     Indeks Baris
     * @param y     Indeks Kolom
     * @param value Nilai angka petunjuk pada posisi tersebut
     */
    public Coordinate(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    /**
     * ambil posisi baris
     * 
     * @return Indeks baris
     */
    public int getX() {
        return x;
    }

    /**
     * atur posisi baris
     * 
     * @return Indeks baris baru
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * ambil posisi kolom
     * 
     * @return Indeks kolom
     */
    public int getY() {
        return y;
    }

    /**
     * atur posisi kolom
     * 
     * @return Indeks kolom baru
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * ambil nilai clue
     * 
     * @return Indeks kolom
     */
    public int getValue() {
        return value;
    }

    /**
     * atur nilai clue
     * 
     * @return Indeks kolom baru
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * hitung jumlah maksimum tetangga yang mungkin dimiliki oleh koordinat tertentu
     * Logika:
     * Pojok (Corner): Memiliki 4 tetangga (termasuk diri sendiri)
     * Tepi (Edge): Memiliki 6 tetangga
     * Tengah (Middle): Memiliki 9 tetangga (blok 3x3 penuh)
     *
     * @param n Ukuran grid
     * @return Jumlah maksimal sel dalam jangkauan clue (4, 6, 9)
     */
    public int findSumNeighbor(int n) {
        // Konversi ukuran grid (n) menjadi n-1
        n--;

        if (this.x == 0 || this.x == n)
            if (this.y == 0 || this.y == n)
                // Kasus Pojok Kiri-Atas, Kanan-Atas, Kiri-Bawah, Kanan-Bawah
                return 4;
            else
                // Kasus pinggir Atas atau Bawah (bukan pojok)
                return 6;
        else if (this.y == 0 || this.y == n)
            // Kasus Tepi kiri atau kanan (tapi bukan pojok)
            return 6;
        else
            // tengah
            return 9;
    }
}
