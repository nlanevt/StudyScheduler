
public class generate {

	static String TEST_DATA_LOCATION = System.getProperty("user.dir") + "/test/ResourceData";
	static String TEST_OUTPUT_LOCATION = System.getProperty("user.dir") + "/test/WeeklyProblems";
	static String PROD_DATA_LOCATION = System.getProperty("user.dir") + "/data/ResourceData";
	static String PROD_OUTPUT_LOCATION = System.getProperty("user.dir") + "/output/WeeklyProblems";
	
	public static void main(String[] args) {
		System.out.println("Working Directory: " + System.getProperty("user.dir"));
		loadAndWriteProblems(args);
	}
	
	private static void loadAndWriteProblems(String[] args) {
		String data_location = null;
		String output_location = null;
		
		if (args.length != 2) System.out.println("Insufficient number of commands. Format: run/test [number of problems]");
		
		if (args[0].equalsIgnoreCase("run")) { //Run Production Data
			data_location = PROD_DATA_LOCATION;
			output_location = PROD_OUTPUT_LOCATION;
			
		}
		else if (args[0].equalsIgnoreCase("test")) { // Run Test Mode Data
			data_location = TEST_DATA_LOCATION;
			output_location = TEST_OUTPUT_LOCATION;		
		}
		else {
			System.out.println("Type 'run' to run the program.");
			return;
		}
		
		int new_problems_count = Integer.valueOf(args[1]);
		
		ResourceCache cache = new ResourceCache(data_location);
		ProblemsCreator generator = new ProblemsCreator(cache, new_problems_count, 0, 0);
		generator.generateProblems(true);
		generator.writeOutput(output_location);
		
		cache.saveResources();
	}
}
