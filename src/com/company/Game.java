package com.company;

import javax.swing.*;

public class Game {

    /*
        general game
        MC plays as 1
     */
    private int[] board;
    private int turn;
    protected Game(int boardSize)
    {
        board = new int[boardSize];
    }
    public Game(int[] _board, int _turn)
    {
        board= _board.clone();
        turn = _turn;
    }
    protected void resetGame(int to)
    {
        System.out.println("error use default resetGame");
    }
    protected void setTurn(int to)
    {
        if(to == 0)
            turn = (Data.onIn(2)?1:-1);
        else
            turn = to;
    }

    protected int doMove(GameMove move)
    {
        System.out.println("error do move from default");
        return -2;
    }

    private int checkWin()
    {
        /*
        -2 - ERROR
        -1 - (-1) win
        0 - game not over
        1 - (1) win
        2 - draw
         */
        System.out.println("error default checkWin activate");
        return -2;
    }
    protected void switchTurn()
    {
        turn = -turn;
    }

    protected GameMove getAlgorithmMove(int id)
    {
        return new GameMove();
    }


    public void printBoard()
    {
        final int size = (int)Math.sqrt(board.length);
        for(int i = 0; i < size; i++)
        {
            for(int g = 0; g < size; g++)
            {
                System.out.print(board[i * size + g]);
                if(g != size - 1)
                    System.out.print("\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    public int[] getBoard()
    {
        return board;
    }
    public int getTurn()
    {
        return turn;
    }

    protected ImageIcon getImage(int kind)
    {
        System.out.println("error try to get image for default");
        return null;
    }
}
