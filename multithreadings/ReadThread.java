package com.company.multithreadings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

//thread care reprezinta modelul de Producer
//lucreaza in sinergie cu thread-ul ProcessThread sincronizate de un buffer si de o bariera

public class ReadThread extends Thread {
    private File inputImageFile;
    private byte[] outputImageBuffer;
    private BufferImage bufferImage;

    public ReadThread(File inputImageFile, BufferImage bufferImage) {
        this.inputImageFile = inputImageFile;
        this.bufferImage = bufferImage;
    }

    //functia citeste toata lungimea fisierului
    //o imparte in 4 parti , apoi introduce pe rand in bufferul sincronizat aceste date
    //se foloseste un start/end pentru a tine evidenta numarului de biti introdusi in noul array
    //se creaaza un nou arr de lungime (start-end) si se repeta procesul
    @Override
    public void run() {
        int start, end;
        long totalFileLength;
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(inputImageFile);
            totalFileLength = inputImageFile.length();

            for (int i = 0; i < 4; i++) {
                start = (int) (i * (double) totalFileLength / 4);
                end = (int) Math.min((i + 1) * (double) totalFileLength / 4, totalFileLength);
                outputImageBuffer = new byte[end - start];

                fileInputStream.read(outputImageBuffer, 0, end - start);
                bufferImage.insertIntoBuffer(outputImageBuffer);

                System.out.println(Thread.currentThread().getName() + ": Produced " + (i + 1) + "/4 from input file length");

                Thread.sleep(1000);

                MainWorkerThread.myBarrier.await();
            }

        } catch (Exception e) {
            handleException(e, Thread.currentThread().getName());
        }
    }

    //functie care face handled la erori: fiind foarte multe tipuri de erori ce pot aparea, mai intai se verifica ce tip de eroare este
    //si se trateaza in functie de cum apare si de catre ce parte din cod este aruncata
    private void handleException(Exception e, String threadName) {
        if (e instanceof FileNotFoundException) {
            System.out.println("Cannot found the input image file path at: " + threadName);
        } else if (e instanceof IOException) {
            System.out.println("Error reading the input fileL at: " + threadName);
        } else {
            e.printStackTrace();
        }
    }
}
