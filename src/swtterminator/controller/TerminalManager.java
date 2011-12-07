package swtterminator.controller;

import java.awt.Dimension;
import java.util.HashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;

import e.gui.MenuItemProvider;
import e.util.Preferences.Listener;
import swtterminator.views.TerminalHolder;
import swtterminator.views.TerminalHolderRestorable;
import terminator.view.JTerminalPane;
import terminator.view.TerminalPaneHost;
import terminator.Terminator;

public class TerminalManager implements TerminalPaneHost, Listener {
	class NewTerminalAction extends Action {
		private TerminalHolder terminal;

		public NewTerminalAction(TerminalHolder terminal) {
			this.terminal = terminal;
			
			this.setText("New Terminal");
			this.setToolTipText("Open new Terminal");
			this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		}
		
		public void run() {
			TerminalManager.this.newTerminal(terminal, false);
		}
	}
	
	public static final String RESTORABLE_ID = TerminalHolderRestorable.ID;
	public static final String VOLATILE_ID = TerminalHolder.ID;
	
	static TerminalManager instance = new TerminalManager();
	private HashSet<TerminalHolder> terminals = new HashSet<TerminalHolder>();

	public static TerminalManager getDefault() {
		return instance;
	}
	
	public TerminalManager() {
		Terminator.getPreferences().addPreferencesListener(this);
	}

	@Override
	public void cycleTab(int delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveCurrentTab(int direction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectedTabIndex(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean confirmClose(String processesUsingTty) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void closeTerminalPane(JTerminalPane terminalPane) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFrameTitle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminalNameChanged(JTerminalPane terminalPane) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTerminalSize(Dimension size) {
		// TODO Auto-generated method stub

	}

	@Override
	public MenuItemProvider createMenuItemProvider(JTerminalPane terminalPane) {
		// TODO Auto-generated method stub
		return null;
	}

	public void registerTerminal(TerminalHolder terminal) {
		this.terminals.add(terminal);
	}

	public void unregisterTerminal(TerminalHolder terminal) {
		this.terminals.remove(terminal);
	}

	public void newTerminal(boolean isStable) {
		newTerminal(null, isStable);
	}
	
	void newTerminal(TerminalHolder terminal, boolean isStable) {
		IWorkbenchPage page;
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage();
		String id = null;
		if (terminals.size() != 0) {
			id = "id	" + terminals.size() + System.currentTimeMillis();
		}
		System.out.println(id);

		try {
			page.showView(isStable ? RESTORABLE_ID : VOLATILE_ID, id, IWorkbenchPage.VIEW_ACTIVATE);
		} catch (PartInitException e) {
			errorMessage("Cannot initiate new Terminal");
		}
	}

	void errorMessage(String text) {
		Status status = new Status(IStatus.ERROR, TerminalHolder.ID, text);
		StatusManager.getManager().handle(status, StatusManager.SHOW);
	}
	
	public void fillPulldown(TerminalHolder terminal, IMenuManager manager) {
		manager.add(new NewTerminalAction(terminal));
	}
	
	public void fillToolBar(TerminalHolder terminal, IToolBarManager manager) {
		manager.add(new NewTerminalAction(terminal));
	}

	@Override
	public void preferencesChanged() {
		for(TerminalHolder t : terminals) {
			t.getTerminal().optionsDidChange();
		}
	} 
}
