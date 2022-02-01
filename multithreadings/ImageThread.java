package com.company.multithreadings;

import com.company.processAlgorithms.ProcessImage;

import java.util.List;

//clasa abstracta care extinde clasa Thread, clasa prin care putem creea fire de executie in cadrul programului nostru
//sunt creeaza implementari abstracte pentru functii ce sunt implementate in clasele de functii pentru fiecare tip de thread in parte

public abstract class ImageThread extends Thread {
    public int factor;
    public boolean isBright;

    public ImageThread(int factor, boolean isBright) {
        this.factor = factor;
        this.isBright = isBright;
    }

    //metode abstracte ce sunt implementate in clasa ProcessThreadFunctin, metode prin care sunt procesati bitii cititi
    public abstract void modifyImage(ProcessImage processImage) throws Exception;

    public abstract byte[] mapImageFragments(int totalImageLength, List<byte[]> imageFragment);


}
