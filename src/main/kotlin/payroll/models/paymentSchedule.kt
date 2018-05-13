package payroll.models

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

interface PaymentSchedule {
    fun isPayDay(date: LocalDate): Boolean
    fun getPayPeriodStartDate(payDate: LocalDate): LocalDate
}

object WeeklySchedule: PaymentSchedule {
    override fun getPayPeriodStartDate(payDate: LocalDate): LocalDate {
        return payDate.minusDays(5)
    }

    override fun isPayDay(date: LocalDate): Boolean {
        return date.dayOfWeek === DayOfWeek.FRIDAY
    }
}

object MonthlySchedule: PaymentSchedule {
    override fun getPayPeriodStartDate(payDate: LocalDate): LocalDate {
        return LocalDate.of(payDate.year, payDate.month, 1)
    }

    override fun isPayDay(date: LocalDate): Boolean {
        return isLastDayOfMonth(date)
    }

    private fun isLastDayOfMonth(date: LocalDate): Boolean {
        val endDateOfMonth = date.withDayOfMonth(date.lengthOfMonth())
        return date == endDateOfMonth
    }
}

object BiWeeklySchedule: PaymentSchedule {

    override fun getPayPeriodStartDate(payDate: LocalDate): LocalDate {
        return payDate.minusDays(13)
    }

    override fun isPayDay(date: LocalDate): Boolean {
        return isEvenWeek(date) && date.dayOfWeek === DayOfWeek.FRIDAY
    }

    private fun isEvenWeek(date: LocalDate): Boolean {
        val weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()
        val weekNumber = date.get(weekOfYear)

        return weekNumber % 2 == 0
    }
}