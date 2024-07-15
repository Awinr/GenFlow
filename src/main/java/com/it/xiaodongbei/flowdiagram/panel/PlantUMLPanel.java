package com.it.xiaodongbei.flowdiagram.panel;

/**
 * @Author: cxc
 * @CreateTime: 2023-11-01  00:29
 * @Description: TODO
 * @Version: 1.0
 */

import net.sourceforge.plantuml.SourceStringReader;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class PlantUMLPanel extends JPanel {
    private Image plantUmlImage;


    public PlantUMLPanel(String plantUmlSource) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPlantUML(plantUmlSource);
        MediaTracker tracker = new MediaTracker(new JPanel()); // 使用JPanel作为媒介对象
        tracker.addImage(plantUmlImage, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        int width = plantUmlImage.getWidth(null);
        int height = plantUmlImage.getHeight(null);
        this.setPreferredSize(new Dimension(width,height));
    }

    public Image getPlantUmlImage() {
        return plantUmlImage;
    }

    public void setPlantUmlImage(Image plantUmlImage) {
        this.plantUmlImage = plantUmlImage;
    }

    public void setPlantUML(String plantUmlSource) {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            SourceStringReader reader = new SourceStringReader(plantUmlSource);
            reader.outputImage(outputStream);
            plantUmlImage = Toolkit.getDefaultToolkit().createImage(((ByteArrayOutputStream) outputStream).toByteArray());
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (plantUmlImage != null) {
            g.drawImage(plantUmlImage, 0, 0, this);
        }
    }
}

