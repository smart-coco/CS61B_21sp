package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.Arrays;
import java.lang.Comparable;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static TETile[][] generate_tiles = new TETile[Engine.WIDTH][Engine.HEIGHT];

    private final long SEED = 2873129;
    private Random RANDOM = new Random(SEED);

    /**
     * Method used for exploring a fresh world. This method should handle all
     * inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {

        this.ter.initialize(this.WIDTH, this.HEIGHT);
        TETile[][] randomTiles = this.generate_world();
        this.ter.renderFrame(randomTiles);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a
     * series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The
     * engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For
     * example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the
     * first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean
        // interface
        // that works for many different input types.

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;

        // N###S:new world

        // L:load world

        // Q:quit world

        // WSAD:control avator
    }

    // recording to seed return generate_world
    public TETile[][] generate_world() {
        // init
        TETile[][] generate_tiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int i = 0; i < Engine.WIDTH; i++) {
            for (int j = 0; j < Engine.HEIGHT; j++) {
                generate_tiles[i][j] = Tileset.WALL;
            }
        }

        // # generate rooms
        // ## get random number of room
        int room_count = RandomUtils.uniform(this.RANDOM, 10, 30);

        // ## definition room class
        class Room implements Comparable<Room> {
            private int location_x;
            private int location_y;
            private int a;
            private int b;

            public Room(int location_x, int location_y, int a, int b) {
                this.location_x = location_x;
                this.location_y = location_y;
                this.a = a;
                this.b = b;
            }

            public int get_location_x() {
                return this.location_x;
            }

            public int get_location_y() {
                return this.location_y;
            }

            public int get_a() {
                return this.a;
            }

            public int get_b() {
                return this.b;
            }

            @Override
            public int compareTo(Room other) {
                return this.get_location_x() - other.get_location_x();
            }
        }
        // ## repeat generate rooms and record
        Room[] rooms = new Room[room_count];
        int avator_location_x = Engine.WIDTH / 2;
        int avator_location_y = Engine.HEIGHT / 2;
        boolean avator_flag = false;
        for (int i = 0; i < room_count; i++) {
            int location_x = RandomUtils.uniform(this.RANDOM, 1, Engine.WIDTH);
            int location_y = RandomUtils.uniform(this.RANDOM, 1, Engine.HEIGHT);
            int a = RandomUtils.uniform(this.RANDOM, 2, 5);
            int b = RandomUtils.uniform(this.RANDOM, 2, 5);

            // ### detect boundary condition
            if ((location_x - a) >= 0 && (location_x + a) <= Engine.WIDTH - 1 && (location_y - b) >= 0
                    && (location_y + b) <= Engine.HEIGHT - 1) {
                // ### init room
                rooms[i] = new Room(location_x, location_y, a, b);

                // ### generate room
                for (int j = location_x - a; j <= location_x + a; j++) {
                    for (int z = location_y - b; z <= location_y + b; z++) {
                        generate_tiles[j][z] = Tileset.FLOWER;
                    }
                }

                // ### record avator
                if (!avator_flag) {
                    avator_location_x = location_x;
                    avator_location_y = location_y;
                    avator_flag = true;
                }
            } else {
                // ### init invalid room
                rooms[i] = new Room(-1, -1, -1, -1);
            }
        }

        // ## sort according to location_x
        Arrays.sort(rooms);

        // # generate hallways
        // ## connect rooms from left to right
        for (int i = 0; i < room_count - 1; i++) {
            // ## not invalid room
            if (rooms[i].get_location_x() != -1) {
                // ## condition 1
                if (rooms[i].get_location_y() > rooms[i + 1].get_location_y()) {
                    for (int j = rooms[i].get_location_y(); j >= rooms[i + 1].get_location_y(); j--) {
                        generate_tiles[rooms[i].get_location_x()][j] = Tileset.FLOWER;
                    }
                    for (int j = rooms[i].get_location_x(); j <= rooms[i + 1].get_location_x(); j++) {
                        generate_tiles[j][rooms[i + 1].get_location_y()] = Tileset.FLOWER;
                    }
                } else {
                    // ## condition 2
                    for (int j = rooms[i].get_location_y(); j <= rooms[i + 1].get_location_y(); j++) {
                        generate_tiles[rooms[i].get_location_x()][j] = Tileset.FLOWER;
                    }
                    for (int j = rooms[i].get_location_x(); j <= rooms[i + 1].get_location_x(); j++) {
                        generate_tiles[j][rooms[i + 1].get_location_y()] = Tileset.FLOWER;
                    }

                }
            }
        }

        /// # generate avator
        generate_tiles[avator_location_x][avator_location_y] = Tileset.AVATAR;

        return generate_tiles;
    }
}