package br.com.caelum.vraptor.panettone.parser.ast;

import java.util.LinkedList;

import br.com.caelum.vraptor.panettone.parser.RuleChunk;
import br.com.caelum.vraptor.panettone.parser.TextChunk;

public class PannetoneAST {

	private LinkedList<Node> nodes;
	
	public PannetoneAST() {
		this.nodes = new LinkedList<Node>();
	}
	
	public void createNode(RuleChunk rule, TextChunk text) {
		nodes.add(rule.getRule().getNode(text));
	}
	
	public void walk(ASTWalker walker) {
		
		for(Node node : nodes) {
			walker.visitBefore(node);
			node.accept(walker);
			walker.visitAfter(node);
		}
	}

	@Override
	public String toString() {
		return nodes.toString();
	}

}
