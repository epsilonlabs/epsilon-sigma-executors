
![Epsilon-Sigma-Executors](/epsilon-sigma-xi.png?raw=true)

# Epsilon Executors

Epsilon executors aim to provide an easy API for executing Epsilon engines in non-eclipse environments.

## Supported versions

epsilon-sigma-executors | Epsilon   | 
------------------------|-----------|
2.0.0                   | 1.6.x     |
1.0.0                   | 1.5.x     |



## Installation

The easiest way is to add a maven dependency to your project's pom:

```
<dependencies>
	<dependency>
   		<groupId>org.eclipse.epsilon.labs/groupId>
		<artifactId>epsilon-sigma-executors</artifactId>
		<version>2.0.0</version>
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