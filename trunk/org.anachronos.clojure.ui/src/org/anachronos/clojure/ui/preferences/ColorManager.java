package org.anachronos.clojure.ui.preferences;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Manages the editor colors. Is responsible for disposing all used colors.
 * 
 * @author km
 */
public class ColorManager {
    protected final Map<RGB, Color> fColorTable = new HashMap<RGB, Color>(10);

    public void dispose() {
	final Collection<Color> colors = fColorTable.values();
	for (final Color color : colors) {
	    color.dispose();
	}
    }

    public Color getColor(RGB rgb) {
	Color color = fColorTable.get(rgb);
	if (color == null) {
	    color = new Color(Display.getCurrent(), rgb);
	    fColorTable.put(rgb, color);
	}
	return color;
    }
}
