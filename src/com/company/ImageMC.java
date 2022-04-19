package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageMC {

    private String name;
    private boolean sigmoid;
    private NeuralNetwork model;
    DATASET Set;
    public ImageMC(String _name, int[] _size, boolean _sigmoid, String setLocation, int imageSize) throws FileNotFoundException {
        name = _name;
        sigmoid = _sigmoid;
        model = new NeuralNetwork(_size);
        Set = new DATASET(setLocation,imageSize);
    }
    public double runSimulation(int testPresent)
    {
        int right = 0;
        int wrong = 0;
        for(int i = 0; i < Set.getLength(); i++)
        {
            if(Data.randomNumber(0,100) < testPresent)
            {
                if(test(Set.getImage(i),Set.getImageResult(i)))
                    right++;
                else
                    wrong++;
            }
        }
        return (double) right / wrong;
    }
    public double calculateRightPresent()
    {
        int right = 0;
        int wrong = 0;
        for(int i = 0; i < Set.getLength(); i++)
        {
            if(test(Set.getImage(i),Set.getImageResult(i)))
                right++;
            else
                wrong++;
        }
        return Data.round((double)((100.0 / (right + wrong)) * right),3);
    }
    public double getCost()
    {
        double cost = 0;
        for(int i = 0; i < Set.getLength(); i++)
        {
            cost += getCost(i);
        }
        return cost / Set.getLength();
    }
    public double getCost(int in)
    {
        return getCost(model.calculateValue(Set.getImage(in),sigmoid),Set.getImageResult(in));
    }
    public double getCost(double[] score, int result)
    {
        double cost = 0;
        for(int i = 0; i < score.length; i++)
        {
            if(i == result)
                cost += Math.pow(score[i] - 1,2);
            else
                cost += Math.pow(score[i],2);
        }
        return cost;
    }
    public void trainGrid()
    {
        NeuralNetwork.backTrain d = model.getBackTrain();
        d.backpropagation(this,sigmoid);
    }
    public void testGrid()
    {
        NeuralNetwork.backTrain d = model.getBackTrain();
        d.testBack(this,sigmoid);
    }
    public DATASET getSet()
    {
        return Set;
    }

    public boolean test(int[] image, int result)
    {
        return Data.getBiggestLoc(getTestResult(image)) == result;
    }
    public double[] getTestResult(int[] image)
    {
        return model.calculateValue(image,sigmoid);
    }

    public void exportModel(String name, long startTime,boolean sigmoid) throws IOException {
        model.exportModel(name,startTime,0,"data base",0,runSimulation(100),sigmoid);
    }
    public void importModel(String name) throws IOException {
        model = new NeuralNetwork(model.importModel(name));
    }

    public NeuralNetwork getModel()
    {
        return model;
    }
}
