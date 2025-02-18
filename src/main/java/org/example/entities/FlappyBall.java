package org.example.entities;
import javafx.scene.chart.ScatterChart;
import org.example.services.ServiceCarte;
import org.example.services.ServiceMiniGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class FlappyBall extends MiniGame implements ActionListener, KeyListener {
    ServiceMiniGame service = new ServiceMiniGame();
    ServiceCarte serviceCarte = new ServiceCarte();
    boolean gameOver = false;
    private int isDeleted=0;
    private int W=360;
    private int H=640;
    private int test=0;
    public boolean start=false;
    private boolean isPaused = false;
    private Image background=new ImageIcon("src/main/resources/images/background.png").getImage();
    private Image ballImg=new ImageIcon("src/main/resources/images/ball.png").getImage();
    private Image topPipe= new ImageIcon("src/main/resources/images/toppipe.png").getImage();
    private Image bottomPipe=new ImageIcon("src/main/resources/images/bottompipe.png").getImage();
    private int ballX=W/8;
    private int ballY=H/2;
    private int ballWidth=30;
    private int ballHeight=30;
    private Timer gameLoop;
    private Ball ball;
    private int volY=0;
    private int grav=1;
    private int pipeX=W;
    private int pipeY=0;
    private int pipeWidth=64;
    private int pipeHeight=512;
    private int volX=-4;
    private ArrayList<Pipe>pipes;
    private Timer pipeTimer;
    private Random random=new Random();



    public void placePipes()
    {

        int randowPipey=(int) (pipeY-pipeWidth/4 - Math.random()*(pipeHeight/2)); //(0-1) * pipeHzigh/2 ->(0-256) //128 //0-128 - (0-256) -->1/4 pH
        int open=H/4;
        Pipe toppipe=new Pipe(topPipe,'t');
        toppipe.y=randowPipey;
        pipes.add(toppipe);
        Pipe bottompipe=new Pipe(bottomPipe,'b');
        bottompipe.y=toppipe.y+pipeHeight+open;
        pipes.add(bottompipe);
    }
    public FlappyBall() {
        super(2, 0, 1, "", null);
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(W,H));
        ball = new Ball(ballImg);
        pipes = new ArrayList<Pipe>();
       

    }
    public void paint(Graphics g) {
        super.paint(g);
        draw(g);

    }
    public void draw(Graphics g) {
        g.drawImage(background,0,0,W,H,null);
        g.drawImage(ball.img,ball.x,ball.y,ball.width,ball.height,null);
        for(int i=0;i<pipes.size();i++) {
            Pipe pipe=pipes.get(i);
            g.drawImage(pipe.image,pipe.x,pipe.y,pipe.width,pipe.height,null);
        }

        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over Score:" + String.valueOf(score),10,H/2);
        }else
        {
            g.setColor(Color.WHITE);
            g.drawString("Score: "+String.valueOf(score),10,30);
        }
    }
    public void move()
    {
        volY+=grav;
        ball.y+=volY;
        ball.y= Math.max(ball.y,0);
        for(int i=0;i<pipes.size();i++) {
            Pipe pipe=pipes.get(i);
            pipe.x+=volX;
            if (!pipe.passed && ball.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 5;
            }
            if(collision(ball, pipe)){
                gameOver=true;
            }
        }
        if (ball.y>H)
            gameOver=true;

    }

    public void saveGameResult() {
        try {
            this.data=convertDataString();
            this.result="Lost";
            service.ajouter(this);
            System.out.println("JAWEK BHI");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void simplePauseTimer() {

        int pauseDuration = 60000;
        Timer timer = new Timer(100, new ActionListener() {
            private long startTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsedTime = System.currentTimeMillis() - startTime;

                if (!isPaused) {
                   // System.out.println("Game resumed");
                    ((Timer) e.getSource()).stop();
                    return;
                }
                if (elapsedTime >= pauseDuration) {
                    try {
                        isDeleted=1;
                        ServiceMiniGame s = new ServiceMiniGame();
                        int id = s.lastSavedGame();
                        s.supprimer(id);
                        System.out.println("sayey tfaskhet");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    ((Timer)e.getSource()).stop();
                }
            }
        });

        timer.start();
    }
    public String convertDataString() {
        StringBuilder data = new StringBuilder();
        data.append("Ball,")
                .append(ball.x).append(",")
                .append(ball.y).append(";");
        for (Pipe p : pipes) {
            data.append("Pipes,")
                    .append(p.x).append(",")
                    .append(p.y).append(",")
                    .append(p.type).append(";");
        }
        return data.toString();
    }
    public void resumeGame(String data)
    {
        pipes.clear();

        String[] elements = data.split(";");
        for (String element : elements) {
            String[] values = element.split(",");

            if (values[0].equals("Ball")) {
                ball.x = Integer.parseInt(values[1]);
                ball.y = Integer.parseInt(values[2]);
            } else if (values[0].equals("Pipes")) {

                char type = values[3].charAt(0);
                Image pipeImage = (type == 't') ? topPipe : bottomPipe;
                Pipe pipe = new Pipe(pipeImage, type);
                pipe.x = Integer.parseInt(values[1]);
                pipe.y = Integer.parseInt(values[2]);
                pipes.add(pipe);
            }
        }
        gameOver = false;
        volY = 0;
        gameLoop.start();
        pipeTimer.start();
    }
    public boolean collision(Ball a,Pipe b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }
    public void checkAndCreateReward(MiniGame miniGame) throws SQLException {

        if (score>10 && serviceCarte.VerifierCarte("commune",this.user) )
        {
            Carte carte=new Carte(1000,1,"commune","");
            try{
                serviceCarte.ajouter(carte);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (score>1000 && serviceCarte.VerifierCarte("rare",this.user) )
        {
            Carte carte=new Carte(2000,1,"rare","");
            try{
                serviceCarte.ajouter(carte);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (score>2500 && serviceCarte.VerifierCarte("legandaire",this.user))
        {
            Carte carte=new Carte(3000,1,"legandaire","");
            try{
                serviceCarte.ajouter(carte);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void updateGameResult() {

        try {
            int id = service.lastSavedGame();
            String data = convertDataString();
            this.data=data;
            service.modifier(this,id);
            System.out.println("JAWEK BHI tupdata");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();
        if (gameOver && test==0)
        {

            saveGameResult();
            //System.out.println(this);
            try{
                checkAndCreateReward(this);
            }catch(SQLException ex){
                ex.printStackTrace();
            }
            pipeTimer.stop();
            gameLoop.stop();
        } else if (gameOver && test==1) {
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
        if  (e.getKeyCode()==KeyEvent.VK_SPACE)
        {
            if (start)
            {
                volY=-11;
            }
            else
            {
                            pipeTimer = new Timer(1500,new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            placePipes();
                        }
                    });
                    pipeTimer.start();
                    gameLoop=new Timer(1000/60,this);
                    gameLoop.start();
            }
        }
        if (e.getKeyCode()==KeyEvent.VK_P)
        {
            isPaused = true;
            test=1;
            saveGameResult();
            pipeTimer.stop();
            gameLoop.stop();
            score=0;
            simplePauseTimer();
        }
        if (e.getKeyCode()==KeyEvent.VK_R)

            {
                if (isDeleted==0)
                {
                    isPaused = false;
                    try{
                        int id =service.lastSavedGame();
                        String data = service.getGameDataById(id);
                        resumeGame(data);
                    }catch(SQLException ex) {
                        ex.printStackTrace();
                    }
                }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    @Override
    public String toString()
    {
        return "FlappyBall"+ data;
    }


}
