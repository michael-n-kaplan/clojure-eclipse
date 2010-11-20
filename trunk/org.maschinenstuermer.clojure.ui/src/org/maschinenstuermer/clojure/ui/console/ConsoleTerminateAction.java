package org.maschinenstuermer.clojure.ui.console;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.commands.ITerminateHandler;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.internal.ui.DebugPluginImages;
import org.eclipse.debug.internal.ui.IDebugHelpContextIds;
import org.eclipse.debug.internal.ui.IInternalDebugUIConstants;
import org.eclipse.debug.internal.ui.commands.actions.DebugCommandService;
import org.eclipse.debug.internal.ui.views.console.ConsoleMessages;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IUpdate;

@SuppressWarnings("restriction")
public class ConsoleTerminateAction extends Action implements IUpdate {

	private ClojureConsole console;
	private final IWorkbenchWindow window;

	/**
	 * Creates a terminate action for the console 
	 */
	public ConsoleTerminateAction(final IWorkbenchWindow window, final ClojureConsole console) {
		super(ConsoleMessages.ConsoleTerminateAction_0); 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IDebugHelpContextIds.CONSOLE_TERMINATE_ACTION);
		this.console = console;
		this.window = window;
		setToolTipText(ConsoleMessages.ConsoleTerminateAction_1); 
		setImageDescriptor(DebugPluginImages.getImageDescriptor(IInternalDebugUIConstants.IMG_LCL_TERMINATE));
		setDisabledImageDescriptor(DebugPluginImages.getImageDescriptor(IInternalDebugUIConstants.IMG_DLCL_TERMINATE));
		setHoverImageDescriptor(DebugPluginImages.getImageDescriptor(IInternalDebugUIConstants.IMG_LCL_TERMINATE));
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IDebugHelpContextIds.CONSOLE_TERMINATE_ACTION);
		update();
	}

	public void update() {
		IProcess process = console.getProcess(); 
		setEnabled(process.canTerminate());
	}
	
	public void run() {
		IProcess process = console.getProcess();
		List<Object> targets = collectTargets(process);
		targets.add(process);
        DebugCommandService service = DebugCommandService.getService(window);
        service.executeCommand(ITerminateHandler.class, targets.toArray(), null);
	}
	
	/**
	 * Collects targets associated with a process.
	 * 
	 * @param process
	 * @return associated targets
	 */
	private List<Object> collectTargets(IProcess process) {
        final ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        final ILaunch[] launches = launchManager.getLaunches();
        final List<Object> targets = new ArrayList<Object>();
        for (int i = 0; i < launches.length; i++) {
        	final ILaunch launch = launches[i];
        	final IProcess[] processes = launch.getProcesses();
            for (int j = 0; j < processes.length; j++) {
            	final IProcess process2 = processes[j];
                if (process2.equals(process)) {
                	final IDebugTarget[] debugTargets = launch.getDebugTargets();
                    for (int k = 0; k < debugTargets.length; k++) {
                        targets.add(debugTargets[k]);
                    }
                    return targets; // all possible targets have been terminated for the launch.
                }
            }
        }
        return targets;
    }

    public void dispose() {
	    console = null;
	}
}
