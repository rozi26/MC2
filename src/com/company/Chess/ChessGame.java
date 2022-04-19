package com.company.Chess;

import com.company.Data;
import com.company.Game;
import com.company.GameMove;

public class ChessGame extends Game {

    private int[] board;
    public ChessGame()
    {
        super(64);
        board = super.getBoard();
        resetGame(0);
    }
    public void resetGame(int to)
    {
        int[] orger = {8,4,5,15,1000,5,4,8};
        for(int i = 0; i < 8; i++)
        {
            board[i] = -orger[i];
            board[i + 8] = -1;
            board[i + 48] = 1;
            board[i + 56] = orger[i];
        }
        for(int i = 16; i < 48; i++)
        {
            board[i] = 0;
        }
        setTurn(to);
    }
    protected int doMove(GameMove _move)
    {
        ChessMove move = (ChessMove)_move;
        moveForward(move.getFrom(),move.getTo());
        super.switchTurn();
        return checkWin();
    }


    private void moveForward(int from, int to)
    {
        board[to] = board[from];
        board[from] = 0;
    }
    private void moveBackward(int from, int to, int eat)
    {
        board[from] = board[to];
        board[to] = eat;
    }
    public int checkWin()
    {
        //only king eat
        boolean thereWhiteKing = false;
        boolean thereBlackKing = false;
        for(int i = 0; i < 64; i++)
        {
            if(board[i] == 1000)
                thereWhiteKing = true;
            if(board[i] == -1000)
                thereBlackKing = true;
        }
        if(!thereWhiteKing)
            return -1;
        if(!thereBlackKing)
            return 1;
        return 0;
    }

    public GameMove getAlgorithmMove(int id)
    {
        if(super.getTurn() != 1)
            Data.toolReverser(board);
        GameMove move;
        if(id == 1)
        {
            move =  getBestMoveFromScore(1);
        }
        else
            move = null;
        if(super.getTurn() != 1) {
            ((ChessMove)move).opeLoc();
            Data.toolReverser(board);
        }
        return move;
    }


    private GameMove getBestMoveFromScore(int scoreId)
    {
        ChessMove[] moveList = getAllMoves(true);
        double[] scores = new double[moveList.length];
        for (int i = 0; i < moveList.length; i++) {
            ChessMove m = moveList[i];
            moveForward(m.getFrom(),m.getTo());
            scores[i] = getScore(scoreId);
            moveBackward(m.getFrom(),m.getTo(),m.getEat());
        }
       /* for(int i = 0; i < moveList.length; i++)
        {
            System.out.println("(" + board[moveList[i].getFrom()] + ") " + moveList[i].toString());
        }
        System.out.println();*/
        return moveList[Data.getBiggestLoc(scores)];
    }
    private double getScore(int id)
    {
        if(id == 1)
        {
            int score = 0;
            for(int i = 0; i < 64; i++)
            {
                score += board[i];
            }
            return score;
        }
        else
            return 0;
    }
    public ChessMove[] getAllMoves(boolean white)
    {
        ChessMove[] movesFake = new ChessMove[100];
        int count = 0;
        for(int i = 0; i < 64; i++)
        {
            if((white && board[i] > 0) || (!white && board[i] < 0))
            {
                final char[] moveChar = Data.doMove(board,i);
                for(int g = 0; g < 64; g++)
                {
                    if(moveChar[g] == 'y' || ((white && moveChar[g] == 'g') || (!white && moveChar[g] == 'o')))
                    {
                        movesFake[count] = new ChessMove(i,g,board[g]);
                        count++;
                    }
                }
            }
        }
        ChessMove[] realMoves = new ChessMove[count];
        for(int i = 0; i < count; i++)
        {
            realMoves[i] = movesFake[i];
        }
        return realMoves;
    }
}
