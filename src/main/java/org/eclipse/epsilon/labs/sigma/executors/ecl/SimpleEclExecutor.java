/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.labs.sigma.executors.ecl;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.IEclModule;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.erl.execute.control.RuleProfiler;
import org.eclipse.epsilon.labs.sigma.executors.EpsilonExecutorException;
import org.eclipse.epsilon.labs.sigma.executors.LanguageExecutor;
import org.eclipse.epsilon.labs.sigma.executors.ModuleWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ECL executor.
 *
 * @author Sina Madani
 */
public class SimpleEclExecutor implements LanguageExecutor<MatchTrace> {
	
	/**
	 * Instantiates a new simple ECL executor that uses an {@link EclModule} as its module.
	 * @see EclModule
	 */
	public SimpleEclExecutor() {
		this(new EclModule());
	}

	@Override
	public MatchTrace execute() throws EolRuntimeException {
		logger.info("Executing ECL module.");
        return module.execute();
	}

	@Override
	public LanguageExecutor<MatchTrace> parsed(File file) throws EpsilonExecutorException {
		logger.info("Parsing ECL file at {}", file.getAbsolutePath());
		return new SimpleEclExecutor(this.module, this.delegate.parsed(file));
	}

	@Override
	public LanguageExecutor<MatchTrace> parsed(String code) throws EpsilonExecutorException {
		logger.info("Parsing ECL code <{}...> ", code.substring(0, 100));
		return new SimpleEclExecutor(this.module, this.delegate.parsed(code));
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
	public void preProcess() { }

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

	static final Logger logger = LoggerFactory.getLogger(SimpleEclExecutor.class);

	private final IEclModule module;

	private final LanguageExecutor<MatchTrace> delegate;

	/**
	 * Instantiates a new simple ECL executor that uses the provided {@link IEclModule}.
	 * @see IEclModule
	 *
	 * @param mdl 					the ECL module to use
	 */
	private SimpleEclExecutor(IEclModule mdl) {
		this(mdl, new ModuleWrap<>(mdl));
	}
	private SimpleEclExecutor(IEclModule mdl, LanguageExecutor<MatchTrace> delegate) {
		this.module = mdl;
		this.delegate = delegate;
	}
}
