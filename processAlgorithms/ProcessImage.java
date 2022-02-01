package com.company.processAlgorithms;

import java.io.File;
//clasa ce se afla la al treilea nivel de abstractizare pentru ramura de procesare a imaginii
//se ofera ca input un fisier in care se doreste scrierea imaginii, cat si un array de biti ce a fost
//procesat de catre combinatia de Thread-uri de tip Producer - Consumer

public class ProcessImage extends ImageAlgorithm {
    private File outputFile;

    public ProcessImage(byte[] inputImageByte, File outputFile) {
        super(inputImageByte);
        this.outputFile = outputFile;
    }

    //se implementeaza metoda de crestere a luminozitatii
    public void increaseBrightness(int brightnessFactor) throws Exception {
        modifyImage(outputFile, true, brightnessFactor);
    }

    //se implementeaza metoda de scadere a luminozitatii
    public void reduceBrightness(int brightnessFactor) throws Exception {
        modifyImage(outputFile, false, brightnessFactor);
    }

}
