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

/**
 * A base path resolver that uses a prefix (basePath) to create full paths.
 * 
 * @author Horacio Hoyos Rodriguez
 * @since 1.6
 */
public class BasePathResolver implements IRelativePathResolver {

    private String basePath;

    public BasePathResolver(String basePath) {
        this.basePath = basePath;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public String resolve(String relativePath) {
        return basePath + relativePath;
    }
}
