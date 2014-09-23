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

public class LineNumberWalker implements ASTWalker {

	private CodeBuilder code;
	private ASTWalker w;

	public LineNumberWalker(CodeBuilder code, ASTWalker w) {
		this.code = code;
		this.w = w;
	}
	
	@Override
	public void visitBefore(Node node) {
		code.append("// line " + node.getBeginLine() + "\n");
		w.visitBefore(node);
	}

	@Override
	public void visitAfter(Node node) {
		w.visitAfter(node);
	}

	@Override
	public void visitPrintVariable(PrintVariableNode node) {
		w.visitPrintVariable(node);
	}

	@Override
	public void visitVariableDeclaration(VariableDeclarationNode node) {
		w.visitVariableDeclaration(node);
	}

	@Override
	public void visitHTML(HTMLNode node) {
		w.visitHTML(node);
	}

	@Override
	public void visitExpression(ExpressionNode node) {
		w.visitExpression(node);
	}

	@Override
	public void visitScriptletPrint(ScriptletPrintNode node) {
		w.visitScriptletPrint(node);
	}

	@Override
	public void visitScriptlet(ScriptletNode node) {
		w.visitScriptlet(node);
	}

	@Override
	public void visitInjectDeclaration(InjectDeclarationNode node) {
		w.visitInjectDeclaration(node);
	}

	@Override
	public void visitReusableVariable(ReusableVariableNode node) {
		w.visitReusableVariable(node);
	}

	@Override
	public void visitComment(CommentNode node) {
		w.visitComment(node);
	}


}
