package com.github.syuchan1005.gitprefix.git;

import com.intellij.openapi.components.ApplicationComponent;
import git4idea.actions.GitRepositoryAction;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;

public abstract class AbstractGitDialogInjector implements ApplicationComponent {
	public static void beforeShow(Object dialog) {
	}

	public static void afterShow(Object dialog) {
	}

	public abstract String getInjectClassName();

	@Override
	public void initComponent() {
		try {
			GitInjectorUtil.injectClassPath();

			ClassLoader git4ideaClassLoader = GitRepositoryAction.class.getClassLoader();
			ClassPool classPool = ClassPool.getDefault();
			classPool.appendClassPath(new LoaderClassPath(git4ideaClassLoader));
			classPool.appendClassPath(new ClassClassPath(this.getClass()));
			CtClass ctClass = classPool.get(this.getInjectClassName());
			ctClass.defrost();

			CtMethod make = CtNewMethod.make(
					"public void show() { " +
							this.getClass().getCanonicalName() + ".beforeShow(this);" +
							"super.show();" +
							this.getClass().getCanonicalName() + ".afterShow(this);" +
							"}", ctClass);
			ctClass.addMethod(make);

			ctClass.writeFile();
			ctClass.toClass(git4ideaClassLoader, GitRepositoryAction.class.getProtectionDomain());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
