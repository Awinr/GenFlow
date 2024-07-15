package com.it.xiaodongbei.flowdiagram.component;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Author: cxc
 * @CreateTime: 2023-11-05  23:23
 * @Description: TODO
 * @Version: 1.0
 */
public class ExportButton extends JButton {
    private final Project project;
    private final AnActionEvent event;

    private final Image plantUMLImage;

    public ExportButton(Project proj, AnActionEvent evt, Image plantUMLPanel) {
        this.project = proj;
        this.event = evt;
        this.plantUMLImage = plantUMLPanel;
        this.setIcon(IconLoader.findIcon("/icons/save.svg"));
        this.setToolTipText("Export");
        addActionListener(createStartActionListener());

    }

    private ActionListener createStartActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取你要导出的ImageIcon
                // 转换ImageIcon到BufferedImage
                Image image = plantUMLImage;
                BufferedImage bufferedImage = new BufferedImage(
                        image.getWidth(null),
                        image.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D bGr = bufferedImage.createGraphics();
                bGr.drawImage(image, 0, 0, null);
                bGr.dispose();

                // 创建文件选择器并提示用户保存文件
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Image");

                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    // 确保文件有一个适当的扩展名
                    String filePath = fileToSave.getAbsolutePath();
                    if (!filePath.endsWith(".png")) {
                        fileToSave = new File(filePath + ".png");
                    }

                    // 写入图片到文件系统
                    try {
                        ImageIO.write(bufferedImage, "png", fileToSave);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
    }
}
