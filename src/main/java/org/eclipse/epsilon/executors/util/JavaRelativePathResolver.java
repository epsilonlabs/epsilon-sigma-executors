/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.executors.util;

import org.eclipse.epsilon.eol.models.IRelativePathResolver;

import java.util.regex.Pattern;

/**
 * An IRelativePathResolver that resolves resource URLs in the gen(bin/target) folder to the src folder.
 * NOTE: This class is intended to be used in testing environments where it is desirable to access/modify
 * scripts in the src folder as opposed to the gen folder. In deployment stick to the resources as loaded
 * form the class or provide other means to access the scripts.
 * NOTE: The implemented strategy only works with resources added side-by-side with the Java code (i.e. in the
 * same folders).
 * NOTE: If your maven build changes the default locations of resources in the target folder then using the provided
 * find regex and replace value will fail to resolve the original files correctly. In this case you can provide your
 * own find regex and replace values (see {@link #setGenFolder(Pattern)} and {@link #setSrcFolder(String)}).
 *
 * This RelativePathResolver can be used to resolve ExL scripts or model locations, in the output folder of a Java
 * project to the original source folder. The gen folder lookup is a regex expression, which can be useful to match
 * complex structures.
 *
 * The class provides static fields for the basic java style (bin -&gt; src) and for maven like structures
 * (target/classes/ -&gt; src/main/java). By default java style resolution is used.

 *
 * <pre>
 * {@code
 * URL scriptUrl = SomeClass.getClass().getResource(/some/resource/location/myScript.eol);
 * // scriptUrl = "some/path/bin/some/resource/location/myScript.eol"
 * // Resolve
 * IRelativePathResolver pr = new JavaRelativePathResolver();
 * String srcPath = pr.resolve(scriptUrl.toString();
 * System.out.println(srcPath); // prints: some/path/src/some/resource/location/myScript.eol"
 * }
 * </pre>
 *
 * @author Horacio Hoyos Rodriguez
 * @since 1.6
 */
public class JavaRelativePathResolver extends BasePathResolver implements IRelativePathResolver {

    public static final Pattern JAVA_GEN_FOLDER = Pattern.compile("/bin/");
    public static final String JAVA_SRC_FOLDER = "/src/";
    public static final Pattern MAVEN_GEN_FOLDER = Pattern.compile("/target/classes/");
    public static final String MAVEN_SRC_FOLDER = "/src/main/java";

    private Pattern genFolder;
    private String srcFolder;

    public JavaRelativePathResolver() {
        super("");
        genFolder = JAVA_GEN_FOLDER;
        srcFolder = JAVA_SRC_FOLDER;
    }

    public JavaRelativePathResolver(String basePath) {
        super(basePath);
    }

    public void setGenFolder(Pattern genFolder) {
        this.genFolder = genFolder;
    }

    public void setSrcFolder(String srcFolder) {
        this.srcFolder = srcFolder;
    }

    @Override
    public String resolve(String s) {
        String fullPath = super.resolve(s);
        return genFolder.matcher(fullPath).replaceAll(srcFolder);
    }
}
