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

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.labs.sigma.executors.util.ExecutionTimeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Epsilon Executor is used to run the different Language executors.
 *
 * @author Horacio Hoyos Rodriguez
 *
 */
public class EpsilonExecutor implements Executor {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(EpsilonExecutor.class);

    /**
     * The Epsilon Module that implements the specific language engine.
     */
    private final EpsilonLanguageExecutor<?> languageExecutor;

    /**
     * The Script to be executed. Alternatively a block of code can be provided, see {@link #code}.
     * If both are present, the Script takes priority.
     */
    private final Path script;

    /**
     * The Code to be executed. Alternatively a script file can be provided, see {@link #script}.
     * If both are present, the Script takes priority
     */
    private final String code;

    /**
     * The runtime parameters.
     */
    private final Map<String, Object> parameters; // = new HashMap<>(4);

    /**
     * The Models used during execution.
     */
    private final Set<IModel> models; // = new HashSet<>(4);

    /**
     * The Native Type delegates used during execution.
     */
    private final Set<IToolNativeTypeDelegate> nativeDelegates; // = new HashSet<>(8);

    /**
     * The Dispose Models flag indicates if models will be disposed after execution.
     */
    private final boolean disposeModels; // = true;

    /**
     * The Profile Execution flag indicates if the execution should be measured.
     */
    private final boolean profileExecution; // = false;


    /** The time data. */
    private final ExecutionTimeData timeData;


	/**
	 * Instantiates a new epsilon executor.
	 *
	 * @param languageExecutor 		the language executor
	 * @param scriptPath 			the script path
	 */
    public EpsilonExecutor(
        EpsilonLanguageExecutor<?> languageExecutor,
        Path scriptPath) {
        this(languageExecutor, scriptPath, null, Collections.emptySet(), Collections.emptyMap(), Collections.emptySet(), true, false);
    }

    /**
     * Instantiates a new epsilon executor.
     *
     * @param languageExecutor		the language executor
     * @param scriptPath		 	the script path
     * @param models 				the models to run the script against
     */
    public EpsilonExecutor(
    	EpsilonLanguageExecutor<?> languageExecutor,
        Path scriptPath,
        Collection<IModel> models) {
        this(languageExecutor, scriptPath, null, models, Collections.emptyMap(), Collections.emptySet(), true, false);
    }


	/**
	 * Instantiates a new epsilon executor.
	 *
	 * @param languageExecutor		the language executor
	 * @param scriptPath 			the script path
	 * @param models 				the models to run the script against
	 * @param parameters 			parameters to pass to the execution
	 * @param nativeDelegates 		the native delegates required for execution
	 */
	public EpsilonExecutor(
		EpsilonLanguageExecutor<?> languageExecutor,
	    Path scriptPath,
        Collection<IModel> models,
    	Map<String, Object> parameters,
		Collection<IToolNativeTypeDelegate> nativeDelegates) {
        this(languageExecutor, scriptPath, null, models, parameters, nativeDelegates, true, false);
    }

	/**
	 * Instantiates a new epsilon executor.
	 *
	 * @param languageExecutor 		the language executor
	 * @param code 					the code to execute
	 */
    public EpsilonExecutor(
    	EpsilonLanguageExecutor<?> languageExecutor,
        String code) {
        this(languageExecutor, null, code, Collections.emptySet(), Collections.emptyMap(), Collections.emptySet(), true, false);
    }

    /**
     * Instantiates a new epsilon executor.
     *
     * @param languageExecutor 		the language executor
     * @param code	 				the code to execute
     * @param models 				the models to run the code against
     */
    public EpsilonExecutor(
    	EpsilonLanguageExecutor<?> languageExecutor,
        String code,
        Collection<IModel> models) {
        this(languageExecutor, null, code, models, Collections.emptyMap(), Collections.emptySet(), true, false);
    }

    /**
     * Instantiates a new epsilon executor.
     *
     * @param languageExecutor		the language executor
     * @param code 					the code to execute
     * @param models 				the models to run the script against
	 * @param parameters 			parameters to pass to the execution
	 * @param nativeDelegates 		the native delegates required for execution
     */
    public EpsilonExecutor(
    	EpsilonLanguageExecutor<?> languageExecutor,
        String code,
        Collection<IModel> models,
    	Map<String, Object> parameters,
		Collection<IToolNativeTypeDelegate> nativeDelegates) {
        this(languageExecutor, null, code, models, parameters, nativeDelegates, true, false);
    }

    /**
     * Instantiates a new epsilon executor with the given details. If both the script and code are given, only the script
     * is used.
     *
     * @param languageExecutor 		the language executor
     * @param scriptPath 			the script path
	 * @param code 					the code to execute
	 * @param models 				the models to run the script against
	 * @param parameters 			parameters to pass to the execution
	 * @param nativeDelegates 		the native delegates required for execution
	 * @param disposeModels 		the dispose models flag, if true models are disposed after execution
     * @param profileExecution 		the profile execution flag
     */
    public EpsilonExecutor(
       	EpsilonLanguageExecutor<?> languageExecutor,
        Path scriptPath,
        String code,
        Collection<IModel> models,
      	Map<String, Object> parameters,
    	Collection<IToolNativeTypeDelegate> nativeDelegates,
        boolean disposeModels,
        boolean profileExecution) {
        super();
    	this.languageExecutor = languageExecutor;
    	this.script = scriptPath;
    	this.code = code;
    	this.models = new HashSet<>(models);
    	this.parameters = parameters;
    	this.nativeDelegates = new HashSet<>(nativeDelegates);
    	this.disposeModels = disposeModels;
    	this.profileExecution = profileExecution;
    	this.timeData = profileExecution ? new ExecutionTimeData() : null;
    }



	@Override
	public Optional<ExecutionTimeData> getExecutionTimeData() {
		return Optional.ofNullable(timeData);
	}

    @Override
	public <T> Runnable executeInThread() {
    	class OneShotTask implements Runnable {
    		private T result;
            public void run() {
            	try {
					result = invokeExecutor();
				} catch (EpsilonExecutorException e) {
					throw new RuntimeException(e);
				}
            }
            @SuppressWarnings("unused")
			public T getResult() { return result;}
        }
		return new OneShotTask();
    }

    @Override
	@SuppressWarnings("unchecked")
	public <R> R invokeExecutor() throws EpsilonExecutorException {
        logger.info("Executing engine.");
        preProfile();
        prepareExecution();
        logger.info("Pre-process execution");
        startStage("preProcess");
        languageExecutor.preProcess();
        R result;
        if (profileExecution) {
    		endStage("preProcess");
        }
        try {
        	if (profileExecution) {
        		startStage("execute");
            }
            // @SuppressWarnings("unchecked") OK because each engine has different return types.
            result = (R) languageExecutor.execute();
        }
        catch (EolRuntimeException e) {
        	String msg = "Error executing the module.";
            logger.error(msg, e);
            throw new EpsilonExecutorException(msg, e);
        }
        finally {
        	if (profileExecution) {
        		endStage("execute");
            }
        }
        logger.info("Post-process execution.");
        if (profileExecution) {
    		startStage("postProcess");
        }
        languageExecutor.postProcess();
        if (profileExecution) {
    		endStage("postProcess");
        }

        if (profileExecution) {
        	if (timeData != null) {
        	timeData.endModule(languageExecutor);
        	timeData.logEnd();
        	}
        }
        return result;
	}

	@Override
	public void dispose() {
		if (disposeModels) {
			logger.info("Disposing models");
			languageExecutor.disposeModelRepository();
		}
		else {
			logger.info("Removing models from context models");
			languageExecutor.clearModelRepository();
		}
		models.clear();
		parameters.clear();
		logger.info("Dispose context");
		languageExecutor.dispose();
	}

	/**
	 * Start stage.
	 *
	 * @param stageName the stage name
	 */
	private void startStage(String stageName) {
		if (timeData != null) {
			timeData.startStage(stageName);
		}
	}

	/**
	 * End stage.
	 *
	 * @param stageName the stage name
	 */
	private void endStage(String stageName) {
		if (timeData != null) {
			timeData.endStage(stageName);
		}
	}




    /**
     * Pre profile.
     */
    private void preProfile() {
    	if (timeData != null) {
    		timeData.logStart();
    	}
    }

    /**
     * Prepare execution.
     *
     * @throws EpsilonExecutorException the epsilon executor exception
     */
    private void prepareExecution() throws EpsilonExecutorException {
    	if (!( (script == null) || (code == null) )) {
    		throw new EpsilonExecutorException("No script or code to execute");
    	}
        logger.info("Parsing source/code.");
		parseSource();
    	startStage("prepareExecution");
        logger.info("Adding models to executor");
		languageExecutor.addModels(models);
		logger.info("Adding parameters to context.");
		languageExecutor.addParameters(parameters);

		logger.info("Adding Native Type Delegates");
		languageExecutor.addNativeTypeDelegates(nativeDelegates);
		if (profileExecution) {
    		endStage("prepareExecution");
        }
    }

	/**
	 * Parses the source.
	 *
	 * @throws EpsilonExecutorException the epsilon executor exception
	 */
	private void parseSource() throws EpsilonExecutorException {
		// Think this should be part of constructor, so object is correct at construction
	  try {
	  	if (script != null) {
	  		languageExecutor.parse(script.toFile());
	      }
	      else {
	      	languageExecutor.parse(code);
	      }
	  }
	  catch (Exception e) {
	  	String culprit = script != null ? "script" : "code";
	      logger.error("Failed to parse provided {}", culprit, e);
	      throw new EpsilonExecutorException("Failed to parse script or code", e);
	  }
	  if (!languageExecutor.getParseProblems().isEmpty()) {
	      logger.error("Parse errors occurred");
			System.err.println("Parse errors occurred...");
			for (ParseProblem problem : languageExecutor.getParseProblems()) {
				System.err.println(problem);
			}
	      throw new EpsilonExecutorException("Parse errors occurred.");
		}
	}
}