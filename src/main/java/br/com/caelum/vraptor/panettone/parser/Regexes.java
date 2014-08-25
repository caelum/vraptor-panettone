package br.com.caelum.vraptor.panettone.parser;

public class Regexes {

	public static final String RULECHUNK_START_REGEX = "\\(###";

	public static final String SPACE = "\\s*";
	public static final String CLASS_NAME = "[\\w\\.\\_]+";
	public static final String GENERICS = "(<\\s*" + CLASS_NAME + "\\s*>)?";
	
	
	public static final String DOT = "\\.";
	public static final String BRACKETS = "\\[\\]";
	public static final String QUOTES = "\\\"";
	public static final String SIMPLE_QUOTES = "'";
	public static final String UNDERSCORE = "\\_";
	public static final String SPECIAL_CHARS = "[\\w" + DOT + BRACKETS + QUOTES + SIMPLE_QUOTES + UNDERSCORE + "]";
}
