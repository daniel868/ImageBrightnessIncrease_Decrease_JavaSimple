package com.company.processAlgorithms;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

//clasa de extinde clasa AlgorithmUtils
//sunt folosite functiile pentru a mapa noii bitii care se vor forma
//nucleul central al aplicatie, deoarece aici se modifica luminozitatea imaginii

public abstract class ImageAlgorithm extends AlgorithmUtils {
    private long startTime, endTime;
    private float elapsedTime;

    private BufferedImage modifyImage;


    public ImageAlgorithm(byte[] inputImageByte) {
        super(inputImageByte);
    }

    //functie importanta ce modifica fiecare pixel dintr un vector de biti
    //este primit de la thread-urile care au avut de lucru cu citirea si maparea imaginii
    //se merge dupa 2 coordonate: x si y
    //pentru fiecare culoare a punctului de coordonate X,Y se adauga/scade factorul de luminozitate "brightness"
    //scaderea sau adaugarea acestui factor se realizeaza in functie de variabila de tip boolean isBright

    public void modifyImage(File outputFile, boolean isBright, int brightnessFactor) throws Exception {
        System.out.println(Thread.currentThread().getName() + ": Start processing image");
        startTime = System.currentTimeMillis();

        int currentWidth, currentHeight;
        int oldRed, oldGreen, oldBlue;
        int newRed, newGreen, newBlue;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.getInputImageByte());

        BufferedImage originalImage = ImageIO.read(byteArrayInputStream);

        modifyImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        currentHeight = originalImage.getHeight();
        currentWidth = originalImage.getWidth();

        for (int y = 0; y < currentHeight; y++) {
            for (int x = 0; x < currentWidth; x++) {

                Color c = new Color(originalImage.getRGB(x, y));
                if (isBright) {
                    oldRed = c.getRed() + brightnessFactor;
                    oldGreen = c.getGreen() + brightnessFactor;
                    oldBlue = c.getBlue() + brightnessFactor;
                } else {
                    oldRed = c.getRed() - brightnessFactor;
                    oldGreen = c.getGreen() - brightnessFactor;
                    oldBlue = c.getBlue() - brightnessFactor;
                }

                newRed = calculateNewValue(oldRed);
                newGreen = calculateNewValue(oldGreen);
                newBlue = calculateNewValue(oldBlue);

                modifyImage.setRGB(x, y, new Color(newRed, newGreen, newBlue).getRGB());
            }
        }
        endTime = System.currentTimeMillis();
        elapsedTime = (float) (endTime - startTime) / 1000;
        System.out.print(Thread.currentThread().getName() + ": Finished processing image. Elapsed time: ");
        System.out.printf("%.3f s\n", elapsedTime);

        writeIntoOutputFile(modifyImage, outputFile);
    }

    //metoda statica ce are ca output un obiect grafic de tip ImageIcon
    //functie folosita strict in cadrul interfatei de GUI
    //in momentul in care se doreste afisarea unei noi imagini in aplicatie, se apeleaza aceasta metoda cu parametrul necesar de Buffered Image
    //se foloseste minimizarea la dimenisuni mai mici (400X350) pentru a putea intra in fereasta de aplicatie deschisa
    //folosita pentru afisarea imagiii default, cat si imaginii procesate

    public static ImageIcon loadImageFromBuffer(BufferedImage bufferedImage) throws IOException {
        ImageIcon icon;
        Image resultImg;
        BufferedImage output;

        resultImg = bufferedImage.getScaledInstance(400, 350, Image.SCALE_DEFAULT);
        output = new BufferedImage(400, 350, BufferedImage.TYPE_INT_RGB);
        output.getGraphics().drawImage(resultImg, 0, 0, null);
        icon = new ImageIcon(output);

        return icon;
    }

    public BufferedImage getModifyImage() {
        return this.modifyImage;
    }

}
