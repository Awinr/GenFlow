package com.it.aaron.codeflow.panel;

import com.intellij.ui.components.JBScrollPane;
import com.it.aaron.codeflow.toolbar.MyJToolBar;

import javax.swing.*;
import java.awt.*;


public class MainJPanel extends JPanel {
    private MyJToolBar toolBar;
    private JBScrollPane jScrollPane;
    private PlantUMLPanel plantUMLPanel;

    public MainJPanel(PlantUMLPanel plantUMLPanel, JButton startButton, JButton settingButton, JButton exportButton) {
        this.setLayout(new BorderLayout());
        this.toolBar = new MyJToolBar();
        this.jScrollPane = new JBScrollPane(plantUMLPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // 将JScrollPane添加到主面板的中间
        this.add(jScrollPane, BorderLayout.CENTER);
        // 将工具栏添加到主面板的左边
        this.add(toolBar, BorderLayout.WEST);
        this.toolBarAddComponent(startButton, settingButton, exportButton);


    }

    public void toolBarAddComponent(JButton startButton, JButton settingButton, JButton exportButton) {
        toolBar.add(startButton);
        toolBar.add(settingButton);
        toolBar.add(exportButton);
    }
}
