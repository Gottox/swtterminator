package swtterminator;

import java.io.File;
import java.net.URL;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

import swtterminator.preferences.TerminatorPreferenceStore;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	TerminatorPreferenceStore store;
	BundleContext context;
	
	@Override
	public IPreferenceStore getPreferenceStore() {
		return store;
	}

	// The plug-in ID
	public static final String PLUGIN_ID = "swtterminator"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		System.setProperty("sun.awt.noerasebackground", "true");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		this.context = context;
		getRunningConfigurationLocation();
		File configdir = this.getRunningConfigurationLocation();
		String config = configdir.toString() + File.separator + "terminator.config";
		store = new TerminatorPreferenceStore(config);
		System.setProperty("org.jessies.terminator.logDirectory", configdir.toString() + File.separator + "logs");

	}

	public BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public File getRunningConfigurationLocation() {
		ServiceTracker<?, Location> tracker = null;
		Filter filter = null;
		try {
			filter = context.createFilter(Location.CONFIGURATION_FILTER	);
		} catch (InvalidSyntaxException e) {
			// ignore this. It should never happen as we have tested the above
			// format.
		}
		tracker = new ServiceTracker<Object, Location>(context, filter, null);
		tracker.open();
		Location location = tracker.getService();
		URL url = location.getURL();
		if (!url.getProtocol().equals("file")) //$NON-NLS-1$
			return null;
		return new File(url.getFile());
	}
}
