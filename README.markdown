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
to use Panettone. But if you really need some self-help guidance in order to choose a template engine, we recommend [these books](http://www.amazon.com/Paulo-Coelho/e/B000AQ3HB8/ref=sr_ntt_srch_lnk_1?qid=1408630389&sr=8-1).

So we will try avoiding quick awesome expressive compact fluid adjective sentences while describing this library. 

# First example: 

hello.tone:

```
<%@ List<User> users %>
<html>
<% for(User user : users) { %>
<h1>Hi ${user.name}</h1>
<% }; %>
</html>
```

Hello.java (VRaptor):

```
result.use(hello.class).render(users);
```

Hello.java (standalone):

```
new hello(out).render(users);
```

# Download

Download it here: 

# VRaptor-Panettone API - high level API

1. Start the compiler:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar --watch br.com.caelum.vraptor.mymodelpackage 
```

2. Create your source panettone file at `src/main/templates`, such as `hello.tone`, yummy.

```
<%@ String message %>
<html>
<h1><%= message %></h1>
</html>
```

Save it. Look now for `templates/hello.java` at `target/view-classes`. Add this path to your classpath!

# Keep watching x Compile once

Compile your templates once:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar br.com.caelum.vraptor.mymodelpackage 
```

Keep watching for changes:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar --watch br.com.caelum.vraptor.mymodelpackage 
```

# ANT example

Copy and paste ready

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

# VRAPTOR: defaults

Simply define injected variables in your template:

```
<%$ @Inject Localizer l %>
```

And compile:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar javax.inject.* br.com.caelum.vraptor.i18n.* 
```

# Methods and lambdas

Yeah, we all love methods, and we know we should be careful with logic in the view layer, right?
Methods *do not* have access to the local variables defined in the templates, you need to receive them if you want.

<%$
public void love(User user) {
	out.write("yes, " + user.getName() + ", I do.");
}
%>

And invoke it:

<%
love(user);
%>

Want a method that is cheap and access the variables? Define a lambda:

<%
Runnable love = () -> {
	out.write("Hi " + user.getName());
};
%>

And invoke it:

<% love.run(); %>

Yes, this is Java 8 compatible, in fact we require Java 8.

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

VRaptor version:
```
<html><% use(partial.class).render(); %></html>
```

# Default values

You can define a object reference variable (no primitive, sorry) with a default value by simply initializing it:

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

Be careful with default variables hell, as with any other language. This is a beta feature and does not feel right, does it?

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

3 - you will probably use this, the one you saw so far
2 - Compiling Templates by hand
1 - Rendering strings to be compiled

# CompiledTemplate - middle level API

# Template API - low level API

Use the Template Class to instantiate and render the string of a compatible Java method to what you want to render.
Use this String as you wish. You can print and memorize it, for instance. Or you can use it to compile it using Java's Compiler API. 

# Why shouldn't I use this other template engine?

Compare your template engine options and check if it makes sense for you, your team, your project and your company's short and long term goals.
The main issues we try to tackle in other Java world template engines:

- freemarker creepy error messages
- velocity old bugs
- jsp jasper limitations or servlet container requirements
- Twirl scala dependencies
- other language template engines: the need to learn other languages

For those reasons we choose to stick to a Java type safe one.

# Teach me more

Go through the test cases, there are plenty of working examples.

# What is that crazy parser I saw?

Good question. The parser was not intended to be a complex one. It should run in a linear time, and preferably just
do a one step read so it precompiles to Java. So far, so good. Help refactoring the parser is appreciated.  

# Development

To build a jar SNAPSHOT run `mvn package`.

# Issues, help, contributing

Fork, write code, write test, send pull request :)

Register issues in our github tracker

Talk to us at www.guj.com.br or twitter @guilhermecaelum