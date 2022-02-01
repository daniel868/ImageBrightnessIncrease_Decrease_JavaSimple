package com.company;

import com.company.inputs.Input;
import com.company.inputs.MyInputClass;
import com.company.multithreadings.MainWorkerThread;
import com.company.ui.MyWindow;

import java.util.Scanner;
//clasa care se ocupa de pornirea aplicatiei
//aici se selecteaza modul de lucru (cu UI/parametrii in linie de comanda)
//se ofera datale catre thread-ul principal pentru lucrul cu parametrii din linie de comanda
//altfel se porneste interfata grafica
public class MyApp {
    private int appUse;
    private MainWorkerThread mainWorkerThread;
    private Input params;

    public MyApp() {
    }

    public void start() {
        Scanner inputScanner = new Scanner(System.in);
        MyInputClass myInputClass = new MyInputClass(inputScanner);
        appUse = inputScanner.nextInt();

        try {
            if (appUse != 0 && appUse != 1) {
                throw new Exception("Not selected 0 or 1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(130);
        }
        //daca se alege 0  => se folosesc parametrii din linie de comanda
        //se citesc parametrii din linie de comanda in functie de mesajele furnizate
        //sunt verificate datele, apoi sa de drumul la executia thread-ului
        if (appUse == 0) {
            params = myInputClass.readFromCommandLine();
            mainWorkerThread = new MainWorkerThread(params.getInputFilePath(), params.getOutputFilePath(),
                    params.isBright(), params.getFactor());
            mainWorkerThread.start();

            try {
                mainWorkerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            //daca s alege 1 => se porneste interfata de GUI
            MyWindow myWindow = new MyWindow();
            myWindow.setVisible(true);
        }
    }
}
