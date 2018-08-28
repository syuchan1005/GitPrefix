package com.github.syuchan1005.gitprefix.comments;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class PrefixResourceCommenter implements Commenter {
	@NotNull
	@NonNls
	private static final String LINE_COMMENT_PREFIX = "#";

	@Override
	public String getLineCommentPrefix() {
		return LINE_COMMENT_PREFIX;
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
