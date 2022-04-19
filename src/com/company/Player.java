package com.company;

import com.company.XANDO.xnoGame;
import com.company.XANDO.xnoMC;
import com.company.XANDO.xnoMove;

import java.io.IOException;
import java.util.Scanner;

public class Player {
    public static void playXANDO(String importName) throws IOException {
        GameMC mc = new xnoMC(new int[]{9,9,9},true);
        mc.importModel(importName);
        final boolean sigmoid = mc.getImportSigmoid(importName);
        System.out.println("model a have simple acraccy of " + mc.runSimulation(100000,2,sigmoid) + "  (sigmoid is " + sigmoid + ")");
        Scanner scanner = new Scanner(System.in);
        boolean keep = true;
        Game g = new xnoGame();
        g.resetGame(1);
        final boolean userStart = g.getTurn() == -1;
        while (keep)
        {
            if(!userStart)
            {
                g.doMove(mc.getModelMove(g));
            }
            while (true)
            {
                g.printBoard();
                System.out.print("\nenter your move: ");
                int move = scanner.nextInt();
                if(move == -1) {keep = false; break;}
                if(g.doMove(new xnoMove(move)) != 0)
                    break;
                g.printBoard();
                if(g.doMove(mc.getModelMove(g)) != 0)
                    break;
            }
            g.printBoard();
            System.out.println("game over");
            g.resetGame(0);
        }
    }
}
