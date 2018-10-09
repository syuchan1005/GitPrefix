package com.github.syuchan1005.gitprefix.git.injector;

import com.intellij.openapi.components.BaseComponent;
import git4idea.actions.GitRepositoryAction;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.LoaderClassPath;
import org.jetbrains.annotations.NotNull;

public class GitInjectorManager implements BaseComponent {
	@Override
	public void initComponent() {
		try {
			GitInjectorUtil.injectClassPath();

			ClassPool classPool = ClassPool.getDefault();
			classPool.appendClassPath(new ClassClassPath(this.getClass()));
			classPool.appendClassPath(new LoaderClassPath(GitRepositoryAction.class.getClassLoader()));

			for (InjectorType type : InjectorType.values()) {
				GitInjectorUtil.injectClass(classPool, type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disposeComponent() {
	}

	@NotNull
	@Override
	public String getComponentName() {
		return "GitInjectorManager";
	}

	public enum InjectorType {
		TAG(GitTagDialogInjector.class, "git4idea.ui.GitTagDialog",
				"com.github.syuchan1005.gitprefix.git.injector.GitTagDialogInjector"),
		MERGE(GitMergeDialogInjector.class, "git4idea.merge.GitMergeDialog",
				"com.github.syuchan1005.gitprefix.git.injector.GitMergeDialogInjector");

		private final Class<? extends AbstractGitDialogInjector> clazz;
		private final String injectClassName;
		private final String injectorClassName;

		InjectorType(Class<? extends AbstractGitDialogInjector> clazz, String injectClassName, String injectorClassName) {
			this.clazz = clazz;
			this.injectClassName = injectClassName;
			this.injectorClassName = injectorClassName;
		}

		public Class<? extends AbstractGitDialogInjector> getClazz() {
			return clazz;
		}

		public String getInjectClassName() {
			return injectClassName;
		}

		public String getInjectorClassName() {
			return injectorClassName;
		}

		@Override
		public String toString() {
			return InjectorType.class.getCanonicalName() + "." + super.toString();
		}
	}
}
