package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.asList;

import java.io.IOException;

public class CommandLineCompiler {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		if(args.length == 0) {
			System.out.println("Usage: java -jar vraptor-panettone.jar DEFAULT_IMPORT1 DEFAULT_IMPORT2");
			System.exit(1);
		}
		VRaptorCompiler compiler = new VRaptorCompiler(asList(args));
		compiler.start();
		System.out.println("Compiler up and running, hit ENTER to stop...");
		System.in.read();
		compiler.stop();
	}

}
