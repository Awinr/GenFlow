package com.it.xiaodongbei.flowdiagram.component;/**
*@Author: cxc
*@CreateTime: 2023-11-05  22:48
*@Description: TODO
*@Version: 1.0
*/

import com.github.javaparser.quality.NotNull;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.awt.event.ActionListener;

public class StartButton extends JButton {

    private final Icon startIcon= IconLoader.findIcon("/icons/play.svg");
    private final Project project;
    private final AnActionEvent event;

    public StartButton( Project proj, AnActionEvent evt) {
        this.project = proj;
        this.event = evt;
        initButton();
    }

    private void initButton() {
        this.setIcon(startIcon);
        this.setToolTipText("start");
        addActionListener(createStartActionListener());
    }

    private ActionListener createStartActionListener() {
        return s -> {
            // 获取 ActionManager 的实例
            ActionManager actionManager = ActionManager.getInstance();

            // 通过在 plugin.xml 中定义的 ID 获取 GenAPIDocAction 动作实例
            AnAction action = actionManager.getAction("GenAPIDocAction");

            if (action != null) {
                // 从数据管理器获取数据上下文
                DataContext dataContext = DataManager.getInstance().getDataContext(this);

                // 需要确保 dataContext 包含所需的所有数据
                // 比如，如果你的 actionPerformed 方法需要访问当前项目，你需要像这样添加它:
                dataContext = new DataContext() {
                    @Override
                    public Object getData(@NotNull String dataId) {
                        if (CommonDataKeys.PROJECT.is(dataId)) {
                            return project; // 确保你已经有了project变量的引用
                        }
                        return DataManager.getInstance().getDataContext(StartButton.this).getData(dataId);
                    }
                };

                // 调用 GenAPIDocAction 的 actionPerformed 方法
                action.actionPerformed(event);
            }
        };
    }
}


