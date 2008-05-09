/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A package fragment is a portion of the workspace corresponding to an entire package,
 * or to a portion thereof. The distinction between a package fragment and a package
 * is that a package with some name is the union of all package fragments in the class path
 * which have the same name.
 * <p>
 * Package fragments elements need to be opened before they can be navigated or manipulated.
 * The children are of type <code>IJavaScriptUnit</code> (representing a source file) or
 * <code>IClassFile</code> (representing a binary class file).
 * The children are listed in no particular order.
 * </p>
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 *  
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public interface IPackageFragment extends IParent, IJavaScriptElement, IOpenable, ISourceManipulation {

	/**
	 * <p>
	 * The name of package fragment for the default package (value: the empty
	 * string, <code>""</code>).
	 * </p>
 	*/
	public static final String DEFAULT_PACKAGE_NAME = ""; //$NON-NLS-1$
	/**
	 * Returns whether this fragment contains at least one Java resource.
	 * @return true if this fragment contains at least one Java resource, false otherwise
	 * @exception JavaScriptModelException if this element does not exist or if an
	 *		exception occurs while accessing its corresponding resource.
	 */
	boolean containsJavaResources() throws JavaScriptModelException;
	/**
	 * Creates and returns a compilation unit in this package fragment
	 * with the specified name and contents. No verification is performed
	 * on the contents.
	 *
	 * <p>It is possible that a compilation unit with the same name already exists in this
	 * package fragment.
	 * The value of the <code>force</code> parameter effects the resolution of
	 * such a conflict:<ul>
	 * <li> <code>true</code> - in this case the compilation is created with the new contents</li>
	 * <li> <code>false</code> - in this case a <code>JavaScriptModelException</code> is thrown</li>
	 * </ul>
	 *
	 * @param contents the given contents
	 * @param force specify how to handle conflict is the same name already exists
	 * @param monitor the given progress monitor
	 * @param name the given name
	 * @exception JavaScriptModelException if the element could not be created. Reasons include:
	 * <ul>
	 * <li> This Java element does not exist (ELEMENT_DOES_NOT_EXIST)</li>
	 * <li> A <code>CoreException</code> occurred while creating an underlying resource
	 * <li> The name is not a valid compilation unit name (INVALID_NAME)
	 * <li> The contents are <code>null</code> (INVALID_CONTENTS)
	 * </ul>
	 * @return a compilation unit in this package fragment
	 * with the specified name and contents
	 */
	IJavaScriptUnit createCompilationUnit(String name, String contents, boolean force, IProgressMonitor monitor) throws JavaScriptModelException;
	/**
	 * Returns the class file with the specified name
	 * in this package (for example, <code>"Object.class"</code>).
	 * The ".class" suffix is required.
	 * This is a handle-only method.  The class file may or may not be present.
	 * @param name the given name
	 * @return the class file with the specified name in this package
	 */
	IClassFile getClassFile(String name);
	/**
	 * Returns all of the class files in this package fragment.
	 *
	 * <p>Note: it is possible that a package fragment contains only
	 * compilation units (in other words, its kind is <code>K_SOURCE</code>), in
	 * which case this method returns an empty collection.
	 *
	 * @exception JavaScriptModelException if this element does not exist or if an
	 *		exception occurs while accessing its corresponding resource.
	 * @return all of the class files in this package fragment
	 */
	IClassFile[] getClassFiles() throws JavaScriptModelException;
	/**
	 * Returns the compilation unit with the specified name
	 * in this package (for example, <code>"Object.js"</code>).
	 * The name has to be a valid compilation unit name.
	 * This is a handle-only method.  The compilation unit may or may not be present.
	 *
	 * @param name the given name
	 * @return the compilation unit with the specified name in this package
	 * @see JavaScriptConventions#validateCompilationUnitName(String name, String sourceLevel, String complianceLevel)
	 */
	IJavaScriptUnit getJavaScriptUnit(String name);
	/**
	 * Returns all of the compilation units in this package fragment.
	 *
	 * <p>Note: it is possible that a package fragment contains only
	 * class files (in other words, its kind is <code>K_BINARY</code>), in which
	 * case this method returns an empty collection.
	 * </p>
	 *
	 * @exception JavaScriptModelException if this element does not exist or if an
	 *		exception occurs while accessing its corresponding resource.
	 * @return all of the compilation units in this package fragment
	 */
	IJavaScriptUnit[] getJavaScriptUnits() throws JavaScriptModelException;
	/**
	 * Returns all of the compilation units in this package fragment that are
	 * in working copy mode and that have the given owner.
	 * <p>
	 * Only existing working copies are returned. So a compilation unit handle that has no
	 * corresponding resource on disk will be included if and only if is in working copy mode.
	 * </p>
	 * <p>Note: it is possible that a package fragment contains only
	 * class files (in other words, its kind is <code>K_BINARY</code>), in which
	 * case this method returns an empty collection.
	 * </p>
	 *
	 * @param owner the owner of the returned compilation units
	 * @exception JavaScriptModelException if this element does not exist or if an
	 *		exception occurs while accessing its corresponding resource.
	 * @return all of the compilation units in this package fragment
	 * @since 3.0
	 */
	IJavaScriptUnit[] getJavaScriptUnits(WorkingCopyOwner owner) throws JavaScriptModelException;
	/**
	 * Returns the dot-separated package name of this fragment, for example
	 * <code>"java.lang"</code>, or <code>""</code> (the empty string),
	 * for the default package.
	 *
	 * @return the dot-separated package name of this fragment
	 */
	String getElementName();
	/**
	 * Returns this package fragment's root kind encoded as an integer.
	 * A package fragment can contain source files (i.e. files with one of
	 * the {@link JavaScriptCore#getJavaScriptLikeExtensions() Java-like extensions}),
	 * or <code>.class</code> files. This is a convenience method.
	 *
	 * @exception JavaScriptModelException if this element does not exist or if an
	 *		exception occurs while accessing its corresponding resource.
	 * @return this package fragment's root kind encoded as an integer
	 * @see IPackageFragmentRoot#K_SOURCE
	 * @see IPackageFragmentRoot#K_BINARY
	 */
	int getKind() throws JavaScriptModelException;
	/**
	 * Returns an array of non-Java resources contained in this package fragment.
	 * <p>
	 * Non-Java resources includes other files and folders located in the same
	 * directory as the compilation units or class files for this package
	 * fragment. Source files excluded from this package by virtue of
	 * inclusion/exclusion patterns on the corresponding source classpath entry
	 * are considered non-Java resources and will appear in the result
	 * (possibly in a folder).
	 * </p>
	 *
	 * @exception JavaScriptModelException if this element does not exist or if an
	 *		exception occurs while accessing its corresponding resource.
	 * @return an array of non-Java resources (<code>IFile</code>s,
	 *              <code>IFolder</code>s, or <code>IStorage</code>s if the
	 *              package fragment is in an archive) contained in this package
	 *              fragment
	 * @see IIncludePathEntry#getInclusionPatterns()
	 * @see IIncludePathEntry#getExclusionPatterns()
	 */
	Object[] getNonJavaScriptResources() throws JavaScriptModelException;
	/**
	 * Returns whether this package fragment's name is
	 * a prefix of other package fragments in this package fragment's
	 * root.
	 *
	 * @exception JavaScriptModelException if this element does not exist or if an
	 *		exception occurs while accessing its corresponding resource.
	 * @return true if this package fragment's name is a prefix of other package fragments in this package fragment's root, false otherwise
	 */
	boolean hasSubpackages() throws JavaScriptModelException;
	/**
	 * Returns whether this package fragment is a default package.
	 * This is a handle-only method.
	 *
	 * @return true if this package fragment is a default package
	 */
	boolean isDefaultPackage();

	boolean isSource();
}
