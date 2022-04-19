package com.company;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class Data {
    public static double sigmoid(double num)
    {
        return 1 / (1 + Math.pow(Math.E,-num));
        // return num / (Math.E + Math.abs(num));
    }
    public static double[] sigmoid(double[] list)
    {
        double[] result = new double[list.length];
        for(int i = 0; i < result.length; i++)
        {
            result[i] = sigmoid(list[i]);
        }
        return result;
    }
    public static int getBiggestLoc(double[] list)
    {
        double biggest = list[0];
        int biggestLocation = 0;
        int passCount = 2;
        for(int i = 1; i < list.length; i++)
        {
            if(list[i] > biggest)
            {
                passCount = 2;
                biggest = list[i];
                biggestLocation = i;
            }
            else if(list[i] == biggest && onIn(passCount))
            {
                passCount++;
                biggestLocation = i;
            }
        }
        return biggestLocation;
    }

    static Random random = new Random();
    public static boolean onIn(int in)
    {
        return random.nextInt(in) == 0;
    }
    public static int randomNumber(int from, int to)
    {
        return random.nextInt(from,to);
    }
    public static double randomDoube(double from, double to, double blocker)
    {
        if(from == to)
            return from;
        if(blocker != 0)
            return Data.onIn(2)?random.nextDouble(blocker,to):-random.nextDouble(blocker,-from);
        return random.nextDouble(from,to);
    }
    static String getTimeDiffrent(long start, long end) {
        String time = "";
        long diffrent = end - start;
        if(diffrent >= 31557600000l)
        {
            time += (diffrent / 31557600000l) + " years, ";
            diffrent %= 31557600000l;
        }
        if (diffrent >= 86400000)
        {
            time += (diffrent / 86400000) + " days, ";
            diffrent %= 86400000;
        }
        if(diffrent >= 3600000)
        {
            time += (diffrent / 3600000) + " hours, ";
            diffrent %= 3600000;
        }
        if(diffrent >= 60000)
        {
            time += (diffrent / 60000) + " minutes, ";
            diffrent %= 60000;
        }
        if(diffrent >= 1000)
        {
            time += (diffrent / 1000) + " seconds, ";
            diffrent %= 1000;
        }
        time += diffrent + " milliseconds";
        return time;
    }

    public static String getTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).toString();
    }
    public static String getTime(long after)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now().plus(after, ChronoUnit.MILLIS);
        return dtf.format(now).toString();
    }
    public static int[] removeLast(int[] list)
    {
        int[] arr = new int[list.length - 1];
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = list[i];
        }
        return arr;
    }

    public static void printImage(int[] pixels)
    {
        for(int i =0; i < 28; i++)
        {
            for(int g = 0; g < 28; g++)
            {
                System.out.print((pixels[i * 28 + g] > 127)?"\220":"\176");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }



    //chess Move
    public static char[] doMove(int[] tools, int loc)
    {
        return switch (tools[loc]) {
            case 1 -> whitePawnMove(tools, loc);
            case -1 -> blackPawnMove(tools, loc);
            case 4, -4 -> hoursMove(tools, loc);
            case 5, -5 -> bisMove(tools, loc);
            case 8, -8 -> towerMove(tools, loc);
            case 15, -15 -> queenMove(tools, loc);
            case 1000, -1000 -> kingMove(tools, loc);
            default -> new char[64];
        };
    }
    public static char[] doMove(char[] tools, int loc)
    {
        return doMove(charToolsToInt(tools),loc);
    }
    public static int[] charToolsToInt(char[] toolsChar)
    {
        int[] tools = new int[64];
        for(int i = 0; i < 64; i++)
        {
            tools[i] = charToInt(toolsChar[i]);
        }
        return tools;
    }
    private static int charToInt(char a)
    {
        return switch (a) {
            case 'a' -> 1;
            case 'b' -> 4;
            case 'c' -> 5;
            case 'd' -> 8;
            case 'e' -> 15;
            case 'f' -> 1000;
            case 'g' -> -1;
            case 'h' -> -4;
            case 'i' -> -5;
            case 'j' -> -8;
            case 'k' -> -15;
            case 'l' -> -1000;
            default -> 0;
        };
    }
    private static char intToChar(int a)
    {
        if(a == 0)
            return 'n';
        for(int i = 97; i < 109; i++)
        {
            if(charToInt((char)i) == a)
                return (char) i;
        }
        return 'E';
    }
    private static boolean up;
    private static boolean down;
    private static boolean left;
    private static boolean right;
    private static void setDor(char[] colors, int loc)
    {
        left = loc % 8 == 0;
        right = loc % 8 == 7;
        up = loc < 8;
        down = loc > 55;
        for(int i = 0; i < 64; i++)
        {
            colors[i] = 'n';
        }
    }
    public static char[] whitePawnMove(int[] tools,int loc)
    {
        char[] colors = new char[64];
        setDor(colors, loc);
        if(!up && tools[loc - 8] == 0)
        {
            colors[loc - 8] = 'y';
            if(loc < 56 && loc >= 48 && tools[loc - 16] == 0)
                colors[loc - 16] = 'y';
        }
        if(!right && !up && isBlack(tools[loc - 7]))
            colors[loc - 7] = 'g';
        else if(!right && !up && isWhite(tools[loc - 7]))
            colors[loc - 7] = 'o';
        if(!left && !up && isBlack(tools[loc - 9]))
            colors[loc - 9] = 'g';
        else if(!left && !up && isWhite(tools[loc - 9]))
            colors[loc - 9] = 'o';
        return colors;
    }
    public static char[] blackPawnMove(int[] tools, int loc)
    {
        char[] colors = new char[64];
        setDor(colors, loc);
        if(!down && tools[loc + 8] == 0)
        {
            colors[loc + 8] = 'y';
            if(loc < 16 && loc >= 8 && tools[loc + 16] == 0)
                colors[loc + 16] = 'y';
        }
        if(!right && !down && isBlack(tools[loc + 9]))
            colors[loc + 9] = 'g';
        else if(!right && !down && tools[loc + 9] != 0)
            colors[loc + 9] = 'o';
        if(!left && !down && isBlack(tools[loc + 7]))
            colors[loc + 7] = 'g';
        else if(!left && !down && tools[loc + 7] != 0)
            colors[loc + 7] = 'o';
        return colors;
    }
    public static char[] hoursMove(int[] tools, int loc)
    {
        char[] colors = new char[64];
        setDor(colors, loc);
        if(loc > 16 && !left)
        {
            if(tools[loc - 17] == 0)
                colors[loc - 17] = 'y';
            else if(isBlack(tools[loc - 17]))
                colors[loc - 17] = 'g';
            else
                colors[loc - 17] = 'o';
        }
        if(loc > 15 && !right)
        {
            if(tools[loc - 15] == 0)
                colors[loc - 15] = 'y';
            else if(isBlack(tools[loc - 15]))
                colors[loc - 15] = 'g';
            else
                colors[loc - 15] = 'o';
        }
        if(!(loc % 8 == 0 || loc % 8 == 1) && !up)
        {
            if(tools[loc - 10] == 0)
                colors[loc - 10] = 'y';
            else if(isBlack(tools[loc - 10]))
                colors[loc - 10] = 'g';
            else
                colors[loc - 10] = 'o';
        }
        if(!(loc % 8 == 6 || loc % 8 == 7) && !up)
        {
            if(tools[loc - 6] == 0)
                colors[loc - 6] = 'y';
            else if(isBlack(tools[loc - 6]))
                colors[loc - 6] = 'g';
            else
                colors[loc - 6] = 'o';
        }
        if(!(loc % 8 == 0 || loc % 8 == 1) && !down)
        {
            if(tools[loc + 6] == 0)
                colors[loc + 6] = 'y';
            else if(isBlack(tools[loc + 6]))
                colors[loc + 6] = 'g';
            else
                colors[loc + 6] = 'o';
        }
        if(!(loc % 8 == 6 || loc % 8 == 7) && !down)
        {
            if(tools[loc + 10] == 0)
                colors[loc + 10] = 'y';
            else if(isBlack(tools[loc + 10]))
                colors[loc + 10] = 'g';
            else
                colors[loc + 10] = 'o';
        }
        if(loc < 48 && !left)
        {
            if(tools[loc + 15] == 0)
                colors[loc + 15] = 'y';
            else if(isBlack(tools[loc + 15]))
                colors[loc + 15] = 'g';
            else
                colors[loc + 15] = 'o';
        }
        if(loc < 48 && !right)
        {
            if(tools[loc + 17] == 0)
                colors[loc + 17] = 'y';
            else if(isBlack(tools[loc + 17]))
                colors[loc + 17] = 'g';
            else
                colors[loc + 17] = 'o';
        }
        return colors;
    }
    public static char[] bisMove(int[] tools, int loc)
    {
        char[] colors = new char[64];
        setDor(colors, loc);
        for(int i = loc + 7; i < 64; i+= 7)
        {
            if(i % 8 == 7)
                break;
            else
            {
                if(isWhite(tools[i])) {
                    colors[i] = 'o';
                    break;
                }
                else if(isBlack(tools[i]))
                {
                    colors[i] = 'g';
                    break;
                }
                else
                    colors[i] = 'y';
            }
        }
        for(int i = loc + 9; i < 64; i+= 9)
        {
            if(i % 8 == 0)
                break;
            else
            {
                if(isWhite(tools[i])) {
                    colors[i] = 'o';
                    break;
                }
                else if(isBlack(tools[i]))
                {
                    colors[i] = 'g';
                    break;
                }
                else
                    colors[i] = 'y';
            }
        }
        for(int i = loc - 7; i >= 0; i-= 7)
        {
            if(i % 8 == 0)
                break;
            else
            {
                if(isWhite(tools[i])) {
                    colors[i] = 'o';
                    break;
                }
                else if(isBlack(tools[i]))
                {
                    colors[i] = 'g';
                    break;
                }
                else
                    colors[i] = 'y';
            }
        }
        for(int i = loc - 9; i >= 0; i-= 9)
        {
            if(i % 8 == 7)
                break;
            else
            {
                if(isWhite(tools[i])) {
                    colors[i] = 'o';
                    break;
                }
                else if(isBlack(tools[i]))
                {
                    colors[i] = 'g';
                    break;
                }
                else
                    colors[i] = 'y';
            }
        }
        return colors;
    }
    public static char[] towerMove(int[] tools, int loc)
    {
        char[] colors = new char[64];
        setDor(colors, loc);
        //System.out.println(loc);
        if(!up)
        {
            for(int i = loc - 8; i >= 0; i -= 8)
            {
                if(isWhite(tools[i])) {
                    colors[i] = 'o';
                    break;
                }
                else if(isBlack(tools[i]))
                {
                    colors[i] = 'g';
                    break;
                }
                else
                    colors[i] = 'y';
            }
        }
        if(!down)
        {
            for(int i = loc + 8; i < 64; i += 8)
            {
                if(isWhite(tools[i])) {
                    colors[i] = 'o';
                    break;
                }
                else if(isBlack(tools[i]))
                {
                    colors[i] = 'g';
                    break;
                }
                else
                    colors[i] = 'y';
            }
        }
        if(!right)
        {
            for(int i = loc + 1; i % 8 != 0; i++)
            {
                if(isWhite(tools[i])) {
                    colors[i] = 'o';
                    break;
                }
                else if(isBlack(tools[i]))
                {
                    colors[i] = 'g';
                    break;
                }
                else
                    colors[i] = 'y';
            }
        }
        if(!left)
        {
            for(int i = loc - 1; i % 8 != 7 && i != -1; i--) {
                if (isWhite(tools[i])) {
                    colors[i] = 'o';
                    break;
                } else if (isBlack(tools[i])) {
                    colors[i] = 'g';
                    break;
                } else
                    colors[i] = 'y';
            }
        }
        return colors;
    }
    public static char[] queenMove(int[] tools, int loc)
    {
        char[] colors = bisMove(tools, loc);
        char[] add = towerMove(tools, loc);
        for(int i = 0; i < 64; i++)
        {
            if(add[i] != 'n')
                colors[i] = add[i];
        }
        return colors;
    }
    public static char[] kingMove(int[] tools, int loc)
    {
        char[] colors = new char[64];
        setDor(colors, loc);
        if(!up)
        {
            if(tools[loc - 8] == 0)
                colors[loc - 8] = 'y';
            else if(isBlack(tools[loc - 8]))
                colors[loc - 8] = 'g';
            else if(tools[loc] - 8 != 'n')
                colors[loc - 8] = 'o';
        }
        if(!right)
        {
            if(tools[loc + 1] == 0)
                colors[loc + 1] = 'y';
            else if(isBlack(tools[loc + 1]))
                colors[loc + 1] = 'g';
            else if(tools[loc] + 1 != 'n')
                colors[loc + 1] = 'o';
            if(!down)
            {
                if(tools[loc + 9] == 0)
                    colors[loc + 9] = 'y';
                else if(isBlack(tools[loc + 9]))
                    colors[loc + 9] = 'g';
                else if(tools[loc] + 9 != 'n')
                    colors[loc + 9] = 'o';
            }
            if(!up)
            {
                if(tools[loc - 7] == 0)
                    colors[loc - 7] = 'y';
                else if(isBlack(tools[loc - 7]))
                    colors[loc - 7] = 'g';
                else if(tools[loc] - 7 != 'n')
                    colors[loc - 7] = 'o';
            }
        }
        if(!down)
        {
            if(tools[loc + 8] == 0)
                colors[loc + 8] = 'y';
            else if(isBlack(tools[loc + 8]))
                colors[loc + 8] = 'g';
            else if(tools[loc] + 8 != 'n')
                colors[loc + 8] = 'o';
        }
        if(!left)
        {
            if(tools[loc - 1] == 0)
                colors[loc - 1] = 'y';
            else if(isBlack(tools[loc - 1]))
                colors[loc - 1] = 'g';
            else if(tools[loc] - 1 != 'n')
                colors[loc - 1] = 'o';
            if(!down)
            {
                if(tools[loc + 7] == 0)
                    colors[loc + 7] = 'y';
                else if(isBlack(tools[loc + 7]))
                    colors[loc + 7] = 'g';
                else if(tools[loc] + 7 != 'n')
                    colors[loc + 7] = 'o';
            }
            if(!up)
            {
                if(tools[loc - 9] == 0)
                    colors[loc - 9] = 'y';
                else if(isBlack(tools[loc - 9]))
                    colors[loc - 9] = 'g';
                else if(tools[loc] - 9 != 'n')
                    colors[loc - 9] = 'o';
            }
        }
        return colors;
    }

    public static boolean isWhite(int tool)
    {
        return tool > 0;
    }
    public static boolean isBlack(int tool)
    {
        return tool < 0;
    }
    public static boolean isWhite(char tool)
    {
        return tool == 'a' || tool == 'b' || tool =='c' || tool =='d' || tool =='e' || tool=='f';
    }
    public static boolean isBlack(char tool)
    {
        return tool != 'n' && !isWhite(tool);
    }


    public static ImageIcon getImage(char kind, char color)
    {
        if(color == 'o') color = 'r';
        switch (kind)
        {
            case 'n': return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\" + color + ".png");
            case 'a':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\whitesol" + color + ".png");
            case 'b':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\whitehorse" + color + ".png");
            case 'c':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\whitebis" + color + ".png");
            case 'd':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\whiteHook" + color + ".png");
            case 'e':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\whitequeen" + color + ".png");
            case 'f':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\whiteking" + color + ".png");
            case 'g':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\blacksol" + color + ".png");
            case 'h':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\blackhorse" + color + ".png");
            case 'i':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\blackbis" + color + ".png");
            case 'j':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\blackhook" + color + ".png");
            case 'k':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\blackqueen" + color + ".png");
            case 'l':  return new ImageIcon("D:\\programing\\back\\java\\android\\chess\\chess tools\\chess.com packge\\chessExtaned\\blackking" + color + ".png");
        }
        return null;
    }
    public static int locOpisade(int num)
    {
        return (7 - (num / 8)) * 8 + (num % 8);
    }
    public static void toolReverser(char[] tools)
    {
        char[] newTools = new char[64];
        for(int i = 0; i < 64; i++)
        {
            newTools[i] = tools[locOpisade(i)];
            if(newTools[i] < 103)
                newTools[i] = (char)((int)(newTools[i]) + 6);
            else if(newTools[i] != 'n')
                newTools[i] = (char)((int)(newTools[i]) - 6);
        }
        System.arraycopy(newTools, 0, tools, 0, 64);
    }
    public static void toolReverser(int[] tools)
    {
        int[] newTools = new int[64];
        for(int i = 0; i < 64; i++)
        {
            newTools[i] = tools[locOpisade(i)];
            newTools[i] = -newTools[i];
        }
        System.arraycopy(newTools, 0, tools, 0, 64);
    }


    public static double getAcrracyStandardDeviation(GameMC mc, int algoritem, boolean sigmoid)
    {
        final int depth = 9;
        final int testAcrracy = 1000;
        final long startTime = System.currentTimeMillis();
        if(true) // get expected time
        {
            final int expectedTimeConstentAcrracy = 1000;
            mc.runSimulation(expectedTimeConstentAcrracy,algoritem,sigmoid);
            final long expectedTimeConstent = System.currentTimeMillis() - startTime;
            long expectedTime = 0;
            for(int i = 10; i < Math.pow(10,depth); i *= 10)
            {
                expectedTime += (long)(expectedTimeConstent / ((double)expectedTimeConstentAcrracy / i)) * testAcrracy;
                System.out.println("depth " + getNumberLength(i) + " (" + i + ") will take " + getTimeDiffrent(0,expectedTime) + " run time is (" + Data.getTimeDiffrent(0,(long)(expectedTimeConstent / ((double)expectedTimeConstentAcrracy / i))) + ")");
            }
            //System.out.println("the expected time is " + Data.getTimeDiffrent(0,expectedTime));
        }
        System.out.println("input mc: " + mc.getName() + ", algoritem: \"" + algoritem + "\", sigmoid: " + sigmoid);
        System.out.println("start stander deviation test. depth = " + depth + ", test acrracy = " + testAcrracy);
        for(int i = 10; i <= Math.pow(10,depth); i *= 10)
        {
            getAcrracyStandardDeviation(mc,algoritem,sigmoid,i,testAcrracy);
        }
        return 0;
    }
    public static void getAcrracyStandardDeviation(GameMC mc, int algoritem, boolean sigmoid, int checkCount, int testAcrracy)
    {
        double best = mc.runSimulation(checkCount,algoritem,sigmoid);
        double worst = best;
        final long startTime = System.currentTimeMillis();
        for(int g = 0; g < testAcrracy; g++)
        {
            double a = mc.runSimulation(checkCount,algoritem,sigmoid);
            best = Math.max(best,a);
            worst = Math.min(worst,a);
        }
        System.out.println("when checkCount is " + checkCount + " the standard deviation range is " + (best - worst) + " (best: " + best + ", worst: " + worst + ") one run time is (" + Data.getTimeDiffrent(startTime / testAcrracy,System.currentTimeMillis() / testAcrracy) + ")");
    }
    private static int getNumberLength(int num)
    {
        if(num == 0)
            return 0;
        return getNumberLength(num / 10) + 1;
    }

    public static String getFileLocaiton()
    {
        return "D:\\programing\\Data\\javaMC\\";
    }
    public static double round(double num, int after)
    {
       return ((int)(num * Math.pow(10,after))) / Math.pow(10,after);
    }
    /*public static String moveToCode(chessMC.move m)
    {
        int become = 0;
        int form = m.getFrom();
        int to = m.getTo();
        char eat = intToChar(m.getEat());
        char _from = (char)((form + 64) + ((become > 2)?0:become * 64));
        char _to = (char)((to + 64) + ((become > 2)?(become - 2) * 64:0));
        char _eat = (char)((int)(eat));
        // System.out.println("send report of from = " + _from + " to = " + _to + " eat = " + _eat);
        return Character.toString(_from) + _to + _eat;
    }*/
}
