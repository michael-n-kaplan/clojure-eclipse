package org.anachronos.clojure.ui.preferences;

import org.eclipse.swt.graphics.RGB;

/**
 * Default color constants used for syntax highlighting a clojure file.
 * 
 * @author km
 */
public interface ClojureColorConstants {
    RGB CHARACTER = new RGB(0, 255, 0);
    RGB COMMENT = new RGB(128, 0, 128);
    RGB KEYWORD = new RGB(192, 0, 192);
    RGB LIST = new RGB(128, 128, 128);
    RGB MAP = new RGB(128, 128, 0);
    RGB NUMBER = new RGB(0, 0, 255);
    RGB STRING = new RGB(0, 128, 0);
    RGB SYMBOL = new RGB(0, 0, 128);
    RGB VECTOR = new RGB(0, 128, 128);
    RGB PREDEFINED_SYMBOL = new RGB(0, 0, 192);
}
