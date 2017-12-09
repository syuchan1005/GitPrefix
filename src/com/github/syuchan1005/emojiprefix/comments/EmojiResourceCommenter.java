package com.github.syuchan1005.emojiprefix.comments;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @see org.jetbrains.yaml.YAMLCommenter
 * @see com.intellij.codeInsight.generation.CommentByLineCommentHandler#postInvoke
 */
public class EmojiResourceCommenter implements Commenter {
    @NotNull
    @NonNls
    private static final String LINE_COMMENT_PREFIX = "#";

    @Override
    public String getLineCommentPrefix() {
        return LINE_COMMENT_PREFIX;
    }

    @Override
    public String getBlockCommentPrefix() {
        // N/A
        return null;
    }

    @Override
    public String getBlockCommentSuffix() {
        // N/A
        return null;
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
