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

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.IEclModule;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.executors.EpsilonLanguageExecutor;
import org.eclipse.epsilon.executors.ModuleWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ECL executor.
 *
 * @author Sina Madani
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
        return (MatchTrace) module.execute();
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
	public void addParamters(Map<String, ?> parameters) {
		delegate.addParamters(parameters);
	}

	@Override
	public void addNativeTypeDelegates(Collection<IToolNativeTypeDelegate> nativeDelegates) {
		delegate.addNativeTypeDelegates(nativeDelegates);
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
	public void preProcess() {
		
	}
	@Override
	public void postProcess() {
		
	}

}
