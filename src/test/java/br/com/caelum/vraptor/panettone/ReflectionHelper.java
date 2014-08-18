package br.com.caelum.vraptor.panettone;

import static org.junit.Assert.assertNotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionHelper {
	static void run(PrintWriter out, Class<?>[] types, Class<?> type, Object... params) {
		try {
			Method method = type.getDeclaredMethod("render", types);
			assertNotNull(method);
			
			run(out, type, method, params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	static  void run(PrintWriter out, Class<?> type, Method method, Object[] params) throws InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object instance = createInstance(out, type);
		method.invoke(instance, params);
	}
	static Object createInstance(PrintWriter out, Class<?> type) throws InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object instance = type.getConstructor(PrintWriter.class).newInstance(out);
		return instance;
	}
	
	static String run(Class<?> type, Class<?>[] types, Object...params) {
		StringWriter output = new StringWriter();
		PrintWriter out = new PrintWriter(output, true);
		run(out, types, type, params);
		return output.getBuffer().toString();
	}
}
