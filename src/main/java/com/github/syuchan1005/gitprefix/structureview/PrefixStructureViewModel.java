package com.github.syuchan1005.gitprefix.structureview;

import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class PrefixStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
	public PrefixStructureViewModel(@NotNull PsiFile psiFile) {
		super(psiFile, new PrefixStructureViewElement(psiFile));
	}

	@NotNull
	@Override
	public Sorter[] getSorters() {
		return new Sorter[]{Sorter.ALPHA_SORTER};
	}

	@Override
	public boolean isAlwaysShowsPlus(StructureViewTreeElement structureViewTreeElement) {
		return false;
	}

	@Override
	public boolean isAlwaysLeaf(StructureViewTreeElement element) {
		return element instanceof PrefixResourceFile;
	}
}
