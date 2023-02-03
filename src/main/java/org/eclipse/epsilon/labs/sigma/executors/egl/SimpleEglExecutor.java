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
import org.eclipse.epsilon.labs.sigma.executors.EpsilonExecutorException;
import org.eclipse.epsilon.labs.sigma.executors.LanguageExecutor;
import org.eclipse.epsilon.labs.sigma.executors.ModuleWrap;
import org.eclipse.epsilon.labs.sigma.executors.ecl.SimpleEclExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The EGL executor.
 *
 * @author Horacio Hoyos Rodriguez
 */
public class SimpleEglExecutor implements LanguageExecutor<Optional<String>> {

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
		this(mdl, new ModuleWrap<>(mdl));
	}

	private SimpleEglExecutor(
		EglTemplateFactoryModuleAdapter mdl,
		LanguageExecutor<Optional<String>> delegate) {
		this.module = mdl;
		this.delegate = delegate;
	}


	@Override
	public Optional<String> execute() throws EolRuntimeException {
		logger.info("Executing current EGL template.");
		String r = (String) module.execute();
		return Optional.ofNullable(r);
	}

	@Override
	public LanguageExecutor<Optional<String>> parsed(File file) throws EpsilonExecutorException {
		logger.info("Parsing EGL file at {}", file.getAbsolutePath());
		return new SimpleEglExecutor(this.module, this.delegate.parsed(file));
	}

	@Override
	public LanguageExecutor<Optional<String>> parsed(String code) throws EpsilonExecutorException {
		logger.info("Parsing EGL code <{}...> ", code.substring(0, 100));
		return new SimpleEglExecutor(this.module, this.delegate.parsed(code));
	}

	@Override
	public List<ParseProblem> parseProblems() {
		return delegate.parseProblems();
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

	private static final Logger logger = LoggerFactory.getLogger(SimpleEglExecutor.class);

	private EglTemplateFactoryModuleAdapter module;

	private LanguageExecutor<Optional<String>> delegate;

}
