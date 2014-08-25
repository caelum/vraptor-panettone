package br.com.caelum.vraptor.panettone.parser;


public class TextChunk {

	private final String text;
	
	public TextChunk(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}


	@Override
	public String toString() {
		return "[chunk=" + text + "]";
	}
	
}
