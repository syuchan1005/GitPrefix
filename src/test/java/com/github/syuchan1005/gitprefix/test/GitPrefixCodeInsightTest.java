package com.github.syuchan1005.gitprefix.test;

import com.github.syuchan1005.gitprefix.filetype.PrefixResourceFileType;
import com.github.syuchan1005.gitprefix.grammar.PrefixResourceFile;
import com.github.syuchan1005.gitprefix.util.PrefixResourceFileUtil;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.generation.actions.CommentByBlockCommentAction;
import com.intellij.codeInsight.generation.actions.CommentByLineCommentAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.containers.ContainerUtil;
import java.util.Collection;
import java.util.List;

public class GitPrefixCodeInsightTest extends LightCodeInsightFixtureTestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected String getTestDataPath() {
		return "src/test/resources";
	}

	public void testCompletion() {
		myFixture.configureByFile("DefaultTestData.gitprefix");
		myFixture.complete(CompletionType.BASIC);
		List<String> strings = myFixture.getLookupElementStrings();
		assertNotNull("Completion is not found", strings);
		assertTrue(strings.contains("tada:"));
	}

	public void testAnnotator() {
		myFixture.configureByFile("AnnotatorTestData.gitprefix");
		myFixture.checkHighlighting(true, true, true, true);
	}

	public void testFormatter() {
		myFixture.configureByFiles("FormatterTestData.gitprefix");
		WriteCommandAction.runWriteCommandAction(getProject(), () -> CodeStyleManager.getInstance(getProject())
				.reformatText(myFixture.getFile(), ContainerUtil.newArrayList(myFixture.getFile().getTextRange())));
		myFixture.checkResultByFile("FormatterTestDataAfter.gitprefix");
	}

	public void testRename() {
		myFixture.configureByFile("RenameTestData.gitprefix");
		myFixture.renameElementAtCaret("empty");
		myFixture.checkResultByFile("RenameTestData.gitprefix", "RenameTestDataAfter.gitprefix", true);
	}

	public void testFolding() {
		myFixture.testFolding(getTestDataPath() + "/FoldingTestData.gitprefix");
	}

	public void testFindUsages() {
		Collection<UsageInfo> usageInfos = myFixture.testFindUsages("FindUsagesTestData.gitprefix");
		assertEquals(1, usageInfos.size());
	}

	public void testLineCommenter() {
		myFixture.configureByText(PrefixResourceFileType.INSTANCE, "<caret>:tada: test");
		CommentByLineCommentAction commentAction = new CommentByLineCommentAction();
		commentAction.actionPerformedImpl(getProject(), myFixture.getEditor());
		myFixture.checkResult("//:tada: test");
		commentAction.actionPerformedImpl(getProject(), myFixture.getEditor());
		myFixture.checkResult(":tada: test");
	}

	public void testBlockCommenter() {
		myFixture.configureByText(PrefixResourceFileType.INSTANCE, "<selection>:tada: test</selection>");
		CommentByBlockCommentAction commentAction = new CommentByBlockCommentAction();
		commentAction.actionPerformedImpl(getProject(), myFixture.getEditor());
		myFixture.checkResult("/*\n:tada: test*/\n");
		commentAction.actionPerformedImpl(getProject(), myFixture.getEditor());
		myFixture.checkResult(":tada: test\n");
	}

	public void testStructured() {
		myFixture.configureByFile("StructuredTestData.gitprefix");
		PrefixResourceFile file = (PrefixResourceFile) myFixture.getFile();
		file = PrefixResourceFileUtil.createStructuredFile(file, true);
		myFixture.configureByText(PrefixResourceFileType.INSTANCE, file.getText() + "\n");
		myFixture.checkResultByFile("StructuredTestDataAfter.gitprefix", true);
	}
}
