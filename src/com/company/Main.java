package com.company;

import com.company.XANDO.xnoMC;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        System.out.println("hello world");
        final long startTime = System.currentTimeMillis();
        ImageMC mc = new ImageMC("hello",new int[]{28*28,16,16,10},true,"D:\\shit\\download\\mnist_784_csv.csv",28);
        System.out.println("start present = " + mc.calculateRightPresent());
        mc.trainGrid();
        //mc.testGrid();
        mc.exportModel("imageTest4",startTime,true);
        for(int i = 0; i < 2; i++)
        {
            final int result = mc.getSet().getImageResult(i);
            double[] score = mc.getTestResult(mc.getSet().getImage(i));
            System.out.print("\n[R: " + result + "][M: " + Data.getBiggestLoc(score) + "]");
            for(int g = 0; g < score.length; g++)
            {
                if(g == result)
                    System.out.print("{" + Data.round(score[g],3) + "}");
                else
                    System.out.print("[" + Data.round(score[g],3) + "]");
            }
        }
        System.out.println("\nend present = " + mc.calculateRightPresent());
    }


}
