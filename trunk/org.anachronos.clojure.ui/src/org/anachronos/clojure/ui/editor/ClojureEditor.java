package org.anachronos.clojure.ui.editor;

import org.anachronos.clojure.core.ClojureLanguageToolkit;
import org.anachronos.clojure.ui.ClojureUILanguageToolkit;
import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.anachronos.clojure.ui.preferences.ClojurePreferenceConstants;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.internal.ui.editor.ScriptOutlinePage;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

/**
 * Editor for clojure files with syntax highlighting and parens matching.
 * 
 * @author km
 */
public class ClojureEditor extends ScriptEditor {
    private static final String ID = "org.anachronos.clojure.ui.editor";
    private DefaultCharacterPairMatcher bracketMatcher;

    public ClojureEditor() {
	super();
	final IColorManager colorManager = ClojureUIPlugin.getDefault()
		.getTextTools().getColorManager();
	final IPreferenceStore preferenceStore = ClojureUILanguageToolkit
		.getDefault().getPreferenceStore();
	final ClojureSourceViewerConfiguration configuration = new ClojureSourceViewerConfiguration(
		colorManager, preferenceStore, this);
	setSourceViewerConfiguration(configuration);
    }

    @Override
    protected void connectPartitioningToElement(IEditorInput input,
	    IDocument document) {
	if (document instanceof IDocumentExtension3) {
	    IDocumentExtension3 extension = (IDocumentExtension3) document;
	    final boolean partioningNotSet = extension
		    .getDocumentPartitioner(ClojureDocumentSetupParticipant.CLOJURE_PARTIONING) == null;
	    if (partioningNotSet) {
		ClojureDocumentSetupParticipant participant = new ClojureDocumentSetupParticipant();
		participant.setup(document);
	    }
	}
    }

    @Override
    protected ScriptOutlinePage doCreateOutlinePage() {
	return new ClojureOutlinePage(this, getScriptPreferenceStore());
    }

    @Override
    protected void initializeEditor() {
	super.initializeEditor();
	setPreferenceStore(ClojureUIPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected boolean affectsTextPresentation(PropertyChangeEvent event) {
	return getScriptSourceViewerConfiguration().affectsTextPresentation(
		event)
		|| super.affectsTextPresentation(event);
    }

    private ScriptSourceViewerConfiguration getScriptSourceViewerConfiguration() {
	return (ScriptSourceViewerConfiguration) getSourceViewerConfiguration();
    }

    @Override
    protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
	final ScriptSourceViewerConfiguration sourceViewerConfig = getScriptSourceViewerConfiguration();
	sourceViewerConfig.handlePropertyChangeEvent(event);
	super.handlePreferenceStoreChanged(event);
    }

    @Override
    protected void configureSourceViewerDecorationSupport(
	    SourceViewerDecorationSupport support) {
	bracketMatcher = new DefaultCharacterPairMatcher(new char[] { '(', ')',
		'[', ']', '{', '}' });
	support.setCharacterPairMatcher(bracketMatcher);
	support.setMatchingCharacterPainterPreferenceKeys(
		ClojurePreferenceConstants.HIGHLIGHT_MATCHING_BRACKETS,
		ClojurePreferenceConstants.MATCHING_BRACKETS_COLOR);
	support.install(getPreferenceStore());

	super.configureSourceViewerDecorationSupport(support);
    }

    @Override
    public void gotoMatchingBracket() {
	ISourceViewer sourceViewer = getSourceViewer();
	IDocument document = sourceViewer.getDocument();
	if (document == null)
	    return;

	IRegion selection = getSignedSelection(sourceViewer);

	int selectionLength = Math.abs(selection.getLength());
	if (selectionLength > 1) {
	    return;
	}

	// #26314
	int sourceCaretOffset = selection.getOffset() + selection.getLength();
	if (isSurroundedByBrackets(document, sourceCaretOffset))
	    sourceCaretOffset -= selection.getLength();

	IRegion region = bracketMatcher.match(document, sourceCaretOffset);
	if (region == null) {
	    return;
	}

	int offset = region.getOffset();
	int length = region.getLength();

	if (length < 1)
	    return;

	int anchor = bracketMatcher.getAnchor();
	// http://dev.eclipse.org/bugs/show_bug.cgi?id=34195
	int targetOffset = (ICharacterPairMatcher.RIGHT == anchor) ? offset + 1
		: offset + length;

	boolean visible = false;
	if (sourceViewer instanceof ITextViewerExtension5) {
	    ITextViewerExtension5 extension = (ITextViewerExtension5) sourceViewer;
	    visible = (extension.modelOffset2WidgetOffset(targetOffset) > -1);
	} else {
	    IRegion visibleRegion = sourceViewer.getVisibleRegion();
	    // http://dev.eclipse.org/bugs/show_bug.cgi?id=34195
	    visible = (targetOffset >= visibleRegion.getOffset() && targetOffset <= visibleRegion
		    .getOffset()
		    + visibleRegion.getLength());
	}

	if (!visible) {
	    return;
	}

	if (selection.getLength() < 0)
	    targetOffset -= selection.getLength();

	sourceViewer.setSelectedRange(targetOffset, selection.getLength());
	sourceViewer.revealRange(targetOffset, selection.getLength());
    }

    @Override
    public String getEditorId() {
	return ID;
    }

    @Override
    public IDLTKLanguageToolkit getLanguageToolkit() {
	return ClojureLanguageToolkit.getDefault();
    }

    @Override
    protected IPreferenceStore getScriptPreferenceStore() {
	return ClojureUIPlugin.getDefault().getPreferenceStore();
    }
}
