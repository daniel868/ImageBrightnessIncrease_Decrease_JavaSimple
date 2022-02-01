package com.company.processAlgorithms;

import com.company.multithreadings.ProcessThread;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

//clasa de baza de la care se pleaca pentru procesarea imaginii cerute
//implementeaza 2 functii ajutatoare ce sunt folosite de clasele care vor extinde aceasta clasa

public abstract class AlgorithmUtils {
    private byte[] inputImageByte;
    private long startTime, endTime;
    private float elapsedTime;

    public AlgorithmUtils(byte[] inputImageByte) {
        this.inputImageByte = inputImageByte;
    }

    //Functie care scrie continutul primit ca BufferedImage intr-un fisier de output
    //Se logheaza toate mesajele + durata acestora
    //Se scrie in fisierul dat ca parametru, folosind biblioteca ImageIO, si formatul de tip .bmp
    //Se adauga o noua intrare in lista sincronizata, intrare ce ne ajuta la modul de lucru al ProgressBar

    public void writeIntoOutputFile(BufferedImage modifiedImage, File outputFile) {
        try {
            startTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + ": Start writing the output file");
            ImageIO.write(modifiedImage, "bmp", outputFile);
            endTime = System.currentTimeMillis();
            elapsedTime = (float) (endTime - startTime) / 1000;
            System.out.print(Thread.currentThread().getName() + ": Finishing writing the output file. Execution time: ");
            System.out.printf("%.3f s\n", elapsedTime);

            ProcessThread.tasksDone.add(ProcessThread.tasksDone.size() + 1);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not write " + outputFile + " as output file");
        }
    }


    //Functie folosita pentru a calcula pixelii de tip RGB pentru noua imagine
    //Pentru a reda imaginea noua formata, se impun conditii legate de valorile pe care acestia le pot avea
    //Cu alte cuvinte intre 0->255 valori pentru spectrul RGB

    public int calculateNewValue(int currentValue) {
        int modifyValue = currentValue;
        if (currentValue >= 256) {
            modifyValue = 255;
        } else if (currentValue < 0) {
            modifyValue = 0;
        }
        return modifyValue;
    }


    public byte[] getInputImageByte() {
        return inputImageByte;
    }

    public void setInputImageByte(byte[] inputImageByte) {
        this.inputImageByte = inputImageByte;
    }
}
