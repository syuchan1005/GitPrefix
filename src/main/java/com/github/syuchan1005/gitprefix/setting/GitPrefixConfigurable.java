package com.github.syuchan1005.gitprefix.setting;

import com.github.syuchan1005.gitprefix.GitPrefixData;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Objects;

public class GitPrefixConfigurable implements SearchableConfigurable {
    private Project myProject;
    private GitPrefixConfigurableGUI gui = null;
    private GitPrefixData prefixData = null;

    public GitPrefixConfigurable(@Nonnull Project project) {
        this.myProject = project;
        this.prefixData = GitPrefixData.getInstance(myProject);
    }

    @NotNull
    @Override
    public String getId() {
        return "perference.gitprefix.GitPrefixConfigurable";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "GitPrefix";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        gui = new GitPrefixConfigurableGUI(this.myProject, this.prefixData.getIsPathType(), this.prefixData.getGitPrefixPath());
        return gui.getRootPane();
    }

    @Override
    public void disposeUIResources() {
        gui = null;
    }

    @Override
    public boolean isModified() {
        return gui != null && gui.isModified();
    }

    @Override
    public void apply() {
        this.prefixData.setIsPathType(gui.getDefaultRadioButton().isSelected() ? "DEFAULT" : "CUSTOM");
        this.prefixData.setGitPrefixPath(gui.getPathField().getText());
    }
}
