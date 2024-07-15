package com.it.xiaodongbei.flowdiagram.toolbar;

import javax.swing.*;
import java.awt.*;

/**
 * @Author: cxc
 * @CreateTime: 2023-11-05  22:23
 * @Description: TODO
 * @Version: 1.0
 */
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
