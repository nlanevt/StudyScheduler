import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class ProblemsCreator {
	private ResourceCache cache;
	private ArrayList<String> problems;
	private int data_problems_amount;
	private int alg_problems_amount;
	private int new_problems_count = 0;
	//private int MAX_CS_PROBLEMS = 0;
	
	
	public ProblemsCreator(ResourceCache cache, int number_of_new_problems, int data_problems_amount, int alg_problems_amount) {
		this.data_problems_amount = (cache.getDataStructures().length >= data_problems_amount) ? data_problems_amount : cache.getDataStructures().length;
		this.alg_problems_amount = (cache.getAlgorithms().length >= alg_problems_amount) ? alg_problems_amount : cache.getAlgorithms().length;
		this.cache = cache;
		problems = null;
		new_problems_count = number_of_new_problems;
	}
	
	public ArrayList<String> generateProblems(boolean isOrdered) {
		if (problems != null) return problems; // Do not generate twice. Has a domino affect on the problems. 
		
		problems = new ArrayList<String>();
		String[] data_alg_problems = generateDataAndAlgProblems();
		ArrayList<String> resource_problems = generateResourceProblems(isOrdered);
		String space = " ";
		
		// Get data, alg and book problems and put them all into the result array
		int i = 0;
		for (i = 0; i < data_problems_amount+alg_problems_amount; i++) 
			problems.add(i+1 + ".)" + space + data_alg_problems[i]);
		
		for (int j = 0; j < resource_problems.size(); j++) {
			problems.add(i+1 + ".)" + space + resource_problems.get(j));
			i++;
		}
		
		return problems;
	}
	
	public boolean writeOutput(String problemsOutputLocation) {
		if (!cache.isDateReadyToSave() || problems == null) return false;
		
		try{
		    PrintWriter writer = new PrintWriter(problemsOutputLocation, "UTF-8");

		    writer.println("PROBLEMS FOR THE WEEK OF " + 
		    		DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(Utils.GetMostRecentSunday())+ ": ");
		    
			for (int i = 0; i < problems.size(); i++) {
				writer.println(problems.get(i));
			}
		    
		    writer.close();
		    System.out.println("Output Written to " + problemsOutputLocation);
		} catch (IOException e) {
			System.err.format("--Exception occurred trying to write to '%s'.", problemsOutputLocation);
		    e.printStackTrace();
		}
		
		return true;
	}
	
	/*
	 * Randomly select a chosen number of data structures and algorithms to build
	 * Problem form: Build a Binary Search Tree in C
	 * There can be no repetition of problems.
	 * 
	 */
	private String[] generateDataAndAlgProblems() {
		String[] result = new String[data_problems_amount + alg_problems_amount];
		int[] data_indexes = new int[data_problems_amount];
		int[] alg_indexes = new int[alg_problems_amount];
		int data_cache_size = cache.getDataStructures().length;
		int alg_cache_size = cache.getAlgorithms().length;
		int lang_cache_size = cache.getLanguages().length;
		
		int counter = 0;
		// Build the Data problems first:
		for (int i = 0; i < data_problems_amount; i++) {
			int j = 0;
			int number = (int)(Math.random() * data_cache_size);
			
			while (j < data_problems_amount) {
				if (number == data_indexes[j]) {
					j = 0;
					number = (int)(Math.random() * data_cache_size);
				}
				j++;
			}
			
			data_indexes[i] = number;
			result[counter++] = "Implement a " + cache.getDataStructures()[number] 
					+ " in " + cache.getLanguages()[(int)(Math.random() * lang_cache_size)];
		}
		
		// Build the Algorithm problems:
		for (int i = 0; i < alg_problems_amount; i++) {
			int j = 0;
			int number = (int)(Math.random() * alg_cache_size);
			
			while (j < alg_problems_amount) {
				if (number == alg_indexes[j]) {
					j = 0;
					number = (int)(Math.random() * alg_cache_size);
				}
				j++;
			}
			
			alg_indexes[i] = number;
			result[counter++] = "Implement a " + cache.getAlgorithms()[number] 
					+ " in " + cache.getLanguages()[(int)(Math.random() * lang_cache_size)];
		}
		
		return result;
	}

	/*
	 * (1) Get and Append All Due Chapters to result
	 * (2) Get and Append New Chapters to result
	 * *Ignores individual problems
	 */
	private ArrayList<String> generateResourceProblems(boolean isOrdered) {
		ArrayList<String> result = new ArrayList<String>();
		
		//(1) get all due chapters
		result.addAll(getDueChapters());
		
		//(2) Find New Random Math Chapter(s) and append to result
		result.addAll(getNewChapterOfType(Field.MATH, new_problems_count, isOrdered));
		
		//(3) Find New Random CS Chapter(s) and append to result
		//result.addAll(getNewChapterOfType(Field.CS, MAX_CS_PROBLEMS, isOrdered));
		
		return result;
	}
	
	private ArrayList<String> getDueChapters() {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Resource> resources = cache.getActiveResources();
		
		int due_chapter_counter = 0;
		//(1) Find all due chapters from active resources and append them to result
		for (Resource resource : resources) {
			ArrayList<Chapter> due_chapters = resource.getDueChapters();
			for (Chapter chapter : due_chapters) {
				String partition_name = chapter.getChapterNumber().contains(".") ? "Section" : "Chapter";
				result.add("Review " + resource.getTitle() + ", " + partition_name + " " + chapter.getChapterNumber());
				due_chapter_counter++;
			}
		}
		
		if (due_chapter_counter == 0)
			result.add("* No Chapters Due to Review!");
		
		return result;
	}
	
	/*
	 * Get an unstudied chapter
	 * Ordered = the lowest chapter unstudied in the resource.
	 * Unordered = a random chapter from the resource
	 * amount = the number of chapters in that field. 
	 */
	private ArrayList<String> getNewChapterOfType(Field field, int amount, boolean isOrdered) {
		ArrayList<String> result = new ArrayList<String>();
		int new_chapter_counter = 0;
		ArrayList<Resource> resources = cache.getActiveResourcesOfType(field);
		while (!resources.isEmpty() && new_chapter_counter < amount) {
			int resource_index = (int) (Math.random() * resources.size());
			Resource resource = resources.get(resource_index);
			ArrayList<Chapter> new_chapters = resource.getNewChapters();
			
			if (new_chapters.isEmpty()) {
				resources.remove(resource_index);
			}
			else {
				int chapter_index = isOrdered ? 0 : (int) (Math.random() * new_chapters.size());
				Chapter new_chapter = new_chapters.get(chapter_index);
				String partition_name = new_chapter.getChapterNumber().contains(".") ? "Section" : "Chapter";
				result.add("NEW: Study " + resource.getTitle() + ", " + partition_name + " " + new_chapter.getChapterNumber());
				new_chapter.SetAsStudied();
				new_chapter_counter++;
			}
		}
		
		if (new_chapter_counter == 0) {
			result.add("* No New " + field.toString() + " Chapters available!");
		}
		
		return result;
	}
}
