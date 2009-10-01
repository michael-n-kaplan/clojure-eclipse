package org.anchronos.clojure.ui.editor;

import org.eclipse.jface.text.rules.ICharacterScanner;

/**
 * Extends {@link ICharacterScanner} interface with the ability to skip the
 * current char.
 * 
 * @author km
 */
public interface ICharacterScanner2 extends ICharacterScanner {
    /**
     * Skips the current char.
     */
    void skip();
}
