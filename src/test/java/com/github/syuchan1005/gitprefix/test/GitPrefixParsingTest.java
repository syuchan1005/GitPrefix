package com.github.syuchan1005.gitprefix.test;

import com.github.syuchan1005.gitprefix.psi.PrefixResourceParserDefinition;
import com.intellij.lang.ParserDefinition;
import com.intellij.testFramework.ParsingTestCase;
import org.jetbrains.annotations.NotNull;

public class GitPrefixParsingTest extends ParsingTestCase {
	public GitPrefixParsingTest() {
		super("", "gitprefix", new PrefixResourceParserDefinition());
	}

	public void testParsingTestData() {
		doTest(true);
	}

	@Override
	protected String getTestDataPath() {
		return "src/test/resources";
	}

	@Override
	protected boolean skipSpaces() {
		return false;
	}

	@Override
	protected boolean includeRanges() {
		return true;
	}
}
