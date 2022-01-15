
public class generate {

	static String resourceDataTestLocation = System.getProperty("user.dir") + "/test/ResourceData";
	static String problemOutputTestLocation = System.getProperty("user.dir") + "/test/WeeklyProblems";
	static String resourceDataLocation = System.getProperty("user.dir") + "/data/ResourceData";
	static String problemOutputLocation = System.getProperty("user.dir") + "/output/WeeklyProblems";
	
	public static void main(String[] args) {
		System.out.println("Working Directory: " + System.getProperty("user.dir"));
		if (args[0].equalsIgnoreCase("run"))
			loadAndWriteProblems(true);
		else if (args[0].equalsIgnoreCase("test")) {
			System.out.println("Test Mode On...");
			loadAndWriteProblems(false);
		}
		else {
			System.out.println("Type 'run' to run the program.");
		}
	}
	
	private static void loadAndWriteProblems(boolean isProd) {
		ResourceCache cache = null;
		ProblemsCreator generator = null;

		if (isProd) {
			cache = new ResourceCache(resourceDataLocation);
			generator = new ProblemsCreator(cache, 0, 0);
			generator.generateProblems(true);
			generator.writeOutput(problemOutputLocation);
		}
		else { // Runs the test data and outpu
			cache = new ResourceCache(resourceDataTestLocation);
			generator = new ProblemsCreator(cache, 0, 0);
			generator.generateProblems(true);
			generator.writeOutput(problemOutputTestLocation);
		}
		
		cache.saveResources();
	}
}
