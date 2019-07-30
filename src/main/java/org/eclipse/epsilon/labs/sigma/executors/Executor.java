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

import org.eclipse.epsilon.labs.sigma.executors.util.ExecutionTimeData;

import java.io.PrintStream;
import java.util.Optional;

/**
 * The Executor interface defines the API for Epsilon executors
 * @author Horacio Hoyos Rodriguez
 */
public interface Executor {

	ExecutionTimeData NO_TIME_DATA = new ExecutionTimeData();
	
	/**
	 * Return the execution time date for the executor. If no profiling was enabled, the return
	 * value will be emtpy.
	 * @see ExecutionTimeData
	 * @return An {@link Optional} with the execution time data, if any.
	 */
	Optional<ExecutionTimeData> getExecutionTimeData();

	/**
	 * Create a Runnable so the executor can be executed in a thread. Internally the
	 * {@link #invokeExecutor()} is called. Any exceptions will be wrapped inside a
	 * {@link RuntimeException}.
	 * @param <T> 			the return type of the executed module
	 * @return  the result of the execution
	 */
	<T> Runnable executeInThread();

	/**
	 * Execute the specific executor by executing all the execution stages.
	 * @return						the result of the execution
	 * @throws EpsilonExecutorException if there is an error during execution
	 * @param <R> 			the return type of the executed module
	 */
	<R> R invokeExecutor() throws EpsilonExecutorException;

	/**
	 * Disposes the executor. Implementing classes should perform any clean actions.
	 * This method should be invoked after execute.
	 * It is not invoked automatically because in some cases the user may need
	 * to access execution information that is still in the executor's context.
	 * The disposeModels flag determines if the models used by the module are also
	 * disposed (default true).
	 */
	void dispose();

	/**
	 * Change the output stream of the Executor. This will change the target of all the <code>print</code> and
	 * <code>println</code> statements in the script/code.
	 *
	 * @param outputStream          the stream to redirect the output to
	 * @return  a new Executor that uses the provided stream for output
	 * @since 2.1.0
	 */
	Executor redirectOutputStream(PrintStream outputStream);

	/**
	 * Change the output stream of the Executor. This will change the target of all the warning messages generated
	 * by the engine.
	 *
	 * @param warningStream          the stream to redirect the output to
	 * @return  a new Executor that uses the provided stream for output
	 * @since 2.1.0
	 */
	Executor redirectWarningStream(PrintStream warningStream);

	/**
	 * Change the output stream of the Executor. This will change the target of all the error messages generated
	 * by the engine.
	 *
	 * @param errorStream          the stream to redirect the output to
	 * @return  a new Executor that uses the provided stream for output
	 * @since 2.1.0
	 */
	Executor redirectErrorStream(PrintStream errorStream);

}