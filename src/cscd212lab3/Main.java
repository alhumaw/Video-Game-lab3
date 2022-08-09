package cscd212lab3;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import cscd212classes.lab3.entities.characters.Farmer;
import cscd212classes.lab3.entities.characters.Goblin;
import cscd212lib.lab2.ConsoleColors;
import cscd212classes.lab2.Canvas;
import cscd212classes.lab2.Color;
import cscd212classes.lab3.GameMaster;
import cscd212classes.lab3.entities.Agent;
import cscd212classes.lab3.entities.Enemy;
import cscd212classes.lab3.entities.Player;
import cscd212classes.lab3.entities.characters.SimpleHero;

import java.util.ArrayList;
import java.util.Scanner;

import static com.raylib.Jaylib.BLACK;
import static com.raylib.Jaylib.BLANK;
import static com.raylib.Jaylib.BeginDrawing;
import static com.raylib.Jaylib.ClearBackground;
import static com.raylib.Jaylib.CloseAudioDevice;
import static com.raylib.Jaylib.CloseWindow;
import static com.raylib.Jaylib.DrawRectangle;
import static com.raylib.Jaylib.EndDrawing;
import static com.raylib.Jaylib.FLAG_WINDOW_HIGHDPI;
import static com.raylib.Jaylib.FLAG_WINDOW_TOPMOST;
import static com.raylib.Jaylib.FLAG_WINDOW_TRANSPARENT;
import static com.raylib.Jaylib.Image;
import static com.raylib.Jaylib.InitAudioDevice;
import static com.raylib.Jaylib.InitWindow;
import static com.raylib.Jaylib.IsCursorOnScreen;
import static com.raylib.Jaylib.IsMouseButtonPressed;
import static com.raylib.Jaylib.LoadImage;
import static com.raylib.Jaylib.LoadMusicStream;
import static com.raylib.Jaylib.Music;
import static com.raylib.Jaylib.PlayMusicStream;
import static com.raylib.Jaylib.SetConfigFlags;
import static com.raylib.Jaylib.SetMusicVolume;
import static com.raylib.Jaylib.SetWindowIcon;
import static com.raylib.Jaylib.UnloadMusicStream;
import static com.raylib.Jaylib.UpdateMusicStream;
import static com.raylib.Jaylib.WindowShouldClose;
import static com.raylib.Raylib.DrawText;

public class Main {

    private static ArrayList<Agent> npcs;
    private static Player hero;
    private static ArrayList<Enemy> enemies;
    private static GameMaster gm;

    private static int state;

    /**
     * The start of this program
     *
     * @param args argument past in to this program
     */
    public static void main(final String[] args) {
        // Check with user if they want to use GUI
        Scanner kb = new Scanner(System.in);
        System.out.print("Use RayLib (Y/N): ");
        String input = kb.nextLine().trim().toUpperCase();
        boolean isGUIFlag = input.charAt(0) == 'Y';

        // Set up the game environment
        setUpGame();

        if (isGUIFlag) { // Run using GUI
            System.out.println(
                    ConsoleColors.RED +
                    "Warring RayLib(JayLib) will print it's logs when its window close" +
                    ConsoleColors.RESET
            );
            printCurrent();
            guiMain();
        } else { // Run using terminal
            do {
                printCurrent();
            } while (runNext());
            printCurrent();
        }
    }

    /**
     * runs the gui (raylib)
     * <br> on right click in window to run next
     * @NOTE most of this code is base off and calls of c and cpp code
     */
    private static void guiMain() {
        final int[] windowSize = new int[]{800, 512};

        setUpWindowInMain(windowSize);

        Music music = setupSoundAndMusic();

        Canvas canvas = gm.getMap();

        int padding = 10;
        int spacing = 5;
        int mapRow = canvas.getNumOfRow();
        int mapCol = canvas.getNumOfCols();
        int boxWidth = ((windowSize[0] - (padding * 2)) - ((mapCol - 1) * spacing)) / mapCol;
        int boxHeight = ((windowSize[1] - (padding * 2)) - ((mapRow - 1) * spacing)) / mapRow;

        Agent[][] agents = new Agent[mapRow][mapCol];

        while (!WindowShouldClose()) {
            // Updates
            UpdateMusicStream(music);

            // Run next pre-program move when user click in the window
            if (IsMouseButtonPressed(0) && IsCursorOnScreen()) {
                runNext();
                printCurrent();// Print things to the conssoal
            }

            if (canvas != gm.getMap()) {
                canvas = gm.getMap();
                mapRow = canvas.getNumOfRow();
                mapCol = canvas.getNumOfCols();
                boxWidth = ((windowSize[0] - (padding * 2)) - ((mapCol - 1) * spacing)) / mapCol;
                boxHeight = ((windowSize[1] - (padding * 2)) - ((mapRow - 1) * spacing)) / mapRow;
            }

            for (Agent npc : npcs) {
                agents[npc.getX()][npc.getY()] = npc;
            }
            agents[hero.getX()][hero.getY()] = hero;
            for (Agent enemy : enemies) {
                agents[enemy.getX()][enemy.getY()] = enemy;
            }

            // Screen
            BeginDrawing();
            ClearBackground(BLANK);

            for (int i = 0; i < mapRow; i++) {
                for (int j = 0; j < mapCol; j++) {
                    Color ourColor = canvas.getColor(i, j);
                    Raylib.Color color;
                    if (ourColor != null) {
                        color = new Jaylib.Color(
                                ourColor.getRed(),
                                ourColor.getGreen(),
                                ourColor.getBlue(),
                                ourColor.getAlpha());
                    } else {
                        color = new Jaylib.Color(255, 255, 255, 100);
                    }
                    DrawRectangle(
                            padding + i * boxWidth + i * spacing,
                            padding + j * boxHeight + j * spacing,
                            boxWidth, boxHeight, color
                    );
                    if (ourColor != null) {
                        DrawText(
                                agents[i][j].getClass().getSimpleName(),
                                padding + i * boxWidth + i * spacing + 10,
                                padding + j * boxHeight + j * spacing + 10,
                                10,
                                BLACK
                        );
                    }
                }
            }

            EndDrawing();
        }

        UnloadMusicStream(music);

        CloseAudioDevice();

        CloseWindow();
    }

    /**
     * Setup music (raylib)
     * @return the control for the music
     */
    private static Music setupSoundAndMusic() {
        InitAudioDevice();

        Music music = LoadMusicStream("res/music/Loops/OveMelaa - Trance Bit Bit Loop.ogg");
        SetMusicVolume(music, 0.04f);
        PlayMusicStream(music);
        return music;
    }

    /**
     * Set up the window for the gui (raylib)
     * @param windowSize the size of the window
     */
    private static void setUpWindowInMain(final int[] windowSize) {
        SetConfigFlags(FLAG_WINDOW_HIGHDPI);
        SetConfigFlags(FLAG_WINDOW_TRANSPARENT);
        SetConfigFlags(FLAG_WINDOW_TOPMOST);
        InitWindow(windowSize[0], windowSize[1], "EWU CSCD 212 Game - Lab3");

        Image logo = LoadImage("res/icon/eagle.png");
        SetWindowIcon(logo);
    }

    /**
     * Run the next step
     * @return if there is any more steps left
     */
    private static boolean runNext() {
        switch (state) {
            case 0: case 7:
                hero.useSpecialAbility();
                break;
            case 1:
                hero.move(6, 6);
                break;
            default:
        }
        updateAgents();
        state++;
        return (state < 8);
    }

    /**
     * run the update on all agents
     */
    public static void updateAgents() {
        for (Agent npc : npcs) {
            npc.upDate();
        }
        hero.upDate();
        for (Agent enemy : enemies) {
            enemy.upDate();
        }
    }

    /**
     * Setting up the game
     */
    private static void setUpGame() {
        // GameMaster
        gm = GameMaster.getGameMaster();

        // Making entities
        npcs = new ArrayList<>();
        npcs.add(new Farmer("Jorag"));
        npcs.trimToSize();
        hero = new SimpleHero("Bob");
        enemies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            enemies.add(new Goblin());
        }
        enemies.trimToSize();

        // gm listing to all the agents
        for (Agent npc : npcs) {
            npc.addPropertyChangeListener(gm);
        }
        hero.addPropertyChangeListener(gm);
        gm.addPropertyChangeListener(hero);
        for (Enemy enemy : enemies) {
            enemy.addPropertyChangeListener(gm);
            gm.addPropertyChangeListener(enemy);
        }

        // Set x and y
        enemies.get(0).setX(9);
        enemies.get(0).setY(9);

        enemies.get(1).setX(8);
        enemies.get(1).setY(9);

        enemies.get(2).setX(9);
        enemies.get(2).setY(8);

        enemies.get(3).setX(8);
        enemies.get(3).setY(8);

        enemies.get(4).setX(0);
        enemies.get(4).setY(8);

        npcs.get(0).setX(8);
        npcs.get(0).setY(0);

        hero.setX(0);
        hero.setY(0);

        // setup State
        state = 0;
    }

    /**
     * The current state of the game get print to the console
     */
    private static void printCurrent() {
        System.out.println(" ------ Current State ------ ");
        System.out.println(" ------ NPC Status ------ ");
        for (Agent npc : npcs) {
            System.out.println(npc);
        }
        System.out.println(" ------ Enemies Status ------ ");
        for (Enemy enemy : enemies) {
            System.out.println(enemy);
        }
        System.out.println(" ------ Player status ------ ");
        System.out.println(hero);
        System.out.println(" ------ Map ------ ");
        System.out.println(gm.getMap());
        System.out.println();
        System.out.println();
    }
}
