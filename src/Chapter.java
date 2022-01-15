import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class Chapter {
	private Resource resource;
	private String chapter_number;
	private String chapter_title;
	private int times_studied;
	private LocalDate last_date_studied;
	private boolean isActive;
	private Problem[] problems;
	private int number_of_problems;
	
	public Chapter(Resource resource, String chapter_number, String chapter_title, String[] problems, int number_of_problems, int times_studied, LocalDate last_date_studied, boolean isActive) {
		this.times_studied = times_studied;
		this.chapter_number = chapter_number;
		this.chapter_title = chapter_title == null ? "" : chapter_title;
		this.resource = resource;
		this.last_date_studied = last_date_studied;
		this.isActive = isActive;
		this.number_of_problems = number_of_problems;
		this.problems = this.buildProblems(problems);
	}
	
	public Chapter(Resource resource, int chapter_number, int number_of_problems, String chapter_title) {
		times_studied = 0;
		last_date_studied = null;
		this.chapter_number = String.valueOf(chapter_number);
		this.chapter_title = chapter_title == null ? "" : chapter_title;
		this.resource = resource;
		this.isActive = true;
		this.number_of_problems = number_of_problems;
		
		problems = buildProblems(null);
	}
	
	public void setActiveStatus(boolean active_status) {
		this.isActive = active_status;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public int getNumberOfTimesStudied() {
		return times_studied;
	}
	
	public Resource getResource() {
		return resource;
	}
	
	public String getTitle() {
		return chapter_title;
	}
	
	public String getChapterNumber() {
		return chapter_number;
	}
	
	public Problem[] getProblems() {
		return problems;
	}
	
	public int getNumberOfProblems() {
		return number_of_problems;
	}
	
	public void Studied() {
		last_date_studied = last_date_studied == null ? LocalDate.now() : last_date_studied.plusWeeks(getInterval());
		times_studied++;
	}
	
	public boolean isDue() {
		long diff = last_date_studied == null ? 0 : ChronoUnit.WEEKS.between((Temporal) last_date_studied, LocalDate.now());
		return times_studied > 0 && getInterval() <= diff;
	}
	
	public LocalDate getLastDateStudied() {
		return last_date_studied;
	}
	/*
	 * interval chart
	 * 	t|formula|i
	 * 	0|2(0)+1 |1
	 *  1|2(1)+1 |3
	 *  2|2(2)+1 |5
	 *  3|2(3)+1 |7
	 *  ...
	 *  interval = 2*(times_studied)+1
	 *  Due Date = last_date_studied + interval (weeks)
	 */
	private long getInterval() {
		return (2*times_studied) + 1;
	}
	
	private Problem[] buildProblems(String[] problems) {
		
		Problem[] problem_set = null;
		
		if (problems == null) {
			problem_set = new Problem[this.number_of_problems];
			for (int i = 0; i < this.number_of_problems; i++) {
				problem_set[i] = new Problem(this, i+1, null);
			}
		}
		else if (problems.length != this.number_of_problems) {
			System.out.println("Error with building problems in Chapter " + chapter_number + "; Lengths not equal!");
		}
		else {
			problem_set = new Problem[this.number_of_problems];
			int times_completed = 0;
			boolean isProblemActive = true;
			for (int i = 0; i < problems.length; i++) {
				if (problems[i] == null) {
					problem_set[i] = new Problem(this, i+1, null);
				}
				else {
					isProblemActive = problems[i].equalsIgnoreCase("I") ? false : true;
					times_completed = isProblemActive ? Integer.valueOf(problems[i]) : 0;
					problem_set[i] = new Problem(this, i+1, times_completed, isProblemActive);
				}
			}
		}
		
		return problem_set;
	}
}
