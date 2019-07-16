/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.executors.eol;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.executors.ModuleWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The EOL executor.
 *
 * @author Horacio Hoyos Rodriguez
 */
public class SimpleEolExecutor implements EolExecutor  {

	private static final Logger logger = LoggerFactory.getLogger(SimpleEolExecutor.class);

	private final Optional<String> operationName;
	private final List<Object> arguments;
	private final EolMode mode;
	private IEolModule module;
	private ModuleWrap delegate;
	
	/**
	 * Instantiates a new simple EOL executor. This executor will execute the complete code/script.
	 *
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
	 * Instantiates a new simple EOL executor that will only execute a single operation within the code/script.
	 * The constructor accepts a list of arguments to pass to the operation, i.e. arguments are
	 * position based.
	 *
	 * @param oprtnNm 				the name of the operation to invoke
	 * @param arguments 			the arguments to be passed to the operation (position based).
	 *
	 */
	public SimpleEolExecutor(String oprtnNm, List<Object> arguments) {
		this(oprtnNm, arguments, new EolModule());
	}

	/**
	 * Instantiates a new simple EOL executor that the provided {@link IEolModule} as its module,
	 * but that will only execute a single operation within the code/script.
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
