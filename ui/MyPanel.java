package com.company.ui;

import com.company.inputs.Input;
import com.company.inputs.MyInputClass;
import com.company.multithreadings.ProcessThread;
import com.company.processAlgorithms.ImageAlgorithm;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//clasa custom ce este folosita pentru a descrie elementele ce dorim sa fie afisate in GUI
//sunt incluse butoane, o imagine, un slider si o bara de progres
//de asemenea, este folosit si un file selector pentru a selecta fisierele ce dorim sa fie folosite la procesare

public class MyPanel extends JPanel implements ActionListener {
    private JButton openInputFileDialogBtn, openOutputFileDialogBtn, modifyImageBtn;
    private JFileChooser fileChooser;
    private JTextField inputFilePath, outputFilePath;
    private JLabel imageLabel;
    private JSlider slider;
    private ImageIcon icon;

    private JProgressBar progressBar;

    public MyPanel() {
        //se defineste un element de tip JFileChooser ce ne ajuta pentru selectarea fisierelor pe care le vom utiliza
        //se seteaza parametrii (sa aleaga fisiere de tip bmp )

        FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP images", "bmp");
        fileChooser = new JFileChooser(".\\");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(filter);

        //sunt adaugate 2 elemente de tip textbox pentru a afisa si a stoca locatiile de citire/scriere pentru imagine
        inputFilePath = new JTextField(50);
        outputFilePath = new JTextField(50);


        this.setLayout(new FlowLayout());

        //adaugate butoanele de executie
        openInputFileDialogBtn = new JButton("Choose input file");
        openInputFileDialogBtn.addActionListener(this);
        openOutputFileDialogBtn = new JButton("Choose output file");
        openOutputFileDialogBtn.addActionListener(this);
        modifyImageBtn = new JButton("Start");
        modifyImageBtn.addActionListener(this);



        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        inputPanel.add(openInputFileDialogBtn);
        inputPanel.add(inputFilePath);


        JPanel outputPanel = new JPanel();
        outputPanel.add(openOutputFileDialogBtn);
        outputPanel.add(outputFilePath);

        JPanel inOutPanel = new JPanel();
        inOutPanel.setLayout(new BoxLayout(inOutPanel, BoxLayout.Y_AXIS));

        //adaugam un slider ce ne permite selectarea valorii de luminozitate
        //definim valorile pentru aceast progress bar
        slider = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);


        //se face load la o imagine default, imagine ce trebuie sa fie in pachetul sursa
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(".\\default.bmp"));
            icon = ImageAlgorithm.loadImageFromBuffer(bufferedImage);
            imageLabel = new JLabel(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.add(inOutPanel);

        inOutPanel.add(imageLabel);
        inOutPanel.add(inputPanel);
        inOutPanel.add(outputPanel);
        inOutPanel.add(slider);
        inOutPanel.add(progressBar);

        //in momentul in care se termina de modificat imaginea, se afiseaza pe ecran si imaginea modificata
        JPanel modifyImagePanel = new JPanel();
        modifyImagePanel.add(modifyImageBtn);

        inOutPanel.add(modifyImagePanel);
    }

    //in functie de butoanele apasate, avem un eveniment ce are rolul de a executa o anumita parte de cod
    //pentru butoanele de selectarea a fisierelor de intrare/iesire avem eveniment diferit
    //pentru butonul de start -> se lanseaza in executie "programul" propriu-zis
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton temp = (JButton) e.getSource();


        if (temp.equals(openInputFileDialogBtn) || temp.equals(openOutputFileDialogBtn)) {
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                System.out.println("Choose input file path: " + file.getAbsolutePath());
                if (temp.equals(openInputFileDialogBtn)) {
                    inputFilePath.setText(file.getAbsolutePath());
                } else {
                    outputFilePath.setText(file.getAbsolutePath());
                }
            } else {
                System.out.println("Error opening the file");
            }
            progressBar.setValue(0);
            ProcessThread.tasksDone.clear();
        } else if (temp.equals(modifyImageBtn)) {
            progressBar.setValue(0);
            ProcessThread.tasksDone.clear();

            MyInputClass inputClass = new MyInputClass();
            Input params = inputClass.readFromUi(inputFilePath.getText(), outputFilePath.getText(), slider.getValue());
            UiThread task = new UiThread(params, imageLabel, outputFilePath.getText());
            task.addPropertyChangeListener(
                    evt -> {
                        if ("progress".equals(evt.getPropertyName())) {
                            progressBar.setValue((Integer) evt.getNewValue());
                        }
                    });
            task.execute();
        }
    }
}
