/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.executors;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;

/**
 * The IEpsilonLanguageExecutor defines a common executor API that the different Epsilon languages 
 * can use to facilitate running Epsilon scripts in standalone applications.
 * <p>
 * The API provides 4 phases to the execution of an Epsilon Module: {@link #preProcess}, {@link #execute},
 * {@link #postProcess}, and {@link #dispose}.
 * The 4 phases should provide enough flexibility for specific executors to correctly prepare
 * execution, execute, do any post execution processing and finally dispose the executor.
 *
 * @param <R> 						the specific type returned by the executor
 *
 * @author Horacio Hoyos Rodriguez
 *
 */
public interface EpsilonLanguageExecutor<R> {

	/**
	 * Parse the file using the language parser.
	 * @param file					the file to parse
	 * @return true, if the File contains a valid program for the language
	 * @throws Exception if the parser encounters any issues
	 */
	boolean parse(File file) throws Exception;
	
	/**
	 * Parse the string using the language parser.
	 * @param code					A string that contains the code to parse
	 * @return true, if the File contains a valid program for the language
	 * @throws Exception if the parser encounters any issues
	 */
	boolean parse(String code) throws Exception;
	
	/**
	 * Return a list of parsing problems.
	 * @see #parse(File)
	 * @return	a list of parse problems
	 */
	List<ParseProblem> getParseProblems();
	
	/**
	 * Add the collection of models to the models used during execution.
	 * <p>
	 * 
	 * @param models				the models to add
	 * @see IModel
	 */
	void addModels(Collection<IModel> models);
	
	/**
	 * Add parameters to the executor. Parameters allow the language executor to access additional,
	 * external, information. Parameters are defined as key:value pairs, where the key is the name
	 * by which the parameter can be identified in the program, and the value is any Object.
	 * @param parameters			a Map of key:value pairs.
	 */
	void addParamters(final Map<String, ?> parameters);
	
	/**
	 * Add any required NativeTypeDelegates. Native type delegates allow Java classes to be directly
	 * used inside E?L programs. 
	 * @param nativeDelegates		the native type delegates to use for execution
	 * @see IToolNativeTypeDelegate
	 */
	void addNativeTypeDelegates(Collection<IToolNativeTypeDelegate> nativeDelegates);
	
	/**
	 * Dispose the model repository used by the executor. This methods effectively disposes all
	 * models from the repository. Model disposal is model type specific, but in general it is
	 * expected that models would be stored and unloaded from memory. As a result, after this
	 * method is called, the models used in this executor can not be used further.
	 */
	void disposeModelRepository();
	
	/**
	 * Clear the model repository used by the executor. As opposed to {@link #disposeModelRepository()},
	 * this method only removes the models from the repository, i.e. the models can be used further.
	 */
	void clearModelRepository();

	/**
	 * Dispose the executor. All models, parameters and native type delegates will be removed/unloaded.
	 */
	void dispose();

    /**
     * This method will be invoked before execution of the script
     */
    void preProcess();
    
    /**
     * This method will be invoked after execution of the script
     */
    void postProcess();

    /**
     * Execute the provided script, against the list of models using the executor's module.
     * @throws EolRuntimeException if there is an exception during execution
	 * @return the result of the execution
     */
    R execute() throws EolRuntimeException;
   
}
