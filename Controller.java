import javax.swing.JFrame;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Controller implements KeyListener 
{
    

    //CONTROL VARS

    int ARR, DAS; //shift speed and delay

    //softdrop implementation
    boolean softDropping;
    int sdReferenceTime;

    //slide after falling
    int slideStart;
    int slideMax;

    //tracking rotation presses
    boolean lRotPressed, rRotPressed;
    boolean lRotated, rRotated;

    int random;




    GameBoard game;
    GraphicsWindow window;
    
    public Controller (GameBoard game, GraphicsWindow window)
    {
        this.game = game;
        this.window = window;
        
        
        //CONTROL VARS
        //initializing stuff
        softDropping = false;
        lRotPressed = false;
        rRotPressed = false;
        lRotated = false;
        rRotated = false;
        slideStart = 0;

        //SETTINGS
        ARR = 10;
        DAS = 60;
        slideMax = 500;

        //KEYBINDS
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        //TODO DAS implementation: for now the autoshift is just the automatic repeating of a held key.
        if (key == KeyEvent.VK_COMMA)
            game.falling.shift(false);

        if (key == KeyEvent.VK_SLASH)
            game.falling.shift(true);

        //begin an instant softdrop
        if (key == KeyEvent.VK_PERIOD && !softDropping)
        {
            softDropping = true;
            sdReferenceTime = window.count;
        }

        //rotations
        if(key == KeyEvent.VK_Z)
        {
           lRotPressed = true;
        }

        if(key == KeyEvent.VK_X)
        {
            rRotPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        //softdrop stop
        if (key == KeyEvent.VK_PERIOD)
        {
            softDropping = false;
            sdReferenceTime = 0;
        }

        //left and right rotate logic
        if(key == KeyEvent.VK_Z)
        {
           lRotPressed = false;
        }

        if(key == KeyEvent.VK_X)
        {
            rRotPressed = false;
        }
    }


    private void vertCollide()
    {
        boolean collideTrigger = false;
        for (Cell cell : game.falling.cells) {
            if (cell.yPos == 1 || game.gameState[cell.xPos][cell.yPos-1] != null)
            {
                //this.game.nextPiece();
                game.falling.falling = false;
                collideTrigger = true;
            }
        }
        if (!collideTrigger) game.falling.falling = true;
        else if (slideStart == 0) slideStart = window.count;
    }

    public void update()
    {

            //initial vertical collision check
            vertCollide();
            //checking whether it slid out back into falling after slide duration
            if ((slideStart + slideMax) == window.count)
            {
                slideStart = 0; //specific value that collide can overwrite again (slide ends so now new one allowed)
                vertCollide(); //... by doing another collision check 
                if (!game.falling.falling)
                {
                    
                    this.game.savePiece();
                    this.game.falling = new Piece(3,20, game.xStep, game.yStep, randomPiece(), game); //new piece
                }
            }
            //theres potential to clean up above with recursion but too m u h c thinking required

        //performed every fall-tick
        //TODO replace 500 with variable speed setting dropdown
             //falling logic
             if(window.count % 500 == 0)
             {
                if (game.falling.falling)
                {
                    if (!softDropping)
                        this.game.falling.fall();
                }
                else
                {
                //    this.game.savePiece();
                //    this.game.falling = new Piece(3,20, game.xStep, game.yStep, randomPiece()); //new piece
                }
            }

        if (window.count % ARR == 0)
        {
            //TODO post-DAS repeated shift

            //softdrop
            if (softDropping && game.falling.falling)
            {
                this.game.falling.fall();
            }
        }

        //check each line to see if it should be cleared (inefficient, checks rows that can be logically ruled out)
        for(int row = 0; row<game.gameState[0].length; row++)
        {
            int cellCount = 0;
            for (int col = 0; col<game.gameState.length; col++)
            {
                if (game.gameState[col][row] != null) cellCount++;
                else break;
            }
            if (cellCount == game.gameState.length) 
                game.clearLine(row);
        }

    
        //rotation logic
        if (lRotPressed && !lRotated)
        {
            game.falling.lrRotate(false);
            lRotated = true;
        }
        
        if (!lRotPressed)
            lRotated = false;

        if (rRotPressed && !rRotated)
        {
            game.falling.lrRotate(true);
            rRotated = true;
        }

        if (!rRotPressed)
            rRotated = false;
    }

    //truly random piece (no bag)
    public char randomPiece()
    {
        
        char output = 'x';
        //random = ThreadLocalRandom.current().nextInt(1,7);
        //int random = ThreadLocalRandom.current().nextInt(1,7);
        random = (int) (Math.random() * 7);

        switch(this.random)
        {
            case 0: 
                output = 'I';
                break;
            case 1: 
                output = 'J';
                break;
            case 2: 
                output = 'L';
                break;
            case 3: 
                output = 'O';
                break;
            case 4:
                output = 'S';
                break;
            case 5: 
                output = 'T';
                break;
            case 6: 
                output = 'Z';
                break;
        }
        return output;
    }

    public char[] randomBag()
    {
        char[] output = new char[7];
        List<Character> possibilities = new ArrayList<>(Arrays.asList('I','J','L','O','S','T','Z'));//[];

        for (int i = 0; i < output.length; i++)
        {
            random = (int) (Math.random() * possibilities.size());
            output[i] = possibilities.get(random);
            possibilities.remove(random);
        }
        return output;
    }
}