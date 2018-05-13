package payroll

import payroll.models.Interval
import java.time.LocalDate

fun isInInterval(date: LocalDate, interval: Interval): Boolean {
    return date >= interval.first && date <= interval.second
}