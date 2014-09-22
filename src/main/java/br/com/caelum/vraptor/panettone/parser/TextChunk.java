package br.com.caelum.vraptor.panettone.parser;


public class TextChunk {

	private final String text;
	private final int beginLine;
	
	public TextChunk(String text, int beginLine) {
		this.text = text;
		this.beginLine = beginLine;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "[chunk=" + text + "]";
	}

	public int getBeginLine() {
		return beginLine;
	}
	
}
