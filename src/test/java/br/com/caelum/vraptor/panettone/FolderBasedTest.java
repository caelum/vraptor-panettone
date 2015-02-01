package br.com.caelum.vraptor.panettone;

import java.io.File;

import org.junit.After;
import org.junit.Before;

public class FolderBasedTest {

	protected File dir;
	
	@Before
	public void setupDirs() {
		dir = new File("target/tmp");
		dir.mkdirs();
	}
	
	@After
	public void cleanupDirs() {
		new FileIO(dir, dir).deleteAll();
	}

}
