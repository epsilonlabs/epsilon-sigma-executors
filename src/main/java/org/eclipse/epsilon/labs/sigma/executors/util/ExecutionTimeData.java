/*********************************************************************
 * Copyright (c) 2019 The University of York.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 **********************************************************************/
package org.eclipse.epsilon.labs.sigma.executors.util;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.erl.dom.NamedRule;
import org.eclipse.epsilon.erl.execute.control.RuleProfiler;
import org.eclipse.epsilon.labs.sigma.executors.LanguageExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.epsilon.common.util.OperatingSystem.getJavaVersion;
import static org.eclipse.epsilon.common.util.OperatingSystem.getOsNameAndVersion;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getCpuName;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getNumberOfHardwareThreads;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getTime;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * A class that keeps track of the execution durations of the different stages of execution, and
 * about the execution of the scripts and its rules (if rule based language).
 * @author Horacio Hoyos Rodriguez
 *
 */
public class ExecutionTimeData {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ExecutionTimeData.class);

	/** The log messages separator. */
	private static final String logMessagesSeparator = "-----------------------------------------------------";

	/** The os name and version. */
	private final String osNameAndVersion;

	/** The java version. */
	private final String javaVersion;

	/** The cpu name. */
	private final String cpuName;

	/** The logical processors. */
	private final int logicalProcessors;

	/** The date. */
	private final String date;

	/** The start nanos. */
	private Long startNanos;
	/**
	 * The measured execution information.
	 */
	private final Map<String, Duration> profiledStages = new HashMap<>();

	/** The profiled rules. */
	private final Map<String, Duration> profiledRules = new HashMap<>();

	/** The started. */
	private final Map<String, Long> started = new HashMap<>();

	/** The duration. */
	private Duration duration;

	/**
	 * Instantiates a new execution time data.
	 */
	public ExecutionTimeData() {
		osNameAndVersion = getOsNameAndVersion();
		javaVersion = getJavaVersion();
		cpuName = getCpuName();
		logicalProcessors = getNumberOfHardwareThreads();
		date = getTime();
	}

	/**
	 * Log start.
	 */
	public void logStart() {
		startNanos = System.nanoTime();
		logger.info(buildLines(
				osNameAndVersion,
				javaVersion,
				cpuName,
				"Logical processors: " + logicalProcessors,
				"Starting execution at " + date,
				logMessagesSeparator
			));
	}

	/**
	 * Log end.
	 */
	public void logEnd() {
		long endTimeNanos = System.nanoTime();
		this.duration = Duration.ofNanos(endTimeNanos-startNanos);
		logger.info(buildLines("",
			"Profiled processes:",
			profiledStages.entrySet().stream().map(e -> String.format("%s:%s", e.getKey(), e.getValue())),
			"Finished execution at " + getTime(),
			logMessagesSeparator
		));
		logger.info(buildLines("",
				"Profiled rules:",
				profiledRules.entrySet().stream().map(e -> String.format("%s:%s", e.getKey(), e.getValue())),
				logMessagesSeparator
			));

	}

	/**
	 * Builds the lines.
	 *
	 * @param lines the lines
	 * @return the string
	 */
	public String buildLines(Object... lines) {
		StringBuilder linesAsStr = new StringBuilder();
		String nL = System.lineSeparator();
		for (Object line : lines) {
			linesAsStr.append(line).append(nL);
		}
		return linesAsStr.toString();
	}

	/**
	 * Start stage.
	 *
	 * @param name the name
	 */
	public void startStage(String name) {
		started.put(name, System.nanoTime());
	}

	/**
	 * End stage.
	 *
	 * @param name the name
	 */
	public void endStage(String name) {
		long endTimeNanos = System.nanoTime();
		Long startTime = started.getOrDefault(name, endTimeNanos);
		profiledStages.put(name, Duration.ofNanos(endTimeNanos-startTime));
	}

	/**
	 * End module.
	 *
	 * @param languageExecutor the language executor
	 */
	public void endModule(LanguageExecutor<?> languageExecutor) {
		Optional<RuleProfiler> ruleProfiler = languageExecutor.getRuleProfiler();
		if(ruleProfiler.isPresent()) {
			for (Map.Entry<ModuleElement, Duration> entry : ruleProfiler.get().getExecutionTimes().entrySet()) {
				if (entry.getKey() instanceof NamedRule) {
					NamedRule rule = (NamedRule) entry.getKey();
					Duration oldValue = profiledRules.put(rule.getName(), entry.getValue());
					if (oldValue != null) {
						System.err.println("Value for rule " + rule.getName() + " was replaced.");
					}
				}
			}
		}

	}

	/**
	 * Return the duration of the "prepareExecution" stage. A negative value indicates that the
	 * stage was not signalled as finished.
	 *
	 * @return the prepare execution duration
	 */
	public Optional<Duration> getPrepareExecutionDuration() {
		return Optional.ofNullable(profiledStages.get("prepareExecution"));
	}

	/**
	 * Return the duration of the "preProcess" stage. A negative value indicates that the
	 * stage was not signalled as finished.
	 *
	 * @return the pre process duration
	 */
	public Optional<Duration> getPreProcessDuration() {
		return Optional.ofNullable(profiledStages.get("preProcess"));
	}

	/**
	 * Return the duration of the "postProcess" stage. A negative value indicates that the
	 * stage was not signalled as finished.
	 *
	 * @return the post process duration
	 */
	public Optional<Duration> getPostProcessDuration() {
		return Optional.ofNullable(profiledStages.get("postProcess"));
	}

	/**
	 * Return the duration of the Epsilon module execution. A negative value indicates that the
	 * stage was not signalled as finished.
	 *
	 * @return the scrpit execution duration
	 */
	public Optional<Duration> getScrpitExecutionDuration() {
		return Optional.ofNullable(profiledStages.get("execute"));
	}

	/**
	 * Gets the total duration.
	 *
	 * @return the total duration
	 */
	public Optional<Duration> getTotalDuration() {
		return Optional.ofNullable(this.duration);
	}

	/**
	 * Gets the rule duration.
	 *
	 * @param name the name
	 * @return the rule duration
	 */
	public Optional<Duration> getRuleDuration(String name) {
		return Optional.ofNullable(profiledRules.get(name));
	}

	/**
	 * Gets the rules durations.
	 *
	 * @return the rules durations
	 */
	public Iterator<Map.Entry<String, Duration>> getRulesDurations() {
		return profiledRules.entrySet().iterator();
	}

}