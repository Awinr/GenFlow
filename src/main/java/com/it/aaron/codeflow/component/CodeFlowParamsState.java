package com.it.aaron.codeflow.component;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 插件左侧的小扳手中的设置
 */
@State(name = "sequenceParams", storages = {@Storage("sequencePlugin.xml")})
public class CodeFlowParamsState implements PersistentStateComponent<CodeFlowParamsState> {
    public int callDepth = 5;
    public boolean displayComments = false;
    public boolean noColors = false;
    public boolean noPrivateMethods = false;
    public boolean noConstructors = false;

    @Deprecated(since = "2.2.0", forRemoval = true)
    public boolean smartInterface = false;

    @Transient
    private final List<ConfigListener> _listeners = new ArrayList<>();

    public CodeFlowParamsState() {
    }

    public static @NotNull CodeFlowParamsState getInstance() {
        return ApplicationManager.getApplication().getService(CodeFlowParamsState.class);
    }

    public void addConfigListener(ConfigListener listener) {
        _listeners.add(listener);
    }

    public void removeConfigListener(ConfigListener listener) {
        _listeners.remove(listener);
    }

    public void fireConfigChanged() {
        for (ConfigListener configListener : _listeners) {
            configListener.configChanged();
        }
    }

    @Override
    public @Nullable CodeFlowParamsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CodeFlowParamsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
