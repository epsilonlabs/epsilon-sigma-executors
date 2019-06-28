/*********************************************************************
* Copyright (c) 2019 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.executors.eml;

import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.eml.trace.MergeTrace;

/**
 * A class to hold the EML results
 * @author Horacio Hoyos Rodriguez
 * @since 1.6
 *
 */
public class EmlTraces {
	
	private final MatchTrace matchTrace;
	private final MergeTrace mergeTrace;

	public EmlTraces(MatchTrace matchTrace, MergeTrace mergeTrace) {
		this.matchTrace = matchTrace;
		this.mergeTrace = mergeTrace;
	}

	public MatchTrace getMatchTrace() {
		return matchTrace;
	}

	public MergeTrace getMergeTrace() {
		return mergeTrace;
	}

}