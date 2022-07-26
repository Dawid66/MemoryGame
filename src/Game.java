import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game extends Board {
    private final List<String> words;
    private final int cardNumber;

    public Game(int width, int height, int cardNumber) {
        super(width, height);
        this.cardNumber = cardNumber;
        words = new ArrayList<>();
    }

    public void Start() {
        Scanner scanner = new Scanner(System.in);
        LoadFile();
        String level;
        int chances = 0;
        System.out.println("Welcome to the memory game");

        do {
            System.out.println("choose the difficulty level\n ");
            System.out.println("press 0 to choose easy level (8 chances) or 1 to choose hard level (8 chances)\n");
            System.out.println("press x to exite\n");

            level = scanner.next();

            if (level.equals("0")) chances = 10;
            else if (level.equals("1")) chances = 8;
            else System.out.println("There is no such level");
            if (level.equalsIgnoreCase("x")) break;
        } while (chances == 0);

        PuttingCardsOnTable();
        int success = 0;

        while (success != 4) {
            System.out.println("Guess chances: " + chances);

            Show();
            String firstChoice = scanner.next();
            if (CheckingChoose(firstChoice)) {
                int[] firstCardPosition = FormatChange(firstChoice);
                do {
                    if (!array[firstCardPosition[0]][firstCardPosition[1]].getFound()) {
                        array[firstCardPosition[0]][firstCardPosition[1]].setFound(true);
                        break;
                    }
                    System.out.println("This word has already been discovered");
                    firstChoice = scanner.next();
                    firstCardPosition = FormatChange(firstChoice);
                } while (true);

                Show();
                String secondChoice;
                do {
                    secondChoice = scanner.next();
                    if (CheckingChoose(secondChoice)) {
                        int[] secondCardPosition = FormatChange(secondChoice);
                        do {
                            if (!array[secondCardPosition[0]][secondCardPosition[1]].getFound()) {
                                array[secondCardPosition[0]][secondCardPosition[1]].setFound(true);
                                break;
                            }
                            System.out.println("This word has already been discovered");
                            secondChoice = scanner.next();
                            secondCardPosition = FormatChange(secondChoice);
                        } while (true);
                        Show();
                        if (!array[secondCardPosition[0]][secondCardPosition[1]].equals(array[firstCardPosition[0]][firstCardPosition[1]])) {
                            array[secondCardPosition[0]][secondCardPosition[1]].setFound(false);
                            array[firstCardPosition[0]][firstCardPosition[1]].setFound(false);
                            chances--;
                        }else {
                            success++;
                        }
                    } else {
                        System.out.println("card with this position does not exist");
                    }
                } while (!CheckingChoose(secondChoice));
                if (chances == 0) {
                    System.out.println("you lost \n");
                    break;
                }
            } else {
                System.out.println("card with this position does not exist");
            }
        }
        if (success == 4) System.out.println("you win \n");
        System.out.println("if you want to play again select y");
        System.out.println("otherwise, select any other button");
        if (scanner.next().equals("y")) Start();

    }

    private void Show() {
        for (int i = 0; i < width; i++) {
            System.out.print("    " + (i + 1));
        }
        System.out.println();
        for (int i = 0; i < height; i++) {
            char row = (char) ('A' + i);
            System.out.print(row + "   ");
            for (int j = 0; j < width; j++) {
                if (array[i][j].getFound()) {
                    System.out.print(array[i][j].getValue() + "    ");
                } else System.out.print("X    ");
            }
            System.out.println();
        }
    }

    private void LoadFile() {
        File file = new File("source\\Words.txt");
        Scanner scannerFile;
        try {
            scannerFile = new Scanner(file);
            while (scannerFile.hasNext()) {
                words.add(scannerFile.next());
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
    }

    private ArrayList<Card> DrawWords() {
        ArrayList<Card> chooseWords = new ArrayList<>();
        for (int i = 0; i < cardNumber; i++) {
            int index = (int) (Math.random() * words.size());
            chooseWords.add(new Card(words.get(index)));
            chooseWords.add(new Card(words.get(index)));
        }
        return chooseWords;
    }

    private void PuttingCardsOnTable() {
        ArrayList<Card> wordList = DrawWords();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int index = (int) (Math.random() * wordList.size());
                array[i][j] = wordList.remove(index);
            }
        }
    }

    private boolean CheckingChoose(String choose) {
        return (choose.length() == 2 &&
                (choose.toUpperCase().charAt(0) == 'A' || choose.toUpperCase().charAt(0) == 'B') &&
                (Character.getNumericValue(choose.charAt(1)) <= width &&
                        Character.getNumericValue(choose.charAt(1)) > 0));
    }

    private int[] FormatChange(String choose) {
        if (choose.toUpperCase().charAt(0) == 'A') {
            return new int[]{0, Character.getNumericValue(choose.charAt(1)) - 1};
        } else {
            return new int[]{1, Character.getNumericValue(choose.charAt(1)) - 1};
        }
    }


}
