package com.it.aaron.codeflow.editor;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public class MyCustomFileEditor implements FileEditor {
    private final Project project;
    private final VirtualFile file;
    private JPanel panel;

    public MyCustomFileEditor(Project project, VirtualFile file) {
        this.project = project;
        this.file = file;
        this.panel = createEditorPanel();
    }

    private JPanel createEditorPanel() {
        JPanel panel = new JPanel();
        // 在此处自定义编辑器的UI
        panel.add(new JLabel("This is a custom editor"));
        return panel;
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return panel;
    }

    @NotNull
    @Override
    public String getName() {
        return "My Custom Editor";
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        // 设置编辑器状态
    }

    @Override
    public boolean isModified() {
        // 返回编辑器内容是否已被修改
        return false;
    }

    @Override
    public boolean isValid() {
        return file.isValid();
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
        // 添加属性更改监听器
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
        // 移除属性更改监听器
    }

    @Override
    public void dispose() {
        // 资源清理
    }

    @Override
    public <T> @Nullable T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {

    }
    private void addDocumentListener() {
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null) {
            document.addDocumentListener(new DocumentListener() {
                @Override
                public void beforeDocumentChange(DocumentEvent event) {
                    // 处理文档变更前事件
                }

                @Override
                public void documentChanged(DocumentEvent event) {
                    // 处理文档变更后事件
                }
            });
        }
    }
}
