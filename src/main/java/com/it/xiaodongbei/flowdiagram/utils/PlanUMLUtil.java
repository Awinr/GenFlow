package com.it.xiaodongbei.flowdiagram.utils;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

/**
 * @Author: cxc
 * @CreateTime: 2023-11-06  21:48
 * @Description: TODO
 * @Version: 1.0
 */
public class PlanUMLUtil {
    public static void createPlantUml(StringBuilder plantUML, BlockStmt cu,boolean isAddMethod) {
        try {

            plantUML.append("@startuml\n");
            plantUML.append("start\n");
            // 处理所有嵌套的 IfStmt
            processNestedIfStatements(cu, 0, plantUML,isAddMethod);
            plantUML.append("stop\n");
            plantUML.append("@enduml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static void processNestedIfStatements(Node node, int nestingLevel, StringBuilder plantUML, boolean isTrue) {
        if (node instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) node;
            String ifCondition = ifStmt.getCondition().toString();
            boolean isOutermostIf = !ifStmt.getParentNode().filter(parent -> parent instanceof IfStmt).isPresent();

            if (isOutermostIf) {
                plantUML.append(indent(nestingLevel) + "if(" + ifCondition + ")");
                plantUML.append(indent(nestingLevel) + "then (true)\n");
                processNestedIfStatements(ifStmt.getThenStmt(), nestingLevel + 1, plantUML,isTrue);
            } else {
                plantUML.append(indent(nestingLevel) + "elseif(" + ifCondition + ")");
                plantUML.append(indent(nestingLevel) + "then (true)\n");
                processNestedIfStatements(ifStmt.getThenStmt(), nestingLevel + 1, plantUML,isTrue);
            }

            if (ifStmt.hasElseBranch()) {
                Node elseNode = ifStmt.getElseStmt().get();
                if (elseNode instanceof IfStmt) {
                    processNestedIfStatements(elseNode, nestingLevel, plantUML,isTrue);
                } else {
                    plantUML.append(indent(nestingLevel) + "else(false)\n");
                    processNestedIfStatements(elseNode, nestingLevel + 1, plantUML,isTrue);
                }
            }

            if (isOutermostIf) {
                plantUML.append(indent(nestingLevel) + "endif\n");
            }
        } else if (node instanceof WhileStmt) {
            WhileStmt whileStmt = (WhileStmt) node;
            String whileCondition = whileStmt.getCondition().toString();
            plantUML.append(indent(nestingLevel) + "while (" + whileCondition + ") is (true)\n");
            processNestedIfStatements(whileStmt.getBody(), nestingLevel + 1, plantUML,isTrue);
            plantUML.append(indent(nestingLevel) + "endwhile (false)\n");
        } else if (node instanceof ReturnStmt) {
            plantUML.append("stop\n");
        } else {
            if (isTrue) {
                // 处理表达式语句，如方法调用和赋值
                if (node instanceof ExpressionStmt) {
                    ExpressionStmt expressionStmt = (ExpressionStmt) node;
                    // 格式化输出以适应UML图
                    plantUML.append(indent(nestingLevel) + ": " + expressionStmt.getExpression().toString() + ";\n");
                }else if (node instanceof ForStmt) {
                    ForStmt forStmt = (ForStmt) node;
                    String forCondition = String.join("; ", forStmt.getInitialization().toString(),
                            forStmt.getCompare().toString(),
                            forStmt.getUpdate().toString());
                    plantUML.append(indent(nestingLevel) + "repeat :(" + forCondition + ")\n");
                    processNestedIfStatements(forStmt.getBody(), nestingLevel + 1, plantUML, isTrue);
                    plantUML.append(indent(nestingLevel) + "repeat while (" + forStmt.getCompare().get() + ") is (true)\n");
                }
            }
            // Recursively process all child nodes
            for (Node child : node.getChildNodes()) {
                processNestedIfStatements(child, nestingLevel, plantUML,isTrue);
            }
        }
    }
    private static String indent(int level) {
        return "    ".repeat(level);
    }
}
