package time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeCheck {

	public static void main(String[] args) {

		System.out.println("Parametru  | Data valida | Inainte/Dupa | Diferenta");

		for (String element : args) {
			System.out.println(generareString(element));
		}

	}

	public static String generareString(String arg) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date now = new Date();

		StringBuffer s = new StringBuffer("");
		s.append(arg);

		try {

			Date date1 = simpleDateFormat.parse(arg);

			long difference = date1.getTime() - now.getTime();
			long diffdays = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
			s.append(" | Da          | ");
			if (diffdays == 0) {

				s.append("Egal         | ").append(diffdays);

			} else {
				s.append(diffdays > 0 ? "Dupa         | " + diffdays
						: "Inainte      | " + (-diffdays));

			}

		} catch (ParseException e) {
			s.append("        | Nu          |               ");
		}

		return s.toString();
	}

}
