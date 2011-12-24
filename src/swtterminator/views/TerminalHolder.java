package swtterminator.views;

import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.pathvisio.gui.swt.awt.EmbeddedSwingComposite;

import swtterminator.controller.TerminalManager;
import terminator.view.JTerminalPane;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class TerminalHolder extends ViewPart {
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "swtterminator.views.TerminalHolder";

	private TerminalManager manager;

	private Frame frame;

	private JTerminalPane terminal;

	private EmbeddedSwingComposite composite;

	public JTerminalPane getTerminal() {
		return terminal;
	}

	public void setTerminal(JTerminalPane terminal) {
		this.terminal = terminal;
	}

	public Frame getFrame() {
		return frame;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}

	/**
	 * The constructor.
	 */
	public TerminalHolder() {
		manager = TerminalManager.getDefault();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		terminal = JTerminalPane.newShell();
		composite = new EmbeddedSwingComposite(parent, 0) {
			@Override
			protected JComponent createSwingComponent() {
				return terminal;
			}
		};
		composite.populate();
		terminal.start(manager);
		hookContextMenu();
		contributeToActionBars();
		manager.registerTerminal(this);
	}

	@Override
	public void dispose() {
		manager.unregisterTerminal(this);
		super.dispose();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				TerminalHolder.this.fillContextMenu(manager);
			}
		});
		/*Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);*/
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager menumanager) {
		manager.fillPulldown(this, menumanager);
	}

	private void fillContextMenu(IMenuManager menumanager) {
		//
		// Other plug-ins can contribute there actions here
		menumanager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager menumanager) {
		manager.fillToolBar(this, menumanager);
	}

	@Override
	public void setFocus() {
		composite.setFocus();
		terminal.grabFocus();
	}
}