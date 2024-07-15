package com.it.xiaodongbei.flowdiagram.component;

import com.it.xiaodongbei.flowdiagram.panel.PlantUMLPanel;

import javax.swing.*;

/**
 * @Author: cxc
 * @CreateTime: 2023-11-06  00:15
 * @Description: TODO
 * @Version: 1.0
 */
public class MyJScrollPane extends JScrollPane {

    public MyJScrollPane(PlantUMLPanel plantUMLPanel) {
        super(plantUMLPanel);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
}
