package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;

public class CommandLineCompiler {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		validate(args);
		
		List<String> allArgs = asList(args);
		boolean watch = allArgs.contains("--watch");
		VRaptorCompiler compiler = new VRaptorCompiler(importPackages(allArgs));
		if(watch) {
			compiler.start();
			System.out.println("Compiler up and running, hit ENTER to stop...");
			System.in.read();
			compiler.stop();
		} else {
			compiler.compileAll();
		}
	}

	private static void validate(String[] args) {
		if(args.length == 0) {
			System.err.println("Usage: java -jar vraptor-panettone-version.jar [--watch] DEFAULT_IMPORT1 DEFAULT_IMPORT2");
			System.err.println("--watch means keep watching the folder");
			System.exit(1);
		}
	}

	private static List<String> importPackages(List<String> args) {
		return args.stream()
				.filter(s -> !s.equals("--watch"))
				.collect(toList());
	}

}
