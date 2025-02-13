package org.example.entities;
import org.example.services.ServiceMiniGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.HashSet;
public class PacMan extends MiniGame implements ActionListener ,KeyListener{
    public ServiceMiniGame getServiceMiniGame() {
        return serviceMiniGame;
    }

    public void setServiceMiniGame(ServiceMiniGame serviceMiniGame) {
        this.serviceMiniGame = serviceMiniGame;
    }

    class Block {
        int x;
        int y;
        int width;
        int height;
        int sx;
        int sy;
        Image image;
        char direction='U';
        int velX=0;
        int velY=0;

        Block(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
            sx = x;
            sy = y;

        }
        void updateDirection(char direction) {
            this.direction = direction;
            if (this.direction == 'U') {
                this.velX = 0;
                this.velY = -10;
            }
            else if (this.direction == 'D') {
                this.velX = 0;
                this.velY = 10;
            }
            else if (this.direction == 'L') {
                this.velX = -10;
                this.velY = 0;
            }
            else if (this.direction == 'R') {
                this.velX = 10;
                this.velY = 0;
            }


        }
        public void reset()
        {
            this.x=sx;
            this.y=sy;

        }
    }
    private ServiceMiniGame serviceMiniGame = new ServiceMiniGame();

    private int row = 19;
    private int col = 19;
    private int tileSize = 30;
    private Image wallImage = new ImageIcon("src/main/resources/images/wall.png").getImage();
    private Image ball = new ImageIcon("src/main/resources/images/ball.png").getImage();
    private Image orangeGhost = new ImageIcon("src/main/resources/images/Mbappe.png").getImage();
    private Image pinkGhost = new ImageIcon("src/main/resources/images/Haland.png").getImage();
    private Image blueGhost = new ImageIcon("src/main/resources/images/Messi.png").getImage();
    private Image foodimg = new ImageIcon("src/main/resources/images/food.png").getImage();



    private HashSet<Block> walls;
    private HashSet<Block> ghosts;
    private HashSet<Block> foods;
    private Timer gameLoop;
    private Block pacman;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int lives=3;
    boolean go=false;
    private String[] Map = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XX X",
            "X  X X       X X  X",
            "X XX X X oXX X XXXX",
            "X       bp        X",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "XXXXXXXXXXXXXXXXXXX"

    };

    public boolean getGameloop()
    {
        return gameLoop.isRunning();
    }
    public PacMan() {
        super(1, 0, 1, "", null);
        setPreferredSize(new Dimension(row * tileSize, col * tileSize));
        setBackground(new Color(24, 82, 10));
        loadMap();
        for (Block ghost : ghosts) {
            char direction = directions[random.nextInt(4)];
            ghost.updateDirection(direction);
        }
        gameLoop=new Timer(50,this);
        gameLoop.start();
        addKeyListener(this);
        setFocusable(true);

    }
    public void loadMap() {
        walls = new HashSet<>();
        ghosts = new HashSet<>();
        foods = new HashSet<>();
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                String row = Map[r];
                char mapChar = row.charAt(c);
                if (mapChar == 'X') {
                    Block wall = new Block(c * tileSize, r * tileSize, tileSize, tileSize, wallImage);
                    walls.add(wall);
                }
                if (mapChar == 'b') {
                    Block ghost = new Block(c * tileSize, r * tileSize, tileSize, tileSize, blueGhost);
                    ghosts.add(ghost);
                }
                if (mapChar == 'o') {
                    Block ghost = new Block(c * tileSize, r * tileSize, tileSize, tileSize, orangeGhost);
                    ghosts.add(ghost);
                }
                if (mapChar == 'p') {
                    Block ghost = new Block(c * tileSize, r * tileSize, tileSize, tileSize, pinkGhost);
                    ghosts.add(ghost);
                }
                if (mapChar == 'P') {
                    pacman = new Block(c * tileSize, r * tileSize, tileSize, tileSize, ball);
                }
                if (mapChar == ' ') {
                    Block food = new Block(c * tileSize, r * tileSize, tileSize, tileSize, foodimg);
                    foods.add(food);
                }
            }

        }
    }

    public void draw(Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        for (Block food : foods) {
            g.drawImage(food.image, food.x, food.y, food.width, food.height, null);
        }
        g.setColor(Color.BLACK );
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if (go){
            g.drawString("Game Over:"+String.valueOf(score), tileSize/2, tileSize/2);
        }else
            g.drawString("x"+String.valueOf(lives)+" Score"+String.valueOf(score), tileSize/2, tileSize/2);

    }

    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }
    public void move()
    {
        pacman.x += pacman.velX;
        pacman.y += pacman.velY;
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velX;
                pacman.y -= pacman.velY;
                break;
            }
        }
        for (Block ghost : ghosts) {
            if (collision(ghost, pacman))
            {
                lives-=1;
                if (lives==0)
                {
                    go=true;
                    return;
                }
                resetp();
            }
            if (ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D') {

                ghost.updateDirection('D');
            }
            ghost.x += ghost.velX;
            ghost.y += ghost.velY;
            for (Block wall : walls) {
                if (collision(ghost, wall)| ghost.x <= 0 || ghost.x + ghost.width >= getWidth() || ghost.y <= 0 || ghost.y + ghost.height >= getHeight())
                {
                    ghost.x -= ghost.velX;
                    ghost.y -= ghost.velY;
                    char direction = directions[random.nextInt(4)];
                    ghost.updateDirection(direction);
                }
            }
        }
        Block food = null;
        for (Block f : foods) {
            if (collision(pacman, f)) {
                food = f;
                score+=10;
            }
        }
        foods.remove(food);

    }
    public void resetp()
    {
        pacman.reset();
        pacman.velX=0;
        pacman.velY=0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char direction = directions[random.nextInt(4)];
            ghost.updateDirection(direction);
        }
    }
    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }
    public void saveGameResult() {
        try {
            String data = convertDataString();
            this.data=data;
            serviceMiniGame.ajouter(this);
            System.out.println("JAWEK BHI");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public String convertDataString() {
        StringBuilder data = new StringBuilder();
        data.append("PacMan,")
                .append(pacman.x).append(",")
                .append(pacman.y).append(",")
                .append(pacman.direction).append(";");
        for (Block ghost : ghosts) {
            data.append("Ghost,")
                    .append(ghost.x).append(",")
                    .append(ghost.y).append(",")
                    .append(ghost.direction).append(";");
        }
        for (Block food : foods) {
            data.append("Food,")
                    .append(food.x).append(",")
                    .append(food.y).append(";");
        }
        data.append("Lives,").append(lives).append(";");
        return data.toString();
    }
    public void resumeGame(String data) {
        ghosts.clear();
        foods.clear();
        String[] parts = data.split(";");

        for (String part : parts) {
            String[] elements = part.split(",");

            if (elements.length > 0) {
                switch (elements[0]) {
                    case "PacMan":
                        pacman.x = Integer.parseInt(elements[1]);
                        pacman.y = Integer.parseInt(elements[2]);
                        pacman.direction = elements[3].charAt(0);
                        break;

                    case "Ghost":
                        Block ghost = new Block(
                                Integer.parseInt(elements[1]),
                                Integer.parseInt(elements[2]),
                                tileSize, tileSize,
                                getGhostImage(elements[3].charAt(0))
                        );
                        ghost.direction = elements[3].charAt(0);
                        ghosts.add(ghost);
                        break;

                    case "Food":
                        Block food = new Block(
                                Integer.parseInt(elements[1]),
                                Integer.parseInt(elements[2]),
                                tileSize, tileSize,
                                foodimg
                        );
                        foods.add(food);
                        break;

                    case "Lives":
                        lives = Integer.parseInt(elements[1]);
                        break;
                }
            }
        }
        gameLoop.start();
        repaint();
        move();
    }
    private Image getGhostImage(char type) {
        switch (type) {
            case 'b':
                return blueGhost;
            case 'o':
                return orangeGhost;
            case 'p':
                return pinkGhost;
            default:
                return blueGhost;
        }
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(go)
        {
            System.out.println(this);
            gameLoop.stop();
            saveGameResult();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_M)
        {
            try {
                int lastGameId = serviceMiniGame.lastSavedGame();
                if (lastGameId != -1)
                {
                    String data = serviceMiniGame.getGameDataById(lastGameId);
                    resumeGame(data);
                } else
                {
                    System.out.println("No saved games found.");
                }
            }catch(SQLException ex) {
                ex.printStackTrace();
            }

        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            gameLoop.stop();
            saveGameResult();

        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

    }
    @Override
    public String toString() {
        return "PacMan{ type=" + type +", score=" + score +", user=" + user +", result='" + result ;
    }
}


