package br.com.caelum.vraptor.panettone;

public class VRaptorCompilationListener implements CompilationListener {

	@Override
	public String[] getInterfaces() {
		return new String[] { "br.com.caelum.vraptor.View" };
	}

	private final static String DEFAULT_CONSTRUCTOR_BODY = "try {\n"+
			"	this.result = result;\n" +
			"	this.out = req.getWriter();\n"+
			"} catch (java.io.IOException e) {\n"+
			"	throw new RuntimeException(e);\n"+
			"}\n";

	@Override
	public String overrideConstructor(String typeName) {
		return "@javax.inject.Inject\n"
				+ "@javax.annotation.Nullable\n"
				+ "private javax.servlet.http.HttpServletResponse req;\n"
				+ "@javax.inject.Inject\n"
				+ "private br.com.caelum.vraptor.Result result;\n"
				+ "@javax.annotation.PostConstruct\n"
				+ "public void fillout() {\n"
				+ "try {\n"+
				"	this.out = req.getWriter();\n"+
				"} catch (java.io.IOException e) {\n"+
				"	throw new RuntimeException(e);\n"+
				"}\n"
				+ "}\n"
				+ "private <T extends br.com.caelum.vraptor.View> T use(Class<T> type) { return result.use(type); }\n";
	}

}
