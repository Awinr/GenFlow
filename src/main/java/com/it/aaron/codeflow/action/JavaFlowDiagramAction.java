package com.it.aaron.codeflow.action;

import com.github.javaparser.StaticJavaParser;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.it.aaron.codeflow.Constant;
import com.it.aaron.codeflow.component.ExportButton;
import com.it.aaron.codeflow.component.SettingButton;
import com.it.aaron.codeflow.component.StartButton;
import com.it.aaron.codeflow.panel.MainJPanel;
import com.it.aaron.codeflow.panel.PlantUMLPanel;
import com.it.aaron.codeflow.utils.CodeFlowUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class JavaFlowDiagramAction extends AnAction {

    public AnActionEvent event;

    public SettingButton settingButton = new SettingButton();

    private PlantUMLPanel plantUMLPanel;

    private StartButton startButton;

    private MainJPanel mainJPanel;

    private ExportButton exportButton;

    /**
     * 动作启用/禁用状态
     *根据文件类型启用或禁用菜单。当前只有 java 文件才能启用菜单。
     *
     * @param event event
     */
    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);

        // Presentation对象包含了与用户界面相关的信息，如操作的名称、描述、图标和 启用/禁用开关。
        Presentation presentation = event.getPresentation();

        // Program Structure Interface  PSI 会将代码解析成一个树状结构，其中包含了类、方法、变量等节点。
        // PSI 提供了一系列接口和方法，允许开发者读取和修改这些节点
        @Nullable PsiElement psiElement = event.getData(CommonDataKeys.PSI_FILE);
        presentation.setEnabled(isEnabled(psiElement));
    }

    private boolean isEnabled(PsiElement psiElement) {
        String id = psiElement.getLanguage().getID();
        return psiElement != null && id.equals(Constant.JAVA);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        event = e;
        StringBuilder codeString = new StringBuilder();
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (!(psiFile instanceof PsiJavaFile)) {
            return;
        } // PsiElement 是 PSI 树中的一个通用节点，表示代码中的任何元素。
        PsiElement elementAtCaret = PsiUtilBase.getElementAtCaret(editor); // 获取当前编辑器中光标位置所在的 PsiElement。
        PsiMethod method = PsiTreeUtil.getParentOfType(elementAtCaret, PsiMethod.class);
        if (method == null) {
            return;
        }
        PsiCodeBlock methodBody = method.getBody(); // 方法体包含在大括号 {} 中的所有代码。
        if (methodBody == null) {
            return;
        }
        Result result = new Result(project, methodBody);
        // 创建流程图内容
        CodeFlowUtil.createPlantUml(codeString, StaticJavaParser.parseBlock(result.methodBody.getText()), settingButton.isToolWindowCreated());

        // 创建流程图内容面板
        plantUMLPanel = new PlantUMLPanel(codeString.toString());
        // 启动内容
        startButton = new StartButton(result.getProject(), event);
        exportButton = new ExportButton(result.getProject(), event, plantUMLPanel.getPlantUmlImage());
        mainJPanel = new MainJPanel(plantUMLPanel, startButton, settingButton, exportButton);
        // 获取 ToolWindow
        ToolWindow toolWindow = ToolWindowManager.getInstance(result.getProject()).getToolWindow("PlantUMLToolWindow");
        // 设置内容
        toolWindow.getContentManager().removeAllContents(true); // 清除之前的内容
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(mainJPanel, "", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.setSplitMode(true, null); // 将窗口设置为拆分模式，显示在左上角

        // 显示 ToolWindow
        toolWindow.activate(null);
    }


    private static class Result {

        private final Project project;

        private final PsiCodeBlock methodBody;

        public Result(Project project, PsiCodeBlock methodBody) {
            this.project = project;
            this.methodBody = methodBody;
        }

        public Project getProject() {
            return project;
        }

        public PsiCodeBlock getMethodBody() {
            return methodBody;
        }
    }
}
