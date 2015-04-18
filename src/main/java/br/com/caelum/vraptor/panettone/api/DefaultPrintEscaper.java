package br.com.caelum.vraptor.panettone.api;

import com.google.common.html.HtmlEscapers;

public class DefaultPrintEscaper implements PrintEscaper {

	public String escape(String string){
		return HtmlEscapers.htmlEscaper().escape(string);
	}
}
