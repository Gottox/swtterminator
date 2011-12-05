package swtterminator.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import swtterminator.controller.TerminalManager;

public class NewTerminal extends AbstractHandler {
	public NewTerminal() {
	}

	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TerminalManager manager = TerminalManager.getDefault();
		manager.newTerminal(false);
		return null;
	}
}
