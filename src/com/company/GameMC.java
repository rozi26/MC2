package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

public class GameMC {
    final int start = Data.getGAMEMCSTARTER();

    private String name;
    private Game game;
    private NeuralNetwork model;
    private boolean sigmoid;
    public GameMC(String _name, Game _game, int[] _size, boolean _sigmoid)
    {
        model = new NeuralNetwork(_size);
        name = _name;
        game = _game;
        sigmoid = _sigmoid;
    }
    public double runSimulation(int runs, int algorithm, boolean sigmoid)
    {
        /*
        0 - model wins
        1 - opponent wins
        2 - draws
         */
        int[] results = new int[3];
        for(int i = 0; i < runs; i++)
        {
            results[runGame(algorithm,start)]++;
        }
       // for(int i = 0; i < 3; i++){System.out.println("result[" + i + "] = " + results[i]);}
        return (double) results[1] / results[0];
    }
    private int runGame(int algorithm, int starter)
    {
        game.resetGame(starter);
        for(int i = 0; i < 1000; i++)
        {
            int result;
            if(game.getTurn() == 1)
                result = game.doMove(getModelMove(game));
            else
                result = game.doMove(game.getAlgorithmMove(algorithm));
            if(result != 0)
            {
                if(result == -1)
                    return 0;
                return result;
            }
        }
        return 2;
    }


    public GameMove getModelMove(Game game)
    {
        System.out.println("error get model move from default");
        return new GameMove();
    }

    public void trainGrid(int algorithmID, int accuracy)
    {
        model.gridTrain(this,algorithmID,accuracy,sigmoid);
    }



    public void exportModel(String name, long startTime,int runs, int algorithmID, int runCounts, int simRuns, boolean sigmoid) throws IOException {
        model.exportModel(name,startTime,runs,Integer.toString(algorithmID),runCounts,runSimulation(simRuns,algorithmID,sigmoid),sigmoid);
    }
    public void importModel(String name) throws IOException {
        model = new NeuralNetwork(model.importModel(name));
    }
    public boolean getImportSigmoid(String name) throws FileNotFoundException {
        return model.getImportModelSigmoid(name);
    }
    public String getName()
    {
        return name;
    }
    protected NeuralNetwork getModel()
    {
        return model;
    }
    public boolean getSigmoid()
    {
        return sigmoid;
    }
}
