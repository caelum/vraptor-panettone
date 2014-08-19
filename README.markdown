## vraptor-panettone

VRaptor Panettone is a type safe template language written in Java:

```
<%@ String message %>
<html>
<h1><%= message %></h1>
</html>
```

# VRaptor-Panettone API - high level API

Run in your command line:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar --watch br.com.caelum.vraptor.mymodelpackage 
```

Create your source panettone file at `src/main/templates`, such as `hello.tone`.

```
<%@ String message %>
<html>
<h1><%= message %></h1>
</html>
```

As soon as you save your file, there should be a new file at `target/view-classes`.

# Keep watching x Compile once

Compile your templates once:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar br.com.caelum.vraptor.mymodelpackage 
```

Keep watching for changes:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar --watch br.com.caelum.vraptor.mymodelpackage 
```

# Defaults
It will look at the directories X, Y for classes and jars

# ANT

```
<project name="myproject" default="compile-views">

	<path id="running.path.id">
		<fileset dir="src/main/webapp/WEB-INF/lib" />
		<pathelement location="src/main/webapp/WEB-INF/classes" />
	</path>
	<target name="compile-views">
		<java jar="lib/local/vraptor-panettone-0.9.0-SNAPSHOT.jar" classpathref="running.path.id">
			<arg>br.com.caelum.myproject.model.*</arg>
			<arg>br.com.caelum.myproject.model.course.*</arg>
		</java>
	</target>
	<target name="~compile-views">
		<java jar="lib/local/vraptor-panettone-1.0.1.jar" classpathref="running.path.id">
			<arg>--watch</arg>
			<arg>br.com.caelum.myproject.model.*</arg>
			<arg>br.com.caelum.myproject.model.course.*</arg>
		</java>
	</target>
</project>
```

Then just run the following to compile once:
```
ant compile-views
```

Or to keep watching:

```
ant ~compile-views
```

# Maven

# How to add Defaults

Simply create a class called `DefaultTemplate` that has all the injects you need, put it into one of the auto import packages.

```
package br.com.caelum.vraptor.panettone;

public class DefaultTemplate {

	@Inject;
	private LinkedTo linkTo;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	@Nullable
	private User loggedUser;
}
```

If the compiled template finds multiple classes called `DefaultTemplate` in the auto imported packages, it will extends one of them, you don't know which one.

# API Levels

3 - you will probably use this
2 - Compiling Templates by hand
1 - Rendering types

# CompiledTemplate - middle level API

# Template API - low level API

Use the Template Class to instantiate and render the string of a compatible Java method to what you want to render.
Use this String as you wish. 

# Development

To build a jar SNAPSHOT run `mvn package`.

# TODO
- usar no gnarus em um branch
- vraptor4 support CompiledTemplate return (generate example at src/extras)
- vraptor3 support CompiledTemplate return (generate example at src/extras)
- auto-reload com o classloader separado
- docs
	vantagens
	- a mesma que a deles https://www.playframework.com/documentation/2.3.x/ScalaTemplates
	- pq java? mensagens de erro iguais que as deles. todas as vantagens da linguagem
	- pq <%%> ao inves de ${}? ao inves de @?
- o compiler ser chamado em build via maven para packagear (maven plugin)
- body com lambda
<% template2(usuario, () -> { %>

<% }); %>
- syntax sugar: ${} => a.b ou a.getB ou a.isB (verifica qual existe em compilacao). interface ELParser
	ELParser padrao acessa request scoped e mantem nao typesafe para migrar facil o JSP, com mensagem amigavel