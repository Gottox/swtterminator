package swtterminator.views;

import java.awt.Frame;

import javax.swing.JPanel;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

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

	private static final class HolderFocus implements FocusListener {
		private JPanel pane;

		public HolderFocus(JPanel pane) {
			this.pane = pane;
		}

		@Override
		public void focusLost(FocusEvent e) {
		}

		@Override
		public void focusGained(FocusEvent e) {
			pane.requestFocus();
		}
	}

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "swtterminator.views.TerminalHolder";

	private TerminalManager manager;

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
		Composite composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
	    Frame frame = SWT_AWT.new_Frame(composite);

		JTerminalPane terminal = JTerminalPane.newCommandWithName("/bin/bash",
				"Test", "/tmp");
		parent.addFocusListener(new HolderFocus(terminal));
		frame.add(terminal);
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
		// manager.fillPulldown(menumanager);
	}

	private void fillContextMenu(IMenuManager menumanager) {
		//
		// Other plug-ins can contribute there actions here
		menumanager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager menumanager) {
		// manager.fillToolBar(this, menumanager);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}