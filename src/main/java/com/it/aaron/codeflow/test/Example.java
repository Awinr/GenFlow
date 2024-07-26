package com.it.aaron.codeflow.test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.List;

public class Example {
    public static void main(String[] args) {
        String code = """
            public class Test {
                public void testMethod() {
                    // First line comment
                    // Second line comment
                    int x = 10;
                }
            }
            """;

        CompilationUnit cu = StaticJavaParser.parse(code);

        // 获取所有表达式语句
        cu.findAll(ExpressionStmt.class).forEach(expressionStmt -> {
            // 获取与该语句相关联的所有注释
            List<Comment> comments = expressionStmt.getAllContainedComments();

            // 打印注释内容
            for (Comment comment : comments) {
                System.out.println(comment.getContent());
            }
        });
    }
}
