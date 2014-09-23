package br.com.caelum.vraptor.panettone;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor.panettone.parser.PanettoneParser;
import br.com.caelum.vraptor.panettone.parser.ast.ASTWalker;
import br.com.caelum.vraptor.panettone.parser.ast.CommentNode;
import br.com.caelum.vraptor.panettone.parser.ast.ExpressionNode;
import br.com.caelum.vraptor.panettone.parser.ast.HTMLNode;
import br.com.caelum.vraptor.panettone.parser.ast.InjectDeclarationNode;
import br.com.caelum.vraptor.panettone.parser.ast.PrintVariableNode;
import br.com.caelum.vraptor.panettone.parser.ast.ReusableVariableNode;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletNode;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletPrintNode;
import br.com.caelum.vraptor.panettone.parser.ast.VariableDeclarationNode;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class PanettoneWalker implements ASTWalker {
	
	private final StringBuilder code = new StringBuilder();
	private final ELEvaluator el = new ELEvaluator();
	private final List<String> variables = new ArrayList<>();
	private final StringBuilder injects = new StringBuilder();

	@Override
	public void visitPrintVariable(PrintVariableNode node) {
		String value = node.getExpr();
		elValue(value, node.getBeginLine());
	}

	private void elValue(String value, int beginLine) {
		String expression = el.evaluate(value);
		code.append("write(" + expression +"); // line " + beginLine + "\n");
	}

	@Override
	public void visitVariableDeclaration(VariableDeclarationNode node) {
		String variableFull = node.getType() + " " + node.getName() + " /* line " + node.getBeginLine() + " */";
		variables.add(variableFull);
		
		if(node.getDefaultValue()!=null) {
			code.append(String.format("if(%s == null) %s = %s;\n", node.getName(), node.getName(), node.getDefaultValue()));
		}
	}

	@Override
	public void visitHTML(HTMLNode node) {
		linePrint(node.getHtml(), node.getBeginLine());
	}
	
	private void linePrint(String parts, int beginLine) {
		String[] allParts = parts.split("\n");
		int last = allParts.length - 1;
		code.append("// line " + beginLine + "\n");
		for (int i = 0; i < allParts.length; i++) {
			String part = allParts[i];
			if(part.isEmpty()) continue;
			String slash = i == last ? "" : "\\n";
			code.append("write(\"" + escapeSlashesAndQuotes(part) + slash + "\");\n");
		}
	}

	private String escapeSlashesAndQuotes(String content) {
		return content.replace("\\", "\\\\").replace("\"", "\\\"");
	}

	@Override
	public void visitExpression(ExpressionNode node) {
		elValue(node.getExpr(), node.getBeginLine());
	}

	@Override
	public void visitScriptletPrint(ScriptletPrintNode node) {
		code.append("write(" + node.getExpr() + "); // line " + node.getBeginLine() + "\n");
	}

	@Override
	public void visitScriptlet(ScriptletNode node) {
		code.append(node.getScriptlet() + "// line " + node.getBeginLine() + "\n");
	}

	@Override
	public void visitInjectDeclaration(InjectDeclarationNode node) {
		injects.append("@javax.inject.Inject private " + node.getType() + " " + node.getName() + "; // line " + node.getBeginLine() + "\n");
	}

	public String getJavaCode() {
		String parameters = variables.stream().collect(joining(","));
		String prefix = "public void render(" + parameters + ") {\n";
		String sufix = "}\n";
		return injects + prefix + code.toString() + sufix;
	}

	@Override
	public void visitReusableVariable(ReusableVariableNode node) {
		String name = node.getName();
		code.append(format("Runnable %s = () -> {\n", name));
		String content = node.getContent();
		new PanettoneParser().parse(content).walk(this);
		code.append("}; // line " + node.getBeginLine() + "\n");
	}

	/**
	 * Outputs the comment for debugging purposes
	 */
	@Override
	public void visitComment(CommentNode node) {
		code.append("/*" + node.getComment() + " // line " + node.getBeginLine() + " */");
	}

}
