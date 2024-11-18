package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /**
     * Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'.
     */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = { "You can do this!", "I believe in you!",
            "You got this!", "You're a star!", "Go Bears!",
            "Too easy for you!", "Wow, so impressive!" };

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);

        // TEST
        // game.flashSequence(game.generateRandomString(3));
        // game.solicitNCharsInput(3);
        // System.out.println(game.generateRandomString(9));

        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /*
         * Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as
         * its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is
         * (width, height)
         */
        this.width = width;
        this.height = height;

        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // Initialize random number generator
        this.rand = new Random(seed);

    }

    public String generateRandomString(int n) {
        // Generate random string of letters of length n
        String ret = "";

        for (int i = 0; i < n; i++) {
            ret += CHARACTERS[this.rand.nextInt(26)];
        }
        return ret;
    }

    public void drawFrame(String s) {
        // Take the string and display it in the center of the screen

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5 * this.width, 0.5 * this.height, s);

        StdDraw.show();
        // If game is not over, display relevant game information at the top of
        // the screen
        if (!this.gameOver) {
            // draw line
            StdDraw.line(0, this.height * 0.95, this.width, this.height * 0.95);

            // draw round
            StdDraw.textLeft(0, this.height * 0.97, "Round:" + this.round);

            // TODO:draw status

            // draw encouragement
            StdDraw.textRight(this.width, this.height * 0.97, MemoryGame.ENCOURAGEMENT[this.rand.nextInt(7)]);

            StdDraw.show();
        }
    }

    public void flashSequence(String letters) {
        // Display each character in letters, making sure to blank the screen
        // between letters
        for (char c : letters.toCharArray()) {
            // show c
            this.drawFrame(Character.toString(c));

            // delay
            try {
                Thread.sleep(1000); // 延时1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // show blank
            this.drawFrame("");

            // delay
            try {
                Thread.sleep(500); // 延时1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String solicitNCharsInput(int n) {
        // Read n letters of player input
        String ret = "";
        int count = 0;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                ret += StdDraw.nextKeyTyped();
                this.drawFrame(ret);
                count += 1;
            }
            if (count == n) {
                break;
            }
        }
        return ret;
    }

    public void startGame() {
        // Set any relevant variables before the game starts
        this.round = 1;

        // Establish Engine loop
        while (true) {
            this.drawFrame("Round:" + this.round);
            String target_string = this.generateRandomString(round);
            this.flashSequence(target_string);
            String input_string = this.solicitNCharsInput(round);
            if (input_string.equals(target_string)) {
                this.round += 1;
            } else {
                this.drawFrame("Game Over! You made it to round:" + this.round);
                return;
            }
        }
    }

}
