package com.company.multithreadings;

import com.company.processAlgorithms.ProcessImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;

public class ProcessThread extends ProcessThreadFunction {
    //Lista sincronizata intre thread-uri -> o folosesc pentru a face update la interfate de ProgressBar
    //se adauga pe rand task-urile terminate de thread-ul ProcessThread -> in acest proiect, acesta este thread-ul de tip consumer
    //se face update la bara de progres in functie de cat de mult am progresat
    public static List<Integer> tasksDone = Collections.synchronizedList(new ArrayList<>());

    private File outputFile;
    private boolean isBright;
    private int factor;
    private ProcessImage processImage;

    private BufferImage bufferImage;
    private byte[] processImageBuffer;
    private List<byte[]> imageFragment = new ArrayList<>();
    private byte[] finalProcessedImage;
    private int totalImageLength;

    private long startTime, endTime;
    private float elapsedTime;

    public ProcessThread(BufferImage bufferImage, File outputFile, boolean isBright, int factor) {
        super(factor, isBright);
        this.bufferImage = bufferImage;
        this.outputFile = outputFile;
        this.isBright = isBright;
        this.factor = factor;
    }

    //se foloseste un buffer sincronizat, de capacitate 1
    //este preluata fractiunea de biti citita de thread-ul producer
    //se adauga intr - lista de array , ce mai tarziu o sa ajunga un singur array de biti
    //dupa terminarea job-ului de consumer, acest thread mai are responsabilitatea de a mapa totii bitii procesati intr un singur arr
    //procesarea si modificarea pentru a obtine o noua imagine

    @Override
    public void run() {
        try {
            for (int i = 0; i < 4; i++) {
                startTime = System.currentTimeMillis();

                processImageBuffer = bufferImage.consumeFromBuffer();
                imageFragment.add(processImageBuffer);
                totalImageLength += processImageBuffer.length;


                Thread.sleep(1000);

                endTime = System.currentTimeMillis();
                elapsedTime = (float) (endTime - startTime) / 1000;

                System.out.print(Thread.currentThread().getName() + ": Consumed " + (i + 1) + "/4 from input file length. Execution time: ");
                System.out.printf("%.3f s\n", elapsedTime);
                tasksDone.add(tasksDone.size() + 1);
                MainWorkerThread.myBarrier.await();
            }

            Thread.sleep(1000);

            finalProcessedImage = mapImageFragments(totalImageLength, imageFragment);

            processImage = new ProcessImage(finalProcessedImage, outputFile);

            modifyImage(processImage);


        } catch (Exception e) {
            handleException(e, Thread.currentThread().getName());
        }
    }

    public BufferedImage getModifyImage() {
        return processImage.getModifyImage();
    }

    //functie care face handled la erori: fiind foarte multe tipuri de erori ce pot aparea, mai intai se verifica ce tip de eroare este
    //si se trateaza in functie de cum apare si de catre ce parte din cod este aruncata

    private void handleException(Exception e, String threadName) {
        if (e instanceof InterruptedException || e instanceof BrokenBarrierException) {
            System.out.println("Error at cyclic barrier for thread: " + threadName);
        } else if (e instanceof IOException) {
            System.out.println("Could not process image. Error occurred on : " + threadName);
        } else {
            e.printStackTrace();
        }
    }
}
