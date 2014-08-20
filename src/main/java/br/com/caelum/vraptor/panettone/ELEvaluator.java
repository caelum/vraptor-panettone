package br.com.caelum.vraptor.panettone;

public class ELEvaluator {

	public String evaluate(String evaluation) {
		// TODO create a EL supplier. and make this optional.
		StringBuilder sb = new StringBuilder();
		// TODO can be faster
		for(int i=0;i<evaluation.length();i++) {
			char currentChar = evaluation.charAt(i);
			if(currentChar=='\'' || currentChar=='"') {
				int end = evaluation.indexOf(currentChar, i + 1);
				if (end == -1) {
					throw new CompilationIOException("Unfinished " + evaluation.charAt(i) + " expression language statement.");
				}
				sb.append(evaluation.substring(i, end + 1));
				i = end;
			} else if(currentChar=='[') {
				sb.append(".get(");
				int end = evaluation.indexOf("]", i);
				if (end == -1) {
					throw new CompilationIOException("Not found a closing ] for your expression");
				}
				// TODO no support to nested ] so far
				String internalContent = evaluation.substring(i + 1, end);
				sb.append(evaluate(internalContent));
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
					sb.append(Character.toUpperCase(evaluation.charAt(i + 1)));
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

	private int getNextDelimiter(String evaluation, int i) {
		for(int j=i;j<evaluation.length();j++) {
			char c = evaluation.charAt(j);
			if (c == '.' || c == '[' || c == '(' || c==')' || c==']' || c=='"' || c=='\'' || c==' ')
				return j;
		}
		return -1;
	}
}
