package com.it.aaron.codeflow.utils;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

// astNode 表示一个代码块。
public class CodeFlowUtil {
    public static void createPlantUml(StringBuilder codeString, BlockStmt astNode, boolean isAddMethod) {
        try {

            codeString.append("@startuml\n");
            codeString.append("start\n");
            // 处理所有嵌套的 IfStmt
            processNestedIfStatements(codeString, astNode, isAddMethod, 0);
            codeString.append("stop\n");
            codeString.append("@enduml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * astNode 是包含 { } 的代码块
     * 代码块中总体可以分为两种内容
     *   1.声明语句，以 ";" 结尾
     *   2.其他代码块: for/while/try catch/if 但这种后面还是跟着 { }代码块
     */
    private static void processNestedIfStatements(StringBuilder codeString, Node astNode, boolean isTrue, int nestingLevel) {
        if (astNode instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) astNode;
            String ifCondition = ifStmt.getCondition().toString();
            boolean isOutermostIf = !ifStmt.getParentNode().filter(parent -> parent instanceof IfStmt).isPresent(); //判断 ifStmt 是否是多级 if else 语句 中的第一个 if 语句

            if (isOutermostIf) {
                codeString.append(indent(nestingLevel) + "if(" + ifCondition + ")");
                codeString.append(indent(nestingLevel) + "then (true)\n");
                processNestedIfStatements(codeString, ifStmt.getThenStmt(), isTrue, nestingLevel + 1);
            } else { // 如果不是第一个 if 语句，则说明是 else if 语句
                codeString.append(indent(nestingLevel) + "elseif(" + ifCondition + ")");
                codeString.append(indent(nestingLevel) + "then (true)\n");
                processNestedIfStatements(codeString, ifStmt.getThenStmt(), isTrue, nestingLevel + 1);
            }

            if (ifStmt.hasElseBranch()) {
                Node elseNode = ifStmt.getElseStmt().get();
                if (elseNode instanceof IfStmt) {
                    processNestedIfStatements(codeString, elseNode, isTrue, nestingLevel);
                } else {
                    codeString.append(indent(nestingLevel) + "else(false)\n");
                    processNestedIfStatements(codeString, elseNode, isTrue, nestingLevel + 1);
                }
            }

            if (isOutermostIf) {
                codeString.append(indent(nestingLevel) + "endif\n");
            }
        } else if (astNode instanceof WhileStmt) {
            WhileStmt whileStmt = (WhileStmt) astNode;
            String whileCondition = whileStmt.getCondition().toString();
            codeString.append(indent(nestingLevel) + "while (" + whileCondition + ") is (true)\n");
            processNestedIfStatements(codeString, whileStmt.getBody(), isTrue, nestingLevel + 1);
            codeString.append(indent(nestingLevel) + "endwhile (false)\n");
        } else if (astNode instanceof ReturnStmt) {
            codeString.append("stop\n");
        } else { // 以上都不是，则说明是 { }代码块/声明语句
            if (isTrue) {
                // 处理表达式语句，如方法调用和赋值，遇到try catch 直接append其中的声明语句
                if (astNode instanceof ExpressionStmt) {
                    ExpressionStmt expressionStmt = (ExpressionStmt) astNode;
                    // 格式化输出以适应流程图
                    codeString.append(indent(nestingLevel) + ": " + expressionStmt.getExpression().toString() + ";\n");
                }else if (astNode instanceof ForStmt) {
                    ForStmt forStmt = (ForStmt) astNode;
                    String forCondition = String.join("; ", forStmt.getInitialization().toString(),
                            forStmt.getCompare().toString(),
                            forStmt.getUpdate().toString());
                    codeString.append(indent(nestingLevel) + "repeat :(" + forCondition + ")\n");
                    processNestedIfStatements(codeString, forStmt.getBody(), isTrue, nestingLevel + 1);
                    codeString.append(indent(nestingLevel) + "repeat while (" + forStmt.getCompare().get() + ") is (true)\n");
                }
            }
            // 如果是代码块，递归处理代码块中所有子节点 子节点属于同一个嵌套级别
            for (Node child : astNode.getChildNodes()) {
                processNestedIfStatements(codeString, child, isTrue, nestingLevel);
            }
        }
    }
    private static String indent(int level) {
        return "    ".repeat(level); // 控制缩进级别
    }
}
