package  com.amolina.weather.clima.utils

import android.content.Context
import com.amolina.weather.clima.R
import com.amolina.weather.clima.ui.base.tryOrDefault
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit
import android.text.format.DateFormat


const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val DATE_FORMAT_1_TO_24_HOURS = "yyyy-MM-dd kk:mm:ss"
const val DATE_FORMAT_WITH_OR_SEPARATOR = "yyyy-MM-dd | HH:mm"
const val DATE_FORMAT_SEPARATOR_SLASH = "yyyy/MM/dd"
const val DATE_FORMAT_SEPARATOR_MINUS = "yyyy-MM-dd"
const val DATE_FORMAT_FACEBOOK = "dd/MM/yyyy"
const val DATE_FORMAT_MONTH_YEAR = "MM/yyyy"
const val DATE_FORMAT_DAY_MONTH = "dd MMMM"
const val DATE_FORMAT_DAY_MONTH_YEAR = "d MMMM, yyyy"
const val DATE_FORMAT_PAY = "dd/MM/yyyy \' · \' hh:mma"
const val HOUR_FORMAT = "HH:mm:ss"
const val HOUR_FORMAT_WITHOUT_SECONDS = "HH:mm"
const val HOUR_FORMAT_SCHEDULE = "hh:mm a"
const val HOUR_FORMAT_MINUTES_SECONDS = "mm:ss"
const val READABLE_FORMAT_DATE_WITHOUT_TIME = "EEEE d MMMM"
const val READABLE_FORMAT_DATE_WITHOUT_TIME_COMMA = "EEEE, d MMM"
const val READABLE_FORMAT_MONTH_DAY = "MMMM d"
const val READABLE_FORMAT_DATE = "EEEE d MMMM, hh:mm a"
const val EXTENDED_FORMAT_DATE = "EEE MMM d HH:mm:ss z yyyy"
const val SERVER_FORMAT_DATE = "EEE, dd MMM yyyy HH:mm:ss zzz"
const val DATE_FORMAT_GMT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val ADULT_MIN_AGE = 18
const val FORMAT_EEEE = "EEEE"
const val FORMAT_SCHEDULE_DATE = "EEE dd, hh:mm a"
const val FORMAT_SCHEDULE_DATE_DAY = "EEEE dd"
const val FORMAT_SUPPORT = "dd MMM, hh:mm a"
const val DEBT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
const val DEBT_PRETY_DATE_FORMAT = "EEEE d, MMMM yyyy - HH:mm:ss a"
const val FORMAT_NO_SPACE = "yyyyMMdd"
const val ONLY_MONTH_NAME = "MMMM"


private const val UPDATE_INTERVAL: Int = 60

fun currentDateString(): String = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Date())

fun getDateAsString(calendar: Calendar, format: String, locale: Locale = Locale.US): String {
    return SimpleDateFormat(format, locale).format(calendar.time)
}

fun dateStringWithoutDayName(context: Context, date: String): String {
    val dateFormat = context.resources.getString(R.string.date_format_month_text)
    val dateToFormat = SimpleDateFormat(DATE_FORMAT, Locale.US).parse(date)
    return SimpleDateFormat(dateFormat, Locale.getDefault()).format(dateToFormat)
}

fun dateStringWithoutDayName(dateFormat: String, date: String): String {
    val dateToFormat = SimpleDateFormat(DATE_FORMAT, Locale.US).parse(date)
    return SimpleDateFormat(dateFormat, Locale.getDefault()).format(dateToFormat)
}

fun getDateFromFormatToFormat(date: String, initialFormat: String, finalFormat: String): String {
    return tryOrDefault({
        val dateToFormat = SimpleDateFormat(initialFormat, Locale.US).parse(date)
        SimpleDateFormat(finalFormat, Locale.getDefault()).format(dateToFormat)
    }, "")
}

fun getDateWithFormat(date: String, format: String): String =
    getDateFromFormatToFormat(date, DATE_FORMAT, format)

fun dateStringWithMonthYear(context: Context, date: String): String {
    return tryOrDefault({
        val dateFormat = context.resources.getString(R.string.date_format_month_year_text)
        val locale = Locale(Locale.getDefault().language)
        val dateToFormat = SimpleDateFormat(DATE_FORMAT, locale).parse(date)
        SimpleDateFormat(dateFormat, locale).format(dateToFormat)
    }, "")
}

fun getDayOfTheMonth(date: String): String {
    return tryOrDefault({
        val locale = Locale(Locale.getDefault().language)
        val dateToFormat = SimpleDateFormat(DATE_FORMAT_SEPARATOR_MINUS, locale).parse(date)
        SimpleDateFormat("dd", locale).format(dateToFormat)
    }, "")
}

fun getDateByFormat(date: String, format: String): String {
    return tryOrDefault({
        val locale = Locale(Locale.getDefault().language)
        val dateToFormat = SimpleDateFormat(DATE_FORMAT_SEPARATOR_MINUS, locale).parse(date)
        SimpleDateFormat(format, locale).format(dateToFormat)
    }, "")
}

fun getReadableFormatFromDate(date: Date): String {
    val dateFormat = HOUR_FORMAT
    val locale = Locale(Locale.getDefault().language)
    val sdf = SimpleDateFormat(dateFormat, locale)
    return sdf.format(date)
}

fun getExpectedTimeOfArrivalInMinutes(date: String): Int {
    return tryOrDefault({
        val df = SimpleDateFormat(DATE_FORMAT_1_TO_24_HOURS, Locale.ENGLISH)
        val expectedArrivalDate = df.parse(date)

        var minutesRemain = TimeUnit.MILLISECONDS.toMinutes(expectedArrivalDate.time - Date().time).toInt()
        minutesRemain = Math.max(0, minutesRemain)
        minutesRemain
    }, 0)
}

@JvmOverloads
fun stringToCalendar(time: String?, dateFormat: String = HOUR_FORMAT): Calendar {
    return tryOrDefault({
        val format = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        val date = format.parse(time)
        val calendar = GregorianCalendar.getInstance()
        calendar.timeInMillis = date.time
        calendar
    }, Calendar.getInstance())
}

fun isValidDate(text: String): Boolean {
    val dateFormat = if (text.contains("/")) {
        DATE_FORMAT_SEPARATOR_SLASH
    } else {
        DATE_FORMAT_SEPARATOR_MINUS
    }
    val date = stringToCalendar(text, dateFormat)
    return date != null
}

fun convertSecondsToMmSs(seconds: Long): String {
    val s = seconds % 60
    val m = seconds / 60 % 60
    return String.format(Locale.ENGLISH, "%02d:%02d", m, s)
}

@JvmOverloads
fun getDateFromTimeStamp(time: Long, format: String = DATE_FORMAT): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = time
    return getDateAsString(calendar, format)
}

private fun getAgeCalendar(birthday: String): Calendar {
    val dateFormat = if (birthday.contains("/")) {
        DATE_FORMAT_SEPARATOR_SLASH
    } else {
        DATE_FORMAT_SEPARATOR_MINUS
    }
    return stringToCalendar(birthday, dateFormat)
}

private fun getAge(birthday: String): Int {
    val date = getAgeCalendar(birthday)
    val currentDate = Calendar.getInstance()
    var age = currentDate.get(Calendar.YEAR) - date.get(Calendar.YEAR)

    val currentMonth = currentDate.get(Calendar.MONTH)
    val monthBirth = date.get(Calendar.MONTH)
    if (monthBirth > currentMonth) {
        age--
    } else if (currentMonth == monthBirth) {
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
        val dayBirth = date.get(Calendar.DAY_OF_MONTH)
        if (dayBirth > currentDay) {
            age--
        }
    }
    return age
}

fun isAdult(birthday: String, adultMinAge: Int = ADULT_MIN_AGE): Boolean {
    val isSuccess = isValidDate(birthday) && birthday.isNotBlank()

    return if (isSuccess) {
        val currentDate = Calendar.getInstance()
        val date = getAgeCalendar(birthday)
        val yearsDiff = getAge(birthday)

        val condition = yearsDiff >= adultMinAge
        condition && currentDate.timeInMillis >= date.timeInMillis
    } else false
}

fun daysBetweenTwoDates(dateString: String, dateStringTwo: String): Long =
    getDifferenceTimeUnitBetweenDates(dateString, dateStringTwo, DATE_FORMAT_SEPARATOR_MINUS, TimeUnit.DAYS)

fun minutesBetweenTwoTimestamps(lowerTimestamp: Long, higherTimestamp: Long): Int {
    return tryOrDefault({
        val diff = higherTimestamp - lowerTimestamp
        TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS).toInt()
    }, Int.MAX_VALUE)
}

fun timestampFromXHoursAgo(hours: Int = 1): Long =
    Date().time - TimeUnit.MILLISECONDS.convert(hours.toLong(), TimeUnit.HOURS)

fun timestampWithGMT(): String {
    val timestamp: String
    val cal = Calendar.getInstance()
    val dfm = SimpleDateFormat(DATE_FORMAT_GMT, Locale.getDefault())
    dfm.timeZone = TimeZone.getTimeZone("GMT")
    timestamp = dfm.format(cal.time)
    return timestamp
}

fun getNotificationDisplay(context: Context, date: String): String? {
    val dateFormat = context.resources.getString(R.string.date_format_day_hour_text)
    val dateToFormat = SimpleDateFormat(DATE_FORMAT, Locale.US).parse(date.replace("T", " "))
    return SimpleDateFormat(dateFormat, Locale.getDefault()).format(dateToFormat)
}

fun getTimeRequestDisplay(date: String): String? {
    val dateToFormat = SimpleDateFormat(DATE_FORMAT, Locale.US).parse(date.replace("T", " "))
    return SimpleDateFormat(DATE_FORMAT_PAY, Locale.getDefault()).format(dateToFormat)
}

fun minutesBetweenTwoDates(dateString: String, dateStringTwo: String): Long =
    getDifferenceTimeUnitBetweenDates(dateString, dateStringTwo, DATE_FORMAT, TimeUnit.MINUTES)

private fun getDifferenceTimeUnitBetweenDates(
    initialDate: String,
    endDate: String,
    dateFormat: String,
    timeUnit: TimeUnit
): Long {
    val format = SimpleDateFormat(dateFormat, Locale.US)
    return tryOrDefault({
        val date1 = format.parse(initialDate)
        val date2 = format.parse(endDate)
        val diff = date2.time - date1.time
        timeUnit.convert(diff, TimeUnit.MILLISECONDS)
    }, -1)
}

fun getTimestampFromDate(
    date: String,
    format: String = DATE_FORMAT_1_TO_24_HOURS,
    defaultValue: Long = 0L
): Long {
    return tryOrDefault({
        SimpleDateFormat(format, Locale.getDefault()).parse(date).time
    }, defaultValue)
}

fun Long.getDiffMillisUntilNow(): Long {
    return Date().time - this
}

fun Long.toMinutes(): Long {
    return TimeUnit.MILLISECONDS.toMinutes(this) -
            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this))
}

fun Long.toSeconds(): Long {
    return TimeUnit.MILLISECONDS.toSeconds(this) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
}

fun formatDebtDate(date: String): String {
    return getDateFromFormatToFormat(date, DEBT_DATE_FORMAT, DEBT_PRETY_DATE_FORMAT)
}

fun getTimeFromDate(date: String): String {
    val localTime = Locale.getDefault()
    val currentDate = SimpleDateFormat(DATE_FORMAT, localTime).parse(date)
    return SimpleDateFormat(HOUR_FORMAT, localTime).format(currentDate)
}

fun isOnRange(startHour: String, endHour: String): Boolean {
    return if (startHour.isNotBlank() && endHour.isNotBlank()) {
        val start = LocalTime.parse(startHour)
        val end = LocalTime.parse(endHour)
        val serverLocalTime = LocalTime.now()
        serverLocalTime.isAfter(start) && serverLocalTime.isBefore(end)
    } else false
}

fun timeStampToDate(timestamp: Long, format: String = DATE_FORMAT): String {
    val currentDate = Date(timestamp)
    val dateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.US)
    return dateFormat.format(currentDate)
}

fun getDateFromUTCTimestamp(mTimestamp: Long, mDateFormate: String): String {
    var date: String? = null
    try {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.timeInMillis = mTimestamp * 1000L
        date = DateFormat.format(mDateFormate, cal.timeInMillis).toString()

        val formatter = SimpleDateFormat(mDateFormate)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = formatter.parse(date)

        val dateFormatter = SimpleDateFormat(mDateFormate)
        dateFormatter.timeZone = TimeZone.getDefault()
        date = dateFormatter.format(value)
        return date
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return date.toString()
}