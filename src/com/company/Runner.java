package com.company;

import com.company.XANDO.xnoMC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class Runner {
    public static void main(String[] args) throws IOException {
        ImageMC m = new ImageMC("number1",new int[]{(28*28),256,16,10},true, "D:\\shit\\download\\mnist_784_csv.csv",28);
        m.trainGrid();
    }
    public Runner()
    {

    }

    public void runGrid(GameMC mc, String exportName, String importName, int runs, int accuracy, int algorithmID, boolean risky, boolean sigmoid) throws IOException
    {
        File folder = new File(Data.getFileLocaiton() + exportName);
        if(!folder.exists())
            folder.mkdir();
        final long startTime = System.currentTimeMillis();
        int virsonUpdate = 0;
        if(!importName.equals(""))
        {
            mc.importModel(importName);
            virsonUpdate = getVirsionStart(exportName,importName);
            System.out.println("import virsion start with " + virsonUpdate);
        }
        System.out.println("start in " + Data.getTime());
        double prevAcraacy = mc.runSimulation(accuracy * 10,algorithmID,sigmoid);
        boolean canExport = true;
        System.out.println(algorithmID + " accuracy of " + prevAcraacy);
        int trainNumber = accuracy;
        for(int i = 0; i < runs; i++)
        {
            mc.trainGrid(algorithmID,trainNumber);
            if(risky)
            {
                mc.exportModel(exportName + "\\virsion " + virsonUpdate + "\\",startTime,runs,algorithmID,virsonUpdate + i,accuracy * 10,sigmoid);
                virsonUpdate++;
                System.out.println("finish run " + i + " acrracy = " + mc.runSimulation(10000,algorithmID,sigmoid) + " (" + Data.getTime() + ")");
            }
            else
            {
                double acraacy = mc.runSimulation(100000,algorithmID,sigmoid);
                if(acraacy < prevAcraacy)
                {
                    System.out.println("acraacy down resat model (" + acraacy + " < " + prevAcraacy + ") (" + trainNumber +")");
                    mc.importModel(exportName + "\\virsion " + (virsonUpdate - 1) + "\\");
                    trainNumber *= 2;
                }
                else
                {
                    trainNumber = accuracy;
                    prevAcraacy = acraacy;
                    mc.exportModel(exportName + "\\virsion " + virsonUpdate,startTime,runs,algorithmID,virsonUpdate + i,accuracy * 10   ,sigmoid);
                    virsonUpdate++;
                }
                if(Double.isInfinite(acraacy))
                {
                    System.out.println("canBeInfinity...");
                    final double checker = mc.runSimulation(10000000,algorithmID,sigmoid);
                    if(Double.isInfinite(acraacy))
                        System.out.println("infinity!!! (virsion " + (virsonUpdate + i - 1) + ")");
                    else
                        System.out.println("not infinity " + checker);
                }
                System.out.println("finish run " + i + " acrracy = " +acraacy + ")");
            }

        }
    }
    public void runImage(ImageMC mc, String exportName, String importName, int runs, boolean risky, boolean sigmoid) throws IOException {
        File folder = new File(Data.getFileLocaiton() + exportName);
        if(!folder.exists())
            folder.mkdir();
        final long startTime = System.currentTimeMillis();
        int virsonUpdate = 0;
        if(!importName.equals(""))
        {
            mc.importModel(importName);
            virsonUpdate = getVirsionStart(exportName,importName);
            System.out.println("import virsion start with " + virsonUpdate);
        }
        System.out.println("start in " + Data.getTime());
        double prevAcraacy = mc.runSimulation(100);
        boolean canExport = true;
        System.out.println("accuracy of " + prevAcraacy);
        for(int i = 0; i < runs; i++)
        {
            mc.trainGrid();
            if(risky)
            {
                mc.exportModel(exportName + "\\virsion " + virsonUpdate + "\\",startTime,sigmoid);
                virsonUpdate++;
                System.out.println("finish run " + i + " acrracy = " + mc.runSimulation(100) + " (" + Data.getTime() + ")");
            }
            else
            {
                double acraacy = mc.runSimulation(100);
                if(acraacy < prevAcraacy)
                {
                    System.out.println("acraacy down resat model (" + acraacy + " < " + prevAcraacy + ")");
                    mc.importModel(exportName + "\\virsion " + (virsonUpdate - 1) + "\\");
                }
                else
                {
                    prevAcraacy = acraacy;
                    mc.exportModel(exportName + "\\virsion " + virsonUpdate + "\\",startTime,sigmoid);
                    virsonUpdate++;
                }
                if(Double.isInfinite(acraacy))
                {
                    System.out.println("canBeInfinity...");
                    final double checker = mc.runSimulation(100);
                    if(Double.isInfinite(acraacy))
                        System.out.println("infinity!!! (virsion " + (virsonUpdate + i - 1) + ")");
                    else
                        System.out.println("not infinity " + checker);
                }
                System.out.println("finish run " + i + " acrracy = " +acraacy + ")");
            }

        }
    }


    private static int getVirsionStart(String name, String importName)
    {
        String importModel = "";
        for(int i = 0; i < importName.length(); i++)
        {
            if(importName.charAt(i) == '\\')
                break;
            else
                importModel += importName.charAt(i);
        }
        if(importModel.equals(name))
        {
            for(int i = 0; i < 100000; i++)
            {
                File f = new File(Data.getFileLocaiton() + name + "\\virsion " + i);
                if(!f.exists())
                    return i;
            }
            return -1;
        }
        return 0;
    }
}
