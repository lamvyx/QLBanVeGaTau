package dao;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

final class DaoUtils {
	private DaoUtils() {
	}

	static LocalDate localDate(Date date) {
		return date == null ? null : date.toLocalDate();
	}

	static LocalDateTime localDateTime(Date date, Time time) {
		if (date == null && time == null) {
			return null;
		}
		LocalDate ngay = date == null ? LocalDate.now() : date.toLocalDate();
		LocalTime gio = time == null ? LocalTime.MIDNIGHT : time.toLocalTime();
		return LocalDateTime.of(ngay, gio);
	}

	static Date sqlDate(LocalDate date) {
		return date == null ? null : Date.valueOf(date);
	}

	static Time sqlTime(LocalDateTime value) {
		return value == null ? null : Time.valueOf(value.toLocalTime());
	}

	static java.sql.Timestamp sqlTimestamp(LocalDateTime value) {
		return value == null ? null : java.sql.Timestamp.valueOf(value);
	}
}