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

import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A builder to build different type of EMC models.
 *
 * @param <M> The type of model being built
 * @param <T> The type of model builder
 *
 * @author Horacio Hoyos Rodriguez
 * @author Beatriz Sanchez Piña
 * @since 1.6
 */
public interface IModelBuilder<M extends IModel, T extends IModelBuilder<M, T>> {

	/**
	 * Utility method to return "this" that allows extending models to return an appropriate T
	 * @return the model builder
	 */
	T self();

	/**
	 * Return a human readable name for the builder. By default the name of the specific IModel returned by the builder
     * is used.
	 * @return The name of the builder
	 */
	default String getName() {
		return ((Class<?>)
				((ParameterizedType)getClass().getGenericSuperclass())
						.getActualTypeArguments()[1]).getName();
	}

    /**
     * Set the model aliases
     * @param aliases the aliases to use for the model
     * @return the builder
     */
	T withAliases(String... aliases);

    /**
     * Store the model on disposal
     * @param storeOnDisposal True to store the model
     * @return the builder
     */
	T storeOnDisposal(boolean storeOnDisposal);

    /**
     * Read the model on load
     * @param readOnLoad True to read the model
     * @return the builder
     */
	T readOnLoad(boolean readOnLoad);

    /**
     * Use the provided name as the model name
     * @param name The name for the model
     * @return the builder
     */
	T withName(String name);

    /**
     * The Path of the model
     * @param modelPath The Path of the model
     * @return the builder
     */
	T withModelPath(Path modelPath);

	default T withModelPath(String modelPath) {
		return withModelPath(Paths.get(modelPath));
	}
	
    /**
     * Build the model
     * @return The M model configured based on the provided values.
     * @throws Exception
     */
    M build() throws Exception;
}
