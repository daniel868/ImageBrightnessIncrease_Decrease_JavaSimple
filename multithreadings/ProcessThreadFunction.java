package com.company.multithreadings;

import com.company.processAlgorithms.ProcessImage;

import java.util.List;

//clasa abstracta ce implementeaza clasa ImageThread
//sunt create implementari ale functiilor abstracte

public abstract class ProcessThreadFunction extends ImageThread {

    public ProcessThreadFunction(int factor, boolean isBright) {
        super(factor, isBright);
    }

    //in functie de parametrul isBright (true/false) se creeaza o noua imagine mai deschisa/inchisa
    //este injectata o clasa de tip ProcessImage, deoarece avem nevoie de functiile oferite de aceasta clasa


    @Override
    public void modifyImage(ProcessImage processImage) throws Exception {
        if (this.isBright) {
            processImage.increaseBrightness(this.factor);
        } else {
            processImage.reduceBrightness(this.factor);
        }

    }

    //in momentul finalizarii citirii de cate k biti, se reitereaza fiecare array de biti din aceasta lista
    //se creeaza un nou array de bit, ce contine toti acesti biti => imaginea citita pe bucati si reasamblata dupa citire

    @Override
    public byte[] mapImageFragments(int totalImageLength, List<byte[]> imageFragment) {
        byte[] processedImage = new byte[totalImageLength];
        int k = 0;
        for (byte[] currentFragment : imageFragment) {
            for (byte b : currentFragment) {
                processedImage[k++] = b;
            }
        }
        return processedImage;
    }
}
