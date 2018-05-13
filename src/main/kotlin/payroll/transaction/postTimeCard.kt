package payroll.transaction

import payroll.Database
import payroll.models.HourlyClassification
import payroll.models.TimeCard
import java.time.LocalDate

class PostTimeCardTransaction(
        private val empId: String,
        private val date: LocalDate,
        private val hours: Int,
        private val db: Database
): Transaction {

    override fun execute() {
        val employee = db.getEmployee(empId)

        if (employee == null) {
            throw Exception("No employee for id $empId")
        }

        val paymentClassification = employee.paymentClassification
        if (paymentClassification is HourlyClassification) {

            val timeCard = TimeCard(date, hours)
            paymentClassification.timeCards.add(timeCard)
        }
        else {
            throw Exception("The selected employee is not hourly")
        }
    }
}