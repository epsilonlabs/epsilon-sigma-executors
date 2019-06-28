/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.executors.model;

import org.eclipse.epsilon.eol.models.IModel;

/**
 * Base class for Cached model builders
 * 
 * @author Horacio Hoyos Rodriguez
 * @since 1.6
 *
 */
public abstract class CachedModelBuilder<M extends IModel, T extends CachedModelBuilder<M, T>>
        extends ModelBuilder<M, T> implements ICachedModelBuilder<M, T> {

    protected boolean useCache = true;

    @Override
    public T useCache(boolean useCache) {
        this.useCache = useCache;
        return self();
    }
}
