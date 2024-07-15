package com.it.xiaodongbei.flowdiagram.component;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author: cxc
 * @CreateTime: 2023-11-05  22:29
 * @Description: TODO
 * @Version: 1.0
 */
public class SettingButton extends JButton {
    private boolean toolWindowCreated=false;

    public SettingButton() {
        this.setIcon(IconLoader.findIcon("/icons/settings.svg"));
        this.setToolTipText("settings");
        // 添加动作监听器
        initButton();

    }

    private void initButton() {

        this.setToolTipText("settings");
        addActionListener(createSettingsActionListener());
    }
    private ActionListener createSettingsActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 用于选择的选项
                Object[] options = {"True", "False"};
                int result = JOptionPane.showOptionDialog(
                        null, // 父组件
                        "Choose the value for addmethods:", // 消息
                        "Settings", // 标题
                        JOptionPane.YES_NO_OPTION, // 选项类型
                        JOptionPane.QUESTION_MESSAGE, // 消息类型
                        IconLoader.findIcon("/icons/settings.svg"), // 使用的图标
                        options, // 选项
                        toolWindowCreated ? options[0] : options[1] // 默认选择当前值
                );

                // 处理用户的选择
                if (result == JOptionPane.YES_OPTION) {
                    // 用户选择了"True"
                    toolWindowCreated = true;
                    System.out.println("Add Methods is now true");
                    // 更新你的设置服务或存储
                } else if (result == JOptionPane.NO_OPTION) {
                    // 用户选择了"False"
                    toolWindowCreated = false;
                    System.out.println("Add Methods is now false");
                    // 更新你的设置服务或存储
                }
            }
        };
    }
    public boolean isToolWindowCreated() {
        return toolWindowCreated;
    }

    public void setToolWindowCreated(boolean toolWindowCreated) {
        this.toolWindowCreated = toolWindowCreated;
    }


}
