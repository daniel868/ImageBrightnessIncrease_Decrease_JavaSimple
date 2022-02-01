package com.company.ui;

import com.company.inputs.Input;
import com.company.multithreadings.MainWorkerThread;
import com.company.multithreadings.ProcessThread;
import com.company.processAlgorithms.ImageAlgorithm;

import javax.swing.*;
import java.awt.image.BufferedImage;

//clasa de tip Thread, dar specifica pentru UI-ul din Java
//folosinta in momentul in care se face click pe butonul "start" din interfata principala
//este o implementare diferinta de Thread, deoarece prin aceasta implementare nu se blocheaza UI-ul cand se ruleaza ceva in background

public class UiThread extends SwingWorker<Void, Void> {
    private Input params;
    private MainWorkerThread mainWorkerThread;
    private JLabel imageLabel;
    private String outputFilePath;

    public UiThread(Input input, JLabel imageLabel, String outputFilePath) {
        this.params = input;
        this.imageLabel = imageLabel;
        this.outputFilePath = outputFilePath;
    }

    @Override
    protected Void doInBackground() throws Exception {
        //creem un nou thread de tip worker si il pornim
        mainWorkerThread = new MainWorkerThread(params.getInputFilePath(), params.getOutputFilePath(),
                params.isBright(), params.getFactor());

        mainWorkerThread.start();

        while (mainWorkerThread.isAlive()) {
            //actualizam progresul in functie de dimensiunea listei ce contine task-urile efectuate
            setProgress(20 * (ProcessThread.tasksDone.size()));
        }

        try {
            mainWorkerThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        //cand se termina de procesat informatia, se extrage din thread-ul worker informatia preluata
        //si se modifica imaginea default cu imaginea preluata
        if (20 * (ProcessThread.tasksDone.size()) == 100) {
            ImageIcon icon;

            BufferedImage bufferedImage = mainWorkerThread.getProcessThread().getModifyImage();
            icon = ImageAlgorithm.loadImageFromBuffer(bufferedImage);
            imageLabel.setIcon(icon);
        }
        return null;
    }

}
