package com.it.aaron.codeflow.component;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SettingButton extends JButton {

    private boolean toolWindowCreated = true;

    public StartButton startButton;

    public SettingButton() {
        this.setIcon(IconLoader.findIcon("/icons/settings.svg"));
        this.setToolTipText("settings");
        // 添加动作监听器
        initButton();

    }

    private void initButton() {
        this.setToolTipText("settings");
        addActionListener(createSettingsActionListener());
        IconLoader.findIcon("/icons/settings.svg"); // 使用的图标
    }

    private ActionListener createSettingsActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DataContext dataContext = DataManager.getInstance().getDataContext();
                Project project = CommonDataKeys.PROJECT.getData(dataContext);
                if (project == null) return;

                OptionsUI.OptionsDialogWrapper dialogWrapper = new OptionsUI.OptionsDialogWrapper(project);
                dialogWrapper.show();
                if (dialogWrapper.isOK()) {
                    CodeFlowParamsState setting = CodeFlowParamsState.getInstance();

                    //state.callDepth = dialogWrapper.getCallStackDepth();
                    setting.displayComments = dialogWrapper.isDisplayComments();
                    setting.noColors = dialogWrapper.isNoColor();
                    //state.noPrivateMethods = dialogWrapper.isNoPrivateMethods();
                    //state.noConstructors = dialogWrapper.isNoConstructors();

                    // Notify parameter change.
                    setting.fireConfigChanged();
                    if (startButton != null) {
                        startButton.doClick();
                    }

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
