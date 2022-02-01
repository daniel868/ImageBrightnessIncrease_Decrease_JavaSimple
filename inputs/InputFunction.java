package com.company.inputs;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;

//clasa abstracta pentru verificarae parametriilor de input
public abstract class InputFunction {
    //aceasta functie verifica daca fisierul introdus de la tastatura este existent
    //daca nu exista fisierul dat prin calea de input, se arunca o eroare de tip FileNotFoundExceptin

    public void checkForInputFile(String inputFilePath, Input input) throws FileNotFoundException {
        if (Files.exists(new File(inputFilePath).toPath()) && inputFilePath.contains(".bmp")) {
            input.setInputFilePath(inputFilePath);
        } else {
            throw new FileNotFoundException("Could not be found " + inputFilePath + " or invalid file format");
        }
    }
    //se verifica ca numele noului fisier de ouput sa nu fie null si sa aiba extensia de bmp la final

    public void checkForOutputFile(String outputFilePath, Input input) throws FileNotFoundException {
        if (!outputFilePath.contains(".bmp")) {
            throw new FileNotFoundException("Could not be read the file where to store the output data");
        } else {
            input.setOutputFilePath(outputFilePath);
        }
    }
}
