package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CommandLineCompiler {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		if(args.length == 0) {
			System.out.println("Usage: java -jar vraptor-panettone.jar [--watch] DEFAULT_IMPORT1 DEFAULT_IMPORT2");
			System.out.println("--watch means keep watching the folder");
			System.exit(1);
		}
		List<String> allArgs = asList(args);
		boolean watch = shouldWatch(importPackages(allArgs));
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

	private static List<String> importPackages(List<String> allArgs) {
		allArgs.remove("--watch");
		return allArgs;
	}

	private static boolean shouldWatch(List<String> allArgs) {
		boolean watch = false;
		if(importPackages(allArgs).contains("--watch")) 
			watch = true;
		return watch;
	}

}
