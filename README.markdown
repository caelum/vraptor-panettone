## vraptor-panettone

# API Levels

3 - you will probably use this
2 - Compiling Templates by hand
1 - Rendering types

# VRaptor-Panettone API - high level API

# CompiledTemplate - middle level API

# Template API - low level API

Use the Template Class to instantiate and render the string of a compatible Java method to what you want to render.
Use this String as you wish. 

# TODO
definirum nome pro projeto
high level api
(1)	auto-imports
	suportar parametros extras, como o "t", o "l", o "linkTo"
(2) java -jar panettone.jar que roda o VRaptorCompiler em um projeto com vraptor
medium level api
(4) docs
	vantagens
	- a mesma que a deles https://www.playframework.com/documentation/2.3.x/ScalaTemplates
	- pq java? mensagens de erro iguais que as deles. todas as vantagens da linguagem
	- user.name ==> user.getName()
	- pq <%%> ao inves de ${}?
(5) o compiler ser chamado em build via ant para packagear (java -jar)
(6) o compiler ser chamado em build via maven para packagear (maven plugin)
