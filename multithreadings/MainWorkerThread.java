package com.company.multithreadings;

import java.io.File;
import java.util.concurrent.CyclicBarrier;

//clasa de tip thread ce coordoneaza thread-urile de tip producer-consumer
//sunt transmisi parametrii prin varargs si este facut cast pe clasa generica Object

public class MainWorkerThread extends Thread {
    //aceasta bariera are ca scop sincronizarea thread-urilo la nivelul de log-uri afisate ( pentru a nu se intercala )
    //deoarece stdout -ul poate sa nu fie sincronizat uneori, aceasta bariera foloseste acest lucru in momentul in care fiecare thread
    //de tip producer sau consumer ajunge la finalul fiecare iteratii, aceastea asteapta la bariera
    public final static CyclicBarrier myBarrier = new CyclicBarrier(2);

    private String inputFilePath, outputFilePath;
    private boolean isBright;
    private int factor;

    private ProcessThread processThread;
    private ReadThread readThread;


    public MainWorkerThread(Object... objects) {
        this.inputFilePath = (String) objects[0];
        this.outputFilePath = (String) objects[1];
        this.isBright = (boolean) objects[2];
        this.factor = (int) objects[3];
    }


    //aceasta functie creeaza thread-urile de tip producer/consumer, le porneste si le si opreste
    //tot in aceasta functie sunt dati si parametrii de in pentru fieacare thread
    @Override
    public void run() {
        File imageFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        BufferImage bufferImage = new BufferImage();
        readThread = new ReadThread(imageFile, bufferImage);
        readThread.setName("Consumer-Thread");
        processThread = new ProcessThread(bufferImage, outputFile, isBright, factor);
        processThread.setName("Producer-Thread");

        readThread.start();
        processThread.start();


        try {
            readThread.join();
            processThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ProcessThread getProcessThread() {
        return processThread;
    }

    public void setProcessThread(ProcessThread processThread) {
        this.processThread = processThread;
    }

    public ReadThread getReadThread() {
        return readThread;
    }

    public void setReadThread(ReadThread readThread) {
        this.readThread = readThread;
    }

}
