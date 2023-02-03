/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.labs.sigma.executors.eol;

import org.eclipse.epsilon.labs.sigma.executors.LanguageExecutor;

import java.util.List;

/**
 * The IEolExecutor API provides additional methods to invoke a single operation inside an EOL 
 * script. The desired operation can be set using {@link #changeOperation(String)} and
 * {@link #changeOperation(String, List)}. Setting the operation name with a non-empty string will set the execution
 * mode to {@link EolMode#OPERATION}. Executing in {@link EolMode#OPERATION} without previously setting the operation
 * name an Exception.
 * 
 * Setting the operation name to an empty string will result in the execution mode to change to 
 * {@link EolMode#SCRIPT}.
 *
 * @author Horacio Hoyos Rodriguez
 */
public interface EolExecutor extends LanguageExecutor<Object> {

	enum EolMode {
		SCRIPT,
		OPERATION
	}

    /**
     * Set the operation name to invoke and any parameters to pass to the operation being invoked.
	 * @param operationName 		the name of the operation to execute
     * @param arguments				the list of arguments to pass to the operation
	 * @return a new {@link SimpleEolExecutor} for the given operation
     */
	EolExecutor changeOperation(String operationName, List<Object> arguments);
	
	/**
     * Set the operation name to invoke. This method can be used for operations with no parameters.
	 *
	 * @param operationName 		the name of the operation to execute
	 * @return a new {@link SimpleEolExecutor} for the given operation
     */
	EolExecutor changeOperation(String operationName);
    
}
