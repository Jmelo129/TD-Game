import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.Border;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.util.Random;
import java.io.IOException;
import javax.sound.sampled.*;

// Main class
public class Main{

    // Various lists to store information about enemies and towers
    static Random rand = new Random();
    static ArrayList<Integer> enemySizes = new ArrayList<Integer>();
    static ArrayList<Integer> enemySpeeds = new ArrayList<Integer>();
    static ArrayList<Integer> enemyXs = new ArrayList<Integer>();
    static ArrayList<Integer> enemyYs = new ArrayList<Integer>();
    static ArrayList<Integer> enemyHealths = new ArrayList<Integer>();
    static ArrayList<Integer> enemyMaxHealths = new ArrayList<Integer>();
    static ArrayList<Integer> enemyTravelDistance = new ArrayList<Integer>();
    static ArrayList<String> enemyDirections = new ArrayList<String>();
    static ArrayList<Towers> towers = new ArrayList<Towers>();

    // Game variables
    static int money=1000;
    static int mouseX, mouseY, spawnRate=700, maxHealth=100, health=maxHealth;
    static double percent;
    static boolean update=false;
    static ImageIcon towerImage;

    // Grid representing the game map
    // 0: Represents a tower-placeable tile
    // 1: Represents the path
    // 2: Represents a placed down tower
    static int mapGrid[][] = {
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,1,0,0,1,0,0,1,1,1,0,0,0,0,0},
                {0,1,1,1,0,0,1,0,0,1,1,0,1,0,1,0,0,0,0,0},
                {0,1,0,0,0,0,1,0,0,0,1,0,1,0,1,0,0,0,0,0},
                {0,1,0,0,0,1,1,0,0,0,1,1,1,0,1,0,0,0,0,0},
                {0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,1,1,1,1,1,1,1,1},
                {0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
        };
    
    // Timers for various game events
    static int timers[] = {0,0,0,0};
    
    // Game initialization and setup
    public static void Game(){
        // JFrame setup
        JFrame jFrame = new JFrame("Tower Defense Game");
        jFrame.setLayout(null);
        jFrame.setSize(1100, 600);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().setBackground(Color.DARK_GRAY);
        jFrame.setResizable(false);
    
        // Drawing panel setup
        JPanel drawingPanel = new MyDrawingPanel();
        drawingPanel.setBounds(-8,0,1100,600);
        drawingPanel.setLayout(null);
        drawingPanel.setOpaque(false);
    
        // Display for money
        JLabel moneyDisplay = new JLabel();
        moneyDisplay.setText("<html><b>$" + money + "</b></html>");
        moneyDisplay.setFont(new Font("SansSerif", Font.PLAIN, 22));
        moneyDisplay.setForeground(new Color(2,170,10));
        moneyDisplay.setBounds(820,150,260,30);
        moneyDisplay.setHorizontalAlignment(JLabel.CENTER);
        drawingPanel.add(moneyDisplay);
    
        // Display for base health
        JLabel waveDisplay = new JLabel();
        waveDisplay.setText("<html><b>Base Health</b></html>");
        waveDisplay.setFont(new Font("SansSerif", Font.PLAIN, 26));
        waveDisplay.setForeground(Color.WHITE);
        waveDisplay.setBounds(820,50,260,30);
        waveDisplay.setHorizontalAlignment(JLabel.CENTER);
        drawingPanel.add(waveDisplay);
    
        // Instructions display
        JLabel instructions = new JLabel();
        instructions.setText("<html><div style='text-align:center; font-size: 18px;'><b><u>Instructions</u></b></div><br>- Each tower costs $100 and to place a tower, make sure you have enough cash and click on a valid tile<br><br>- Your goal is to survive for as long as possible, and if you find a way to survive forever (yes that's possible), then you win!<br><br>- You better stop reading this and focus now, or UR GONNA DIE! :O</html>");
        instructions.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instructions.setForeground(Color.WHITE);
        instructions.setBounds(820,250,260,300);
        instructions.setHorizontalAlignment(JLabel.CENTER);
        drawingPanel.add(instructions);
    
        // Adding components to JFrame
        jFrame.add(drawingPanel);
    
        // Tower image setup
        towerImage = new ImageIcon("untitled/src/Tower1-removebg-preview (2).png");
    
        // Mouse listener setup
        MyMouseListener myMouseListener = new MyMouseListener(drawingPanel, moneyDisplay);
        drawingPanel.addMouseListener(myMouseListener);
        drawingPanel.addMouseMotionListener(myMouseListener);
    
        // Setting JFrame properties
        jFrame.setLocationRelativeTo(null);
        jFrame.getContentPane().add(drawingPanel);
        jFrame.setVisible(true);
        playMusic("untitled/src/MEGALOVANIA.wav");
    
        // Main game loop
        while(true) {
            update=false;
    
            // Increment timers
            for (int i=0; i<4; i++) {
                timers[i]++;
            }
    
            // Decrement tower cooldown timers
            for (int i=0; i<towers.size(); i++) {
                if(towers.get(i).getBT() > 0) {
                    towers.get(i).setBT(towers.get(i).getBT()-1);
                }
            }
    
            // Move enemies and check if they reach the base
            if (timers[0] >= 20) {
                timers[0] = 0;
    
                calculateDirection();
                for (int i=0; i<enemySizes.size(); i++) {
                    if (enemyXs.get(i) <= 790) {
                        if (enemyHealths.get(i) > 0){
                            enemyTravelDistance.set(i, enemyTravelDistance.get(i)+enemySpeeds.get(i));
                            switch(enemyDirections.get(i)) {
                                case "u":
                                    enemyYs.set(i, enemyYs.get(i)-enemySpeeds.get(i));
                                    break;
                                case "d":
                                    enemyYs.set(i, enemyYs.get(i)+enemySpeeds.get(i));
                                    break;
                                case "l":
                                    enemyXs.set(i, enemyXs.get(i)-enemySpeeds.get(i));
                                    break;
                                case "r":
                                    enemyXs.set(i, enemyXs.get(i)+enemySpeeds.get(i));
                                    break;
                            }
                        }
                    } else {
                        health-=enemyHealths.get(i);
                        enemyHealths.set(i, 0);
                        if (health<=0){
                            jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
                        }
                    }
                }
                drawingPanel.repaint();
            }
    
            // Check tower cooldowns and attack enemies
            for (int i=0;i<towers.size();i++) {
                towers.get(i).setWait(towers.get(i).getWait()+1);
                if (towers.get(i).getWait() >= towers.get(i).getFr()){
                    towers.get(i).setTarget(calculateClosest((towers.get(i).getX()*40)+20, (towers.get(i).getY()*40)+20, towers.get(i).getRange()));
                    if (towers.get(i).getTarget() >= 0) {
                        try{
                            towers.get(i).setBulletTargetX(enemyXs.get(towers.get(i).getTarget())+(enemySizes.get(i)/2));
                            towers.get(i).setBulletTargetY(enemyYs.get(towers.get(i).getTarget())+(enemySizes.get(i)/2));
                            towers.get(i).setBT(20);
                            towers.get(i).setWait(0);
                            update=true;
                            enemyHealths.set(towers.get(i).getTarget(), enemyHealths.get(towers.get(i).getTarget()) - towers.get(i).getDmg());
                            if (enemyHealths.get(towers.get(i).getTarget()) <= 0){
                                money+=3;
                                moneyDisplay.setText("<html><b>$" + money + "</b></html>");
                            }
                            System.out.println("Tower " + i + ": " + towers.get(i).getTarget());
                        }
                        catch(Exception e){
                        }
                    }
                }
            }
    
            // Remove defeated enemies from the lists
            for (int i=0;i<enemySizes.size();i++) {
                if (enemyHealths.get(i) <= 0){
                    enemySizes.remove(i);
                    enemySpeeds.remove(i);
                    enemyXs.remove(i);
                    enemyYs.remove(i);
                    enemyHealths.remove(i);
                    enemyMaxHealths.remove(i);
                    enemyDirections.remove(i);
                    enemyTravelDistance.remove(i);
                    i--;
                }
            }
    
            // Spawn new enemies at a certain rate
            if (timers[2] >= spawnRate) {
                timers[2] = 0;
                spawnEnemy();
                drawingPanel.repaint();
            }
    
            // Increase difficulty by decreasing spawn rate over time
            if (timers[3]>=4000){
                timers[3]=0;
                if (spawnRate>70) {
                    spawnRate -= 35;
                }
            }
    
            // Repaint if there were any updates
            if (update) {
                drawingPanel.repaint();
            }
    
            // Pause for a short time to control the game loop speed
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class MyDrawingPanel extends JPanel {
            // Custom paint component method for drawing the game elements
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
    
                // Setting up graphics context
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke((float) 1));
    
                // Tile dimensions and positions
                int width = 40;
                int height = 40;
                int xPos;
                int yPos;
    
                // Drawing the game grid
                for (int y = 0; y<15; y++) {
                    for (int x = 0; x<20; x++) {
                        xPos = (x*40)+8; // Offset b/c Java Swing is gay
                        yPos = y*40;
                        switch(mapGrid[y][x]) {
                            case 0:
                                g.setColor(Color.GREEN);
                                break;
                            case 1:
                                g.setColor(Color.lightGray);
                                break;
                            case 2:
                                g.setColor(Color.GREEN);
                                break;
                        }
                        g.fillRect(xPos, yPos, width, height);
                        if (mapGrid[y][x] == 0) {
                            g.setColor(Color.GRAY);
                            g.drawRect(xPos, yPos, width, height);
                        }
                    }
                }
    
                // Drawing enemies
                for (int i = 0; i < enemySizes.size(); i++) {
                    if (enemyHealths.get(i) > 0){
                        switch(enemyMaxHealths.get(i)){
                            case 4:
                                g.setColor(Color.CYAN);
                                break;
                            case 6:
                                g.setColor(Color.ORANGE);
                                break;
                            case 14:
                                g.setColor(Color.DARK_GRAY);
                                break;
                        }
                        g.fillRect(enemyXs.get(i), enemyYs.get(i), enemySizes.get(i), enemySizes.get(i));
                        g.setColor(Color.RED);
                        g.fillRect(enemyXs.get(i)+(enemySizes.get(i)/2)-15,enemyYs.get(i)+enemySizes.get(i)+5,30,9);
                        g.setColor(Color.GREEN);
                        g.fillRect(enemyXs.get(i)+(enemySizes.get(i)/2)-15,enemyYs.get(i)+enemySizes.get(i)+5,(int)Math.ceil((30*enemyHealths.get(i))/enemyMaxHealths.get(i)),9);
                        g.setColor(Color.BLACK);
                        g.drawRect(enemyXs.get(i)+(enemySizes.get(i)/2)-15,enemyYs.get(i)+enemySizes.get(i)+5,30,9);
                    }
                }
    
                // Drawing base health bar
                g.setColor(Color.RED);
                g.fillRect(850,100,200,40);
                g.setColor(Color.GREEN);
                g.fillRect(850,100,(int)Math.ceil((200*health)/maxHealth),40);
                g.setColor(Color.BLACK);
                g.drawRect(850,100,200,40);
    
                // Drawing towers and their bullets
                for (int i = 0; i < towers.size(); i++) {
                    g.drawImage(towerImage.getImage(), (towers.get(i).getX() * 40)+10, (towers.get(i).getY() * 40)-3, null);
                    if (towers.get(i).getBT() > 0 && towers.get(i).getTarget() >= 0) {
                        g2d.setStroke(new BasicStroke((float) 3));
                        g.setColor(Color.RED);
                        g.drawLine(towers.get(i).getX()*40+28,towers.get(i).getY()*40,towers.get(i).getBulletTargetX(),towers.get(i).getBulletTargetY());
                    }
                }
            }
        }
    
        // Method to play music
        public static void playMusic(String filepath) {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filepath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            }
            catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace(); // Handle the exception appropriately, e.g., show an error message
            }
        }
    
        // Method to spawn new enemies
        public static void spawnEnemy() {
            int enemyChoice = rand.nextInt(3);
            switch(enemyChoice){
                case 0:
                    enemySizes.add(20);
                    enemySpeeds.add(3);
                    enemyXs.add(10);
                    enemyYs.add(130);
                    enemyHealths.add(6);
                    enemyMaxHealths.add(6);
                    break;
                case 1:
                    enemySizes.add(30);
                    enemySpeeds.add(2);
                    enemyXs.add(10);
                    enemyYs.add(125);
                    enemyHealths.add(14);
                    enemyMaxHealths.add(14);
                    break;
                case 2:
                    enemySizes.add(15);
                    enemySpeeds.add(4);
                    enemyXs.add(10);
                    enemyYs.add(132);
                    enemyHealths.add(4);
                    enemyMaxHealths.add(4);
                    break;
            }
            enemyTravelDistance.add(0);
            enemyDirections.add("r");
        }
    
        // Method to calculate enemy directions
        public static void calculateDirection() {
            for (int i=0; i<enemySizes.size(); i++) {
                int arrayX = (int)(Math.floor((enemyXs.get(i)+(enemySizes.get(i)/2))/40));
                int arrayY = (int)(Math.floor((enemyYs.get(i)+(enemySizes.get(i)/2))/40));
    
                if (arrayX<19 && enemyDirections.get(i).equals("r")) {
                    if (mapGrid[arrayY][arrayX+1] != 1 && enemyXs.get(i)+(enemySizes.get(i)/2) >= arrayX*40+20+8) {
                        if (mapGrid[arrayY-1][arrayX] == 1)
                            enemyDirections.set(i, "u");
                        else
                            enemyDirections.set(i, "d");
                    }
                }
    
                if (arrayX>0 && enemyDirections.get(i).equals("l")) {
                    if (mapGrid[arrayY][arrayX-1] != 1 && enemyXs.get(i)+(enemySizes.get(i)/2) <= arrayX*40+20+8) {
                        if (mapGrid[arrayY-1][arrayX] == 1)
                            enemyDirections.set(i, "u");
                        else
                            enemyDirections.set(i, "d");
                    }
                }
    
                if (arrayY>0 && enemyDirections.get(i).equals("u")) {
                    if (mapGrid[arrayY-1][arrayX] != 1 && enemyYs.get(i)+(enemySizes.get(i)/2) <= arrayY*40+20) {
                        if (mapGrid[arrayY][arrayX-1] == 1)
                            enemyDirections.set(i, "l");
                        else
                            enemyDirections.set(i, "r");
                    }
                }
    
                if (arrayY<14 && enemyDirections.get(i).equals("d")) {
                    if (mapGrid[arrayY+1][arrayX] != 1 && enemyYs.get(i)+(enemySizes.get(i)/2) >= arrayY*40+20) {
                        if (mapGrid[arrayY][arrayX-1] == 1)
                            enemyDirections.set(i, "l");
                        else
                            enemyDirections.set(i, "r");
                    }
                }
            }
        }
    
        // Method to calculate the closest enemy within tower range
        public static int calculateClosest(int towX, int towY, int range) {
            int furthestDisplacement=0;
            int target = -1;
    
            for (int i=0; i<enemySizes.size(); i++) {
                int displacement = enemyTravelDistance.get(i);
                double distance = Math.sqrt((towX-enemyXs.get(i))*(towX-enemyXs.get(i)) + (towY-enemyYs.get(i))*(towY-enemyYs.get(i)));
                if (displacement > furthestDisplacement && enemyHealths.get(i) > 0 && distance < range) {
                    furthestDisplacement = displacement;
                    target=i;
                }
            }
            return target;
        }
    
        // Custom mouse listener class for handling mouse events
        static class MyMouseListener implements MouseListener, MouseMotionListener {
            private MyDrawingPanel drawingPanel;
            private JLabel moneyDisplay;
    
            public MyMouseListener(JPanel drawingPanel2, JLabel moneyDisplay) {
                this.drawingPanel = (MyDrawingPanel) drawingPanel2;
                this.moneyDisplay = moneyDisplay;
            }
    
            public void mouseClicked(MouseEvent e) {
                mouseX = (int) (Math.floor(((e.getX() - 8) / 40)));
                mouseY = (int) (Math.floor((e.getY() / 40)));
                if (isValidTile(mouseX, mouseY) && money >= 100) {
                    synchronized (towers) {
                        synchronized (mapGrid) {
                            if (mapGrid[mouseY][mouseX] == 0) {
                                money -= 100;
                                moneyDisplay.setText("<html><b>$" + money + "</b></html>");
                                Towers newTower = new Towers(mouseX, mouseY, 500, 2, 150);
                                newTower.setTarget(-1);
                                towers.add(newTower);
                                mapGrid[mouseY][mouseX] = 2;
                                drawingPanel.repaint();
                            }
                        }
                    }
                }
            }
    
            private boolean isValidTile(int x, int y) {
                return x >= 0 && x < mapGrid[0].length && y >= 0 && y < mapGrid.length;
            }
    
            public void mousePressed(MouseEvent e) {
//            System.out.println("Mouse Pressed at: (" + e.getX() + ", " + e.getY() + ")");
            }
    
            public void mouseReleased(MouseEvent e) {
    //            System.out.println("Mouse Released at: (" + e.getX() + ", " + e.getY() + ")");
            }
    
            public void mouseEntered(MouseEvent e) {
    //            System.out.println("Mouse Entered");
            }
    
            public void mouseExited(MouseEvent e) {
    //            System.out.println("Mouse Exited");
            }
    
            public void mouseMoved(MouseEvent e) {
    //            System.out.println("Mouse Moved at: (" + e.getX() + ", " + e.getY() + ")");
            }
    
            public void mouseDragged(MouseEvent e) {
    //            System.out.println("Mouse Dragged at: (" + e.getX() + ", " + e.getY() + ")");
            }
        }
        // Main method to start the game
        public static void main(String[] args){
            spawnEnemy();
            Game();
        }
    }
    
    
