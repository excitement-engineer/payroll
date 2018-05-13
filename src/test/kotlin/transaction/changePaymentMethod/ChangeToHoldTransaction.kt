package transaction.changePaymentMethod

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.changeEmployee.changePaymentMethod.ChangeToHoldTransaction
import kotlin.test.assertEquals

class ChangeToHoldTransactionTest {

    @Test
    fun testTransaction() {
        val database = InMemoryDatabase()

        val employee = Employee(
                id = "1",
                name = "Bob",
                address = "New York",
                paymentMethod = MailPaycheck("Boston"),
                paymentClassification = HourlyClassification(20.0),
                paymentSchedule = MonthlySchedule,
                unionMembership = null
        )

        database.addEmployee(employee)

        val transaction = ChangeToHoldTransaction(
                empId = "1",
                db = database
        )

        transaction.execute()

        assertEquals(
                expected = HoldPaycheck,
                actual = employee.paymentMethod
        )

    }
}