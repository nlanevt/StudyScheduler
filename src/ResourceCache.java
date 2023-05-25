import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;

enum Field {
	MATH, CS
};

public class ResourceCache {
	private static String[] data_structures;
	private static String[] algorithms;
	private static String[] languages;
	private static ArrayList<Resource> resources;
	private String ResourceDataLocation;
	private LocalDate date_last_saved;
	
	private int MAX_SAVE_DIFFERENCE = 6;
	
	public ResourceCache(String resourceDataLocation) {
		resources = new ArrayList<Resource>();
		this.ResourceDataLocation = resourceDataLocation;
		this.setDataStructures();
		this.setAlgorithms();
		this.setLanguages();
		this.setResources();
	}
	
	public String[] getDataStructures() {
		return data_structures;
	}
	
	public String[] getAlgorithms() {
		return algorithms;
	}
	
	public String[] getLanguages() {
		return languages;
	}
	
	public ArrayList<Resource> getResources() {
		return resources;
	}
	
	public ArrayList<Resource> getActiveResources() {
		ArrayList<Resource> result = new ArrayList<Resource>();
		for (Resource resource : resources) {
			if (resource.isActive())
				result.add(resource);
		}
		
		return result;
	}
	
	public ArrayList<Resource> getActiveResourcesOfType(Field field) {
		ArrayList<Resource> result = new ArrayList<Resource>();
		for (Resource resource : resources) {
			if (resource.isActive() && resource.getField() == field) {
				result.add(resource);
			}
		}
		
		return result;
	}
	
	public void displayResources() {
		if (resources == null) return;
		
		for (Resource resource : resources) {
			resource.display();
			System.out.println("");
		}
	}
	
	public boolean isDateReadyToSave() {
		long diff = date_last_saved == null ? 7 : ChronoUnit.DAYS.between((Temporal) date_last_saved, Utils.GetMostRecentSunday());
		return MAX_SAVE_DIFFERENCE <= diff;
	}
	
	//Data Structures and Algorithms in Java;Lafore;15;0,6,6,5,5,5,5,5,0,5,5,5,5,5,0;T;CS;
	//Format: [chapter_number|date_first_studied|isActive]
	public void saveResources() {
		
		if (!this.isDateReadyToSave()) {
			System.out.println("It is too soon to generate new problems!");
			return;
		}
		
		this.saveBackup();
		
		String col = ";";
		String sub = "|";
		
		try{
		    PrintWriter writer = new PrintWriter(ResourceDataLocation, "UTF-8");
		    
		    writer.println(Utils.GetMostRecentSunday());

		    for (Resource resource : resources) {
		    	String row = resource.getTitle() + col + resource.getAuthor() + col 
		    			+ resource.getNumberOfChapters() + col;
		    	
		    	Chapter[] chapters = resource.getChapters();
		    	
		    	for (int i = 0; i < chapters.length; i++) {
		    		String comma = i < chapters.length-1 ? "," : ""; 
		    		Chapter chapter = chapters[i];
		    		
		    		/*
		    		 * TODO: Need to add exceptions for when times studied and date last studied don't match up.
		    		 * Example: times studied = 2 but last_date_studied is null
		    		 */
		    		row = row + "[" + chapter.getChapterNumber() + 
	    					sub + (chapter.getDateFirstStudied() == null ? "" : chapter.getDateFirstStudied()) + 
	    					sub + (chapter.isActive() ? "T" : "F") + 
	    					"]" + comma;
		    	}
		    	
		    	row = row + col + (resource.isActive() ? "T" : "F") + col + resource.getField().toString() + col;
		    	
		    	writer.println(row);
		    }
		    
		    writer.close();
		    System.out.println("Save Succeeded!");
		    this.displayStats();
		} catch (IOException e) {
			System.err.format("--Exception occurred trying to write to '%s'.", ResourceDataLocation);
		    e.printStackTrace();
		}
	}
	
	/*
	 * Saves the previous copy to a .bak file just in case it was a bad generation.
	 */
	private void saveBackup() {
		try {
			PrintWriter writer = new PrintWriter(ResourceDataLocation+".bak", "UTF-8");
			BufferedReader reader = new BufferedReader(new FileReader(ResourceDataLocation));
		    String line;
		    
		    while ((line = reader.readLine()) != null) {
		    	writer.println(line);
		    }
			
		    reader.close();
		    writer.close();
		    System.out.println("Backup Saved to " + ResourceDataLocation);
		}
		catch (Exception e) {
		    System.err.format("--Exception occurred trying to save the backup of '%s'.", ResourceDataLocation);
		    e.printStackTrace();
		}
	}
	
	private void setDataStructures() {
		data_structures = new String[] {"Binary Search Tree", "Red-Black Tree", "AVL Tree", "B-Tree", "2-3 Tree", 
				"Van Emde Boas Tree", "Spanning Tree", "Minimum Spanning Tree", "Quad Tree", 
				"Splay Tree", "Hash Table", "Chained Hash Table", "Skip List", "Stack",
				"Linked List", "Double Linked List", "Array Heap", "Tree Heap", "Graph", "Directed Graph", 
				"Weighted Graph"
		};
	}
	
	private void setAlgorithms() {
		algorithms = new String[] {"Insertion Sort", "Selection Sort", "Mergesort", "Radixsort", 
				"Quicksort", "Heapsort", "Binary Search", "Matrix Multiplication", "Dijkstras algorithm",
				"Floyd-Warshall algorithm", "Johnsons algorithm", "Bellman-Ford algorithm", "Kruskals algorithm",
				"Prims algorithm", "All-Pairs-Shortest-Path algorithm", "Ford-Fulkerson algorithm", "Edmond-Karp algorithm",
				"Warshalls algorithm", "Depth-first search", "Breadth-first search", "Topological sort algorithm",
				"Floyds algorithm", "Simplex algorithm"
		};
	}
	
	private void setLanguages() {
		languages = new String[] {"Java", "C"};
	}
	
	private boolean setResources() {
		boolean result = false;
		int column_max = 6;
		int row_count = 1;
		String chapter_delimiter = "\\|";
		
		// Get and set date_last_run in order to ensure cache is not changed every time it is called, but only on a weekly basis
		//  no matter if, for whatever reason, the generate method is called by LaunchDaemon or Crontab
		
		try {
		    BufferedReader reader = new BufferedReader(new FileReader(ResourceDataLocation));
		    String line;
		    String[] tokens;
		    
		    while ((line = reader.readLine()) != null) {
		    	tokens = line.split(";");
		    	
		    	//Get the date if it is there
		    	if (row_count == 1 && date_last_saved == null && tokens.length < column_max) {
		    		date_last_saved = LocalDate.parse(line);
		    	}
		    	else if (tokens.length != column_max) {
		    		System.out.println("<1> Error with Resource Data line " + row_count);
		    		resources = null;
		    		break;
		    	}
		    	else {
		    		String title = tokens[0];
			    	String author = tokens[1];
			    	int number_of_chapters = Integer.valueOf(tokens[2]);
			    	String[] chapters = tokens[3].split(",");
			    	boolean isResourceActive = tokens[4].equalsIgnoreCase("T") ? true : false;
			    	Field field = Field.valueOf(tokens[5]);
			    	
			    	if (chapters.length != number_of_chapters) {
			    		System.out.println("<2> Error with Resource Data line " + row_count + " <" + title + "> " 
			    				+ " <" + author + "> " + " <" + number_of_chapters + "> " + " <" + chapters.length + "> " 
			    				+ " <" + isResourceActive + "> ");
			    		resources = null;
			    		break;
			    	}
			    	
			    	Resource resource = new Resource(title, author, number_of_chapters, isResourceActive, field);
			    	
			    	//Format: [chapter_number|first_date_studied|isActive]
			    	for (int i = 0; i < number_of_chapters; i++) {
			    		if (chapters[i].contains("[") || chapters[i].contains("]")) { 
			    			// Already created and written Resource
			    			String[] split = chapters[i].replace("[", "").replace("]", "").split(chapter_delimiter, -1);
			    			if (split.length != 3) {
			    				System.out.println("<3> Error with Resource Data line: Incorrect chapter formatting => " + row_count + " <" + title + "> " 
			    						+ " <" + author + "> " + " <" + number_of_chapters + "> " 
			    						+ " <" + chapters.length + "> " + " <" + isResourceActive + "> <" + split.length + ">");
			    				resources = null;
			    				break;
			    			}
			    			
			    			String chapter_number = split[0];
			    			LocalDate first_date_studied = (split[1].equalsIgnoreCase("") || split[1] == null) ? null : LocalDate.parse(split[1]);
			    			boolean isChapterActive = (split[2].equalsIgnoreCase("T") || split[2].equalsIgnoreCase("") || split[2] == null) ? true : false;
			    			
			    			resource.AddChapter(i+1, chapter_number, first_date_studied, isChapterActive);
			    			
			    		}
			    		else { 
			    			// Brand new and Blank Resource Creation
				    		resource.AddChapter(i+1, String.valueOf(i+1), (LocalDate)null, true); 
			    		}
			    	}
			    	
			    	resources.add(resource);
		    	}
		    	
		    	
		    	row_count++;    			
		    }
		    
		    reader.close();
		    result = true;
		    System.out.println("Successfully read and parsed resource file located at " + ResourceDataLocation);
		}
		catch (Exception e) {
		    System.err.format("--Exception occurred trying to read '%s'.", ResourceDataLocation);
		    e.printStackTrace();
		}
		
		return result;
	}

	public int getNumberOfChaptersLeft() {
		int total = 0;
		for (Resource resource : resources) {
			total = resource.isActive() ? total + resource.getNewChapters().size() : total;
		}
		
		return total;
	}
	
	public int getTotalChapters() {
		int totalChapters = 0;
		
		for (Resource resource : resources) {
			totalChapters = resource.isActive() ? totalChapters + resource.getNumberOfChapters() : totalChapters;
		}
		
		return totalChapters;
	}
	
	public float getPercentageOfChaptersStudied() {
		float totalStudied = 0;
		float totalChapters = 0;
		
		for (Resource resource : resources) {
			if (resource.isActive()) {
				totalStudied = totalStudied + (resource.getNumberOfChapters() - resource.getNewChapters().size());
				totalChapters = totalChapters + resource.getNumberOfChapters();
			}
		}
		
		return (totalStudied / totalChapters) * 100;
	}
	
	private void displayStats() {
		System.out.println("Total Chapters: " + getTotalChapters() + "; Chapters Left: " + getNumberOfChaptersLeft() + "; Percent Studied: " + getPercentageOfChaptersStudied() + "%");
	}
}
