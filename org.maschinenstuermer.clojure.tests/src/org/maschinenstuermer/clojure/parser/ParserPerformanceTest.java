package org.maschinenstuermer.clojure.parser;

import static org.eclipse.xtext.junit.util.IResourcesSetupUtil.addNature;
import static org.eclipse.xtext.junit.util.IResourcesSetupUtil.cleanWorkspace;
import static org.eclipse.xtext.junit.util.IResourcesSetupUtil.createProject;
import static org.eclipse.xtext.junit.util.IResourcesSetupUtil.monitor;
import static org.eclipse.xtext.junit.util.IResourcesSetupUtil.root;
import static org.eclipse.xtext.junit.util.IResourcesSetupUtil.waitForAutoBuild;
import static org.eclipse.xtext.junit.util.JavaProjectSetupUtil.addSourceFolder;
import static org.eclipse.xtext.junit.util.JavaProjectSetupUtil.createJavaProject;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.xtext.ui.XtextProjectHelper;

public class ParserPerformanceTest extends TestCase {

	public void testValidSimpleModel() throws Exception {
		createJavaProjectWithRootSrc("clojure");
		final InputStream source = getClass().getResourceAsStream("core.clj");
		IFile file = createFile("clojure/src/core.clj", source);
		waitForAutoBuild();
		assertEquals(1184, file.findMarkers(EValidator.MARKER, true, IResource.DEPTH_INFINITE).length);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		cleanWorkspace();
	}

	private IJavaProject createJavaProjectWithRootSrc(String string) throws CoreException {
		IJavaProject project = createJavaProject(string);
		addNature(project.getProject(), XtextProjectHelper.NATURE_ID);
		addSourceFolder(project, "src");
		return project;
	}

	public static IFile createFile(final String wsRelativePath, final InputStream in)
	throws CoreException, InvocationTargetException,
	InterruptedException {
		return createFile(new Path(wsRelativePath), in);
	}

	public static IFile createFile(final IPath wsRelativePath, final InputStream in)
	throws CoreException, InvocationTargetException,
	InterruptedException {
		final IFile file = root().getFile(wsRelativePath);
		new org.eclipse.ui.actions.WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor)
			throws CoreException, InvocationTargetException,
			InterruptedException {
				create(file.getParent());
				file.delete(true, monitor());
				file.create(in, true, monitor());
			}

		}.run(monitor());
		return file;
	}


	private static void create(final IContainer container)
	throws CoreException, InvocationTargetException,
	InterruptedException {
		new org.eclipse.ui.actions.WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor)
			throws CoreException, InvocationTargetException,
			InterruptedException {
				if (!container.exists()) {
					create(container.getParent());
					if (container instanceof IFolder) {
						((IFolder) container).create(true, true, monitor());
					} else {
						IProject iProject = (IProject) container;
						createProject(iProject);
					}
				}
			}
		}.run(monitor());
	}
}
