public class Piece
{
    int rotation;
    int[][][] rotations;// = new int[4];
    int[][] position;
    Cell[] cells = new Cell[4];
    int xPos, yPos; //bottom left: a reference to determine cell coords from
    boolean falling;
    int xStep, yStep;
    int shapeID;
    GameBoard game;


   

    public Piece(int x, int y, int xStep, int yStep, char shape, GameBoard game)
    {
        this.rotation = 0;
        //this.position = null;
        this.xPos = x;
        this.yPos = y;
        this.falling = true;
        this.xStep = xStep;
        this.yStep = yStep;
        this.game = game;
        

        switch (shape)
        {
             /*
            Implimenting SRS rotationss: https://strategywiki.org/wiki/File:Tetris_rotation_super.png
            I want to store all possible rotations of a piece within its bounding box.
            A rotation algorithm is not really worth it if I want to comply with all of the standards of SRS.
             */
            //note to self int[rotation][row][column]
            case 'I':
                rotations = new int[][][]{{{0,0,0,0},{1,1,1,1},{0,0,0,0},{0,0,0,0}}, 
                                          {{0,0,1,0},{0,0,1,0},{0,0,1,0},{0,0,1,0}},
                                          {{0,0,0,0},{0,0,0,0},{1,1,1,1},{0,0,0,0}},
                                          {{0,1,0,0},{0,1,0,0},{0,1,0,0},{0,1,0,0}}};
                shapeID = 0;
                                          break;
            case 'J':
                rotations = new int[][][]{{{1,0,0},{1,1,1},{0,0,0}},
                                          {{0,1,1},{0,1,0},{0,1,0}},
                                          {{0,0,0},{1,1,1},{0,0,1}},
                                          {{0,1,0},{0,1,0},{1,1,0}}};
                shapeID = 1;
                                          break;
            case 'L': 
                rotations = new int[][][]{{{0,0,1},{1,1,1},{0,0,0}},
                                          {{0,1,0},{0,1,0},{0,1,1}},
                                          {{0,0,0},{1,1,1},{1,0,0}},
                                          {{1,1,0},{0,1,0},{0,1,0}}};
                shapeID = 2;
                                          break;
            case 'O': //entering it like in pic above, dunno if this is perminent 
                rotations = new int[][][]{{{0,1,1,0},{0,1,1,0},{0,0,0,0}},
                                          {{0,1,1,0},{0,1,1,0},{0,0,0,0}},
                                          {{0,1,1,0},{0,1,1,0},{0,0,0,0}},
                                          {{0,1,1,0},{0,1,1,0},{0,0,0,0}}};
                shapeID = 3;
                                          break;
            case 'S': 
                rotations = new int[][][]{{{0,1,1},{1,1,0},{0,0,0}},
                                          {{0,1,0},{0,1,1},{0,0,1}},
                                          {{0,0,0},{0,1,1},{1,1,0}},
                                          {{1,0,0},{1,1,0},{0,1,0}}};
                shapeID = 4;
                                          break;
            case 'T': 
                rotations = new int[][][]{{{0,1,0},{1,1,1},{0,0,0}},
                                          {{0,1,0},{0,1,1},{0,1,0}},
                                          {{0,0,0},{1,1,1},{0,1,0}},
                                          {{0,1,0},{1,1,0},{0,1,0}}};
                shapeID = 5;
                                          break;
            case 'Z':
                rotations = new int[][][]{{{1,1,0},{0,1,1},{0,0,0}},
                                          {{0,0,1},{0,1,1},{0,1,0}},
                                          {{0,0,0},{1,1,0},{0,1,1}},
                                          {{0,1,0},{1,1,0},{1,0,0}}};
                shapeID = 6;
        }
        //moves

        arrangeCells(rotation);
    }

    public void lrRotate(boolean right)
    {
        if (right)
        {
            if (rotation == 3)
                rotation = 0;
            else rotation++;
        }
        else
        {
            if (rotation == 0)
            rotation = 3;
            else rotation--;
        }
        arrangeCells(rotation);
    }


    public void arrangeCells(int rot)
    {
        position = rotations[rotation];
        int cellIndex = 0;
 
        for(int row = 0; row < position.length; row++)
        {
            for (int col = 0; col < position[row].length; col++)
            {
                if (position[row][col] == 1)
                {
                    //create a cell with coords and stuff
                    cells[cellIndex] = new Cell(xPos + col, yPos + position.length - 1 - row, this.xStep, this.yStep, shapeID); //magic y coordinate
                    cellIndex++;
                }
            }
        }
    }

    public void fall()
    {
        for (Cell cell : cells) {
            //perform checks on gameboard here later
            cell.yPos--;
        }
        this.yPos--;
    }


    //basic left/right movement with check to make sure it is not obstructed by a cell in the gameboard (or gameboard edge)
    public void shift(boolean direction)
    {
        Cell[][] board = game.gameState;
        boolean blocked = false;

        if (direction) //direction = true = right
        {
            for (Cell cell : cells) {
                // is it on the edge?
                if (cell.xPos == board.length-1)
                    blocked = true;
                    //if not, check to its side
                    else if (board[cell.xPos+1][cell.yPos] != null)
                        blocked = true;
            }
            if (!blocked)
            {
                for (Cell cell : cells) {
                    cell.xPos++;
                }
                this.xPos++;
            }
        }else{
            for (Cell cell : cells) {
                if (cell.xPos == 0)
                    blocked = true;
                    else if(board[cell.xPos-1][cell.yPos] != null)
                        blocked = true;
            }
            if (!blocked)
            {
                for (Cell cell : cells) {
                    cell.xPos--;
                }
                this.xPos--;
            }
        }
    }


    public void clearCells()
    {
        cells = new Cell[4];
    }
    //public void
}