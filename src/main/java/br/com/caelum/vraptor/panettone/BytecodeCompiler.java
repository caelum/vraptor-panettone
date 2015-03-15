package br.com.caelum.vraptor.panettone;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BytecodeCompiler implements CompilationListener {

	private final Map<String, Class<?>> types = new HashMap<>();
	private final SimpleJavaCompiler compiler;

	public BytecodeCompiler(SimpleJavaCompiler compiler) {
		this.compiler = compiler;
	}

	/**
	 * Undocumented support: retrieves a compiled class
	 */
	public Class<?> get(String type) {
		if(!types.containsKey(type)) {
			throw new NullPointerException("Unable to find compiled " + type);
		}
		return types.get(type);
	}

	@Override
	public String[] getInterfaces() {
		return null;
	}

	@Override
	public String overrideConstructor(String typeName) {
		return null;
	}

	@Override
	public void finished(File file, CompiledTemplate template) {
		compiler.compileToBytecode(template.getType().getFile());
		types.put(template.getType().getPackagedName(), compiler.getTypeFromNewClassLoader(template));
	}

	@Override
	public void finished(File f, Exception e) {
	}

	@Override
	public void clear() {
	}

	@Override
	public String preprocess(String content) {
		return content;
	}

	@Override
	public Variable[] getExtraInjections() {
		return new Variable[]{};
	}

}
