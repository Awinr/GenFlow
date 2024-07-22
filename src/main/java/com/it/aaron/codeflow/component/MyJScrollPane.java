package com.it.aaron.codeflow.component;

import com.it.aaron.codeflow.panel.PlantUMLPanel;

import javax.swing.*;


public class MyJScrollPane extends JScrollPane {

    public MyJScrollPane(PlantUMLPanel plantUMLPanel) {
        super(plantUMLPanel);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
}
