package com.it.aaron.codeflow.utils;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.it.aaron.codeflow.Constant;

import java.util.List;
import java.util.stream.Collectors;

// astNode 表示一个代码块。
public class CodeFlowUtil {

    // 替换字符串首尾的 [、]、Optional[、]
    public static final String regex = "^(\\[|Optional\\[)|(\\])$";

    public static void createPlantUml(StringBuilder codeString, BlockStmt astNode, boolean isAddMethod) {
        try {
            codeString.append("@startuml\n");
            codeString.append(Constant.PARAM);
            codeString.append("start\n");
            // 处理所有嵌套的 IfStmt
            processNestedIfStatements(codeString, astNode, isAddMethod, 0);
            //codeString.append("end\n");
            codeString.append("@enduml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * astNode 是包含 { } 的代码块,首次是整个方法体
     * 代码块中总体可以分为两种内容
     *   1.声明语句，以 ";" 结尾
     *   2.其他代码块: for/while/try catch/if 但这种后面还是跟着 { }代码块
     */
    private static void processNestedIfStatements(StringBuilder codeString, Node astNode, boolean isTrue, int nestingLevel) {
        if (astNode instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) astNode;
            String ifCondition = ifStmt.getCondition().toString();
            boolean isFirstIf = !ifStmt.getParentNode().filter(parent -> parent instanceof IfStmt).isPresent(); //判断 ifStmt 是否是多级 if else 语句 中的第一个 if 语句
            if (isFirstIf) {
                codeString.append(indent(nestingLevel) + "if(" + ifCondition + ")");
                codeString.append(indent(nestingLevel) + "then (true)\n");
                processNestedIfStatements(codeString, ifStmt.getThenStmt(), isTrue, nestingLevel + 1);
                codeString.append(indent(nestingLevel) + "else (false)\n");
            } else { // 如果不是第一个 if 语句，则说明是 else if 语句
                codeString.append(indent(nestingLevel) + "elseif(" + ifCondition + ")");
                codeString.append(indent(nestingLevel) + "then (true)\n");
                processNestedIfStatements(codeString, ifStmt.getThenStmt(), isTrue, nestingLevel + 1);
                codeString.append(indent(nestingLevel) + "else (false)\n");
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
            if (isFirstIf) {
                codeString.append(indent(nestingLevel) + "endif\n");
            }
        } else if (astNode instanceof WhileStmt) {
            WhileStmt whileStmt = (WhileStmt) astNode;
            String whileCondition = whileStmt.getCondition().toString();
            codeString.append(indent(nestingLevel) + "while (" + whileCondition + ") is (true)\n");
            processNestedIfStatements(codeString, whileStmt.getBody(), isTrue, nestingLevel + 1);
            codeString.append(indent(nestingLevel) + "endwhile (false)\n");
        } else if (astNode instanceof SwitchStmt) {
            SwitchStmt switchStmt = (SwitchStmt) astNode;
            Expression expression = switchStmt.getSelector();
            codeString.append(indent(nestingLevel) + "switch (switch (" +expression.toString() + "))\n");
            NodeList<SwitchEntry> entries = switchStmt.getEntries();
            for (SwitchEntry entry : entries) {
                String caseLabel = entry.getLabels().isEmpty() ? "case(default)" : "case (" +
                        entry.getLabels().stream().map(Object::toString).collect(Collectors.joining(", "))+")";
                codeString.append(indent(nestingLevel) + caseLabel + "\n");
                // switch 中，break 和 return 跳出
                for (Statement statement : entry.getStatements()) {
                    if (statement instanceof BreakStmt) {
                        //codeString.append(indent(nestingLevel + 1) );
                        break;
                    } else if (statement instanceof ReturnStmt) {
                        codeString.append(indent(nestingLevel + 1) + "end\n");
                        break;
                    } else {
                        // 对于其他类型的语句，递归处理
                        processNestedIfStatements(codeString, statement, isTrue, nestingLevel + 1);
                    }
                }
                codeString.append(indent(nestingLevel )); // case 分隔
            }
            codeString.append(indent(nestingLevel) + "endswitch\n");
        } else if (astNode instanceof ReturnStmt) {
            codeString.append("end\n");
        } else if (astNode instanceof ForStmt) {
            ForStmt forStmt = (ForStmt) astNode;
            String initStmt = forStmt.getInitialization().toString().replaceAll(regex, "");
            String compareStmt = forStmt.getCompare().toString().replaceAll(regex, "");
            String updateStmt = forStmt.getUpdate().toString().replaceAll(regex, "");

            codeString.append(indent(nestingLevel) + "repeat :for(" + initStmt + "; " + compareStmt + ")\n");
            processNestedIfStatements(codeString, forStmt.getBody(), isTrue, nestingLevel + 1);
            codeString.append(indent(nestingLevel) + "backward:" + updateStmt +";\n");
            codeString.append(indent(nestingLevel) + "repeat while (" + forStmt.getCompare().get() + ") is (true) not (false)\n");
        } else { // 以上都不是，则说明是 { }代码块/声明语句
            if (isTrue) {
                // 处理表达式语句，如方法调用和赋值，遇到try catch 直接append其中的声明语句
                if (astNode instanceof ExpressionStmt) {
                    ExpressionStmt expressionStmt = (ExpressionStmt) astNode;
                    codeString.append(indent(nestingLevel) + ": " + expressionStmt.getExpression().toString() + ";\n");
                }
            }
            // 如果是代码块，递归处理代码块中所有子节点 子节点属于同一个嵌套级别 先序遍历
            for (Node child : astNode.getChildNodes()) {
                processNestedIfStatements(codeString, child, isTrue, nestingLevel);
            }
        }
    }
    private static String indent(int level) {
        return "    ".repeat(level); // 控制缩进级别
    }
}
