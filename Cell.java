import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Cell {
    boolean occupied;
    boolean falling;
    Color colorState; //0-6: one for each pidce color
    int spriteState;
    int xPos, yPos;
    int xStep, yStep;
    BufferedImage spriteSheet = null;
    BufferedImage cellSprite = null;




    public Cell(int x, int y, int w, int h, int pieceNum) 
    {
        this.occupied = true;
        this.falling = true;
        this.xPos = x;
        this.yPos = y;
        this.xStep = w;
        this.yStep = h;

        

        switch (pieceNum)
        { 
            case 0: 
                colorState = Color.CYAN;
                spriteState = 0;
                break;
            case 1: 
                colorState = Color.BLUE;
                spriteState = 1;
                break;
            case 2: 
                colorState = Color.ORANGE;
                spriteState = 2;
                break;
            case 3: 
                colorState = Color.YELLOW;
                spriteState = 3;
                break;
            case 4: 
                colorState = Color.GREEN;
                spriteState = 4;
                break;
            case 5: 
                colorState = Color.MAGENTA;
                spriteState = 5;
                break;
            case 6:
                colorState = Color.RED;
                spriteState = 6;
                break;
        }
        
        try {
            File skin = new File("puyoskin.png");
            spriteSheet = ImageIO.read(skin);
            cellSprite = spriteSheet.getSubimage(32*spriteState, 0, 32, 32);
        } catch (IOException e) {
        }
    }


    /**
     * overloading for compatibility before i incorporated color/texture
     */
    public Cell(int x, int y, int w, int h)
    {
        this(x,y,w,h,0);
    }


    public void draw(Graphics g)
    {
        if (spriteSheet == null)
        {
        g.setColor(colorState);
        g.fillRect(xPos*xStep, (24-yPos)*yStep, xStep, yStep);
        }
        else
        {
            g.drawImage(cellSprite, xPos*xStep, (24-yPos)*yStep, xStep, yStep, null);
        }
    }



}
