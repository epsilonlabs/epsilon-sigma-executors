/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.executors.egl;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.executors.EpsilonLanguageExecutor;
import org.eclipse.epsilon.executors.ModuleWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The EGX executor.
 *
 * @author Horacio Hoyos Rodriguez
 */
public class SimpleEgxExecutor implements EpsilonLanguageExecutor<Object> {

	private static final Logger logger = LoggerFactory.getLogger(SimpleEgxExecutor.class);
	private EgxModule module;
	private ModuleWrap delegate;
	
	/**
	 * Instantiates a new simple EGX executor that uses an {@link EgxModule} as its module.
	 * @see EgxModule
	 */
	public SimpleEgxExecutor() {
		this(new EgxModule());
	}
	
	/**
	 * Instantiates a new simple EGX executor that uses the provided {@link EglTemplateFactory}
	 * to create a new {@link EgxModule} to use as its module
	 * @see EglTemplateFactory
	 * @see EgxModule
	 *
	 * @param templateFactory 		the template factory to use
	 */
	public SimpleEgxExecutor(EglTemplateFactory templateFactory) {
		this(new EgxModule(templateFactory));
	}
	
	/**
	 * Instantiates a new simple EGX executor with the provided {@link EgxModule}.
	 * @see EgxModule
	 *
	 * @param mdl 					the module
	 */
	public SimpleEgxExecutor(EgxModule mdl) {
		logger.info("Creating the EgxExecutor");
		module = mdl;
		delegate = new ModuleWrap(module);
	}
	
	@Override
	public Object execute() throws EolRuntimeException {
		logger.info("Executing EGX Script.");
		return module.execute();
	}

	public boolean parse(File file) throws Exception {
		return delegate.parse(file);
	}

	public boolean parse(String code) throws Exception {
		return delegate.parse(code);
	}

	public List<ParseProblem> getParseProblems() {
		return delegate.getParseProblems();
	}

	public void addModels(Collection<IModel> models) {
		delegate.addModels(models);
	}

	public void addParamters(Map<String, ?> parameters) {
		delegate.addParamters(parameters);
	}

	public void addNativeTypeDelegates(Collection<IToolNativeTypeDelegate> nativeDelegates) {
		delegate.addNativeTypeDelegates(nativeDelegates);
	}

	public void disposeModelRepository() {
		delegate.disposeModelRepository();
	}

	public void clearModelRepository() {
		delegate.clearModelRepository();
	}

	public void dispose() {
		delegate.dispose();
	}

	public void preProcess() {
	}

	public void postProcess() {
	}
	
}
