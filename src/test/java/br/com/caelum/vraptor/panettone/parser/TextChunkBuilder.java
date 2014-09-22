package br.com.caelum.vraptor.panettone.parser;

public class TextChunkBuilder {

	public static TextChunk to(String chunk) {
		return new TextChunk(chunk, 1);
	}
}
