package com.it.aaron.codeflow.test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PlantUMLPanelExample {
    public static void main(String[] args) {
        // 创建 JFrame
        JFrame frame = new JFrame("PlantUML Panel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // 创建 PlantUML 源代码
        String plantUmlSource = "@startuml\n" +
                //"skinparam dpi 200\n" +
                //"scale 500 width\n" +
                //"scale 2000 height\n" +
                "start\n" +
                ": String ip = request.getHeader(\"x-forwarded-for\");\n" +
                "if(ip == null || ip.length() == 0 || \"unknown\".equalsIgnoreCase(ip))then (true)\n" +
                "    : ip = request.getRemoteAddr();\n" +
                "    if(ip.equals(\"127.0.0.1\"))    then (true)\n" +
                "        : InetAddress inet = null;\n" +
                "        : inet = InetAddress.getLocalHost();\n" +
                "        : e.printStackTrace();\n" +
                "        : System.out.println(\"finally\");\n" +
                "        if(inet != null)        then (true)\n" +
                "            : ip = inet.getHostAddress();\n" +
                "        endif\n" +
                "    endif\n" +
                "endif\n" +
                ": int age = 10;\n" +
                ": System.out.println();\n" +
                ": System.out.println();\n" +
                ": System.out.println();\n" +
                "if(ip == null)then (true)\n" +
                "end\n" +
                "endif\n" +
                "repeat :([int i = 0]; Optional[i < 10]; [i++])\n" +
                "    if(ip != null && ip.length() > 15)    then (true)\n" +
                "        if(ip.indexOf(\",\") > 0)        then (true)\n" +
                "            : ip = ip.substring(0, ip.indexOf(\",\"));\n" +
                "        endif\n" +
                "    endif\n" +
                "repeat while (i < 10) is (true)\n" +
                "while (age < 100) is (true)\n" +
                "    : System.out.println();\n" +
                "endwhile (false)\n" +
                "end\n" +
                "end\n" +
                "@enduml";

        // 创建 PlantUMLPanel 并添加到 JScrollPane
        PlantUMLPanel plantUmlPanel = new PlantUMLPanel(plantUmlSource);
        JScrollPane scrollPane = new JScrollPane(plantUmlPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // 将 JScrollPane 添加到 JFrame
        frame.add(scrollPane);

        // 显示 JFrame
        frame.setVisible(true);
    }
}

class PlantUMLPanel extends JPanel {
    private Image plantUmlImage;

    public PlantUMLPanel(String plantUmlSource) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPlantUML(plantUmlSource);
        MediaTracker tracker = new MediaTracker(new JPanel());
        tracker.addImage(plantUmlImage, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        int width = plantUmlImage.getWidth(null);
        int height = plantUmlImage.getHeight(null);
        this.setPreferredSize(new Dimension(width, height));
    }

    public void setPlantUML(String plantUmlSource) {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            SourceStringReader reader = new SourceStringReader(plantUmlSource);
            reader.outputImage(outputStream);

            byte[] imageData = ((ByteArrayOutputStream) outputStream).toByteArray();
            InputStream inputStream = new ByteArrayInputStream(imageData);
            plantUmlImage = ImageIO.read(inputStream);

            // Trigger a repaint to update the display
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (plantUmlImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // 启用抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


            // 绘制图像
            g2d.drawImage(plantUmlImage, 0, 0, this);

            g2d.dispose();
        }
    }
}