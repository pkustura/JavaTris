import java.awt.Dimension;
import java.lang.reflect.Array;
import java.util.Arrays;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel {
    Cell[][] gameState;
    int rows;
    int columns;
    int xStep, yStep;
    int vanishZone;
    int width, height;
    Piece falling;
    Font myFont;

    
    public GameBoard(int rows, int columns, Cell[][] startState, int w, int h)
    {
        //there is empty array space offscreen above vanishZone to prevent issues with offscreen stacking stuff (garbage/lines stacking from under later)
        vanishZone = 24;
        
        if (rows == 0 || columns == 0)
        {
        //stock tetris board
        this.rows = 40; //vanish zone above 24 
        this.columns = 10;
        }
        else
        {
            this.rows = rows;
            this.columns = columns;
        }
    
        setPreferredSize(new Dimension(w, h));
        width = w;
        height = h;

        this.myFont = new Font("Verdana", Font.BOLD, 16);  //nice font for display purposes

        if (startState == null)
        {
            //if we don't specify a gameboard, start it blank
            this.gameState = new Cell[this.columns][this.rows];
            for (int column = 0; column < gameState.length; column++)
                {
                for (int row = 0; row < gameState[column].length; row++)
                {
                    gameState[column][row] = null;
                }
            }
        }
        else 
        {
            //if a gameboard is provided, override rows and columns
            this.gameState = startState;
            this.rows = startState[0].length; //length of first row
            this.columns = startState.length; //amount of rows
        }


        //TODO write to max rect of aspect ratio columns:rows within size instead of this 
            //this.xStep = width/height * this.rows / this.columns;
        
        if (this.rows < vanishZone)
            this.yStep = h / this.rows;
        else
            this.yStep =  h / vanishZone;

            this.xStep = this.yStep;
        
    }

    public void initPiece(char shape, int x, int y, int xStep, int yStep)
    {
        this.falling = new Piece(x,y, xStep, yStep, shape, this);
    }


    @Override
    public void paintComponent(Graphics g)
    {
    super.paintComponent(g); //clears
    
    g.setFont(myFont);
    //g.drawString("y:" + falling.yPos, 500, 250);

    //drawing grid
    for (int col = 0; col < columns; col++)
    {
        for (int row = 0; row < vanishZone; row++)
        {
            int x = (int)xStep*col;
            int y = (int)yStep*row;

            g.drawRect(x, y, xStep, yStep);
        }
    }

    //drawing static (gamestate) cells
    for (Cell[] cells : gameState) {
        for (Cell cell : cells) {
            if (cell != null)
                cell.draw(g);
        }
    }

    //drawing falling cells
    for (Cell cell : this.falling.cells) {
        cell.draw(g);
        //g.fillRect(cell.xPos*xStep, cell.yPos*yStep, xStep, yStep);
    }
    }


    public void savePiece()
    {
        for (Cell cell : falling.cells) {
            gameState[cell.xPos][cell.yPos] = cell;
        }
    }

    public void clearLine(int line)
    {
        //gamestate[xPos: col#][yPos: row#]
        for (int row = line; row<gameState[0].length-1; row++)
        {
            /*    MINI GAMEBOARD
                  ROW    0 1 2 3   COL
                [][] = {{0,1,0,0}, 0
                        {1,1,1,1}, 1
                        {0,1,0,0}} 2
                        clearing is shifting cells of each col left in the inner arrays, overwriting cleared index
                */
        for (int col = 0; col < gameState.length; col++)
        {
            gameState[col][row] = gameState[col][row + 1];
            if (gameState[col][row] != null)
                gameState[col][row].yPos--;
            //gameState[col][row + 1] = null;
        }
        }
        for (int i = 0; i<gameState[0].length; i++)
        {
            //gameState[gameState.length-1][i] = null;
        }
    }
}

