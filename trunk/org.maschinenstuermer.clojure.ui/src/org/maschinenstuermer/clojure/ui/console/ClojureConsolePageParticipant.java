package org.maschinenstuermer.clojure.ui.console;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.debug.core.model.IStreamsProxy2;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

public class ClojureConsolePageParticipant implements IConsolePageParticipant, IDebugEventSetListener {

    private ClojureConsole console;
    
    private ConsoleTerminateAction terminateAction;

	private IPageBookViewPage page;

	private IContextActivation activatedContext;

	private IHandlerActivation activatedHandler;
    
	private AbstractHandler eofHandler = new AbstractHandler() {
        public Object execute(final ExecutionEvent event) 
        		throws org.eclipse.core.commands.ExecutionException {
            final IStreamsProxy proxy = getProcess().getStreamsProxy();
            if (proxy instanceof IStreamsProxy2) {
                final IStreamsProxy2 proxy2 = (IStreamsProxy2) proxy;
                try {
                    proxy2.closeInputStream();
                } catch (IOException e1) {
                }
            }
            return null;
        }
	};

    public void init(final IPageBookViewPage page, final IConsole console) {
        this.console = (ClojureConsole) console;
        this.page = page;

        final IPageSite site = page.getSite();
		terminateAction = new ConsoleTerminateAction(site.getWorkbenchWindow(), this.console);        		
		final IActionBars actionBars = site.getActionBars();
		configureToolBar(actionBars.getToolBarManager());
        
        DebugPlugin.getDefault().addDebugEventListener(this);
    }
    
    public void dispose() {
		DebugPlugin.getDefault().removeDebugEventListener(this);
		console = null;
    }

    protected void configureToolBar(IToolBarManager mgr) {
		mgr.appendToGroup(IConsoleConstants.LAUNCH_GROUP, terminateAction);
    }

    @SuppressWarnings("rawtypes")
	public Object getAdapter(Class required) {
        if(ILaunchConfiguration.class.equals(required)) {
        	ILaunch launch = getProcess().getLaunch();
        	if(launch != null) {
        		return launch.getLaunchConfiguration();
        	}
        	return null;
        }
        return null;
    }


    public void handleDebugEvents(DebugEvent[] events) {
        for (int i = 0; i < events.length; i++) {
            DebugEvent event = events[i];
            if (event.getSource().equals(getProcess())) {
                Runnable r = new Runnable() {
                    public void run() {
                        if (terminateAction != null) {
                            terminateAction.update();
                        }
                    }
                };
                
                PlatformUI.getWorkbench().getDisplay().asyncExec(r);           
            }
        }
    }
    
    protected IProcess getProcess() {
        return console != null ? console.getProcess() : null;
    }

    public void activated() {
        final IPageSite site = page.getSite();
        final IHandlerService handlerService = 
        	(IHandlerService) site.getService(IHandlerService.class);
        final IContextService contextService = 
        	(IContextService) site.getService(IContextService.class);
        activatedContext = contextService.activateContext("org.eclipse.debug.ui.console");
        activatedHandler = handlerService.activateHandler("org.eclipse.debug.ui.commands.eof", eofHandler);
    }

    public void deactivated() {
        final IPageSite site = page.getSite();
        final IHandlerService handlerService = 
        	(IHandlerService) site.getService(IHandlerService.class);
        final IContextService contextService = 
        	(IContextService) site.getService(IContextService.class);
        handlerService.deactivateHandler(activatedHandler);
		contextService.deactivateContext(activatedContext);
    }
}
