/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.labs.sigma.executors;

/**
 * A base exception to wrap different exceptions that can be
 * thrown during execution of an executor
 *
 * @author Horacio Hoyos Rodriguez
 */
public class EpsilonExecutorException extends Exception {

	private static final long serialVersionUID = -7589325573726453221L;


	/**
	 * Instantiates a new epsilon executor exception.
	 */
	public EpsilonExecutorException() {
		super();
	}

	
	/**
	 * Instantiates a new epsilon executor exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public EpsilonExecutorException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Instantiates a new epsilon executor exception.
     *
     * @param message the message
     */
    public EpsilonExecutorException(String message) {
        super(message);
    }


	/**
	 * Instantiates a new epsilon executor exception.
	 *
	 * @param cause the cause
	 */
	public EpsilonExecutorException(Throwable cause) {
		super(cause);
	}
    
    
}
