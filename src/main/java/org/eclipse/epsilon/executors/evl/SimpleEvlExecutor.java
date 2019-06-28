/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.executors.evl;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.IToolNativeTypeDelegate;
import org.eclipse.epsilon.erl.execute.RuleProfiler;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.IEvlFixer;
import org.eclipse.epsilon.evl.IEvlModule;
import org.eclipse.epsilon.evl.concurrent.EvlModuleParallelElements;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.dom.ConstraintContext;
import org.eclipse.epsilon.evl.execute.CommandLineFixer;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.executors.ModuleWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The EVL executor.
 *
 * @author Horacio Hoyos Rodriguez
 * @since 1.6
 */
public class SimpleEvlExecutor implements EvlExecutor {

	private static final Logger logger = LoggerFactory.getLogger(SimpleEvlExecutor.class);
	private final IEvlModule module;
	private ModuleWrap delegate;
	
	/**
	 * Instantiates a new simple EVL executor that uses an {@link EvlModule} as its module and
	 * a {@link CommandLineFixer} as a constraint fixer.
	 * @see EvlModule
	 * @see EvlModule
	 */
	public SimpleEvlExecutor() {
		this(new EvlModule(), new CommandLineFixer());
    }
    
	/**
	 * Instantiates a new simple EVL executor that uses an {@link EvlModuleParallelElements} as its
	 * module, with the provided number of threads.
	 *
	 * @param parallelism 			the parallelism to use
	 */
	public SimpleEvlExecutor(int parallelism) {
		this(new EvlModuleParallelElements(parallelism), new CommandLineFixer());
    }
	
	/**
	 * Instantiates a new simple EVL executor that uses an {@link EvlModule} as its module and
	 * the provided {@link IEvlFixer} as a constraint fixer.
	 *
	 * @param evlFixer 				the fixer to use
	 */
	public SimpleEvlExecutor(IEvlFixer evlFixer) {
		this(new EvlModule(), evlFixer);
    }
    
	/**
	 * Instantiates a new simple EVL executor that uses an {@link EvlModuleParallelElements} as its
	 * module and the provided {@link IEvlFixer} as a constraint fixer, with the provided number of
	 * threads.
	 *
	 * @param parallelism 			the parallelism o use
	 * @param evlFixer 				the fixer to use
	 */
	public SimpleEvlExecutor(int parallelism, IEvlFixer evlFixer) {
		this(new EvlModuleParallelElements(parallelism), evlFixer);
    }
    
	/**
	 * Instantiates a new simple EVL executor that uses the provided {@link IEvlModule} module and
	 * the provided {@link IEvlFixer} as a constraint fixer.
	 *
	 * @param mdl 					the module
	 * @param evlFixer 				the fixer
	 */
	public SimpleEvlExecutor(IEvlModule mdl, IEvlFixer evlFixer) {
		logger.info("Creating the EvlExecutor");
		module = mdl;
		delegate = new ModuleWrap(module);
		if (module.getUnsatisfiedConstraintFixer() == null) {
			
		}
	}
    
	@Override
	public Collection<UnsatisfiedConstraint> execute() throws EolRuntimeException {
		return module.execute();
	}
	
	@Override
	public List<Constraint> getConstraints() {
		return module.getConstraints();
	}
	
	@Override
	public List<ConstraintContext> getConstraintContexts() {
		return module.getConstraintContexts();
	}

	@Override
	public ConstraintContext getConstraintContext(String name) {
		return module.getConstraintContext(name);
	}
	
	@Override
	public Set<UnsatisfiedConstraint> getUnsatisfiedConstraints() {
		return module.getContext().getUnsatisfiedConstraints();
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

	@Override
	public void logUnsatisfied(Collection<UnsatisfiedConstraint> unsatisfiedConstraints) {
		int numUnsatisfied = unsatisfiedConstraints.size();
    	if (numUnsatisfied > 0) {
    		// Separate critiques from constraints
    		List<UnsatisfiedConstraint> consraints = new ArrayList<>();
    		List<UnsatisfiedConstraint> critiques = new ArrayList<>();
    		unsatisfiedConstraints.stream()
    				.forEach(uc -> {
    					if (uc.getConstraint().isCritique()) {
    						critiques.add(uc);
    					} else {
    						consraints.add(uc);}
    					}
    				);
			logger.warn(String.format("There %s %s unsatisfied Constraint(s).",
					numUnsatisfied > 1 ? "were" : "was",
					numUnsatisfied));
			for (UnsatisfiedConstraint uc : unsatisfiedConstraints) {
				if (uc.getConstraint().isCritique()) {
					logger.warn(uc.getMessage());
				}
				else {
					logger.error(uc.getMessage());
				}
			}
		}
		else {
			logger.info("All constraints have been satisfied.");
		}
	}
	
	@Override
	public void printUnsatisfied(Collection<UnsatisfiedConstraint> unsatisfiedConstraints) {
		printUnsatisfied(unsatisfiedConstraints, new PrintWriter(System.out, true));	
	}

	@Override
	public void printUnsatisfied(Collection<UnsatisfiedConstraint> unsatisfiedConstraints, PrintWriter writer) {
		int numUnsatisfied = unsatisfiedConstraints.size();
    	if (numUnsatisfied > 0) {
    		// Separate critiques from constraints
    		List<UnsatisfiedConstraint> consraints = new ArrayList<>();
    		List<UnsatisfiedConstraint> critiques = new ArrayList<>();
    		unsatisfiedConstraints.stream()
    				.forEach(uc -> {
    					if (uc.getConstraint().isCritique()) {
    						critiques.add(uc);
    					} else {
    						consraints.add(uc);
    					}
    				});
    		Optional<String> maxWidth = unsatisfiedConstraints.stream()
    				.map(uc -> uc.getConstraint().getName())
    				.max(Comparator.comparingInt(String::length));
    		int tabDivision = maxWidth.orElse("        ").length();
			String msg = "Unsatisfied Contraints";
			String division = new String(new char[msg.length()]).replace("\0", "=");
			writer.println();
			writer.println(division);
			writer.println(msg);
			writer.println(division);
			for (UnsatisfiedConstraint uc : consraints) {
				writer.format("\u16D6 %-"+tabDivision+"s %s%n", uc.getConstraint().getName(), uc.getMessage());
			}
			writer.println(division);
			msg = "Unsatisfied Critiques";
			division = new String(new char[msg.length()]).replace("\0", "=");
			writer.println();
			writer.println(division);
			writer.println(msg);
			writer.println(division);
			for (UnsatisfiedConstraint uc : critiques) {
				writer.format("\u16B9 %-"+tabDivision+"s %s%n", uc.getConstraint().getName(), uc.getMessage());
			}
		}
		else {
			String msg = "All constraints have been satisfied";
			String division = new String(new char[msg.length()]).replace("\0", "=");
			writer.println();
			writer.println(division);
			writer.println(msg);
			writer.println(division);
		}
	}

}
