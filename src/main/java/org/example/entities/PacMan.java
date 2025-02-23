package org.example.entities;
import org.example.services.IService;
import org.example.services.ServiceCarte;
import org.example.services.ServiceMiniGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private int pcon=0;
    private int isDeleted=0;
    private ServiceMiniGame serviceMiniGame = new ServiceMiniGame();
    private ServiceCarte serviceCarte = new ServiceCarte();
    private int row = 19;
    private int col = 19;
    private int tileSize = 30;
    private Image wallImage = new ImageIcon("src/main/resources/img/wall.png").getImage();
    private Image ball = new ImageIcon("src/main/resources/img/ball.png").getImage();
    private Image orangeGhost = new ImageIcon("src/main/resources/img/Mbappe.png").getImage();
    private Image pinkGhost = new ImageIcon("src/main/resources/img/Haland.png").getImage();
    private Image blueGhost = new ImageIcon("src/main/resources/img/Messi.png").getImage();
    private Image foodimg = new ImageIcon("src/main/resources/img/food.png").getImage();
    private boolean isPaused = false;
    private boolean isInactive = false;
    private int test=0;
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
    public void simplePauseTimer() {

        int pauseDuration = 60000;
        Timer timer = new Timer(100, new ActionListener() {
            private long startTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsedTime = System.currentTimeMillis() - startTime;

                if (!isPaused) {
                    System.out.println("Game resumed before timer ended");
                    ((Timer) e.getSource()).stop();
                    return;
                }
                if (elapsedTime >= pauseDuration) {
                    try {
                        ServiceMiniGame s = new ServiceMiniGame();
                        int id = s.lastSavedGame();
                        s.supprimer_t(id);
                        System.out.println("sayey tfaskht");
                        isDeleted=1;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
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
                    Block wall = new Block(c * tileSize, r * tileSize, tileSize, tileSize, wallImage,0);
                    walls.add(wall);
                }
                if (mapChar == 'b') {
                    Block ghost = new Block(c * tileSize, r * tileSize, tileSize, tileSize, blueGhost,1);
                    ghosts.add(ghost);
                }
                if (mapChar == 'o') {
                    Block ghost = new Block(c * tileSize, r * tileSize, tileSize, tileSize, orangeGhost,2);
                    ghosts.add(ghost);
                }
                if (mapChar == 'p') {
                    Block ghost = new Block(c * tileSize, r * tileSize, tileSize, tileSize, pinkGhost,3);
                    ghosts.add(ghost);
                }
                if (mapChar == 'P') {
                    pacman = new Block(c * tileSize, r * tileSize, tileSize, tileSize, ball,0);
                }
                if (mapChar == ' ') {
                    Block food = new Block(c * tileSize, r * tileSize, tileSize, tileSize, foodimg,0);
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
    public void checkAndCreateReward(MiniGame miniGame) throws SQLException {

        if (score>10 && serviceCarte.VerifierCarte("commune",this.user) )
        {
            Carte carte=new Carte(1000,1,"commune","");
            try{
                serviceCarte.ajouter_t(carte);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (score>1000 && serviceCarte.VerifierCarte("rare",this.user) )
        {
            Carte carte=new Carte(2000,1,"rare","");
            try{
                serviceCarte.ajouter_t(carte);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (score>2500 && serviceCarte.VerifierCarte("legandaire",this.user))
        {
            Carte carte=new Carte(3000,1,"legandaire","");
            try{
                serviceCarte.ajouter_t(carte);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
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
    public void ResumeGhosts()
    {
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
            serviceMiniGame.ajouter_t(this);
            System.out.println("JAWEK BHI");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void updateGameResult() {

        try {
            int id = serviceMiniGame.lastSavedGame();
            String data = convertDataString();
            this.data=data;
            serviceMiniGame.modifier(this,id);
            System.out.println("JAWEK BHI tupdata");
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
                    .append(ghost.type).append(";");
        }
        for (Block food : foods) {
            data.append("Food,")
                    .append(food.x).append(",")
                    .append(food.y).append(";");
        }
        data.append("Lives,").append(lives).append(";");
        return data.toString();
    }
    public Image getGhostImage(int type) {
        switch (type)

        {
            case 1:
                return blueGhost;
            case 3:
                return pinkGhost;
            case 2:
                return orangeGhost;
        }
        return null;
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
                            pacman.type=0;
                            break;

                        case "Ghost":
                            Block ghost = new Block(
                                    Integer.parseInt(elements[1]),
                                    Integer.parseInt(elements[2]),
                                    tileSize, tileSize,
                                    getGhostImage(Integer.parseInt(elements[3])),
                                    Integer.parseInt(elements[3])
                            );
                            ghost.direction = 'U';
                            ghosts.add(ghost);
                            break;

                        case "Food":
                            Block food = new Block(
                                    Integer.parseInt(elements[1]),
                                    Integer.parseInt(elements[2]),
                                    tileSize, tileSize,
                                    foodimg,
                                    0
                            );
                            foods.add(food);
                            break;

                        case "Lives":
                            lives = Integer.parseInt(elements[1]);
                            break;
                    }
                }
            }

            ResumeGhosts();
            gameLoop.start();
            repaint();
            move();

        }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(go && test==0)
        {
            //System.out.println(this);
            gameLoop.stop();
            try{
                checkAndCreateReward(this);
            }catch(SQLException ex){
                ex.printStackTrace();
            }
            saveGameResult();
        } else if (go && test==1) {
            gameLoop.stop();
            try{
                checkAndCreateReward(this);
            }catch(SQLException ex){
                ex.printStackTrace();
            }
            updateGameResult();

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
        if (e.getKeyCode() == KeyEvent.VK_R)
        {
            isPaused = false;
            if(isDeleted==0)
            {
            try {
                int lastGameId = serviceMiniGame.lastSavedGame();
                if (lastGameId != -1)
                {
                    String data = serviceMiniGame.getGameDataById(lastGameId);
                    test=1;
                    resumeGame(data);
                } else
                {
                    System.out.println("famech");
                }
            }catch(SQLException ex) {
                ex.printStackTrace();
            }
            }

        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            isPaused = true;
            gameLoop.stop();
            saveGameResult();
            simplePauseTimer();




        }


        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
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


