/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.labs.sigma.executors.eml;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.ecl.IEclModule;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.eml.execute.context.EmlContext;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.erl.execute.control.RuleProfiler;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.IEtlModule;
import org.eclipse.epsilon.labs.sigma.executors.EpsilonExecutorException;
import org.eclipse.epsilon.labs.sigma.executors.LanguageExecutor;
import org.eclipse.epsilon.labs.sigma.executors.ModuleWrap;
import org.eclipse.epsilon.labs.sigma.executors.ecl.SimpleEclExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The EML executor.
 *
 * @author Horacio Hoyos Rodriguez
 */
public class SimpleEmlExecutor implements LanguageExecutor<EmlTraces> {
	
    /**
     * Instantiates a new simple EML executor that uses an {@link EtlModule} as its module.
     * @see EtlModule
     */
    public SimpleEmlExecutor() {
    	this(new EtlModule());
    }
    
    /**
     * Instantiates a new simple EML executor that uses the provided {@link IEtlModule} as its module.
     * @see IEtlModule
     *
     * @param mdl 					the module
     */
    public SimpleEmlExecutor(IEtlModule mdl) {
    	logger.info("Creating the EtlExecutor");
    	module = mdl;
    	delegate = new ModuleWrap<>(module);
    }
    
	@Override
	public EmlTraces execute() throws EolRuntimeException {
		module.execute();
		EmlContext c = (EmlContext) module.getContext();
		return new EmlTraces(c.getMatchTrace(), c.getMergeTrace());
	}

	@Override
	public LanguageExecutor<EmlTraces> parsed(File file) throws EpsilonExecutorException {
		logger.info("Parsing EML file at {}", file.getAbsolutePath());
		return new SimpleEmlExecutor(this.module, this.delegate.parsed(file));
	}

	@Override
	public LanguageExecutor<EmlTraces> parsed(String code) throws EpsilonExecutorException {
		logger.info("Parsing EML code <{}...> ", code.substring(0, 100));
		return new SimpleEmlExecutor(this.module, this.delegate.parsed(code));
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

	private static final Logger logger = LoggerFactory.getLogger(SimpleEmlExecutor.class);
	private IEtlModule module;
	private final LanguageExecutor<EmlTraces> delegate;
	private SimpleEmlExecutor(IEtlModule mdl, LanguageExecutor<EmlTraces> delegate) {
		this.module = mdl;
		this.delegate = delegate;
	}
}
