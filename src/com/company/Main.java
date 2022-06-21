package com.company;

import com.company.XANDO.xnoGame;
import com.company.XANDO.xnoMC;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here


        String exportModelTo = ""; //where to save to model (must be filled or set to the default location in the next line)
        //exportModelTo = Data.getDefaultSaveLocation(); System.out.println("export to \"" + exportModelTo + "\"");
        if(exportModelTo.isEmpty()) {System.out.println("\nyou must choose an export file (you can use the Data.getDefaultSaveLocation() to automatically generate location in your documents folder"); return;}
        //-------------------------------------------------------------------------------------------------------------------------------------
        Data.setGAMEMCSTARTER(1); //1 the machine always start -1, you always start, 0 random (0,-1 runs are requires longer run time to master the game)
        final String importModelFrom = ""; //if empty create new model
        final int runs = 20; //how many times to run the training algorithm
        final int accuracy = 500; // higher accuracy mean better results but higher running times
        final int algorithmID = 2; //1 is random, 2 is simple tic-tac-toe algorithm
        final boolean risky = false; //run from the result of newer training algorithm even if its worst
        final boolean sigmoid = false;//if the program should use sigmoid function on the training algorithm
        final int[] size = {9,9,9}; // the neural network's layers size (the first layer need to be in the same size as the input and the last same as the output)
        Runner runner = new Runner();
        xnoMC xno = new xnoMC(size,sigmoid);
        runner.runGrid(xno,exportModelTo,importModelFrom,runs,accuracy,algorithmID,risky,sigmoid);/**/
        System.out.println("\n\n");
        Scanner scanner = new Scanner(System.in); // play against the neural network
        while (true)
        {
            System.out.print("do you want to play against the machine?: ");
            if(Character.toLowerCase(scanner.nextLine().charAt(0)) != 'y') break;
            else
            {
                xnoGame game = new xnoGame();
                game.playAgainstModel(xno);
            }
        }
        System.out.println("\ndone.");
    }

}
