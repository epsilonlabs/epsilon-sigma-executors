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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.erl.IErlModule;
import org.eclipse.epsilon.erl.execute.RuleProfiler;

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
public class ModuleWrap implements EpsilonLanguageExecutor<Object> {

	final IEolModule module;
	
	public ModuleWrap(IEolModule module) {
		this.module = module;
	}

	@Override
	public boolean parse(File file) throws Exception {
		return module.parse(file);
	}

	@Override
	public boolean parse(String code) throws Exception {
		return module.parse(code);
	}

	@Override
	public List<ParseProblem> getParseProblems() {
		return module.getParseProblems();
	}

	@Override
	public void addModels(Collection<IModel> models) {
		this.module.getContext().getModelRepository().addModels(models.toArray(new IModel[0]));
	}

	@Override
	public void addParamters(Map<String, ?> parameters) {
		this.module.getContext().getFrameStack().put(parameters, true);
	}

	@Override
	public void addNativeTypeDelegates(Collection<IToolNativeTypeDelegate> nativeDelegates) {
		this.module.getContext().getNativeTypeDelegates().addAll(nativeDelegates);
	}

	@Override
	public Optional<RuleProfiler> getRuleProfiler() {
		if (module instanceof IErlModule) {
			return Optional.of(((IErlModule)module).getContext().getExecutorFactory().getRuleProfiler());
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
	public Object execute() throws EolRuntimeException {
		throw new UnsupportedOperationException("The ModuleWrap does not support the execute method.");
	}

}
