import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GraphicsWindow extends JFrame implements ActionListener{

    //size of window:
    int WIDTH;
    int HEIGHT;
    Font myFont;

    int TIMER_DELAY;
    Timer t;
    int count;

    GameBoard game;
    Controller controller;

    public GraphicsWindow()
    {
        //calls constructor of the "super class"- JFrame - arg is titlebar name
        super("JavaTris");

        //the JFrame/Graphics Window size:
        WIDTH  = 1050;
        HEIGHT = 500;

        TIMER_DELAY = 1;  //SPEED of graphics, lower number = faster
        //(# of milliseconds between updates to graphics)
        
        //rand = new Random();

        game = new GameBoard(0, 0, null, WIDTH, HEIGHT);    
        controller = new Controller(game, this);

        //allows this.add 
        this.setLayout(new BorderLayout());
        this.add(game,  BorderLayout.CENTER);

        //controls :)
        this.addKeyListener(controller);

        //final setup
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));  
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }


    public void startTimer()
    {
        //test
        game.initPiece(controller.randomPiece(), 3, 20, game.xStep, game.yStep);
        
        t = new Timer(TIMER_DELAY ,this); 
        t.setActionCommand("timerFired");
        t.start();
        count = 0;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("timerFired"))  //timer has fired
        {
            controller.update(); //does all motion and checking and logic
            game.repaint();      //calls paintComponent to redraw everything
            count++;  
        }
    }

    public void updateAll()
    {
    }
}
