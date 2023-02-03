/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.labs.sigma.executors.evl;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.dom.ConstraintContext;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.labs.sigma.executors.LanguageExecutor;

/**
 * The IEvlExecutor API provides additional methods retrieve and print the validation results.
 * 
 * @author Horacio Hoyos Rodriguez
 * 
 */
public interface EvlExecutor extends LanguageExecutor<Collection<UnsatisfiedConstraint>> {
    
    /**
     * Send the unsatisfied constraints to the log, using the implementation logger.
     *
     * @param unsatisfiedConstraints the unsatisfied constraints
     */
    void logUnsatisfied(Collection<UnsatisfiedConstraint> unsatisfiedConstraints);
    
    /**
     * Pretty print the unsatisfied constraints.
     *
     * @param unsatisfiedConstraints the unsatisfied constraints
     */
    void printUnsatisfied(Collection<UnsatisfiedConstraint> unsatisfiedConstraints);
    
    /**
     * Pretty print the unsatisfied constraints using the specified printer.
     *
     * @param unsatisfiedConstraints the unsatisfied constraints
     * @param writer the writer
     */
    void printUnsatisfied(Collection<UnsatisfiedConstraint> unsatisfiedConstraints, PrintWriter writer);
    

	/**
	 * Gets the constraints found in the code/script
	 *
	 * @return the constraints
	 */
	List<Constraint> getConstraints();

	/**
	 * Gets the constraint contexts found in the code/script
	 *
	 * @return the constraint contexts
	 */
	List<ConstraintContext> getConstraintContexts();

	/**
	 * Gets the constraint context with a given name
	 *
	 * @param name the name
	 * @return the constraint context
	 */
	ConstraintContext getConstraintContext(String name);

	/**
	 * Gets the unsatisfied constraints. This method should be called after execution.
	 *
	 * @return the unsatisfied constraints
	 */
	Collection<UnsatisfiedConstraint> getUnsatisfiedConstraints();
    
}
