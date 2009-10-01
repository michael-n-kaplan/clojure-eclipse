package org.anchronos.clojure.ui.editor;

import org.eclipse.swt.graphics.RGB;

/**
 * Default color constants used for syntax highlighting a clojure file.
 * 
 * @author km
 */
public interface ClojureColorConstants {
    RGB DEFAULT = new RGB(0, 0, 0);
    RGB SYMBOL = new RGB(0, 0, 128);
    RGB RESERVED_FUNCTION = new RGB(128, 0, 128);
    RGB LITERAL = new RGB(192, 0, 192);
    RGB COMMENT = new RGB(128, 0, 128);
    RGB STRING = new RGB(0, 128, 0);
}
