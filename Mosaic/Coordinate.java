package Mosaic;

public class Coordinate {
    private int x; // baris
    private int y; // kolom
    private int value; // berapa kotak item yang harus ada di neighbor

    public Coordinate(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int findSumNeighbor(int n) {
        n--;
        if (this.x == 0 || this.x == n) 
            if (this.y == 0 || this.y == n)
                return 4;
            else 
                return 6;
        else 
            if (this.y == 0 || this.y == n)
                return 6;
            else
            return 9;
    }
}
