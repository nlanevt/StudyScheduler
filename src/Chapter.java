import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class Chapter {
	private String chapter_number;
	private LocalDate date_first_studied;
	private boolean isActive;
	
	public Chapter(String chapter_number, LocalDate date_first_studied, boolean isActive) {
		this.chapter_number = chapter_number;
		this.date_first_studied = date_first_studied;
		this.isActive = isActive;
	}
	
	public void setActiveStatus(boolean active_status) {
		this.isActive = active_status;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public String getChapterNumber() {
		return chapter_number;
	}
	
	public boolean IsUnstudied() {
		return date_first_studied == null;
	}
	
	public void SetAsStudied() {
		date_first_studied = Utils.GetMostRecentSunday();
	}
	
	public boolean isDue() {
		long diff = date_first_studied == null ? 0 : ChronoUnit.WEEKS.between((Temporal) date_first_studied, Utils.GetMostRecentSunday());
		return diff > 1 && Utils.IsPerfectSquare(diff);
	}
	
	public LocalDate getDateFirstStudied() {
		return date_first_studied;
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
	/*private long getInterval() {
		long weeks_difference = date_first_studied == null ? 0 : ChronoUnit.WEEKS.between((Temporal) date_first_studied, LocalDate.now());
		
		//return (2*times_studied) + 1;
		return 0;
	}*/
	
}
