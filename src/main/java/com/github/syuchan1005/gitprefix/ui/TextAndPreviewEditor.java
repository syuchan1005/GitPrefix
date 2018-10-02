package com.github.syuchan1005.gitprefix.ui;

import com.github.syuchan1005.gitprefix.icons.GitPrefixIcons;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.icons.AllIcons;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.editor.ex.EditorGutterComponentEx;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.JBSplitter;
import com.intellij.util.ui.JBUI;
import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextAndPreviewEditor extends UserDataHolderBase implements FileEditor {
	protected final TextEditor myEditor;
	protected final FileEditor myPreview;

	@NotNull
	private final MyListenersMultimap myListenersGenerator = new MyListenersMultimap();
	private final String myName;
	private Layout myLayout;
	private JComponent myComponent;
	private SplitEditorToolbar myToolbarWrapper;

	public TextAndPreviewEditor(@NotNull TextEditor editor, @NotNull FileEditor preview, @NotNull String editorName) {
		myEditor = editor;
		myPreview = preview;
		myName = editorName;
	}

	public TextAndPreviewEditor(@NotNull TextEditor editor, @NotNull FileEditor preview) {
		this(editor, preview, "TextAndPreviewEditor");
	}

	@Nullable
	@Override
	public BackgroundEditorHighlighter getBackgroundHighlighter() {
		return myEditor.getBackgroundHighlighter();
	}

	@Nullable
	@Override
	public FileEditorLocation getCurrentLocation() {
		return myEditor.getCurrentLocation();
	}

	@Nullable
	@Override
	public StructureViewBuilder getStructureViewBuilder() {
		return myEditor.getStructureViewBuilder();
	}

	@Override
	public void dispose() {
		Disposer.dispose(myEditor);
		Disposer.dispose(myPreview);
	}

	@Override
	public void selectNotify() {
		myEditor.selectNotify();
		myPreview.selectNotify();
	}

	@Override
	public void deselectNotify() {
		myEditor.deselectNotify();
		myPreview.deselectNotify();
	}

	@NotNull
	@Override
	public JComponent getComponent() {
		if (myComponent == null) {
			final JBSplitter splitter = new JBSplitter(false, 0.5f, 0.15f, 0.85f);
			splitter.setSplitterProportionKey(getSplitterProportionKey());
			splitter.setFirstComponent(myEditor.getComponent());
			splitter.setSecondComponent(myPreview.getComponent());


			myToolbarWrapper = new SplitEditorToolbar(splitter);
			myToolbarWrapper.addGutterToTrack(((EditorGutterComponentEx) (myEditor).getEditor().getGutter()));

			if (myPreview instanceof TextEditor) {
				myToolbarWrapper.addGutterToTrack(((EditorGutterComponentEx) ((TextEditor) myPreview).getEditor().getGutter()));
			}

			if (myLayout == null) {
				String lastUsed = PropertiesComponent.getInstance().getValue(getLayoutPropertyName());
				myLayout = Layout.fromName(lastUsed, Layout.SHOW_EDITOR_AND_PREVIEW);
			}
			adjustEditorsVisibility();

			myComponent = JBUI.Panels.simplePanel(splitter).addToTop(myToolbarWrapper);
		}
		return myComponent;
	}

	@Override
	public void setState(@NotNull FileEditorState state) {
		if (state instanceof MyFileEditorState) {
			final MyFileEditorState compositeState = (MyFileEditorState) state;
			if (compositeState.getFirstState() != null) {
				myEditor.setState(compositeState.getFirstState());
			}
			if (compositeState.getSecondState() != null) {
				myPreview.setState(compositeState.getSecondState());
			}
			if (compositeState.getSplitLayout() != null) {
				myLayout = compositeState.getSplitLayout();
				invalidateLayout();
			}
		}
	}

	private void adjustEditorsVisibility() {
		myEditor.getComponent().setVisible(true);
		myPreview.getComponent().setVisible(myLayout == Layout.SHOW_EDITOR_AND_PREVIEW);
	}

	private void invalidateLayout() {
		adjustEditorsVisibility();
		myToolbarWrapper.refresh();
		myComponent.repaint();

		final JComponent focusComponent = getPreferredFocusedComponent();
		if (focusComponent != null) {
			IdeFocusManager.findInstanceByComponent(focusComponent).requestFocus(focusComponent, true);
		}
	}

	@NotNull
	protected String getSplitterProportionKey() {
		return "TextAndPreviewEditor.SplitterProportionKey";
	}

	@Nullable
	@Override
	public JComponent getPreferredFocusedComponent() {
		switch (myLayout) {
			case SHOW_EDITOR_AND_PREVIEW:
			case SHOW_EDITOR:
				return myEditor.getPreferredFocusedComponent();
			default:
				throw new IllegalStateException(myLayout.myName);
		}
	}

	@NotNull
	@Override
	public String getName() {
		return myName;
	}

	@NotNull
	@Override
	public FileEditorState getState(@NotNull FileEditorStateLevel level) {
		return new MyFileEditorState(myLayout, myEditor.getState(level), myPreview.getState(level));
	}


	@Override
	public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
		myEditor.addPropertyChangeListener(listener);
		myPreview.addPropertyChangeListener(listener);

		final DoublingEventListenerDelegate delegate = myListenersGenerator.addListenerAndGetDelegate(listener);
		myEditor.addPropertyChangeListener(delegate);
		myPreview.addPropertyChangeListener(delegate);
	}

	@Override
	public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
		myEditor.removePropertyChangeListener(listener);
		myPreview.removePropertyChangeListener(listener);

		final DoublingEventListenerDelegate delegate = myListenersGenerator.removeListenerAndGetDelegate(listener);
		if (delegate != null) {
			myEditor.removePropertyChangeListener(delegate);
			myPreview.removePropertyChangeListener(delegate);
		}
	}

	static class MyFileEditorState implements FileEditorState {
		private final Layout mySplitLayout;
		private final FileEditorState myFirstState;
		private final FileEditorState mySecondState;

		public MyFileEditorState(Layout layout, FileEditorState firstState, FileEditorState secondState) {
			mySplitLayout = layout;
			myFirstState = firstState;
			mySecondState = secondState;
		}

		@Nullable
		public Layout getSplitLayout() {
			return mySplitLayout;
		}

		@Nullable
		public FileEditorState getFirstState() {
			return myFirstState;
		}

		@Nullable
		public FileEditorState getSecondState() {
			return mySecondState;
		}

		@Override
		public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level) {
			return otherState instanceof MyFileEditorState
					&& (myFirstState == null || myFirstState.canBeMergedWith(((MyFileEditorState) otherState).myFirstState, level))
					&& (mySecondState == null || mySecondState.canBeMergedWith(((MyFileEditorState) otherState).mySecondState, level));
		}
	}

	@Override
	public boolean isModified() {
		return myEditor.isModified() || myPreview.isModified();
	}

	@Override
	public boolean isValid() {
		return myEditor.isValid() && myPreview.isValid();
	}

	@Nullable
	protected ActionToolbar createToolbar() {
		return null;
	}

	@NotNull
	private String getLayoutPropertyName() {
		return myName + "Layout";
	}

	public enum Layout {
		SHOW_EDITOR("Editor only", GitPrefixIcons.LAYOUT_EDITOR_ONLY),
		// SHOW_PREVIEW("Preview only", AllIcons.General.LayoutPreviewOnly),
		SHOW_EDITOR_AND_PREVIEW("Editor and Preview", GitPrefixIcons.LAYOUT_EDITOR_PREVIEW);

		private final String myName;
		private final Icon myIcon;

		Layout(String name, Icon icon) {
			myName = name;
			myIcon = icon;
		}

		public static Layout fromName(String name, Layout defaultValue) {
			for (Layout layout : Layout.values()) {
				if (layout.myName.equals(name)) {
					return layout;
				}
			}
			return defaultValue;
		}

		public String getName() {
			return myName;
		}

		public Icon getIcon() {
			return myIcon;
		}
	}

	private class DoublingEventListenerDelegate implements PropertyChangeListener {
		@NotNull
		private final PropertyChangeListener myDelegate;

		private DoublingEventListenerDelegate(@NotNull PropertyChangeListener delegate) {
			myDelegate = delegate;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			myDelegate.propertyChange(new PropertyChangeEvent(TextAndPreviewEditor.this, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()));
		}
	}

	private class MyListenersMultimap {
		private final Map<PropertyChangeListener, Pair<Integer, DoublingEventListenerDelegate>> myMap = new HashMap<>();

		@NotNull
		public DoublingEventListenerDelegate addListenerAndGetDelegate(@NotNull PropertyChangeListener listener) {
			if (!myMap.containsKey(listener)) {
				myMap.put(listener, Pair.create(1, new DoublingEventListenerDelegate(listener)));
			} else {
				final Pair<Integer, DoublingEventListenerDelegate> oldPair = myMap.get(listener);
				myMap.put(listener, Pair.create(oldPair.getFirst() + 1, oldPair.getSecond()));
			}

			return myMap.get(listener).getSecond();
		}

		@Nullable
		public DoublingEventListenerDelegate removeListenerAndGetDelegate(@NotNull PropertyChangeListener listener) {
			final Pair<Integer, DoublingEventListenerDelegate> oldPair = myMap.get(listener);
			if (oldPair == null) {
				return null;
			}

			if (oldPair.getFirst() == 1) {
				myMap.remove(listener);
			} else {
				myMap.put(listener, Pair.create(oldPair.getFirst() - 1, oldPair.getSecond()));
			}
			return oldPair.getSecond();
		}
	}

	public class SplitEditorToolbar extends JPanel implements Disposable {
		private final ActionToolbar myRightToolbar;

		private final List<EditorGutterComponentEx> myGutters = new ArrayList<>();

		private final ComponentAdapter myAdjustToGutterListener = new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				adjustSpacing();
			}

			@Override
			public void componentShown(ComponentEvent e) {
				adjustSpacing();
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				adjustSpacing();
			}
		};

		public SplitEditorToolbar(@NotNull final JComponent targetComponentForActions) {
			super(new BorderLayout());

			final ActionToolbar leftToolbar = createToolbar();
			if (leftToolbar != null) {
				leftToolbar.setTargetComponent(targetComponentForActions);
				add(leftToolbar.getComponent(), BorderLayout.WEST);
			}

			ActionGroup group = new DefaultActionGroup(
					new ChangeViewModeAction(Layout.SHOW_EDITOR),
					new ChangeViewModeAction(Layout.SHOW_EDITOR_AND_PREVIEW)/*,
                    new ChangeViewModeAction(Layout.SHOW_PREVIEW) */
			);
			myRightToolbar = ActionManager.getInstance().createActionToolbar("TextAndPreviewEditor", group, true);
			myRightToolbar.setTargetComponent(targetComponentForActions);
			add(myRightToolbar.getComponent(), BorderLayout.EAST);

			addComponentListener(myAdjustToGutterListener);
		}

		public void addGutterToTrack(@NotNull EditorGutterComponentEx gutterComponentEx) {
			myGutters.add(gutterComponentEx);

			gutterComponentEx.addComponentListener(myAdjustToGutterListener);
		}

		public void refresh() {
			adjustSpacing();
			myRightToolbar.updateActionsImmediately();
		}

		private void adjustSpacing() {
			EditorGutterComponentEx leftMostGutter = null;
			for (EditorGutterComponentEx gutter : myGutters) {
				if (!gutter.isShowing()) {
					continue;
				}
				if (leftMostGutter == null || leftMostGutter.getX() > gutter.getX()) {
					leftMostGutter = gutter;
				}
			}

			revalidate();
			repaint();
		}

		@Override
		public void dispose() {
			removeComponentListener(myAdjustToGutterListener);
			for (EditorGutterComponentEx gutter : myGutters) {
				gutter.removeComponentListener(myAdjustToGutterListener);
			}
		}
	}

	private class ChangeViewModeAction extends ToggleAction implements DumbAware {
		private final Layout myActionLayout;

		public ChangeViewModeAction(Layout layout) {
			super(layout.getName(), layout.getName(), layout.getIcon());
			myActionLayout = layout;
		}

		@Override
		public boolean isSelected(AnActionEvent e) {
			return myLayout == myActionLayout;
		}

		@Override
		public void setSelected(AnActionEvent e, boolean state) {
			if (state) {
				myLayout = myActionLayout;
				PropertiesComponent.getInstance().setValue(getLayoutPropertyName(), myLayout.myName, Layout.SHOW_EDITOR_AND_PREVIEW.myName);
				adjustEditorsVisibility();
			}
		}
	}
}