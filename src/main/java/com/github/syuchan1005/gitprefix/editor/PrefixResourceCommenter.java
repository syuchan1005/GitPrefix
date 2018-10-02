package com.github.syuchan1005.gitprefix.editor;

import com.intellij.lang.Commenter;

public class PrefixResourceCommenter implements Commenter {
	@Override
	public String getLineCommentPrefix() {
		return "//";
	}

	@Override
	public String getBlockCommentPrefix() {
		return "/*";
	}

	@Override
	public String getBlockCommentSuffix() {
		return "*/";
	}

	@Override
	public String getCommentedBlockCommentPrefix() {
		return null;
	}

	@Override
	public String getCommentedBlockCommentSuffix() {
		return null;
	}
}
