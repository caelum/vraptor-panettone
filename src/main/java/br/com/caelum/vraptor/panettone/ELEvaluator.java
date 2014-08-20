package br.com.caelum.vraptor.panettone;

import static java.lang.Character.toUpperCase;

public class ELEvaluator {

	public String evaluate(String evaluation) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < evaluation.length(); i++) {
			char currentChar = evaluation.charAt(i);
			if(currentChar=='\'' || currentChar=='"') {
				int end = findNext(evaluation, i + 1, currentChar);
				String myString = evaluation.substring(i, end + 1);
				sb.append(myString);
				i = end;
			} else if(currentChar=='[') {
				sb.append(".get(");
				int end = findNext(evaluation, i, ']');
				// TODO BUG no support to nested ] so far
				String mapAccess = evaluation.substring(i + 1, end);
				sb.append(evaluate(mapAccess));
				sb.append(")");
				i = end;
			} else if(currentChar=='.') {
				int nextDelimiter = getNextDelimiter(evaluation, i+1);
				char nextToken = ' ';
				if(nextDelimiter!=-1) nextToken = evaluation.charAt(nextDelimiter);
				else nextDelimiter = evaluation.length();
				if(nextToken == '(') {
					sb.append('.');
					sb.append(evaluation.substring(i + 1, nextDelimiter));
					i = nextDelimiter - 1;
				} else {
					sb.append(".get");
					sb.append(toUpperCase(evaluation.charAt(i + 1)));
					sb.append(evaluation.substring(i + 2, nextDelimiter));
					sb.append("()");
					i = nextDelimiter - 1;
				}
			} else {
				sb.append(currentChar);
			}
		}
		return sb.toString().replace("'", "\"");
	}

	private int findNext(String evaluation, int i, char toFind) {
		int end = evaluation.indexOf(toFind, i);
		if (end == -1)
			throw new CompilationIOException("Unfinished " + toFind + " expression language statement.");
		return end;
	}

	private int getNextDelimiter(String evaluation, int start) {
		for (int i = start; i < evaluation.length(); i++) {
			char c = evaluation.charAt(i);
			if (c == '.' || c == '[' || c == '(' || c == ')' || c == ']'
					|| c == '"' || c == '\'' || c == ' ')
				return i;
		}
		return -1;
	}
}
