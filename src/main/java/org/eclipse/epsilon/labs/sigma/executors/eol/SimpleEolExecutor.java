/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.labs.sigma.executors.eol;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.concurrent.EolModuleParallel;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.concurrent.EolContextParallel;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.erl.execute.control.RuleProfiler;
import org.eclipse.epsilon.labs.sigma.executors.ModuleWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The EOL executor.
 *
 * @author Horacio Hoyos Rodriguez
 */
public class SimpleEolExecutor implements EolExecutor {

	private static final Logger logger = LoggerFactory.getLogger(SimpleEolExecutor.class);

	private final Optional<String> operationName;
	private final List<Object> arguments;
	private final EolMode mode;
	private IEolModule module;
	private ModuleWrap delegate;
	
	/**
	 * Instantiates a new simple EOL executor that uses an {@link EolModuleParallel} (with one 
	 * thread) as its module.
	 * This executor will execute the complete code/script.
	 * @see EolModuleParallel
	 */
	public SimpleEolExecutor() {
		this(null, Collections.emptyList());
	}
	
	/**
	 * Instantiates a new simple EOL executor hat uses the provided {@link IEolModule} module.
	 * @see IEolModule
	 *
	 * @param mdl 					the module
	 */
	public SimpleEolExecutor(IEolModule mdl) {
		this(null, Collections.emptyList(), mdl);
	}
	
	/**
	 * Instantiates a new simple EOL executor that uses an {@link EolModuleParallel} (with one 
	 * thread) as its module, but that will only execute a single operation within the code/script.
	 * The constructor accepts a list of arguments to pass to the operation, i.e. arguments are
	 * position based.
	 * @see EolModuleParallel
	 * 
	 *
	 * @param oprtnNm 				the name of the operation to invoke
	 * @param arguments 			the arguments to be passed to the operation (position based).
	 * 								
	 */
	public SimpleEolExecutor(String oprtnNm, List<Object> arguments) {
		this(oprtnNm, arguments, 1);
	}
	
	/**
	 * Instantiates a new simple EOL executor that uses an {@link EolModuleParallel} (with one 
	 * thread) as its module, but that will only execute a single operation within the code/script.
	 * The constructor accepts a list of arguments to pass to the operation, i.e. arguments are
	 * position based. Additionally, the number of threads to use can also be specified.
	 * @see EolModuleParallel
	 *
	 * @param oprtnNm 				the name of the operation to invoke
	 * @param argmnts	 			the arguments to be passed to the operation (position based).
	 * @param nmbrThrds 			the number of threads
	 */
	public SimpleEolExecutor(String oprtnNm, List<Object> argmnts, int nmbrThrds) {
		this(oprtnNm, argmnts, new EolModuleParallel(new EolContextParallel(nmbrThrds)));
	}
	
	/**
	 * Instantiates a new simple EOL executor that the provided {@link IEolModule} as its module,
	 * but that will only execute a single operation within the code/script.
	 * The constructor accepts a list of arguments to pass to the operation, i.e. arguments are
	 * position based. Additionally, the number of threads to use can also be specified.
	 * @see EolModuleParallel
	 *
	 * @param oprtnNm 				the name of the operation to invoke
	 * @param argmnts 				the arguments to be passed to the operation (position based).
	 * @param mdl 					the module
	 */
	public SimpleEolExecutor(String oprtnNm, List<Object> argmnts, IEolModule mdl) {
		operationName = Optional.ofNullable(oprtnNm);
		mode = operationName.isPresent() ? EolMode.OPERATION : EolMode.SCRIPT;
		arguments = argmnts;
		module = mdl;
		delegate = new ModuleWrap(module);
	}

	@Override
	public Object execute() throws EolRuntimeException {
		switch(mode) {
			case SCRIPT:
				logger.info("Executing complete EOL script.");
				return module.execute();	
			case OPERATION:
				Operation operation = module
						.getDeclaredOperations()
						.getOperation(operationName.orElseThrow(() -> new EolRuntimeException("Can not invoke executor in OPERATION mode without an operation name assigned.")));
				logger.info("Executing EOL operation {} with arguments: {}.", operationName, arguments);
				return operation.execute(null, arguments, module.getContext());
		}
		return null;
	}

	@Override
	public EolExecutor changeOperation(String operationName, List<Object> arguments) {
		return new SimpleEolExecutor(operationName, arguments);
	}
	
	@Override
	public EolExecutor changeOperation(String operationName) {
		return new SimpleEolExecutor(operationName, Collections.emptyList());
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

}
