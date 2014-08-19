## vraptor-panettone

# API Levels

3 - you will probably use this
2 - Compiling Templates by hand
1 - Rendering types

# VRaptor-Panettone API - high level API

Run in your command line:

```
java -jar vraptor-panettone-version.jar br.com.caelum.vraptor.mymodelpackage 
```

Create your source panettone file at `src/main/templates`, such as `hello.tone`.

```
<%@ String message %>
<html>
<h1><%= message %></h1>
</html>
```

As soon as you save your file, there should be a new file at `target/view-classes`.

# Defaults
It will look at the directories X, Y for classes and jars

# ANT

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

# CompiledTemplate - middle level API

# Template API - low level API

Use the Template Class to instantiate and render the string of a compatible Java method to what you want to render.
Use this String as you wish. 

# TODO
(5) o compiler ser chamado em build via ant para packagear (java -jar)
auto-reload com o classloader separado
	permitir que o vraptor retorne CompiledTemplate e esse compiled template ja resolva
(7) use @ gnarus sample example @vraptor3
(4) docs
	vantagens
	- a mesma que a deles https://www.playframework.com/documentation/2.3.x/ScalaTemplates
	- pq java? mensagens de erro iguais que as deles. todas as vantagens da linguagem
	- pq <%%> ao inves de ${}? ao inves de @?
(6) o compiler ser chamado em build via maven para packagear (maven plugin)
(8) body com lambda
<% template2(usuario, () -> { %>

<% }); %>
syntax sugar: ${} => a.b ou a.getB ou a.isB (verifica qual existe em compilacao). interface ELParser
	ELParser padrao acessa request scoped e mantem nao typesafe para migrar facil o JSP, com mensagem amigavel