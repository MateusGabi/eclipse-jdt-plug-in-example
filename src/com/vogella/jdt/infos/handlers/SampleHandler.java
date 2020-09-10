package com.vogella.jdt.infos.handlers;

import java.util.Arrays;
import java.util.function.Consumer;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.Document;

public class SampleHandler {
	public Object execute() throws ExecutionException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		
		IProject[] projects = workspaceRoot.getProjects();
		
		Arrays.stream(projects).forEach(project -> {
			try {
				printProjectInfo(project);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});		
		
		return new Object();
	}
	
	private void printProjectInfo(IProject project) throws CoreException, JavaModelException {
		System.out.println("Working in project " + project.getName());
		
		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(project);
			printPackageInfos(javaProject);
		}
	}

	private void printPackageInfos(IJavaProject javaProject) throws JavaModelException {
		IPackageFragment[] packageFragments = javaProject.getPackageFragments();
		
		Arrays.stream(packageFragments).forEach(fragment -> {
			try {
				if (fragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
					System.out.println("Package "+ fragment.getElementName());
					printICompilationUnitInfo(fragment);
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	private void printICompilationUnitInfo(IPackageFragment fragment) throws JavaModelException {
		Arrays.stream(fragment.getCompilationUnits()).forEach(unit -> {
			try {
				printCompilationUnitDetails(unit);
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});		
	}

	private void printCompilationUnitDetails(ICompilationUnit unit) throws JavaModelException {
		System.out.println("Source file " + unit.getElementName());
		
		Document document = new Document(unit.getSource());
		System.out.println("Has number of lines: "+ document.getNumberOfLines());
		
		printIMethods(unit);
	}

	private void printIMethods(ICompilationUnit unit) throws JavaModelException {
		Arrays.stream(unit.getTypes()).forEach(type -> {
			try {
				printIMethodDetails(type);
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		});
	}

	private void printIMethodDetails(IType type) throws JavaModelException {
		IMethod[] methods = type.getMethods();
		Arrays.stream(methods).forEach(method -> {
            try {
            	System.out.println("Method name " + method.getElementName());
				System.out.println("Signature " + method.getSignature());
				System.out.println("Return Type " + method.getReturnType());
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		});
	}
}
