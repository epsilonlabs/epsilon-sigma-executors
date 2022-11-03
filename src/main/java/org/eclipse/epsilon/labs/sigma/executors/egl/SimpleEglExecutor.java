/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.labs.sigma.executors.egl;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.erl.execute.control.RuleProfiler;
import org.eclipse.epsilon.labs.sigma.executors.EpsilonLanguageExecutor;
import org.eclipse.epsilon.labs.sigma.executors.ModuleWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The EGL executor.
 *
 * @author Horacio Hoyos Rodriguez
 */
public class SimpleEglExecutor implements EpsilonLanguageExecutor<Optional<String>> {

	private static final Logger logger = LoggerFactory.getLogger(SimpleEglExecutor.class);
	
	private EglTemplateFactoryModuleAdapter module;
	
	private ModuleWrap delegate;

	/**
	 * Instantiates a new simple EGL executor that uses an {@link EglTemplateFactoryModuleAdapter}
	 * (with an {@link EglTemplateFactory}) as its module.
	 * @see EglTemplateFactoryModuleAdapter
	 * @see EglTemplateFactory
	 */
	public SimpleEglExecutor() {
		this(new EglTemplateFactoryModuleAdapter(new EglTemplateFactory()));
    }

	/**
	 * Instantiates a new simple EGL executor that uses an {@link EglTemplateFactoryModuleAdapter}
	 * with the provided {@link EglTemplateFactory}
	 *
	 * @param templateFactory 		the template factory to use 
	 */
	public SimpleEglExecutor(EglTemplateFactory templateFactory) {
		this(new EglTemplateFactoryModuleAdapter(templateFactory));
	}
	
	/**
	 * Instantiates a new simple EGL executor that uses the provided {@link EglTemplateFactoryModuleAdapter}
	 *
	 * @param mdl 					the Template Factory Module Adapter to use
	 */
	public SimpleEglExecutor(EglTemplateFactoryModuleAdapter mdl) {
		logger.info("Creating the EglExecutor");
		module = mdl;
    	delegate = new ModuleWrap(module);
	}
	
	@Override
	public Optional<String> execute() throws EolRuntimeException {
		logger.info("Executing current EGL template.");
		String r = (String) ((EglTemplateFactoryModuleAdapter)module).execute();
		return Optional.ofNullable(r);
	}

	@Override
	public boolean parse(File file) throws Exception {
		return delegate.parse(file);
	}

	@Override
	public boolean parse(String code) throws Exception {
		return delegate.parse(code);
	}

	@Override
	public List<ParseProblem> getParseProblems() {
		return delegate.getParseProblems();
	}

	@Override
	public void addModels(Collection<IModel> models) {
		delegate.addModels(models);
	}

	@Override
	public void addParameters(Map<String, ?> parameters) {
		delegate.addParameters(parameters);
	}

	@Override
	public void addNativeTypeDelegates(Collection<IToolNativeTypeDelegate> nativeDelegates) {
		delegate.addNativeTypeDelegates(nativeDelegates);
	}

	@Override
	public Optional<RuleProfiler> getRuleProfiler() {
		return delegate.getRuleProfiler();
	}

	@Override
	public void disposeModelRepository() {
		delegate.disposeModelRepository();
	}

	@Override
	public void clearModelRepository() {
		delegate.clearModelRepository();
	}

	@Override
	public void dispose() {
		delegate.dispose();
	}

	@Override
	public void preProcess() {	}

	@Override
	public void postProcess() {	}

	@Override
	public void redirectOutputStream(PrintStream outputStream) {
		delegate.redirectOutputStream(outputStream);
	}

	@Override
	public void redirectWarningStream(PrintStream warningStream) {
		delegate.redirectWarningStream(warningStream);
	}

	@Override
	public void redirectErrorStream(PrintStream errorStream) {
		delegate.redirectErrorStream(errorStream);
	}

}
