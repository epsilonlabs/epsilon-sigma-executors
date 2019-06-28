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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import org.eclipse.epsilon.eol.models.IModel;

/**
 * Base class for the model builders.
 *
 * @author Horacio Hoyos Rodriguez
 * @author Beatriz Sanchez Piña
 * @since 1.6
 */
public abstract class ModelBuilder<M extends IModel, T extends ModelBuilder<M, T>> implements IModelBuilder<M, T> {

    protected String name;
    protected String modelUri;
    protected Collection<String> aliases;
    protected boolean storeOnDisposal = false;
    protected boolean readOnLoad = true;

    @Override
    public T withAliases(String ... aliases) {
        this.aliases = Arrays.asList(aliases);
        return self();
    }

    @Override
    public T storeOnDisposal(boolean storeOnDisposal) {
        this.storeOnDisposal = storeOnDisposal;
        return self();
    }

    @Override
    public T readOnLoad(boolean readOnLoad) {
        this.readOnLoad = readOnLoad;
        return self();
    }
    
    @Override
    public T withName(String name) {
        this.name = name;
        return self();
    }
    
    @Override
    public T withModelPath(Path modelPath) {
        this.modelUri = modelPath.toString();
        return self();
    }

}
