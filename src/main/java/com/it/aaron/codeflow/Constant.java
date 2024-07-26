package com.it.aaron.codeflow;

public interface Constant {

    String JAVA = "JAVA";
    String PARAM = "!pragma useVerticalIf on\n"  // 测试处于垂直模式
            + "skinparam conditionStyle InsideDiamond\n" //判断样式为菱形
            + "skinparam ConditionEndStyle hline\n"
            ;
    boolean IS_ADD_COMMENT = true;
    String COMMENT_POSITION = "right:";
}
