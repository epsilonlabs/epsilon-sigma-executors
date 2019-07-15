# Epsilon Executors

Epsilon executors aim to provide an easy API for executing Epsilon engines in non-eclipse environments.

## Installation

The easiest way is to add a maven depedendency to your project's pom:

```
<dependencies>
	<dependency>
   		<groupId>org.eclipse.epsilon.labs/groupId>
		<artifactId>executors</artifactId>
		<version>1.6.0</version>
	</dependency>
</dependencies>
```

If you want to work with the SNAPSHOT versions, you need to add the OSS Sonatype repository to your pom:

```
<repositories>
	...
	<repository>
   		<id>oss-sonatype</id>
		<name>oss-sonatype</name>
		<url>https://oss.sonatype.org/content/repositories/snapshots/</url>
		<snapshots>
			<enabled>true</enabled>
		</snapshots>
		</repository>
	</repositories>
	...
<repositories>	
```