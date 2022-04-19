package com.company;

import com.sun.jdi.ArrayReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.Arrays;
import java.util.Scanner;

public class DATASET{
    private int[][] images;
    private int size;
    public DATASET(String location, int _size) throws FileNotFoundException {
        size = _size;
        images = readSet(location,size);
    }
    private int[][] readSet(String location, int imageSize) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(location));
        scanner.useDelimiter(",");
        StringBuilder stringBuilder = new StringBuilder("");
        int count = 0;
        while (scanner.hasNext())
        {
            scanner.nextLine();
            count++;
        }
        int[][] data = new int[count][imageSize * imageSize + 1];
        scanner = new Scanner(new File(location));
        int lineCount = 0;
        while (scanner.hasNext())
        {
            final String line = scanner.nextLine();
            String number = "";
            int numberCount = 0;
            for(int g = 0; g < line.length(); g++)
            {
                if(line.charAt(g) == ',')
                {
                    data[lineCount][numberCount] = Integer.parseInt(number);
                    numberCount++;
                    number = "";
                }
                else
                    number += line.charAt(g);
            }
            data[lineCount][numberCount] = Integer.parseInt(number) - 128;
            lineCount++;
        }
        return data;
    }

    public int getLength()
    {
        return size * size;
    }
    public int getImageCount()
    {
        return images.length;
    }
    public int[] getImage(int in)
    {
        int[] newArray = new int[images[in].length - 1];
        System.arraycopy(images[in], 0, newArray, 0, newArray.length);
        return newArray;
    }
    public int getImageResult(int in)
    {
        return images[in][size * size]+ 128;
    }

    public void printImage(int in)
    {
        for(int i = 0; i < size; i++) {System.out.print("*");} System.out.println();
        for(int i = 0; i < size; i++)
        {
            System.out.print("*");
            for(int g = 0; g < size; g++)
            {
                if(images[in][i * size + g] > 0)
                    System.out.print("\u25A0");
                else
                    System.out.print(" ");
            }
            System.out.println("*");
        }
        for(int i = 0; i < size; i++) {System.out.print("*");}
        System.out.println("["  + getImageResult(in) + "]");
    }
}
