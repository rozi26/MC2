package com.company.XANDO;

import com.company.Data;
import com.company.Game;
import com.company.GameMove;
import com.company.GameMC;

public class xnoMC extends GameMC {

    public xnoMC(int[] size, boolean sigmoid)
    {
        super("xno",new xnoGame(), size, sigmoid);
    }

    public GameMove getModelMove(Game game)
    {
        final int[] board = game.getBoard();
        double[] results = super.getModel().calculateValue(board,super.getSigmoid());
        while (true)
        {
            int best = Data.getBiggestLoc(results);
            if(board[best] == 0)
                return new xnoMove(best);
            results[best] = -10000;
        }
    }
}
