package org.anachronos.clojure.core.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.anachronos.clojure.core.parser.antlr.ASTNodeConverter;
import org.anachronos.clojure.core.parser.antlr.ClojureLexer;
import org.anachronos.clojure.core.parser.antlr.ClojureParser;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;

public class ClojureSourceParser extends AbstractSourceParser {
    private final class LineModel {
	private final String content;
	private final List<Integer> lineNumOffsets = new ArrayList<Integer>();

	public LineModel(String content) {
	    this.content = content;
	    initLineOffsets();
	}

	private void initLineOffsets() {
	    final String[] lines = content.split("\n");
	    for (int lineNum = 0, offset = 0; lineNum < lines.length; lineNum++) {
		lineNumOffsets.add(offset);
		offset += lines[lineNum].length() + 1;
	    }
	}

	public int getLineOffset(int line) {
	    return lineNumOffsets.get(line);
	}
    }

    @Override
    public ModuleDeclaration parse(char[] fileName, char[] content,
	    IProblemReporter reporter) {
	final String fileNameAsString = new String(fileName);
	ModuleDeclaration file = null;
	try {
	    final String contentAsString = new String(content);
	    final Lexer lexer = new ClojureLexer(new ANTLRStringStream(
		    contentAsString));
	    final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
	    final ClojureParser parser = new ClojureParser(tokenStream);
	    final CommonTree ast = (CommonTree) parser.file().getTree();

	    if (parser.hasErrors())
		reportErrors(reporter, parser, fileNameAsString,
			contentAsString);

	    final CommonTreeNodeStream treeNodeStream = new CommonTreeNodeStream(
		    ast);
	    treeNodeStream.setTokenStream(tokenStream);
	    final ASTNodeConverter astNodeConverter = new ASTNodeConverter(
		    treeNodeStream);
	    file = astNodeConverter.file();
	} catch (RecognitionException e) {
	}
	return file;
    }

    private void reportErrors(final IProblemReporter reporter,
	    final ClojureParser parser, final String fileName,
	    final String content) {
	final Set<Entry<RecognitionException, String>> errorEntries = parser
		.getErrorToMessage().entrySet();

	final LineModel lineModel = new LineModel(content);
	for (final Entry<RecognitionException, String> errorEntry : errorEntries) {
	    final RecognitionException error = errorEntry.getKey();
	    final int line = error.line > 0 ? error.line - 1 : 0;
	    final int offset = lineModel.getLineOffset(line);
	    final int column = error.charPositionInLine;
	    final int start = offset + column;
	    final int end = start + error.token.getText().length();
	    final String message = errorEntry.getValue();
	    final DefaultProblem problem = new DefaultProblem(fileName,
		    message, 0, null, ProblemSeverities.Error, start, end,
		    line, column);
	    reporter.reportProblem(problem);
	}
    }
}