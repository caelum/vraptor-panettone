package br.com.caelum.vraptor.panettone;

import br.com.caelum.vraptor.panettone.parser.ast.ASTWalker;
import br.com.caelum.vraptor.panettone.parser.ast.CommentNode;
import br.com.caelum.vraptor.panettone.parser.ast.ExpressionNode;
import br.com.caelum.vraptor.panettone.parser.ast.HTMLNode;
import br.com.caelum.vraptor.panettone.parser.ast.InjectDeclarationNode;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.PrintVariableNode;
import br.com.caelum.vraptor.panettone.parser.ast.ReusableVariableNode;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletNode;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletPrintNode;
import br.com.caelum.vraptor.panettone.parser.ast.VariableDeclarationNode;

public class DebuggerWalker implements ASTWalker {
	
	private ASTWalker w;

	public DebuggerWalker(ASTWalker w) {
		this.w = w;
	}

	@Override
	public void visitPrintVariable(PrintVariableNode node) {
		System.out.println("print variable: " + node);
		w.visitPrintVariable(node);
	}

	@Override
	public void visitVariableDeclaration(VariableDeclarationNode node) {
		System.out.println("var declar node: " + node);
		w.visitVariableDeclaration(node);
	}

	@Override
	public void visitHTML(HTMLNode node) {
		System.out.println("html: " + node);
		w.visitHTML(node);
	}

	@Override
	public void visitExpression(ExpressionNode node) {
		System.out.println("expression: " + node);
		w.visitExpression(node);
	}

	@Override
	public void visitScriptletPrint(ScriptletPrintNode node) {
		System.out.println("scriptlet print:" + node);
		w.visitScriptletPrint(node);
	}

	@Override
	public void visitScriptlet(ScriptletNode node) {
		System.out.println("scriptlet: " + node);
		w.visitScriptlet(node);
	}

	@Override
	public void visitInjectDeclaration(InjectDeclarationNode node) {
		System.out.println("inject: " + node);
		w.visitInjectDeclaration(node);
	}

	@Override
	public void visitReusableVariable(ReusableVariableNode node) {
		System.out.println("reusable var: " + node);
		w.visitReusableVariable(node);
	}

	@Override
	public void visitComment(CommentNode node) {
		System.out.println("comment: " + node);
		w.visitComment(node);
	}

	@Override
	public void visitBefore(Node node) {
		w.visitBefore(node);
	}

	@Override
	public void visitAfter(Node node) {
		w.visitAfter(node);
	}

}
