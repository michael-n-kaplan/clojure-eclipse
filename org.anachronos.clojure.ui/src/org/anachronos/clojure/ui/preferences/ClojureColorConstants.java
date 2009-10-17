package org.anachronos.clojure.ui.preferences;

import org.eclipse.swt.graphics.RGB;

/**
 * Default color constants used for syntax highlighting a clojure file.
 * 
 * @author km
 */
public interface ClojureColorConstants {
    String CHARACTER_COLOR_PREF_KEY = "CLJ_CHARACTER_COLOR_PREF_KEY";
    String COMMENT_PREF_KEY = "CLJ_COMMENT_PREF_KEY";

    String NUMBER_PREF_KEY = "CLJ_NUMBER_PREF_KEY";
    String STRING_PREF_KEY = "CLJ_STRING_PREF_KEY";

    String LIST_PREF_KEY = "CLJ_LIST_PREF_KEY";
    String MAP_PREF_KEY = "CLJ_MAP_PREF_KEY";
    String VECTOR_PREF_KEY = "CLJ_VECTOR_PREF_KEY";

    String KEYWORD_PREF_KEY = "CLJ_KEYWORD_PREF_KEY";
    String SYMBOL_PREF_KEY = "CLJ_SYMBOL_PREF_KEY";
    String PREDEFINED_SYMBOL_PREF_KEY = "CLJ_PREDEFINED_SYMBOL_PREF_KEY";

    RGB CHARACTER = new RGB(0, 255, 0);
    RGB COMMENT = new RGB(128, 0, 128);

    RGB NUMBER = new RGB(0, 0, 255);
    RGB STRING = new RGB(0, 128, 0);

    RGB LIST = new RGB(128, 128, 128);
    RGB MAP = new RGB(128, 128, 0);
    RGB VECTOR = new RGB(0, 128, 128);

    RGB KEYWORD = new RGB(192, 0, 192);
    RGB SYMBOL = new RGB(0, 0, 0);
    RGB PREDEFINED_SYMBOL = new RGB(0, 0, 192);
}
