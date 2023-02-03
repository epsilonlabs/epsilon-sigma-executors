/*********************************************************************
 * Copyright (c) 2019 The University of York.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 **********************************************************************/
package org.eclipse.epsilon.labs.sigma.executors;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.control.ExecutionController;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.erl.IErlModule;
import org.eclipse.epsilon.erl.execute.control.RuleProfiler;
import org.eclipse.epsilon.labs.sigma.DefaultSigma;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This implementation of EpsilonLanguageExecutor that provides a proxy into the IEolModule methods
 * that are needed to set/add the different values/references needed for execution.
 * 
 * Specific language implementations of EpsilonLanguageExecutor can use these class as a delegate
 * for all but the {@link #preProcess()}, {@link #postProcess()} and {@link #execute()} methods.
 * If any of these three methods is invoked an {@link UnsupportedOperationException} will be thrown. 
 *
 * @author Horacio Hoyos Rodriguez
 */
public class ModuleWrap<R> implements LanguageExecutor<R> {

	final IEolModule module;
	
	public ModuleWrap(IEolModule module) {
		this.module = module;
	}

	@Override
	public LanguageExecutor<R> parsed(File file) throws EpsilonExecutorException {
		try {
			this.module.parse(file);
			return this;
		} catch (Exception e) {
			logger.error("Unable to parse the provided file.", e);
			throw new EpsilonExecutorException("Unable to parse the provided file.", e);
		}
	}

	@Override
	public LanguageExecutor<R> parsed(String code) throws EpsilonExecutorException {
		try {
			this.module.parse(code);
			return this;
		} catch (Exception e) {
			logger.error("Unable to parse the provided code.", e);
			throw new EpsilonExecutorException("Unable to parse the provided file.", e);
		}
	}

	@Override
	public List<ParseProblem> parseProblems() {
		return module.getParseProblems();
	}

	@Override
	public void addModels(Collection<IModel> models) {
		logger.debug("Adding {} models to the model repository", models.size());
		this.module.getContext().getModelRepository().addModels(models.toArray(new IModel[0]));
	}

	@Override
	public void addParameters(Map<String, ?> parameters) {
		Map<String, Variable> params = new HashMap<>(parameters.size());
		for (Map.Entry<String, ?> entry : parameters.entrySet()) {
			if (entry.getValue() instanceof Variable) {
				logger.debug("Adding variable {}:{} to parameters", entry.getKey(), entry.getValue());
				params.put(entry.getKey(), (Variable) entry.getValue());
			}
			else {
				logger.debug("Creating a new variable to add the {}:{} to parameters", entry.getKey(), entry.getValue());
				params.put(entry.getKey(), Variable.createReadOnlyVariable(entry));
			}
		}
		this.module.getContext().getFrameStack().put(params, true);
	}

	@Override
	public void addNativeTypeDelegates(Collection<IToolNativeTypeDelegate> nativeDelegates) {
		logger.debug("Adding {} native type delegates to module", nativeDelegates.size());
		this.module.getContext().getNativeTypeDelegates().addAll(nativeDelegates);
	}

	@Override
	public Optional<RuleProfiler> getRuleProfiler() {
		if (module instanceof IErlModule) {
			final ExecutionController executionController = ((IErlModule) module).getContext().getExecutorFactory().getExecutionController();
			if (executionController instanceof RuleProfiler) {
				return Optional.of((RuleProfiler) executionController);
			}
		}
		return Optional.empty();
	}

	@Override
	public void disposeModelRepository() {
		module.getContext().getModelRepository().dispose();
	}

	@Override
	public void clearModelRepository() {
		module.getContext().getModelRepository().getModels().clear();
	}

	@Override
	public void dispose() {
		module.getContext().dispose();
	}

	@Override
	public void preProcess() {
		throw new UnsupportedOperationException("The ModuleWrap does not support the preProcess method.");
	}
	
	@Override
	public void postProcess() {
		throw new UnsupportedOperationException("The ModuleWrap does not support the postProcess method.");
	}

	@Override
	public R execute() throws EolRuntimeException {
		throw new UnsupportedOperationException("The ModuleWrap does not support the execute method.");
	}

	@Override
	public void redirectOutputStream(PrintStream outputStream) {
		module.getContext().setOutputStream(outputStream);
	}

	@Override
	public void redirectWarningStream(PrintStream warningStream) {
		module.getContext().setWarningStream(warningStream);
	}

	@Override
	public void redirectErrorStream(PrintStream errorStream) {
		module.getContext().setErrorStream(errorStream);
	}

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ModuleWrap.class);

}
