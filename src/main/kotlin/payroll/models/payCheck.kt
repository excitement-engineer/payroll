package payroll.models

import java.time.LocalDate

typealias Interval = Pair<LocalDate, LocalDate>

data class PayCheck(
    val payInterval: Interval,
    // Gross pay minus deductions
    val netPay: Double,

    val deductions: Double,
    val grossPay: Double,

    val paymentMethod: PaymentMethod
)