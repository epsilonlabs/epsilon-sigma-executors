/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.labs.sigma.executors.etl;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.erl.execute.control.RuleProfiler;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.IEtlModule;
import org.eclipse.epsilon.etl.trace.TransformationTrace;
import org.eclipse.epsilon.labs.sigma.executors.EpsilonExecutorException;
import org.eclipse.epsilon.labs.sigma.executors.LanguageExecutor;
import org.eclipse.epsilon.labs.sigma.executors.ModuleWrap;
import org.eclipse.epsilon.labs.sigma.executors.ecl.SimpleEclExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ETL executor
 *
 * @author Horacio Hoyos Rodriguez
 */
public class SimpleEtlExecutor implements LanguageExecutor<TransformationTrace> {

    private static final Logger logger = LoggerFactory.getLogger(SimpleEtlExecutor.class);
    private IEtlModule module;
    private LanguageExecutor<TransformationTrace> delegate;

    /**
     * Instantiates a new simple ETL executor that uses an {@link EtlModule} as its module.
     * @see EtlModule
     */
    public SimpleEtlExecutor() {
    	this(new EtlModule());	
    }
    
    /**
    * Instantiates a new simple ETL executor that uses the provided {@link IEtlModule} as its module.
    * @see IEtlModule
    *
    * @param module 					the module
    */
    public SimpleEtlExecutor(IEtlModule module) {
    	this(module, new ModuleWrap<>(module));
    }


	@Override
	public TransformationTrace execute() throws EolRuntimeException {
		return (TransformationTrace) module.execute();
	}

	@Override
	public LanguageExecutor<TransformationTrace> parsed(File file) throws EpsilonExecutorException {
		logger.info("Parsing ETL file at {}", file.getAbsolutePath());
		return new SimpleEtlExecutor(this.module, this.delegate.parsed(file));
	}

	@Override
	public LanguageExecutor<TransformationTrace> parsed(String code) throws EpsilonExecutorException {
		logger.info("Parsing ETL code <{}...> ", code.substring(0, 100));
		return new SimpleEtlExecutor(this.module, this.delegate.parsed(code));
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

	private SimpleEtlExecutor(
		IEtlModule module,
		LanguageExecutor<TransformationTrace> delegate) {
		this.module = module;
		this.delegate = delegate;
	}

}
