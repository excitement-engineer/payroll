package transaction.changePaymentMethod

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.changeEmployee.changePaymentMethod.ChangeToMailPaycheckTransaction
import kotlin.test.assertEquals

class ChangeToMailPaycheckTransactionTest {

    @Test
    fun testTransaction() {
        val database = InMemoryDatabase()

        val employee = Employee(
                id = "1",
                name = "Bob",
                address = "New York",
                paymentMethod = HoldPaycheck,
                paymentClassification = HourlyClassification(20.0),
                paymentSchedule = MonthlySchedule,
                unionMembership = null
        )

        database.addEmployee(employee)

        val transaction = ChangeToMailPaycheckTransaction(
                empId = "1",
                address = "Boston",
                db = database
        )

        transaction.execute()

        assertEquals(
                expected = MailPaycheck("Boston"),
                actual = employee.paymentMethod
        )
    }
}