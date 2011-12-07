package swtterminator.preferences;

import java.util.List;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import swtterminator.Activator;
import terminator.TerminatorPreferences;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class Preferences
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {
	
	public Preferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("A demonstration of a preference page implementation");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new ColorFieldEditor(TerminatorPreferences.BACKGROUND_COLOR, "Background Color", getFieldEditorParent()));
		addField(new ColorFieldEditor(TerminatorPreferences.CURSOR_COLOR, "Cursor Color", getFieldEditorParent()));
		addField(new ColorFieldEditor(TerminatorPreferences.FOREGROUND_COLOR, "Foreground Color", getFieldEditorParent()));
		addField(new ColorFieldEditor(TerminatorPreferences.SELECTION_COLOR, "Selection Color", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TerminatorPreferences.ANTI_ALIAS, "Anti-alias text", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TerminatorPreferences.BLINK_CURSOR, "Blink cursor", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TerminatorPreferences.BLOCK_CURSOR, "Use block cursor", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TerminatorPreferences.FANCY_BELL, "High-quality rendering of the visual bell", getFieldEditorParent()));
		addField(new FontFieldEditor(TerminatorPreferences.FONT, "Font", "C:\\>",getFieldEditorParent()));
		addField(new BooleanFieldEditor(TerminatorPreferences.HIDE_MOUSE_WHEN_TYPING, "Hide mouse when typing", getFieldEditorParent()));
		List<String> names = terminator.Palettes.names();
		String[][] entries = new String[names.size()][2];
		for(int i = 0; i < entries.length; i++) {
			entries[i][0] = entries[i][1] = names.get(i);
		}
		addField(new ComboFieldEditor(TerminatorPreferences.PALETTE, "Palette", entries, getFieldEditorParent()));
		addField(new BooleanFieldEditor(TerminatorPreferences.SCROLL_ON_KEY_PRESS, "Scroll to bottom on key press", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TerminatorPreferences.SCROLL_ON_TTY_OUTPUT, "Scroll to bottom on output", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TerminatorPreferences.VISUAL_BELL, "Visual bell (as opposed to no bell)", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TerminatorPreferences.USE_ALT_AS_META, "Use alt key as meta key (for Emacs)", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}