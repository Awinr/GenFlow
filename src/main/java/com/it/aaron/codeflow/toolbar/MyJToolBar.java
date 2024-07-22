package com.it.aaron.codeflow.toolbar;

import javax.swing.*;
import java.awt.*;


public class MyJToolBar extends JToolBar {
    public MyJToolBar() {
        super(JToolBar.VERTICAL);
        setFloatable(false);
        setPreferredSize(new Dimension(40,200));
    }

    public void addComponent(Component component){
        add(component);
    }
}
