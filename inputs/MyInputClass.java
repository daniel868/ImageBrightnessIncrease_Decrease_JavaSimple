package com.company.inputs;

import java.io.FileNotFoundException;
import java.util.Scanner;

//clasa prin care programul comunica cu utilizatorul
//toate mesajele de input si output au handle facut in aceaasta clasa
//se foloseste un scanner pentru a capta datele introduse de la tastatura
//clasa care extine clasa de tip InputFunction

public class MyInputClass extends InputFunction {
    private static final String useApp = "Select how to use application: \n" +
            "0: Without User Interface\n1: With User Interface";
    private static final String inputMessage = "Insert full path for the input image file";
    private static final String outputMessage = "Insert full path for the output image file";
    private static final String brightnessTypeMessage = """
            Choose the operation you want to process:
            0: \tLower the brightness \s
            1: \tIncrease the brightness""";
    private static final String factorMessage = "Type a number between 50 - 100 to increase or decrease the brightness";

    private Scanner myScanner;

    public MyInputClass(Scanner myScanner) {
        this.myScanner = myScanner;
        System.out.println(useApp);
    }

    public MyInputClass() {
    }

    //se construieste un obiect de tip Input
    //se citessc datele din command line si se prelucreaza in acelasi timp corectitudinea datelor introduse
    //sunt folosite functiile din InputFunction pentru validarea datelor

    public Input readFromCommandLine() {
        Input input = new Input();

        System.out.println(inputMessage);
        myScanner.nextLine();
        try {
            String typedPath = myScanner.nextLine().trim();
            checkForInputFile(typedPath, input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(130);
        }

        System.out.println(outputMessage);
        String typedPath = myScanner.nextLine().trim();
        try {
            checkForOutputFile(typedPath, input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(130);
        }

        System.out.println(brightnessTypeMessage);
        int brightOption = myScanner.nextInt();
        if (brightOption != 0 && brightOption != 1) {
            System.out.println("Invalid option selected");
            System.exit(130);
        } else {
            input.setBright(brightOption == 1);
        }

        System.out.println(factorMessage);
        int factor = myScanner.nextInt();
        if (factor < 50 || factor > 100) {
            System.out.println("Invalid option selected");
            System.exit(130);
        } else {
            input.setFactor(factor);
        }

        return input;
    }

    //se construieste un obiect de tip Input
    //se citessc datele din interfata GUI si se prelucreaza in acelasi timp corectitudinea datelor introduse
    //sunt folosite functiile din InputFunction pentru validarea datelor
    public Input readFromUi(String inputFilePath, String outputFilePath, int brightFactor) {
        Input input = new Input();
        try {
            checkForInputFile(inputFilePath, input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(130);
        }
        try {
            checkForOutputFile(outputFilePath, input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(130);
        }

        if (brightFactor < 0) {
            input.setBright(false);
            input.setFactor(-(brightFactor));
        } else {
            input.setBright(true);
            input.setFactor(brightFactor);
        }
        return input;
    }
}
