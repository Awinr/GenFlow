package com.it.xiaodongbei.flowdiagram.action;

import com.github.javaparser.StaticJavaParser;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.it.xiaodongbei.flowdiagram.component.ExportButton;
import com.it.xiaodongbei.flowdiagram.component.SettingButton;
import com.it.xiaodongbei.flowdiagram.component.StartButton;
import com.it.xiaodongbei.flowdiagram.panel.MainJPanel;
import com.it.xiaodongbei.flowdiagram.panel.PlantUMLPanel;
import com.it.xiaodongbei.flowdiagram.utils.PlanUMLUtil;

/**
 * @Author: cxc
 * @CreateTime: 2023-10-31  22:21
 * @Description: TODO
 * @Version: 1.0
 */
public class JavaFlowDiagramAction extends AnAction {
    public  AnActionEvent event;
    SettingButton settingButton = new SettingButton();
    private PlantUMLPanel plantUMLPanel;

    private StartButton startButton;
    private MainJPanel mainJPanel;

    private ExportButton exportButton;


    @Override
    public void actionPerformed(AnActionEvent e) {
        event=e;
        StringBuilder plantUML = new StringBuilder();
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
        }
        PsiElement elementAtCaret = PsiUtilBase.getElementAtCaret(editor);
        PsiMethod method = PsiTreeUtil.getParentOfType(elementAtCaret, PsiMethod.class);
        if (method == null) {
            return;
        }
        PsiCodeBlock methodBody = method.getBody();
        if (methodBody == null) {
            return;
        }
        Result result = new Result(project, methodBody);
        // 创建plantuml流程图
        PlanUMLUtil.createPlantUml(plantUML, StaticJavaParser.parseBlock(result.methodBody.getText()),settingButton.isToolWindowCreated());

        // 创建流程图内容面板
        plantUMLPanel = new PlantUMLPanel(plantUML.toString());
        // 启动内容
        startButton = new StartButton(result.project,event);
        exportButton = new ExportButton(result.project,event,plantUMLPanel.getPlantUmlImage());
        mainJPanel = new MainJPanel(plantUMLPanel,startButton,settingButton,exportButton);
        // 获取 ToolWindow
        ToolWindow toolWindow = ToolWindowManager.getInstance(result.project).getToolWindow("PlantUMLToolWindow");
        // 设置内容
        toolWindow.getContentManager().removeAllContents(true); // 清除之前的内容
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(mainJPanel, "", false);
        toolWindow.getContentManager().addContent(content);

        // 显示 ToolWindow
        toolWindow.activate(null);
    }


    private static class Result {
        public final Project project;
        public final PsiCodeBlock methodBody;

        public Result(Project project, PsiCodeBlock methodBody) {
            this.project = project;
            this.methodBody = methodBody;
        }
    }
}
