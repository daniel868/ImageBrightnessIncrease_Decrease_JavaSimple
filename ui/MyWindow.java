package com.company.ui;

import javax.swing.*;
import java.awt.*;

//partea de user interface
//este o definita o clasa care extinde o clasa generica Jframe, clasa specifica Java pentru UI
//sunt definite dimensiunile ferestrei de afisare, precum si modul in care dorim sa o afisam pe ecran (pozitionare in centru )
//este inclusa apoi un nou "Panel" -> o clasa ce inglobeaza toate elementele vizuale incluse in acest proiect

public class MyWindow extends JFrame {
    public static final int WINDOW_HEIGHT = 700;
    public static final int WINDOW_WIDTH = 900;


    public MyWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        this.setLayout(new FlowLayout());

        //se adauga o clasa custom MyPanel ce contine toate elementele vizuale pentru acest proiect
        this.add(new MyPanel());

    }
}
