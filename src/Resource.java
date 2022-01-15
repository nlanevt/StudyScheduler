import java.time.LocalDate;
import java.util.ArrayList;

public class Resource {
	private String title;
	private String author;
	private int number_of_chapters;
	private boolean isActive;
	private Field field;
	
	private Chapter[] chapters;
	
	public Resource(String title, String author, int number_of_chapters, boolean isActive, Field field) {
		this.number_of_chapters = number_of_chapters;
		this.title = title;
		this.author = author;
		this.isActive = isActive;
		chapters = new Chapter[this.number_of_chapters];
		this.field = field;
	}
	
	public boolean addProblems(int chapter_index, int number_of_problems, int[] excluded, int[] completed) {
		if (chapters == null || number_of_chapters == 0)
			return false;
		
		chapters[chapter_index-1] = new Chapter(this, chapter_index, number_of_problems, null); // Official version
		
		// Set -1 to represent problems that are to be excluded. 
		if (excluded != null) {
			for (int i = 0; i < excluded.length; i++) {
				this.excludeProblem(chapter_index, excluded[i]);
			}
		}
		
		// Set 1 to represent problems that have been completed.
		if (completed != null) {
			for (int i = 0; i < completed.length; i++) {
				this.setProblemComplete(chapter_index, completed[i]);
			}
		}
		
		
		return true;
	}
	
	/*
	 * Format: [chapter_number|chapter_title|times_studied|last_date_studied|isActive|(problems)]
	 */
	public boolean addProblems(int chapter_index, String chapter_number, String chapter_title, int times_studied, 
			LocalDate last_date_studied, boolean isActive, int number_of_problems, String[] problems) {
		if (chapters == null || number_of_chapters == 0)
			return false;
		
		if (times_studied == 0) last_date_studied = null;
			
		chapters[chapter_index-1] = new Chapter(this, chapter_number, chapter_title, 
				problems, number_of_problems, times_studied, last_date_studied, isActive);
		
		return true;
	}
	
	public void display() {
		System.out.println("Title: " + title);
		System.out.println("Field: " + field.toString());
		Chapter chapter = null;
		Problem[] problems = null;
		for (int c = 0; c < number_of_chapters; c++) {
			chapter = chapters[c];
			if (chapter != null) {
				problems = chapter.getProblems();
				String partition_name = chapter.getChapterNumber().contains(".") ? "Section" : "Chapter";
				System.out.print(partition_name + " " + chapter.getChapterNumber() + ":\t");
				
				if (problems != null) {
					for (int p = 0; p < problems.length; p++) {
						
						if (problems[p].isActive())
							System.out.print(problems[p].getTimesCompleted() + " ");
						else
							System.out.print("I ");
					}
					
					System.out.println("");
				}
			}
		}
	}
	
	public boolean setProblemComplete(int chapter, int problem) {
		if (!check(chapter, problem)) return false;
		
		chapters[chapter-1].getProblems()[problem-1].wasCompleted();
		return true;
	}
	
	public boolean excludeProblem(int chapter, int problem) {
		if (!check(chapter, problem)) return false;
		
		chapters[chapter-1].getProblems()[problem-1].setActiveStatus(false);
		return true;
	}
	
	public int getNumberOfChapters() {
		return number_of_chapters;
	}
	
	public Problem[] getProblemsAtChapter(int chapter) {
		return chapters[chapter-1].getProblems();
	}
	
	public Chapter[] getChapters() {
		return chapters;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public Field getField() {
		return field;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public ArrayList<Chapter> getDueChapters() {
		ArrayList<Chapter> due_chapters = new ArrayList<Chapter>();
		
		for (Chapter chapter : chapters) {
			if (chapter.isActive() && chapter.isDue()) {
				due_chapters.add(chapter);
			}
		}
		
		return due_chapters;
	}
	
	/*
	 * "NewChapters" means a chapter that was never studied
	 */
	public ArrayList<Chapter> getNewChapters() {
		ArrayList<Chapter> new_chapters = new ArrayList<Chapter>();
		
		for (Chapter chapter : chapters) {
			if (chapter.isActive() && chapter.getNumberOfTimesStudied() == 0) {
				new_chapters.add(chapter);
			}
		}
		
		return new_chapters;
	}
	
	private boolean check(int chapter, int problem) {
		if (chapter >= number_of_chapters) 
			return false;
		if (chapters[chapter-1] == null) 
			return false;
		if (problem >= chapters[chapter-1].getProblems().length) 
			return false;
		
		return true;
	}
}
