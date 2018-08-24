package com.github.syuchan1005.gitprefix.git.tag;

import git4idea.actions.GitRepositoryAction;
import javassist.*;

public class GitTagInjector {
    public static void main(String[] args) {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(GitRepositoryAction.class.getClassLoader()));
            CtClass ctClass = classPool.get("git4idea.ui.GitTagDialog");
            ctClass.defrost();

            CtMethod make = CtNewMethod.make(
                    "public boolean showAndGet() { " +
                            "" +
                            "boolean isOK = super.showAndGet();" +
                            "return isOK;" +
                            "}", ctClass);
            ctClass.addMethod(make);

            ctClass.writeFile();
            ctClass.toClass(GitRepositoryAction.class.getClassLoader(),
                    GitRepositoryAction.class.getProtectionDomain());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
