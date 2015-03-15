package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;

public class Main {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		validate(args);
		
		List<String> allArgs = asList(args);
		boolean watch = allArgs.contains("--watch");
		
		VRaptorCompiler compiler = new VRaptorCompiler(importPackages(allArgs));
		compiler.compileAll();
		if(watch) {
			compiler.start();
			System.out.println("Compiler up and running, hit ENTER to stop...");
			System.in.read();
			compiler.stop();
			System.out.println("Quitting panettone baker");
		}
	}

	private static void validate(String[] args) {
		if(asList(args).contains("-h")) {
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
