package com.it.aaron.codeflow.panel;

import net.sourceforge.plantuml.SourceStringReader;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * 继承自 JPanel ，因此 PlantUMLPanel 实例本身就是一个 Swing 组件。
 */
public class PlantUMLPanel extends JPanel {
    /**
     * 存储生成的流程图
     */
    private Image plantUmlImage;

    public PlantUMLPanel(String plantUmlSource) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPlantUML(plantUmlSource);
        MediaTracker tracker = new MediaTracker(new JPanel()); // 使用JPanel作为媒介对象  tracker用于跟踪图像的加载状态，确保图像在显示前已完全加载
        tracker.addImage(plantUmlImage, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        int width = plantUmlImage.getWidth(null);
        int height = plantUmlImage.getHeight(null);
        this.setPreferredSize(new Dimension(width, height)); // 设置面板的首选大小为图像的大小
    }

    public Image getPlantUmlImage() {
        return plantUmlImage;
    }

    public void setPlantUmlImage(Image plantUmlImage) {
        this.plantUmlImage = plantUmlImage;
    }

    /**
     * 生成图像
     */
    public void setPlantUML(String plantUmlSource) {
        try {
            // 创建一个输出流保存图像数据 用于持久化到磁盘
            OutputStream outputStream = new ByteArrayOutputStream();
            //读取 PlantUML 源代码
            SourceStringReader reader = new SourceStringReader(plantUmlSource);
            // 将读取到的数据输出成图像
            reader.outputImage(outputStream);
            // 从字节数组中创建图像
            plantUmlImage = Toolkit.getDefaultToolkit().createImage(((ByteArrayOutputStream) outputStream).toByteArray());
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制图像到当前组件中
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (plantUmlImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            // 启用抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            // 绘制图像
            g2d.drawImage(plantUmlImage, 0, 0, this);
        }
    }
}

