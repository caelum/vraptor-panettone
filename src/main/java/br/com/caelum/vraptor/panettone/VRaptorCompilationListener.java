package br.com.caelum.vraptor.panettone;

public class VRaptorCompilationListener implements CompilationListener {

	@Override
	public String[] getInterfaces() {
		return new String[] { "br.com.caelum.vraptor.View" };
	}

	@Override
	public String overrideConstructor(String typeName) {
		return "@javax.inject.Inject\n"
				+ "private javax.servlet.http.HttpServletResponse res;\n"
				+ "@javax.inject.Inject\n"
				+ "private br.com.caelum.vraptor.Result result;\n"
				+ "@javax.annotation.PostConstruct\n"
				+ "public void fillout() {\n"
				+ "try {\n"
				+ "	res.setContentType(\"text/html; charset=UTF-8\");\n"
				+ "	this.out = res.getWriter();\n"
				+ "} catch (java.io.IOException e) {\n"
				+ "	throw new RuntimeException(e);\n"
				+ "}\n"
				+ "}\n"
				+ "private <T extends br.com.caelum.vraptor.View> T use(Class<T> type) { return result.use(type); }\n";
	}

}
