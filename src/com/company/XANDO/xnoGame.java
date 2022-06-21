package com.company.XANDO;

import com.company.Data;
import com.company.Game;
import com.company.GameMove;

import javax.swing.*;
import java.util.Random;
import java.util.Scanner;

public class xnoGame extends Game {

    int[] board;
    public xnoGame()
    {
        super(9);
        board = super.getBoard();
        resetGame(0);
    }
    public void resetGame(int to)
    {
        for(int i = 0; i < 9; i++)
        {
            board[i] = 0;
        }
        super.setTurn(to);
    }
    private int checkWin()
    {
        for(int i = 0; i < 9; i+= 3)
        {
            if(board[i] != 0)
            {
                if(board[i] == board[i + 1] && board[i] == board[i + 2])
                    return -super.getTurn();
            }
            if(board[i / 3] != 0 && board[i / 3] == board[(i / 3) + 3] && board[i / 3] == board[(i / 3) + 6])
                return -super.getTurn();
        }
        if(board[4] != 0)
        {
            if(board[0] == board[4] && board[0] == board[8])
                return -super.getTurn();
            if(board[2] == board[4] && board[2] == board[6])
                return -super.getTurn();
        }
        for(int i = 0; i < 9; i++)
        {
            if(board[i] == 0)
                return 0;
        }
        return 2;
    }

    public int doMove(GameMove _move)
    {
        board[((xnoMove)_move).in] = super.getTurn();
        super.switchTurn();
        return checkWin();
    }
    protected ImageIcon getImage(int kind)
    {
        return new ImageIcon("D:\\programing\\images\\MC2\\xando\\b" + kind + ".png");
    }
    public GameMove getAlgorithmMove(int id)
    {
        return new xnoMove(getOpponentMove(id));
    }
    private int getOpponentMove(int id)
    {
        switch (id)
        {
            case 2:
                int color = super.getTurn();
                for(int g = 0; g < 2; g ++)
                {
                    if(g == 1)
                        color = -color;
                    for(int i = 0; i < 3; i++)
                    {
                        if(board[i * 3] == color && board[i * 3] == board[i * 3 + 1] && board[i * 3 + 2] == 0)
                            return i * 3 + 2;
                        if(board[i * 3] == color && board[i * 3] == board[i * 3 + 2] && board[i * 3 + 1] == 0)
                            return i * 3 + 1;
                        if(board[i * 3 + 1] == color && board[i * 3 + 1] == board[i * 3] && board[i * 3 + 2] == 0)
                            return i * 3;
                        if(board[i] == color && board[i] == board[i + 3] && board[i + 6] == 0)
                            return i + 6;
                        if(board[i] == color && board[i] == board[i + 6] && board[i + 3] == 0)
                            return i + 3;
                        if(board[i + 3] == color && board[i + 6] == board[i + 3] && board[i] == 0)
                            return i;
                    }
                    if(board[0] == color && board[0] == board[4] && board[8] == 0)
                        return 8;
                    if(board[0] == color && board[0] == board[8] && board[4] == 0)
                        return 4;
                    if(board[4] == color && board[8] == board[4] && board[0] == 0)
                        return 0;
                    if(board[2] == color && board[2] == board[4] && board[6] == 0)
                        return 6;
                    if(board[2] == color && board[2] == board[6] && board[4] == 0)
                        return 4;
                    if(board[4] == color && board[6] == board[4] && board[2] == 0)
                        return 2;
                }
                return getOpponentMove(1);
            default:
                Random random = new Random();
                while (true)
                {
                    int loc = random.nextInt(9);
                    if(board[loc] == 0)
                        return loc;
                }
        }
    }

    public void printBoard()
    {
        final int size = (int)Math.sqrt(board.length);
        for(int i = 0; i < size; i++)
        {
            for(int g = 0; g < size; g++)
            {
                System.out.print((board[i * size + g] == 0)?"-":(board[i * size + g] == 1)?"X":"O");
                if(g != size - 1)
                    System.out.print("\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    public void playAgainstModel(xnoMC x)
    {
        int turn = (Data.getGAMEMCSTARTER() == 0)?(Data.onIn(2)?1:-1):Data.getGAMEMCSTARTER();
        resetGame(turn);
        int lose;
        while((lose = checkWin()) == 0)
        {
            printBoard();
            if(turn == 1)
                doMove(x.getModelMove(this));
            else
            {
                while (true)
                {
                    System.out.print("enter your move: ");
                    Scanner scanner = new Scanner(System.in);
                    final int to = scanner.nextInt();
                    if(board[to] == 0)
                    {
                        doMove(new xnoMove(scanner.nextInt()));
                        break;
                    }
                    else
                        System.out.println("the square " + to + " is taken");
                }
            }
            turn = -turn;
        }
        System.out.println("\nGAME OVER:");
        printBoard();
        System.out.println(lose == 2?"the game ended with draw":((lose == -1)?"you won":"the machine won"));
    }

}
