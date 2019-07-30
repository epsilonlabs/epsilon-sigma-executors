package org.eclipse.epsilon.labs.sigma.executors;

import org.eclipse.epsilon.labs.sigma.executors.eol.EolExecutor;
import org.eclipse.epsilon.labs.sigma.executors.eol.SimpleEolExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class EpsilonExecutorIntegrationTest {

	private ByteArrayOutputStream baos;

	@BeforeEach
	public void setUp() {
		baos = new ByteArrayOutputStream();
	}

	@AfterEach
	public void tearDown() {
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void runEolOperationWithoutParametersFromScript() throws Exception {
		File script = resource2file(this.getClass(), "/EolOperationWithParameters.eol", "demo", ".eol");
		EolExecutor eol = new SimpleEolExecutor("printHelloWorld", Collections.emptyList());
		Executor executor = new EpsilonExecutor(eol, Paths.get(script.toURI())).redirectOutputStream(new PrintStream(baos));
		executor.invokeExecutor();
		Assertions.assertEquals("Hello World", baos.toString().trim());
	}

	@Test
	public void runEolOperationWithParametersFromScript() throws Exception {
		File script = resource2file(this.getClass(), "/EolOperationWithParameters.eol", "demo", ".eol");
		EolExecutor eol = new SimpleEolExecutor("printHelloWorldAndMessage", Collections.singletonList("Epsilon"));
		Executor executor = new EpsilonExecutor(eol, Paths.get(script.toURI())).redirectOutputStream(new PrintStream(baos));
		executor.invokeExecutor();
		Assertions.assertEquals("Hello World, Epsilon", baos.toString().trim());
	}


	@Test
	public void runEolOperationWithoutParametersFromString() {


	}

	@Test
	public void runEolOperationWithParametersFromString() {

	}

	private File resource2file(Class<?> type, String resource, String name, String extension) throws IOException {
		InputStream stream = type.getResource(resource).openStream();
		byte[] buffer = new byte[stream.available()];
		stream.read(buffer);
		final File tempFile = File.createTempFile(name, extension);
		tempFile.deleteOnExit();
		try (OutputStream outStream = new FileOutputStream(tempFile)) {
			outStream.write(buffer);
		}
		stream.close();
		return tempFile;
	}
}