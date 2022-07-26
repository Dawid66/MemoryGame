public class Board {
    int width, height;
    Card [][] array;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        array = new Card[height][width];
    }
}
