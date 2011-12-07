package swtterminator.preferences;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;
import terminator.Palettes;
import terminator.Terminator;
import terminator.TerminatorPreferences;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.graphics.FontData;

import e.util.GuiUtilities;

public class TerminatorPreferenceStore implements IPersistentPreferenceStore {
	File file;
	boolean changed = false;

	String[] SETTING_KEYS = { TerminatorPreferences.BACKGROUND_COLOR,
			TerminatorPreferences.CURSOR_COLOR,
			TerminatorPreferences.FOREGROUND_COLOR,
			TerminatorPreferences.SELECTION_COLOR, TerminatorPreferences.ALPHA,
			TerminatorPreferences.ALWAYS_SHOW_TABS,
			TerminatorPreferences.ANTI_ALIAS,
			TerminatorPreferences.BLINK_CURSOR,
			TerminatorPreferences.BLOCK_CURSOR,
			TerminatorPreferences.FANCY_BELL, TerminatorPreferences.FONT,
			TerminatorPreferences.HIDE_MOUSE_WHEN_TYPING,
			TerminatorPreferences.INITIAL_COLUMN_COUNT,
			TerminatorPreferences.INITIAL_ROW_COUNT,
			TerminatorPreferences.PALETTE,
			TerminatorPreferences.SCROLL_ON_KEY_PRESS,
			TerminatorPreferences.SCROLL_ON_TTY_OUTPUT,
			TerminatorPreferences.VISUAL_BELL,
			TerminatorPreferences.USE_ALT_AS_META };

	final static HashMap<String, String> DEFAULT_VALUES = new HashMap<String, String>();
	static {
		DEFAULT_VALUES.put(TerminatorPreferences.ALWAYS_SHOW_TABS, "false");
		DEFAULT_VALUES.put(TerminatorPreferences.SCROLL_ON_KEY_PRESS, "true");
		DEFAULT_VALUES.put(TerminatorPreferences.SCROLL_ON_TTY_OUTPUT, "false");
		DEFAULT_VALUES
				.put(TerminatorPreferences.HIDE_MOUSE_WHEN_TYPING, "true");
		DEFAULT_VALUES.put(TerminatorPreferences.VISUAL_BELL, "true");
		DEFAULT_VALUES.put(TerminatorPreferences.USE_ALT_AS_META, "false");

		DEFAULT_VALUES.put(TerminatorPreferences.ANTI_ALIAS, "true");
		DEFAULT_VALUES.put(TerminatorPreferences.BLINK_CURSOR, "true");
		DEFAULT_VALUES.put(TerminatorPreferences.BLOCK_CURSOR, "false");
		DEFAULT_VALUES.put(TerminatorPreferences.FANCY_BELL, "true");
		DEFAULT_VALUES.put(TerminatorPreferences.FONT, "monospace");
		DEFAULT_VALUES.put(TerminatorPreferences.PALETTE, "ANSI");

		DEFAULT_VALUES.put(TerminatorPreferences.BACKGROUND_COLOR, "0,0,0");
		DEFAULT_VALUES.put(TerminatorPreferences.CURSOR_COLOR, "0,0,255");
		DEFAULT_VALUES.put(TerminatorPreferences.FOREGROUND_COLOR, "24,24,24");
		DEFAULT_VALUES
				.put(TerminatorPreferences.SELECTION_COLOR, "179,212,255");
	}

	public TerminatorPreferenceStore(String filename) {
		System.setProperty("org.jessies.terminator.optionsFile", filename);
		Terminator.getPreferences().readFromDisk();
	}

	protected Vector<IPropertyChangeListener> listeners = new Vector<IPropertyChangeListener>();

	@Override
	public boolean getDefaultBoolean(String name) {
		return Boolean.parseBoolean(DEFAULT_VALUES.get(name));
	}

	@Override
	public double getDefaultDouble(String name) {
		return Double.parseDouble(DEFAULT_VALUES.get(name));
	}

	@Override
	public float getDefaultFloat(String name) {
		return Float.parseFloat(DEFAULT_VALUES.get(name));
	}

	@Override
	public int getDefaultInt(String name) {
		return Integer.parseInt(DEFAULT_VALUES.get(name));
	}

	@Override
	public long getDefaultLong(String name) {
		return Long.parseLong(DEFAULT_VALUES.get(name));
	}

	@Override
	public String getDefaultString(String name) {
		return DEFAULT_VALUES.get(name);
	}

	@Override
	public boolean getBoolean(String name) {
		return Terminator.getPreferences().getBoolean(name);
	}

	@Override
	public double getDouble(String name) {
		return Terminator.getPreferences().getDouble(name);
	}

	@Override
	public float getFloat(String name) {
		return (float) Terminator.getPreferences().getDouble(name);
	}

	@Override
	public int getInt(String name) {
		return Terminator.getPreferences().getInt(name);
	}

	@Override
	public long getLong(String name) {
		return Terminator.getPreferences().getInt(name);
	}

	@Override
	public String getString(String name) {
		TerminatorPreferences pref = Terminator.getPreferences();
		Object setting = pref.get(name);
		if (setting instanceof Color[]) {
			return terminator.Palettes.toString((Color[]) setting);
		} else if (setting instanceof Color) {
			Color c = pref.getColor(name);
			return "" + c.getRed() + "," + c.getGreen() + "," + c.getBlue();
		} else if (setting instanceof Font) {
			Font f = pref.getFont(name);
			int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
			int height = (int) Math.round(f.getSize() * 72.0 / resolution);
			FontData data = new FontData(f.getName(), height, f.getStyle());
			return data.toString();
		} else {
			return Terminator.getPreferences().getString(name);
		}
	}

	@Override
	public boolean isDefault(String name) {
		return Terminator.getPreferences().getString(name)
				.equals(DEFAULT_VALUES.get(name));
	}

	@Override
	public boolean needsSaving() {
		return changed;
	}

	@Override
	public void putValue(String name, String value) {
		changed = true;
		TerminatorPreferences pref = Terminator.getPreferences();
		Object setting = pref.get(name);
		if (setting instanceof Color[]) {
			pref.put(name, terminator.Palettes.fromString(value));
		} else if (setting instanceof Color) {
			String[] colors = value.split(",");
			Color newColor = new Color(Integer.parseInt(colors[0]),
					Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
			pref.put(name, newColor);
		} else if (setting instanceof Font) {
			FontData data = new FontData(value);
			int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
			int fontSize = (int) Math.round((double) data.getHeight()
					* resolution / 72.0);
			Font font = new Font(data.getName(), data.getStyle(), fontSize);
			pref.put(name, font);
		} else {
			pref.put(name, value);
		}
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		if (!listeners.contains((listener))) {
			listeners.addElement(listener);
		}
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		listeners.removeElement(listener);
	}

	public void firePropertyChangeEvent(String name, Object oldValue,
			Object newValue) {
		if (listeners.size() > 0
				&& (oldValue == null || !oldValue.equals(newValue))) {
			final PropertyChangeEvent pe = new PropertyChangeEvent(this, name,
					oldValue, newValue);
			for (int i = 0; i < listeners.size(); ++i) {
				IPropertyChangeListener l = listeners.elementAt(i);
				l.propertyChange(pe);
			}
		}
	}

	@Override
	public void setDefault(String name, double value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, float value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, long value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, String defaultObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setToDefault(String name) {
		Terminator.getPreferences().put(name, DEFAULT_VALUES.get(name));
	}

	@Override
	public void setValue(String name, double value) {
		firePropertyChangeEvent(name, value, getDouble(name));
		changed = true;
		Terminator.getPreferences().put(name, value);
	}

	@Override
	public void setValue(String name, float value) {
		firePropertyChangeEvent(name, value, getFloat(name));
		changed = true;
		Terminator.getPreferences().put(name, value);
	}

	@Override
	public void setValue(String name, int value) {
		firePropertyChangeEvent(name, value, getInt(name));
		changed = true;
		Terminator.getPreferences().put(name, value);
	}

	@Override
	public void setValue(String name, long value) {
		firePropertyChangeEvent(name, value, getLong(name));
		changed = true;
		Terminator.getPreferences().put(name, value);
	}

	@Override
	public void setValue(String name, String value) {
		firePropertyChangeEvent(name, value, getString(name));
		putValue(name, value);
	}

	@Override
	public void setValue(String name, boolean value) {
		firePropertyChangeEvent(name, value, getBoolean(name));
		changed = true;
		Terminator.getPreferences().put(name, value);
	}

	@Override
	public boolean contains(String name) {
		return Arrays.asList(SETTING_KEYS).contains(name);
	}

	@Override
	public void save() throws IOException {
		Terminator.getPreferences().writeToDisk();
	}
}
