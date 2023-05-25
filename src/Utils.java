import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Utils {

	public static boolean IsPerfectSquare(long x)
	{
		if (x >= 0) {
		
			long sr = (long)Math.sqrt(x);
		
			return ((sr * sr) == x);
		}
		return false;
	}
	
	public static LocalDate GetMostRecentSunday() {
		return LocalDate.now().with( TemporalAdjusters.previousOrSame( DayOfWeek.SUNDAY )) ;
	}
}
