## panettone

Panettone is a type safe template language written in Java.

# Why?

- refactor-friendly: oh yeah! 
- it is compiled
- it is type safe
- therefore... get your type errors during your view compilation
- debug it
- html + java
- use the same template engine for your view, emails and much more
- easy to learn: you already know Java
- edit once, debug anywhere
- no extra dependencies
- no need to download the entire internet (also known as scala libraries)
- no servlet container requirement
- no jasper compiler issues with some servlet containers
- compilation error messages come from your known compiler
- always supporting the latest Java release

We could use words like "compact", "expressive", "fluid", "awesome", "quickly" and some other self-help phrases to convince you
to use Panettone. So let's avoid quick awesome expressive compact adjectives while describing this library. 

# First example: 

hello.tone:

```
<%@ User user %>
<html>
<h1>Hi ${user.name}</h1>
</html>
```

Hello.java (VRaptor):

```
result.use(hello.class).render(user);
```

Hello.java (standalone):

```
new hello(out).render(user);
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
Take care of your NULLs, please. If you are nullable, it is up to you to be careful with what you did. Don't play nullable, play safe.

# API Levels

3 - you will probably use this
2 - Compiling Templates by hand
1 - Rendering types

# CompiledTemplate - middle level API

# Template API - low level API

Use the Template Class to instantiate and render the string of a compatible Java method to what you want to render.
Use this String as you wish. 

# Why shouldn't I use this other template engine?

Compare your template engine options and check if it makes sense for you, your team, your project and your company's short and long term goals.
The main issues we try to tackle in other Java world template engines:

- freemarker creepy error messages
- velocity old bugs
- jsp jasper limitations or servlet container requirements
- Twirl scala dependencies
- other language template engines: the need to learn other languages

# Development

To build a jar SNAPSHOT run `mvn package`.

# Issues, help, contributing

Fork, write code, write test, send pull request :)

Register issues in our github tracker

Talk to us at www.guj.com.br or twitter @guilhermecaelum

# ISSUES for now
- docs
	- TUTORIAL: DefaultTemplate by DefaultHelpers helpers
	- TUTORIAL: <%$ @javax.inject.Inject Translator t %>
	- create the mailing list