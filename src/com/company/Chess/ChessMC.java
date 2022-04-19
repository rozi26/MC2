package com.company.Chess;

import com.company.Data;
import com.company.Game;
import com.company.GameMove;
import com.company.GameMC;

public class ChessMC extends GameMC {

    public ChessMC(int[] size, boolean sigmoid)
    {
        super("chess1",new ChessGame(),size,sigmoid);
    }

    public GameMove getModelMove(Game _game)
    {
        int[] board = _game.getBoard();
        ChessGame game = (ChessGame)_game;
        ChessMove[] moveList = game.getAllMoves(true);
        double[] scores = new double[moveList.length];
        for(int i = 0; i < moveList.length; i++)
        {
            ChessMove m = moveList[i];
            board[m.getTo()] = board[m.getFrom()];
            board[m.getFrom()] = 0;
            scores[i] = super.getModel().calculateValue(board,super.getSigmoid())[0];
            board[m.getFrom()] = board[m.getTo()];
            board[m.getTo()] = m.getEat();
        }
        return moveList[Data.getBiggestLoc(scores)];
    }

}
