package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class NeuralNetwork {
    neuron[][] model;

    //controls
    final boolean SIGMOID_ON_CREATE = true;
    final int NEURON_CREATE_RANGE_BOTTOM = -10;
    final int NEURON_CREATE_RANGE_TOP = 10;
    final int BLOCK_FROM_ZERO_TO = 1;

    public NeuralNetwork(neuron[][] _model)
    {
        model = _model.clone();
    }
    public NeuralNetwork(int[] sizes)
    {
        model = new neuron[sizes.length][];
        for(int i = 0; i < sizes.length; i++)
        {
            model[i] = new neuron[sizes[i]];
        }
        for(int i = 0; i < sizes.length; i++)
        {
            for(int g = 0; g < sizes[i]; g++)
            {
                if(i == sizes.length - 1)
                    model[i][g] = new neuron(0,0);
                else
                    model[i][g] = new neuron(0,sizes[i + 1]);
            }
        }
    }

    public double[] calculateValue(int[] inputLayer, boolean sigmoid)
    {
        if(inputLayer.length != model[0].length) // check if the input layer is good
        {
            System.out.println("your input is in size " + inputLayer.length + " and the expected length is " + model[0].length);
            return new double[0];
        }
        for(int i = 0; i < inputLayer.length; i++) // copy the first layer
        {
            if(SIGMOID_ON_CREATE)
                model[0][i].setValue(Data.sigmoid(inputLayer[i]));
            else
                model[0][i].setValue(inputLayer[i]);
        }
        for(int i = 1; i < model.length; i++) // calculate the layers
        {
            calculateLayer(i,sigmoid);
        }
        double[] outPut = new double[model[model.length - 1].length];
        for(int i = 0; i < outPut.length; i++)
        {
            outPut[i] = model[model.length - 1][i].getValue();
        }
        return outPut;
    }
    private void calculateLayer(int layerNumber, boolean sigmoid)
    {
        for(int i = 0; i < model[layerNumber].length; i++)
        {
            double value = 0;
            for(int g = 0; g < model[layerNumber - 1].length; g++)
            {
                value += model[layerNumber - 1][g].getNextValue(i);
            }
            model[layerNumber][i].setValue((sigmoid)?Data.sigmoid(value):value);
        }
    }
    private double getNeuronValue(int layer, int index, boolean sigmoid)
    {
        for(int i = 1; i <= layer; i++)
        {
            calculateLayer(i,sigmoid);
        }
        return model[layer][index].getValue();
    }

    public void printModel()
    {
        for(int i = 0; i < model.length; i++)
        {
            for(int g = 0; g < model[i].length; g++)
            {
                System.out.print("[" + model[i][g].getValue() + "]");
            }
            System.out.println();
        }
    }
    //image train methods
    public backTrain getBackTrain()
    {
        return new backTrain();
    }
    public class backTrain
    {
       public backTrain()
       {

       }
        public void testBack(ImageMC mc, boolean sigmoid)
        {
            DATASET set = mc.getSet();
            final double[][][] startModel = recordModel();
            double errorCount = 0;
            final int limit = 1;
            System.out.println("calculate for " + limit);
            for(int a = 0; a < limit; a++)
            {
                final double startError = mc.getCost(a);
                final int result = set.getImageResult(a);
                for(int i = 0; i < model[model.length - 1].length; i++)
                {
                    changeWeights(new double[0][][],model.length - 1,i,i != result,1);
                }
                System.out.println("[" + a + "] = " + (startError - mc.getCost(a))  + "[" + mc.getCost(a) + "]");
                errorCount += startError - mc.getCost(a);
                printImageScoreReport(set,a,mc,sigmoid);
                setRecord(startModel);

            }
            System.out.println("the error down by " + errorCount / limit);
        }
        public void backpropagation(ImageMC mc, boolean sigmoid)
        {
            DATASET set = mc.getSet();
            System.out.println("global cost is " + Data.round(mc.getCost(),3) + " (start)");
            final int readSize = set.getImageCount();
            final int cutterSize = 20;
            final int runsSize = cutterSize * (readSize / cutterSize);

            double[][][][] multerRecorder = new double[cutterSize][model.length - 1][][];
            double[][][] multRecord  = recordModel();

            /* double[][][][] changesKeeper = new double[cutterSize][model.length - 1][][];
            /*[] = run
            [][] = layer
            [][][] neuron index
            [][][][] neron multer index
           /* for(int d1 = 0; d1 < cutterSize; d1++)
            {
                for(int d2 = 0; d2 < model.length - 1; d2++)
                {
                    changesKeeper[d1][d2] = new double[model[d2].length][model[d2 + 1].length];
                }
            }/**/


            for(int a = 0; a < runsSize; a++)
            {
                if(a != 0 && a % cutterSize == 0)
                {
                    System.out.print("start cutter calculation... (" + a / cutterSize + "\\" + runsSize / cutterSize + ")");
                    multRecord = calculateAverage(multerRecorder,multRecord.clone());
                    setRecord(multRecord);
                    if(cutterSize == 1)
                        System.out.print( " private = " + mc.getCost());
                    System.out.println(" global = " + mc.getCost());
                }
                //printImageScoreReport(set,a,mc,sigmoid);
                final int result = set.getImageResult(a);
                for(int i = 0; i < model[model.length - 1].length; i++)
                {
                    if(i != result)
                        changeWeights(/*changesKeeper[a % cutterSize]*/null, model.length - 1,i,true,1);
                }
                changeWeights(/*changesKeeper[a % cutterSize]*/null, model.length - 1,result,false,1);
                //reversRecorder(changesKeeper[a % cutterSize]);
                multerRecorder[a % cutterSize] = recordModel();
                // printImageScoreReport(set,a,mc,sigmoid);
                setRecord(multRecord);
            }
            calculateAverage(multerRecorder,multRecord.clone());
            setRecord(multRecord);
            System.out.println("\nglobal cost is " + Data.round(mc.getCost(),3) + " (end)");
        }
        private void changeWeights(double[][][] recorder,int nLayer,int nIndex, boolean down, int runNumber)
        {
            final boolean sigmoid = true;
            final int topNumbers = model[nLayer - 1].length / 4;
            if(nLayer != 1)
            {
                if(true)// change the brithness of the three strongest connectors
                {
                    double[] strengthList = new double[model[nLayer - 1].length];
                    int[] strengthListIndex = new int[strengthList.length];
                    int passCount = 1;
                    for(int i = 0; i < strengthList.length; i++)
                    {
                        strengthList[i] = -1;
                        neuron n = model[nLayer - 1][i];
                        final double value = n.getMulter(nIndex);
                        for(int g = 0; g < strengthList.length; g++)
                        {
                            if(strengthList[g] == value)
                            {
                                if(Data.onIn(2) || Data.onIn(3) || Data.onIn(8))
                                    passCount++;
                                break;
                            }
                        }
                        for(int g = 0; g < strengthList.length; g++)
                        {
                            if(strengthList[g] == -1)
                            {
                                strengthList[g] = value;
                                strengthListIndex[g] = i;
                                break;
                            }
                            else if(strengthList[g] > value || (strengthList[g] == value && Data.onIn(passCount)))
                            {
                                for(int h = i; h > g; h--)
                                {
                                    strengthList[h] = strengthList[h - 1];
                                    strengthListIndex[h] = strengthListIndex[h - 1];
                                }
                                strengthList[g] = value;
                                strengthListIndex[g] = i;
                                break;
                            }
                        }
                    }
                    for(int i = 0; i < topNumbers; i++)
                    {
                        if(strengthList[i] < 0)
                            changeWeights(recorder,nLayer - 1,strengthListIndex[i],!down,runNumber + 1);
                    }
                    for(int i = strengthList.length - 1; i >= strengthList.length - topNumbers; i--)
                    {
                        if(strengthList[i] > 0)
                            changeWeights(recorder,nLayer - 1,strengthListIndex[i],down,runNumber + 1);
                    }
                }
            }
            if(true) // change the strength of the three brutishness points
            {
                double[] brinesList = new double[model[nLayer - 1].length];
                int[] brinesListIndex = new int[brinesList.length];
                Arrays.fill(brinesList, -1);
                int passCount = 1;
                for(int i = 0; i < brinesList.length; i++)
                {
                    brinesList[i] = -1;
                    neuron n = model[nLayer - 1][i];
                    final double value = n.getValue();
                    for(int g = 0; g < brinesList.length; g++)
                    {
                        if(brinesList[g] == value)
                        {
                            if(Data.onIn(2) || Data.onIn(3) || Data.onIn(8))
                                passCount++;
                            break;
                        }
                    }
                    for(int g = 0; g < brinesList.length; g++)
                    {
                        if(brinesList[g] == -1)
                        {
                            brinesList[g] = value;
                            brinesListIndex[g] = i;
                            break;
                        }
                        else if(brinesList[g] > value || (brinesList[g] == value && Data.onIn(passCount)))
                        {
                            for(int h = i; h > g; h--)
                            {
                                brinesList[h] = brinesList[h - 1];
                                brinesListIndex[h] = brinesListIndex[h - 1];
                            }
                            brinesList[g] = value;
                            brinesListIndex[g] = i;
                            break;
                        }
                    }
                }
                /*for(int i = 0; i < brinesList.length; i++)
                {
                    System.out.println("[" + i + "][" + brinesListIndex[i] + "][" + brinesList[i] + "]");
                }*/
                for(int i = brinesList.length - 1; i >= brinesList.length - topNumbers; i--)
                {
                    neuron n = model[nLayer - 1][brinesListIndex[i]];
                    if(n.getValue() > 0.5)
                    {
                        final double startMulter = n.getMulter(nIndex);
                        if(down)
                            n.changeMulter(nIndex,-getChangeAmount(n.getValue(),n.getMulter(nIndex)));
                        else
                            n.changeMulter(nIndex,getChangeAmount(n.getValue(),n.getMulter(nIndex)));
                    }
                    //recorder[nLayer - 1][brinesListIndex[i]][nIndex] += (n.getMulter(nIndex) - startMulter);
                }
            }
        }
        private double getChangeAmount(double value, double strength)
        {
            return strengthSigmoid(Math.abs(strength) + value * Math.E);
        }
        private double strengthSigmoid(double num)
        {
            num = Math.abs(num);
            return (Math.E*num/(Math.E + num)) + 0.27;
        }
    /*public void gridTrain(ImageMC mc, boolean sigmoid)
    {
        final int accuracy = 100;
        final boolean safeMode = false;
        final double optLevel = 3;
        final double biosLevel = 0.00;
        boolean firstRun = true;
        for(int i = 1; i < model.length - 1; i++)
        {
            for(int g = 0; g < model[i].length; g++)
            {
                 System.out.println("[" + i  +"][" + g + "](" + mc.runSimulation(100) + ")");
                double changeAmount;
                neuron n = model[i][g];
                for(int h = 0; h < model[i][g].getMulterLength(); h++)
                {
                    double bestAcrracyUp = mc.runSimulation(accuracy);
                    double bestAcrracyDown = bestAcrracyUp;
                    final double startAcrracy = bestAcrracyUp;
                    // System.out.println("[" + i +"][" + g + "][" + h + "] (" + bestAcrracyUp + ") (" + Data.getTime() + ")");
                    double bestMulterUp = n.getMulter(h);
                    double bestMulterDown = n.getMulter(h);
                    final double startValue = n.getMulter(h);
                    boolean up = true;
                    changeAmount = 2;
                    while (true)
                    {
                        n.changeMulter(h,n.getMulter(h) + changeAmount);
                        final double acrracy = mc.runSimulation(accuracy);
                        //System.out.println(acrracy);
                        if(acrracy + biosLevel <= ((up)?bestAcrracyUp:bestAcrracyDown))
                        {
                            n.changeMulter(h,n.getMulter(h) - changeAmount);
                            changeAmount /= 10;
                            if(changeAmount == 0.2 | changeAmount == -0.2)
                                changeAmount /= 2;
                        }
                        else
                        {
                            if(up)
                            {
                                bestAcrracyUp = acrracy;
                                bestMulterUp = n.getMulter(h);
                            }
                            else
                            {
                                bestAcrracyDown = acrracy;
                                bestMulterDown = n.getMulter(h);
                            }
                        }
                        //System.out.println(changeAmount + " amount");
                        if(changeAmount <= Math.pow(10,-optLevel))
                        {
                            if(up)
                            {
                                n.changeMulter(h,startValue);
                                up = false;
                                changeAmount = -2;
                            }
                            else
                            {
                                break;
                            }
                        }
                    }
                    if(bestAcrracyUp > bestAcrracyDown)
                    {

                        n.changeMulter(h,bestMulterUp);
                    }
                    else
                    {
                        n.changeMulter(h,bestMulterDown);
                    }
                }
            }
            if(firstRun && i == model.length - 2)
            {
                firstRun = false;
                i = -1;
            }
        }
    }*/

        /*private void reversRecorder(double[][][] recorder)
        {
            for(int i = 0; i < recorder.length; i++) // the layer
            {
                for(int g = 0; g < model[i].length; g++) // the index
                {
                    for(int h = 0; h < model[i + 1].length; h++) // the multer
                    {
                        neuron n = model[i][g];
                        n.changeMulter(h,n.getMulter(h) + recorder[i][g][h]);
                        //System.out.println("[" + i + "][" + g + "][" + h + "] = " + recorder[i][g][h]);
                    }
                }
            }
        }*/
        private double[][][] recordModel()
        {
            double[][][] rec = new double[model.length - 1][][];
            for(int i = 0; i < model.length - 1; i++)
            {
                rec[i] = new double[model[i].length][model[i + 1].length];
                for(int g = 0; g < model[i].length; g++)
                {
                    for(int h = 0; h < model[i + 1].length; h++)
                    {
                        rec[i][g][h] = model[i][g].getMulter(h);
                    }
                }
            }
            return rec;
        }
        private void setRecord(double[][][] record)
        {
            for(int i = 0; i < record.length; i++)
            {
                for(int g = 0; g < record[i].length; g++)
                {
                    for(int h = 0; h < record[i][g].length; h++)
                    {
                        model[i][g].changeMulter(h,record[i][g][h]);
                    }
                }
            }
        }
        private double[][][] calculateAverage(double[][][][] recorder, double[][][] startModelClone)
        {
            final int count = recorder.length;
            double[][][] averageChange = new double[recorder[0].length][][];
            for(int d1 = 0; d1 < recorder[0].length; d1++)
            {
                averageChange[d1] = new double[model[d1].length][model[d1 + 1].length];
                for(int d2 = 0; d2 < recorder[0][d1].length; d2++)
                {
                    for(int d3 = 0; d3 < recorder[0][d1][d2].length; d3++)
                    {
                        double sum = 0;
                        for(int a = 0; a < count; a++)
                        {
                            sum += (recorder[a][d1][d2][d3] - startModelClone[d1][d2][d3]);
                        }
                        startModelClone[d1][d2][d3] = startModelClone[d1][d2][d3] + (sum / count);
                    }
                }
            }
            return startModelClone;
        }
        private void printImageScoreReport(DATASET set, int a, ImageMC mc, boolean sigmoid)
        {
            final int result = set.getImageResult(a);
            final double[] endScore = calculateValue(set.getImage(a),sigmoid);
            System.out.println("\n\ncost is " + Data.round(mc.getCost(endScore,result),3));
            for(int i = 0; i < endScore.length; i++) {if(i == result) System.out.print("{" + Data.round(endScore[i],3) + "}");else System.out.print("[" +  Data.round(endScore[i],3) + "]");}/**/
            System.out.println("(" + (Data.getBiggestLoc(endScore) == result?"R":"W") + ")");
        }
    }

    //game train Methods
    public void gridTrain(GameMC mc, int algorithmID, int accuracy, boolean sigmoid)
    {
        final boolean safeMode = false;
        final double optLevel = 3;
        final double biosLevel = 0.00;
        for(int i = 0; i < model.length - 1; i++)
        {
            for(int g = 0; g < model[i].length; g++)
            {
                // System.out.println("[" + i  +"][" + g + "]");
                double changeAmount;
                neuron n = model[i][g];
                for(int h = 0; h < model[i][g].getMulterLength(); h++)
                {
                    double bestAcrracyUp = mc.runSimulation(accuracy,algorithmID,sigmoid);
                    double bestAcrracyDown = bestAcrracyUp;
                    final double startAcrracy = bestAcrracyUp;
                    // System.out.println("[" + i +"][" + g + "][" + h + "] (" + bestAcrracyUp + ") (" + Data.getTime() + ")");
                    double bestMulterUp = n.getMulter(h);
                    double bestMulterDown = n.getMulter(h);
                    final double startValue = n.getMulter(h);
                    boolean up = true;
                    changeAmount = 2;
                    while (true)
                    {
                        n.changeMulter(h,n.getMulter(h) + changeAmount);
                        final double acrracy = mc.runSimulation(accuracy,algorithmID,sigmoid);
                        //System.out.println(acrracy);
                        if(acrracy + biosLevel <= ((up)?bestAcrracyUp:bestAcrracyDown))
                        {
                            n.changeMulter(h,n.getMulter(h) - changeAmount);
                            changeAmount /= 10;
                            if(changeAmount == 0.2 | changeAmount == -0.2)
                                changeAmount /= 2;
                        }
                        else
                        {
                            if(up)
                            {
                                bestAcrracyUp = acrracy;
                                bestMulterUp = n.getMulter(h);
                            }
                            else
                            {
                                bestAcrracyDown = acrracy;
                                bestMulterDown = n.getMulter(h);
                            }
                        }
                        //System.out.println(changeAmount + " amount");
                        if(changeAmount <= Math.pow(10,-optLevel))
                        {
                            if(up)
                            {
                                n.changeMulter(h,startValue);
                                up = false;
                                changeAmount = -2;
                            }
                            else
                            {
                                break;
                            }
                        }
                    }
                    double safeScore = (safeMode)?mc.runSimulation(accuracy,algorithmID,sigmoid):0;
                    if(bestAcrracyUp > bestAcrracyDown)
                    {

                        if(safeMode && safeScore < startAcrracy)
                        {
                            System.out.println("in [" + i + "][" + g + "][ " + h + "] there acrracy down (up)(" +safeScore + " < " + startAcrracy + ")");
                        }
                        else
                            n.changeMulter(h,bestMulterUp);
                    }
                    else
                    {
                        if(safeMode && mc.runSimulation(accuracy,algorithmID,sigmoid) < startAcrracy)
                        {
                            System.out.println("in [" + i + "][" + g + "][ " + h + "] there acrracy down (down)(" + safeMode + " < " + startAcrracy + ")");
                        }
                        else
                            n.changeMulter(h,bestMulterDown);
                    }
                }
            }
        }
    }




    public neuron[][] exportModelData(){
        return model;
    }
    public NeuralNetwork clone()
    {
        NeuralNetwork newDeep = new NeuralNetwork(model);
        return newDeep;
    }



    public void exportModel(String name, long startTime,int runs, String against, int runCounts, double acraccy, boolean sigmoid) throws IOException {
        String loc = Data.getFileLocaiton() + name;
        File folder = new File(loc);
        //System.out.println("export in D:\\programing\\Data\\javaMC\\" + name);
        if(folder.exists())
            folder.delete();
        folder.mkdir();
        neuron[][] modeler = model;
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i = 0; i < modeler.length - 1; i++)
        {
            stringBuilder.append("[\n");
            for(int g =0; g < modeler[i].length; g++)
            {
                stringBuilder.append("[\n");
                for(int h = 0; h < modeler[i + 1].length; h++)
                {
                    stringBuilder.append(modeler[i][g].getMulter(h));
                    stringBuilder.append(",");
                }
                stringBuilder.append("\n]\n");
            }
            stringBuilder.append("]\n");
        }
        File f = new File(loc + "\\model.txt");
        if(f.exists())
            f.delete();
        System.out.println(f.getPath());
        f.createNewFile();
        FileWriter writer = new FileWriter(f);
        writer.write(stringBuilder.toString());
        writer.close();
        File modelData = new File(loc + "\\data.txt");
        if(modelData.exists())
            modelData.delete();
        modelData.createNewFile();
        String dataText = "";
        dataText = modeler.length + "\n";
        for(int i = 0; i < modeler.length; i++)
        {
            dataText += modeler[i].length + "\n";
        }
        dataText += "sigmoid: " + sigmoid;
        FileWriter dataWrite = new FileWriter(modelData);
        dataWrite.write(dataText);
        dataWrite.close();
        modelData.setWritable(false);
        f.setWritable(false);
        File prop = new File(loc + "\\_prop.txt");
        if(prop.exists())
            prop.delete();
        prop.createNewFile();
        FileWriter propWrite = new FileWriter(prop);
        String propText = "MC vs " + against + "\nruns: " + runCounts + "\ntarget runs: " + runs + "\naccracy: " + acraccy + "\ntime: " + Data.getTimeDiffrent(startTime,System.currentTimeMillis()) + "\nupdate time: " + Data.getTime();
        propWrite.write(propText);
        propWrite.close();
        prop.setWritable(false);
        System.out.println("model exported successfully to " + loc);
    }
    public neuron[][] importModel(String name) throws IOException {
        String loc = Data.getFileLocaiton() + name;
        File data = new File(loc + "data.txt");
        Scanner dataScanner = new Scanner(data);
        final int layerCount = Integer.parseInt(dataScanner.nextLine());
        neuron[][] mod = new neuron[layerCount][];
        for(int i = 0; i < layerCount; i++)
        {
            mod[i] = new neuron[Integer.parseInt(dataScanner.nextLine())];

        }
        for(int i = 0; i < layerCount; i++)
        {
            for(int g = 0;g < mod[i].length; g++)
            {
                if(i == layerCount - 1)
                    mod[i][g] = new neuron(0,0);
                else
                    mod[i][g] = new neuron(0,mod[i + 1].length);
            }
        }
        File f = new File(loc + "model.txt");
        Scanner scanner = new Scanner(f);
        int level = 0;
        int layerLevel = 0;
        int locLevel = 0;
        while (true)
        {
            final String line = scanner.nextLine();
            if(line.equals("["))
                level++;
            else if(line.equals("]"))
            {
                if(level == 1)
                {
                    layerLevel++;
                    if(layerLevel == mod.length - 1)
                        break;
                    locLevel = 0;
                }
                else if(level == 2)
                    locLevel++;
                level--;
            }
            else
            {
                String number = "";
                int numberCount = 0;
                for(int g = 0; g < line.length(); g++)
                {
                    if(line.charAt(g) == ',')
                    {
                        // System.out.println("layer level = " + layerLevel + " loc level = " + locLevel);
                        mod[layerLevel][locLevel].changeMulter(numberCount,Double.parseDouble(number));
                        number = "";
                        numberCount++;
                    }
                    else
                        number += line.charAt(g);
                }
            }
        }

        System.out.println("the model " + name + " copied successfully");
        return mod;
    }
    public boolean getImportModelSigmoid(String name) throws FileNotFoundException {
        String loc = Data.getFileLocaiton() + name + "";
        File data = new File(loc + "\\data.txt");
        Scanner dataScanner = new Scanner(data);
        final int layerCount = Integer.parseInt(dataScanner.nextLine());
        neuron[][] mod = new neuron[layerCount][];
        for(int i = 0; i < layerCount; i++)
        {
            mod[i] = new neuron[Integer.parseInt(dataScanner.nextLine())];

        }
        try
        {
            String moid = dataScanner.nextLine();
            moid = moid.substring(9,moid.length());
            return moid.equals("true");
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public String printCalculaiton(int[] input, boolean sigmoid)
    {
        StringBuilder text = new StringBuilder("");
        if(input.length == model[0].length)
        {
            calculateValue(input,sigmoid);
            for(int i = 0; i < model.length; i++)
            {
                for(int g = 0; g < model[i].length; g++)
                {
                    text.append("[" + model[i][g].getValue() + "]");
                }
                text.append("\n");
            }
        }
        else
            text.append("print- your input layer is " + input.length + " and the expected size is " + model[0].length);
        return text.toString();
    }

    public String getGraphizModel(String name)
    {
        StringBuilder text = new StringBuilder("");
        for(int i = 0; i < model.length - 1; i++)
        {
            for(int g = 0; g < model[i].length; g++)
            {
                neuron n = model[i][g];
                for(int h = 0; h < n.getMulterLength(); h++)
                {
                    text.append("L" + i + "_" + g + "-> " + "L" + (i + 1) + "_" + h + "[style=bold,label=\"X" + n.getMulter(h) + "\",constrain=false];");
                    text.append("\n");
                }
            }
        }
        System.out.println(text.toString());
        return text.toString();
    }
    private class neuron
    {
        private double value;
        private double[] multers;
        public neuron(double _value, int _nextSize)
        {
            value = _value;
            multers = new double[_nextSize];
            for(int i = 0; i < _nextSize; i++)
            {
                multers[i] = Data.randomDoube(NEURON_CREATE_RANGE_BOTTOM,NEURON_CREATE_RANGE_TOP,BLOCK_FROM_ZERO_TO);
            }
        }
        public double getNextValue(int loc)
        {
            return value * multers[loc];
        }


        public void changeMulter(int in, double to)
        {
            multers[in] = to;
        }
        public double getMulter(int in)
        {
            return multers[in];
        }
        public int getMulterLength()
        {
            return multers.length;
        }
        public void setValue(double to)
        {
            value = to;
        }
        public double getValue()
        {
            return value;
        }

        public void read(boolean full)
        {
            System.out.println("value " + getValue() + " linkers count: " + multers.length);
            if(full)
            {
                for(int i = 0; i < multers.length; i++)
                {
                    System.out.println(i + ": " + multers[i]);
                }
            }
        }
    }
}
