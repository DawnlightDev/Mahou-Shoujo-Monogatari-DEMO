package main;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile.TileManager;
import tiles_interactive.InteractiveTile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {
    //SCREEN SETTINGS
    final int originalTileSize = 32; //32 x 32 tile
    final int scale = 2;

    //SETTINGS
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int maxMap = 10;
    public int currentMap = 0;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    //FOR FULL SCREEN
    int screenWidthFull = screenWidth;
    int screenHeightFull = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public CollisionChecker cChecker = new CollisionChecker(this, ui);
    public PathFinder pFinder = new PathFinder(this);
    public EnvironmentManager eManager = new EnvironmentManager(this);
    public SaveLoad saveLoad = new SaveLoad(this);

    //ENTITIES AND OBJECTS
    public Player player = new Player(this, keyH);
    public Entity[][] npc = new Entity[10][10];
    public Entity[][] obj = new Entity[10][50];
    public Entity[][] mon = new Entity[10][10];
    public InteractiveTile[][] iTile = new InteractiveTile[maxMap][50];
    ArrayList<Entity> entityList = new ArrayList<>();
    Sound sound = new Sound();

    //GAME STATE
    public GameState gameState;

    //TRANSITION
    private float alpha = 0f;
    private int fadeDuration = 2000; // 2 seconds
    private Timer fadeInTimer;
    private Timer fadeOutTimer;

    //Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        fadeInTimer = new Timer(100, e -> fadeInStep());
        fadeOutTimer = new Timer(100, e -> fadeOutStep());
    }

    public void setupGame() {
        aSetter.setNPC();
        aSetter.setObject();
        aSetter.setMonster();
        aSetter.setInteractiveTile();

        eManager.setup();

        gameState = GameState.TITLE_STATE;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        setFullScreen();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000f / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            //UPDATE: Update information such as character positions
            update();
            //DRAW: Draw the screen with the updated information
            drawToTempScreen();
            drawToScreen();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void update() {
        if (gameState == GameState.PLAY_STATE) {
            //Player
            player.update();

            //NPC
            for (int i = 0; i < npc.length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            //MONSTER
            for (int i = 0; i < mon.length; i++) {
                if (mon[currentMap][i] != null) {
                    mon[currentMap][i].update();
                }
            }

            //ENVIRONMENT
            eManager.update();

            //INTERACTIVE TILES
            for(int i = 0; i < iTile[currentMap].length; i++) {
                if(iTile[currentMap][i] != null) {
                    iTile[currentMap][i].update();
                }
            }
        }
        if (gameState == GameState.PAUSE_STATE) {

        }

    }

    private void drawToTempScreen() {
        //TITLE SCREEN
        if (gameState == GameState.TITLE_STATE) {
            ui.draw(g2);
        }

        else {
            tileM.draw(g2);

            //ENTITIES
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }

            //MONSTER
            for (int i = 0; i < mon.length; i++) {
                if (mon[i] != null) {
                    entityList.add(mon[currentMap][i]);
                }
            }

            //INTERACTIVE TILES
            for(int i = 0; i < iTile[currentMap].length; i++) {
                if(iTile[currentMap][i] != null) {
                    //System.out.println(iTile[currentMap].length);
                    entityList.add(iTile[currentMap][i]);
                }
            }

            entityList.add(player);

            //Sort
            entityList.sort(new Comparator<Entity>() {
                @Override
                public int compare(Entity o1, Entity o2) {
                    if (o1 == null || o2 == null) {
                        return 0;
                    }
                    return o1.worldY - o2.worldY;
                }
            });

            //Draw Entities
            for (Entity entity : entityList) {
                if (entity != null) {
                    entity.draw(g2);
                }
            }

            //Empty List
            entityList.clear();

            //ENVIRONMENT
            eManager.draw(g2);

            //UI
            ui.draw(g2);

            //LOOT
            if (player.lootDisplayed) {
                Entity loot = obj[currentMap][player.lootIndex];
                if (loot != null) {
                    loot.draw(g2);
                }
            }

        }
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

    public void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }

    private void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidthFull, screenHeightFull, null);
        g.dispose();
    }

    private void setFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        screenWidthFull = Main.window.getWidth();
        screenHeightFull = Main.window.getHeight();
    }

    public BufferedImage fadeImage(BufferedImage inputImage, float alpha) {
        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outputImage.createGraphics();

        // Set the alpha composite
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2.setComposite(alphaComposite);

        // Draw the input image onto the output image
        g2.drawImage(inputImage, 0, 0, null);

        // Dispose of the graphics object
        g2.dispose();

        return outputImage;
    }

    public void fadeIn() {
        // Stop the timers if they're currently running
        fadeInTimer.stop();
        fadeOutTimer.stop();

        // Set the initial delay of the fadeOutTimer
        fadeOutTimer.setInitialDelay(fadeDuration);

        // Start the fadeInTimer
        fadeInTimer.start();
    }

    public void fadeOut() {
        // Stop the timers if they're currently running
        fadeInTimer.stop();
        fadeOutTimer.stop();

        // Set the initial delay of the fadeInTimer
        fadeInTimer.setInitialDelay(fadeDuration);

        // Start the fadeOutTimer
        fadeOutTimer.start();
    }

    private void fadeInStep() {
        // Update the alpha value for the fade-in effect
        alpha += 0.1f;
        if (alpha > 1.0f) {
            alpha = 1.0f;
            fadeInTimer.stop();
        }

        // Set the alpha composite on the Graphics2D object
        Graphics2D g2 = (Graphics2D) getGraphics();
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2.setComposite(alphaComposite);

        // Draw the current contents of the panel onto the screen
        paint(g2);

        // Dispose of the graphics object
        g2.dispose();

        // Trigger a repaint of the panel
        repaint();
    }

    private void fadeOutStep() {
        // Update the alpha value for the fade-out effect
        alpha -= 0.1f;
        if (alpha < 0.0f) {
            alpha = 0.0f;
            fadeOutTimer.stop();
        }

        // Set the alpha composite on the Graphics2D object
        Graphics2D g2 = (Graphics2D) getGraphics();
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2.setComposite(alphaComposite);

        // Draw the current contents of the panel onto the screen
        paint(g2);

        // Dispose of the graphics object
        g2.dispose();

        // Trigger a repaint of the panel
        repaint();
    }

}
