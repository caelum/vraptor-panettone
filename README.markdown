## vraptor-panettone

VRaptor Panettone is a type safe template language written in Java:

```
<%@ String message %>
<html>
<h1><%= message %></h1>
</html>
```

# VRaptor-Panettone API - high level API

(STEP 1) Run in your command line:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar --watch br.com.caelum.vraptor.mymodelpackage 
```

(STEP 2) Create your source panettone file at `src/main/templates`, such as `hello.tone`.

```
<%@ String message %>
<html>
<h1><%= message %></h1>
</html>
```

(STEP 3) As soon as you save your file, there should be a new file at `target/view-classes`. Add this path to your classpath!

# Keep watching x Compile once

Compile your templates once:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar br.com.caelum.vraptor.mymodelpackage 
```

Keep watching for changes:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar --watch br.com.caelum.vraptor.mymodelpackage 
```

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

Simply create a class called `DefaultTemplate` that has all the injects you need, and add a explicit import for it.

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

And compile:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar br.com.caelum.vraptor.mymodelpackage br.com.caelum.vraptor.mymodelpackage.DefaultTemplate 
```

If the compiled template finds multiple classes called `DefaultTemplate` in the auto imported packages, it will extends one of them, you don't know which one:


# Invoking another template

Feel free to include another template in any part of your template:

partial.tone:
```
<body>Hello</body>
```

full.tone:
```
<html><% new templates.partial(out).render(); %></html>
```

# Default values

You can define a variable with a default value by simply initializing it:

```
<%@ String message = "hello" %>
```

Each default variable must be defined in its own context:

```
<%@ User user %>
<%@ String message = "hello" %>
```

Only one method will be generated: `render(User user, String message)` and within its body there will be a default check:

```
if(message==null) message = "hello";
```

Be careful with default variables hell, as with any other language.

# Expression language with $

Current simple support to make it easier to migrate JSP files.
We do not recommend sticking to this one on the long run as we do not intend to make it more complex.

```
${message} ==> write(message);
${message()} ==> write(message());
${message(x)} ==> write(message(x));
${message(a.b)} ==> write(message(a.getB()));
${message.bytes} ==> write(message.getBytes());
${message.bytes.length} ==> write(message.getBytes().getLength());
${message[15]} ==> write(message.get(15));
${message[a.b]} ==> write(message.get(a.getB()));
${message['15']} ==> write(message.get("15"));
${messages.size[bytes]==> write(messages.getSize().get(bytes);
${'xpto'} ==> write("xpto");
```

We currently support some level of nested invocations. Do not abuse.
We currently **DO NOT** support *is* methods.

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

# ISSUES for now
- auto-reload without full reload
- docs
	vantagens
	- a mesma que a deles https://www.playframework.com/documentation/2.3.x/ScalaTemplates
	- pq java? mensagens de erro iguais que as deles. todas as vantagens da linguagem
	- pq <%%> ao inves de ${}? ao inves de @?
	- TUTORIAL: DefaultTemplate by DefaultHelpers helpers
	- TUTORIAL: <%$ @javax.inject.Inject Translator t %>
	- you can debug your view
- cutting some spaces (check templates)
- how to deal with nulls? ${}! <%=! %> (null pointer?)




# ISSUES for later
- vraptor4 support CompiledTemplate return (generate example at src/extras)
- vraptor3 support CompiledTemplate return (generate example at src/extras)
- auto import all view packages
- keep or remove our own compilation phase?
	- improve compilation because there is a Compiler <-> SimpleJavaCompiler <-> CompiledTemplate reference now
	- show code on compilation error
	- ELParser padrao acessa request scoped e mantem nao typesafe para migrar facil o JSP, com mensagem amigavel
	- body com lambda
<% template2(usuario, () -> { %>

<% }); %> ou
<%#body
xpto
%> 
<% use(a).render(x, body); %>

- o compiler ser chamado em build via maven para packagear (maven plugin)
- custom easy default variable for <%$ @javax.inject.Inject Translator t %>
- support other encodings on vraptor-panettone(currently only UTF-8)
