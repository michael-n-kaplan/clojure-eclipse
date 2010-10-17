package org.maschinenstuermer.clojure.ui.wizard;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandFacade;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.OutputImpl;
import org.eclipse.xtend.type.impl.java.JavaBeansMetaModel;
import org.eclipse.xtext.ui.XtextProjectHelper;
import org.eclipse.xtext.ui.util.JavaProjectFactory;
import org.eclipse.xtext.ui.util.ProjectFactory;
import org.eclipse.xtext.ui.wizard.AbstractProjectCreator;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class CustomClojureProjectCreator extends AbstractProjectCreator {
	protected static final String SRC_ROOT = "src";
	
	protected final List<String> SRC_FOLDER_LIST = ImmutableList.of(SRC_ROOT);

	@Inject
	private Provider<JavaProjectFactory> projectFactoryProvider;
	
	@Override
	protected ClojureProjectInfo getProjectInfo() {
		return (ClojureProjectInfo) super.getProjectInfo();
	}
	
	protected String getModelFolderName() {
		return SRC_ROOT;
	}
	
	@Override
	protected List<String> getAllFolders() {
        return SRC_FOLDER_LIST;
    }
	
	protected void enhanceProject(final IProject project, final IProgressMonitor monitor) throws CoreException {
		OutputImpl output = new OutputImpl();
		output.addOutlet(new Outlet(false, getEncoding(), null, true, project.getLocation().makeAbsolute().toOSString()));

		XpandExecutionContextImpl execCtx = new XpandExecutionContextImpl(output, null);
		execCtx.getResourceManager().setFileEncoding("UTF-8");
		execCtx.registerMetaModel(new JavaBeansMetaModel());

		XpandFacade facade = XpandFacade.create(execCtx);
		facade.evaluate("org::maschinenstuermer::clojure::ui::wizard::ClojureNewProject::main", getProjectInfo());

		project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	@Override
	protected ProjectFactory createProjectFactory() {
		return projectFactoryProvider.get();
	}
	

    protected String[] getProjectNatures() {
        return new String[] {
        	JavaCore.NATURE_ID,
			XtextProjectHelper.NATURE_ID
		};
    }
    
    protected String[] getBuilders() {
    	return new String[]{
    		JavaCore.BUILDER_ID,
			XtextProjectHelper.BUILDER_ID
		};
	}
}