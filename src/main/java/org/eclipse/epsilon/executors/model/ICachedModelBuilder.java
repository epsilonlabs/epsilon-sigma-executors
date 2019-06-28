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
 * A model builder for models that support cache
 * @param <T> The type of model builder
 * @param <M> The type of model being built
 *
 * @author Horacio Hoyos Rodriguez
 * @author Beatriz Sanchez Piña
 * @since 1.6
 */
public interface ICachedModelBuilder<M extends IModel, T extends ICachedModelBuilder<M, T>> extends IModelBuilder<M, T> {
	
	/**
	 * Set the useCache for the model
	 * @param useCache				if true, the model will use an element cache.
	 * @return the modified model builder
	 */
    T useCache(boolean useCache);
    
}
