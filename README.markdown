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

Write your `hello.tone.html`:

```
@(List<User> users)
<html>
<% for(User user : users) { %>
<h1>Hi @user</h1>
<% } %>
</html>
```

Write your `HelloWorld.java` and you are ready to type safe and debug your views:

```
new hello(out).render(users);
```

If you use VRaptor:

```
result.use(hello.class).render(users);
```

Your output file will be debugable and good looking:

```
public void render(List<User> users) {
// line 2
	write("<html>\n");
// line 3
 for(User user : users) {
// line 4 
	write("<h1>Hi @user</h1>\n");
// line 5
 } 
// line 6
	write("</html>\n");
}
```

# Eclipse

Why would you use the command line if you have an IDE? Just add the plugin from this update site
and add the project nature:

```
http://panettone-eclipse.herokuapp.com
``` 

After that, do a CTRL+3 and search for "Panettone", you will find the action named "Apply Panettone nature to project"


# Imports

`java.util.*` and `templates.*` are imported by default to all your templates.

You can add extra imports by creating a file `src/main/views/tone.defaults` such as:

```
import br.com.caelum.myproject.model.*
import java.function.*
```

# Defaults using vraptor

Simply define injected variables in your template:

```
<%@ Localizer l %>
```

And compile:

```
java -jar vraptor-panettone-*.jar br.com.caelum.vraptor.i18n.* 
```

# Methods and lambdas

Yeah, we all love methods, and we know we should be careful with logic in the view layer, right?
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

partial.tone.html:
```
<body>Hello</body>
```

full.tone.html:
```
<html><% new partial(out).render(); %></html>
```

VRaptor version:
```
<html><% use(partial.class).render(); %></html>
```

# Invoking another template with a custom body tag

partial.tone.html:
```
<%@ Runnable body %>
<body>@{body.run()}</body>
```

VRaptor version:
```
<html>
@{{body
	Custom code here
@}}
<% use(partial.class).render(body); %>
</html>
```

Because %body% is a `Runnable`, it can be invoked with the method `run`. It has also access to all variables
that the parent template has (and none to its children template).

If you want to make it optional:

(@ Runnable body = () -> {} )

# Parameter defaults (to be implemented)

You can define a object reference variable (no primitive, sorry) with a default value by simply initializing it:

```
(@ String message = "hello" )
```

Each default variable must be defined in its own context:

```
(@ User user )
(@ String message = "hello" )
```

Only one method will be generated: `render(User user, String message)` and within its body there will be a default check:

```
if(message==null) message = "hello";
```

Be careful with default variables hell, as with any other language. This is a beta feature and does not feel right, does it?

# Expression language

Current simple support to make it easier to migrate JSP files. Basically replace all `${` with `@{`. That should work in `90+random()*10`% of the cases.
We do not recommend sticking to this one on the long run as we do not intend to make it more complex,
unless the code is contributed by someone like you, thanks :)

```
@message ==> write(message);
@message.a() ==> write(message.a());
@message.a(x) ==> write(message.a(x));
@{message(a.b)} ==> write(message(a.getB()));
@{message.bytes} ==> write(message.getBytes());
@{message.bytes.length} ==> write(message.getBytes().getLength());
@{message[15]} ==> write(message.get(15));
@{message[a.b]} ==> write(message.get(a.getB()));
@{message['15']} ==> write(message.get("15"));
@{messages.size[bytes]==> write(messages.getSize().get(bytes);
@{'xpto'} ==> write("xpto");
```

The {} are optional in most situations, even in some of the ones where it is seen above, but if the parser gets crazy during compilation time, it means they are required :).
We currently support some level of nested invocations. Do not abuse.
We currently **DO NOT** support *is* methods.
Take care of your NULLs, please. If you are nullable, it is up to you to be careful with what you did. Don't play nullable, play safe.

# Comments

You can add comments to your code with:

```
@-- my comment --@
<%= user.getName() %>
```

And it will be debug friendly:

```
// my comment
write(user.getName());
```

# Debugging

Write your template:

```
(@String mensagem )
<html>
@mensagem
</html>
```

Debug your file as you are used to:

```
public void render(String mensagem ) {
write("<html>");
write( mensagem );
write("</html>");
}
```

Live your life as usual, no crazy `yield`s to debug :).

# Tagfile style invocation

You can also invoke a template using a XML tag syntax. In this mode, all
parameters should be `String` and are considered optional.

```
<tone:header title="MyTitle"/>
```

It's possible to include a body in your tag. In this case, the parameter should
always be `Runnable body`:

```
<tone:header title="MyTitle">
   Body Content
</tone:header>
```

This featured is not meant to be abused. It's main purpose is to allow a friendlier 
syntax to call small front-end components.  

# Quick question and answers

1. Why is everything type safe?
Because we are playing Java.
2. Why is everything compiled?
Because we are playing safer.
3. Why is (almost) everything programmatic?
Because it is easy to provide a config file when something is programatic. It is hard to provide a programatic API to something that is based in text files.
4. Why is everything so hard in life?
Because we are playing hard.

# What are the API levels?

## you will probably use this, the one you saw so far
## Compiling Templates by hand
## Rendering strings to be compiled

Use the Template Class to instantiate and render the string of a compatible Java method to what you want to render.
Use this String as you wish. You can print and memorize it, for instance. Or you can use it to compile it using Java's Compiler API.

# Command line high level api

0. Download from maven central repository

1. Start the compiler with a default import package:
```
java -jar vraptor-panettone-*.jar --watch br.com.caelum.vraptor.mymodelpackage 
```
2. Create your source panettone file at `src/main/views`, such as `hello.tone.html`, yummy.
```
@(String message)
<html>
<h1>@message</h1>
</html>
```
3. Look now for `templates/hello.java` at `target/view-classes`. Add this path to your classpath!

# Keep watching or compile once?

Compile your templates once:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar br.com.caelum.vraptor.mymodelpackage 
```

Keep watching for changes:

```
java -jar vraptor-panettone-0.9.0-SNAPSHOT.jar --watch br.com.caelum.vraptor.mymodelpackage 
```

# During your build

ANT: Copy and paste ready

```
<project name="myproject" default="compile-views">

	<path id="running.path.id">
		<fileset dir="src/main/webapp/WEB-INF/lib" />
		<pathelement location="src/main/webapp/WEB-INF/classes" />
	</path>
	<target name="compile-views">
		<java jar="lib/local/vraptor-panettone-0.9.0-SNAPSHOT.jar" classpathref="running.path.id" fork="true"/>
		<javac destdir="src/main/webapp/WEB-INF/classes" classpathref="running.path.id" debug="on" includeantruntime="false" verbose="false" target="1.8" source="1.8" encoding="UTF-8">
			<src path="target/view-classes" />
		</javac>
	</target>
</project>
```

Then just run the following to compile once:
```
ant compile-views
```

# Why shouldn't I use this other template engine?

Compare your template engine options and check if it makes sense for you, your team, your project and your company's short and long term goals.
The main issues we try to tackle in other Java world template engines:

- freemarker error messages
- jsp jasper limitations or servlet container requirements
- Twirl scala dependencies, hard to understand what you are debugging
- other language template engines: the need to learn other languages

For those reasons we choose to stick to a Java type safe one.

# Teach me more

Go through the test cases, there are plenty of working examples.

# Who do I thank for being 100% sure I won't get a crappy message in my production code?

Thanks Java.

# Development

To build a jar SNAPSHOT: `mvn package`.

To release: `mvn release:prepare && mvn release:perform`

# Contributors

Guilherme Silveira
Maurício Aniche
Rodrigo Turini
Fernanda Bernardo
Felipe Oliveira
Caio Incau

# Issues, help, contributing

Fork, write code, write test, send pull request :)

[![Build Status](https://travis-ci.org/caelum/vraptor-panettone.svg?branch=master)](https://travis-ci.org/caelum/vraptor-panettone)

Register issues in our github tracker

Talk to us at www.guj.com.br or twitter @guilhermecaelum
