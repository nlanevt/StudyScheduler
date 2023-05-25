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
	
	/*
	 * Format: [chapter_number|chapter_title|date_first_studied|isActive|(problems)]
	 */
	public boolean AddChapter(int chapter_index, String chapter_number, LocalDate date_first_studied, boolean isActive) {
		if (chapters == null || number_of_chapters == 0)
			return false;
					
		chapters[chapter_index-1] = new Chapter(chapter_number, date_first_studied, isActive);
		
		return true;
	}
	
	public void display() {
		System.out.println("Title: " + title);
		System.out.println("Field: " + field.toString());
		Chapter chapter = null;
		for (int c = 0; c < number_of_chapters; c++) {
			chapter = chapters[c];
			if (chapter != null) {
				String partition_name = chapter.getChapterNumber().contains(".") ? "Section" : "Chapter";
				System.out.print(partition_name + " " + chapter.getChapterNumber() + ":\t");
			}
		}
	}
	
	public int getNumberOfChapters() {
		return number_of_chapters;
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
			if (chapter.isActive() && chapter.IsUnstudied()) {
				new_chapters.add(chapter);
			}
		}
		
		return new_chapters;
	}
}
