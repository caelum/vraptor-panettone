package br.com.caelum.vraptor.panettone.parser.ast;


public interface ASTWalker {

	void visitPrintVariable(PrintVariableNode node);

	void visitVariableDeclaration(
			VariableDeclarationNode node);

	void visitHTML(HTMLNode node);

	void visitMethodInvocation(MethodInvocationNode node);

	void visitExpression(ExpressionNode node);

	void visitScriptletPrint(ScriptletPrintNode node);

	void visitScriptlet(ScriptletNode node);

	void visitInjectDeclaration(InjectDeclarationNode node);

	void visitReusableVariable(ReusableVariableNode node);

	void visitComment(CommentNode node);

}
