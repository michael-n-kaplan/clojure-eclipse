package org.maschinenstuermer.clojure.ui.wizard;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
	
	@Override
	protected IFile getModelFile(final IProject project) throws CoreException {
		final IFolder srcFolder = project.getFolder(getModelFolderName());
		final IFile file = srcFolder.getFile("main.clj");
		return file;
	}
	
	protected void enhanceProject(final IProject project, final IProgressMonitor monitor) throws CoreException {
		OutputImpl output = new OutputImpl();
		output.addOutlet(new Outlet(false, getEncoding(), null, true, project.getLocation().makeAbsolute().toOSString()));

		XpandExecutionContextImpl execCtx = new XpandExecutionContextImpl(output, null);
		execCtx.getResourceManager().setFileEncoding("UTF-8");
		execCtx.registerMetaModel(new JavaBeansMetaModel());

		XpandFacade facade = XpandFacade.create(execCtx);
		facade.evaluate("org::maschinenstuermer::clojure::ui::wizard::ClojureNewProject::main", getProjectInfo());

		try {
			copyResource(project, "core.clj");
		} catch (Exception e) {
			throw new CoreException(new Status(IStatus.ERROR, 
					"org.maschinenstuermer.clojure.ui", 
					"Error while copying example files!", 
					e));
		}
		project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	private void copyResource(final IProject project, String fileName)
			throws FileNotFoundException, IOException {
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = getClass().getResourceAsStream(fileName);
			final File file = project.getLocation().append(SRC_ROOT).append(fileName).toFile();
			outputStream = new FileOutputStream(file);
			int length;
			byte[] buffer = new byte[16384];
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
		} finally {
			close(inputStream, outputStream);
		}
	}

	private void close(final Closeable... closeables) throws IOException {
		for (Closeable closeable : closeables) {
			if (closeable != null)
				closeable.close();
		}
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