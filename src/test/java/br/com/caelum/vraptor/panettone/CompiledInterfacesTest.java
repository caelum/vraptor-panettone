package br.com.caelum.vraptor.panettone;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.api.RealInterfaceCompiler;

public class CompiledInterfacesTest extends FolderBasedTest {

	private static final List<String> NO_LISTENER = new ArrayList<>();
	private MockedCompiler compiler;
	private CompiledInterfaces interfaces;
	
	@Before
	public void before() {
		this.compiler = new MockedCompiler();
		this.interfaces = new CompiledInterfaces(dir, compiler);
	}
	
	@Test
	public void should_compile_with_an_interface_prefix() {
		interfaces.generate("header", NO_LISTENER, "// hi");
		assertEquals("templates.i_header", compiler.getName());
		assertEquals("// hi", compiler.getMethod());
	}

	@Test
	public void should_consider_packages_when_adding_the_prefix() {
		interfaces.generate("admin/header", NO_LISTENER, "// hi");
		assertEquals("templates.admin.i_header", compiler.getName());
	}

	@Test
	public void should_cache_if_already_compiled() {
		CompiledInterfaces creator = new CompiledInterfaces(dir, new RealInterfaceCompiler());
		creator.generate("admin/header", NO_LISTENER, "// hi");
		
		interfaces.generate("admin/header", NO_LISTENER, "// hi");
		assertEquals(null, compiler.getName());
	}

}
