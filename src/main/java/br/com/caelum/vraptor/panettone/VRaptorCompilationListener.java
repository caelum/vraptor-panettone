package br.com.caelum.vraptor.panettone;

public class VRaptorCompilationListener implements CompilationListener {

	@Override
	public String[] getInterfaces() {
		return new String[] { "br.com.caelum.vraptor.View" };
	}

	@Override
	public String overrideConstructor(String typeName) {
		return "@javax.inject.Inject\n"
				+ "public " + typeName + "(@javax.annotation.Nullable javax.servlet.http.HttpServletResponse req) throws java.io.IOException {\n"
				+ "this.out = req.getWriter();\n"
				+ "}\n";
	}

}
