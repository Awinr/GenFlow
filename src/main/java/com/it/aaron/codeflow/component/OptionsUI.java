package com.it.aaron.codeflow.component;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.JBUI;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;
import java.awt.*;

public class OptionsUI {

    public static class DialogPanel extends JPanel {
        //private final JSpinner jSpinner;
        private final JCheckBox jCheckBoxPFO;
        private final JCheckBox jCheckBoxNGS;
        //private final JCheckBox jCheckBoxNPM;
        //private final JCheckBox jCheckBoxNC;
//        private final JCheckBox jCheckBoxSI;

        public DialogPanel() {
            super(new GridBagLayout());
            final CodeFlowParamsState state = CodeFlowParamsState.getInstance();
            setBorder(BorderFactory.createTitledBorder("Options"));
            GridBagConstraints gc = new GridBagConstraints();
            //gc.gridx = 0;
            //gc.gridy = 0;
            //gc.insets = JBUI.insets(5);
            //gc.anchor = GridBagConstraints.WEST;
            //JLabel jLabel = new JLabel("Call depth:");
            //add(jLabel, gc);

            //gc.gridx = 1;
            //gc.anchor = GridBagConstraints.CENTER;
            //jSpinner = new JSpinner(new SpinnerNumberModel(state.callDepth, 1, 10, 1));
            //jLabel.setLabelFor(jSpinner);
            //add(jSpinner, gc);

            // 展示注释
            gc.gridx = 0;
            gc.gridy = 1;
            gc.anchor = GridBagConstraints.WEST;
            gc.gridwidth = 2;
            gc.insets = JBUI.emptyInsets();
            jCheckBoxPFO = new JCheckBox("Display Comments", state.displayComments);
            add(jCheckBoxPFO, gc);

            // 黑白颜色
            gc.gridx = 0;
            gc.gridy = 2;
            gc.anchor = GridBagConstraints.WEST;
            gc.gridwidth = 2;
            gc.insets = JBUI.emptyInsets();
            jCheckBoxNGS = new JCheckBox("No Colors", state.noColors);
            add(jCheckBoxNGS, gc);

            //gc.gridx = 2;
            //gc.gridy = 1;
            //gc.anchor = GridBagConstraints.WEST;
            //gc.gridwidth = 2;
            //gc.insets = JBUI.emptyInsets();
            //jCheckBoxNPM = new JCheckBox("Skip private methods", state.noPrivateMethods);
            //add(jCheckBoxNPM, gc);
            //
            //gc.gridx = 2;
            //gc.gridy = 2;
            //gc.anchor = GridBagConstraints.WEST;
            //gc.gridwidth = 2;
            //gc.insets = JBUI.emptyInsets();
            //jCheckBoxNC = new JCheckBox("Skip constructors", state.noConstructors);
            //add(jCheckBoxNC, gc);
        }
    }

    public static class OptionsDialogWrapper extends DialogWrapper {
        private final DialogPanel dialogPanel = new DialogPanel();

        public OptionsDialogWrapper(Project project) {
            super(project, false);
            setResizable(false);
            setTitle("CodeFlow Settings");
            init();
        }

        protected JComponent createCenterPanel() {
            return dialogPanel;
        }

        //public JComponent getPreferredFocusedComponent() {
        //    return dialogPanel.jSpinner;
        //}
        //
        //public int getCallStackDepth() {
        //    return (Integer) dialogPanel.jSpinner.getValue();
        //}

        public boolean isDisplayComments() {
            return dialogPanel.jCheckBoxPFO.isSelected();
        }

        public boolean isNoColor() {
            return dialogPanel.jCheckBoxNGS.isSelected();
        }

        //public boolean isNoPrivateMethods() {
        //    return dialogPanel.jCheckBoxNPM.isSelected();
        //}
        //
        //public boolean isNoConstructors() {
        //    return dialogPanel.jCheckBoxNC.isSelected();
        //}

    }

}
