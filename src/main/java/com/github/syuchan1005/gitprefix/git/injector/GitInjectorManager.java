package com.github.syuchan1005.gitprefix.git.injector;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;
import git4idea.actions.GitRepositoryAction;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.LoaderClassPath;

public class GitInjectorManager implements ApplicationComponent {
	@Override
	public void initComponent() {
		try {
			GitInjectorUtil.injectClassPath();

			ClassPool classPool = ClassPool.getDefault();
			classPool.appendClassPath(new LoaderClassPath(GitRepositoryAction.class.getClassLoader()));
			classPool.appendClassPath(new ClassClassPath(this.getClass()));

			for (InjectorType type : InjectorType.values()) {
				GitInjectorUtil.injectClass(classPool, type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public enum InjectorType {
		TAG(GitTagDialogInjector.class, "git4idea.ui.GitTagDialog"),
		MERGE(GitMergeDialogInjector.class, "git4idea.merge.GitMergeDialog");

		private final Class<? extends AbstractGitDialogInjector> clazz;
		private final String injectClassName;
		private final Map<Project, AbstractGitDialogInjector> map = new HashMap<>();

		InjectorType(Class<? extends AbstractGitDialogInjector> clazz, String injectClassName) {
			this.clazz = clazz;
			this.injectClassName = injectClassName;
		}

		public Class<? extends AbstractGitDialogInjector> getClazz() {
			return clazz;
		}

		public String getInjectClassName() {
			return injectClassName;
		}

		public Map<Project, AbstractGitDialogInjector> getMap() {
			return map;
		}

		@Override
		public String toString() {
			return InjectorType.class.getCanonicalName() + "." + super.toString();
		}

		public AbstractGitDialogInjector getOrCreateInjector(Project project) {
			AbstractGitDialogInjector injector = map.get(project);
			if (injector == null) {
				try {
					Constructor<? extends AbstractGitDialogInjector> constructor = clazz.getConstructor(InjectorType.class, Project.class);
					injector = constructor.newInstance(this, project);
				} catch (ReflectiveOperationException ignored) {
				}
				map.put(project, injector);
			}
			return injector;
		}

		public void removeInjector(Project project) {
			map.remove(project);
		}
	}
}
