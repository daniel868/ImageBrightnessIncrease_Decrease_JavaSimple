package com.company.inputs;

//clasa wrapper peste datele care se introduc de la tastatura
//inputFilePath -> pentru calea de input de unde se citeste fisierul de tip .btmp
//outputFilPaht-> pentru calea de ouput unde se va scrie noua imagine de tip .bmp
//factor -> factorul cu care scade /creste luminozitatea imaginii
//isBright -> daca se doreste sa fie mai luminoasa / intunecata imaginea finala

public class Input {
    private String inputFilePath;
    private String outputFilePath;
    private int factor;
    private boolean isBright;


    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public boolean isBright() {
        return isBright;
    }

    public void setBright(boolean bright) {
        isBright = bright;
    }
}
