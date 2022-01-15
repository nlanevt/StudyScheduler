
public class Problem {
	private int times_completed;
	private String problem_desc;
	private Chapter chapter;
	private int problem_number;
	private boolean isActive;
	
	public Problem(Chapter chapter, int problem_number, String problem_desc) {
		this.chapter = chapter;
		this.times_completed = 0;
		this.problem_desc = problem_desc;
		this.isActive = true;
		this.problem_number = problem_number;
	}
	
	public Problem(Chapter chapter, int problem_number, int times_completed, boolean isActive) {
		this.chapter = chapter;
		this.times_completed = times_completed;
		this.isActive = true;
		this.problem_number = problem_number;
	}
	
	public void wasCompleted() {
		times_completed++;
	}
	
	public int getTimesCompleted() {
		return times_completed;
	}
	
	public void setActiveStatus(boolean active_status) {
		this.isActive = active_status;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public Chapter getChapter() {
		return chapter;
	}
	
	public String getProblemDescription() {
		return problem_desc;
	}
	
	public int getProblemNumber() {
		return problem_number;
	}
}
