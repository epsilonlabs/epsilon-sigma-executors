/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.executors.ecl;

import java.io.File;
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
import org.eclipse.epsilon.erl.execute.RuleProfiler;
import org.eclipse.epsilon.executors.EpsilonLanguageExecutor;
import org.eclipse.epsilon.executors.ModuleWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ECL executor.
 *
 * @author Sina Madani
 * @since 1.6
 */
public class SimpleEclExecutor implements EpsilonLanguageExecutor<MatchTrace> {

	static final Logger logger = LoggerFactory.getLogger(SimpleEclExecutor.class);
	
	private final IEclModule module;
	
	private final ModuleWrap delegate;
	
	/**
	 * Instantiates a new simple ECL executor that uses an {@link EclModule} as its module.
	 * @see EclModule
	 */
	public SimpleEclExecutor() {
		this(new EclModule());
	}
	
	/**
	 * Instantiates a new simple ECL executor that uses the provided {@link IEclModule}.
	 * @see IEclModule
	 *
	 * @param mdl 					the ECL module to use
	 */
	public SimpleEclExecutor(IEclModule mdl) {
		logger.info("Creating the EclExecutor");
		module = mdl;
		delegate = new ModuleWrap(module);
	}

	@Override
	public MatchTrace execute() throws EolRuntimeException {
		logger.info("Executing ECL module.");
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

	public Optional<RuleProfiler> getRuleProfiler() {
		return delegate.getRuleProfiler();
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
