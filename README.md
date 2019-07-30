
![Epsilon-Sigma-Executors](/epsilon-sigma-xi.png?raw=true)

# Epsilon Executors

Epsilon executors aim to provide an easy API for executing [Epsilon](https://www.eclipse.org/epsilon/) scripts in non-eclipse environments. The executors API allows users to easily configure the execution environment (i.e. input/output models, external parameters, etc.) required for executing the script. 




## Supported versions

epsilon-sigma-executors | Epsilon   | 
------------------------|-----------|
2.0.0                   | 1.6.x     |
1.0.0                   | 1.5.x     |



## Installation

Using your prefered dependency manager:

### Maven

```
<dependencies>
	<dependency>
   		<groupId>org.eclipse.epsilon.labs/groupId>
		<artifactId>epsilon-sigma-executors</artifactId>
		<version>2.0.0</version>
	</dependency>
</dependencies>
```
### Apache Ivy
```
<dependency org="org.eclipse.epsilon.labs" name="epsilon-sigma-executors" rev="2.0.0" />
```

### Groovy Grape
```
@Grapes( 
@Grab(group='org.eclipse.epsilon.labs', module='epsilon-sigma-executors', version='2.0.0') 
)
```
### Gradle/Grails

```
compile 'org.eclipse.epsilon.labs:epsilon-sigma-executors:2.0.0'
```

### Manually

Alternativly, you can download and add the jars manually to your project. You can finde the executable, sources and javadoc jars here:

```
http://repo1.maven.org/maven2/org/eclipse/epsilon/labs/epsilon-sigma-executors/2.0.0/```

### Snapshots

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

## Usage

Please visit our [wiki](https://github.com/epsilonlabs/epsilon-sigma-executors/wiki) for detailed informaiton on how to use the exeuctors.