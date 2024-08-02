package com.it.aaron.codeflow;

public interface Constant {

    String JAVA = "JAVA";
    String PARAM = "!pragma useVerticalIf on\n"  // 测试处于垂直模式
            + "skinparam conditionStyle InsideDiamond\n" //判断样式为菱形
            + "skinparam ConditionEndStyle hline\n"
            ;
    String STYLE = "<style>\n" +
            "activityDiagram {\n" +
            "  BackgroundColor #00BFFF\n" +
            "  rectangle {\n" +
            "     BackgroundColor lightGreen\n" +
            "  }\n" +
            "  diamond {\n" +
            "    BackgroundColor #FF4500\n" +
            "  }\n" +
            "  note {\n" +
            "    BackgroundColor #EEEE00\n" +
            "  }\n" +
            "}\n" +
            "document {\n" +
            "   BackgroundColor while\n" +
            "}\n" +
            "</style>\n";
    String COMMENT_POSITION = "right:";
}
