/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.executors.etl;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.erl.execute.RuleProfiler;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.IEtlModule;
import org.eclipse.epsilon.etl.trace.TransformationTrace;
import org.eclipse.epsilon.executors.EpsilonLanguageExecutor;
import org.eclipse.epsilon.executors.ModuleWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ETL executor
 *
 * @author Horacio Hoyos Rodriguez
 * @since 1.6
 */
public class SimpleEtlExecutor implements EpsilonLanguageExecutor<TransformationTrace> {

    private static final Logger logger = LoggerFactory.getLogger(SimpleEtlExecutor.class);
    private IEtlModule module;
    private ModuleWrap delegate;

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
    * @param mdl 					the module
    */
    public SimpleEtlExecutor(IEtlModule mdl) {
    	logger.info("Creating the Etl Executor");
    	module = mdl;
    	delegate = new ModuleWrap(module);
    }
    

	@Override
	public TransformationTrace execute() throws EolRuntimeException {
		return (TransformationTrace) module.execute();
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
