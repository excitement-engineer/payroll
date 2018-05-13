package transaction.changeEmployee.changeClassification

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.changeEmployee.changeClassification.ChangeEmployeeToCommissionedTransaction
import kotlin.test.assertEquals

class ChangeEmployeeToCommissionedTransactionTransactionTest {

    @Test
    fun testTransaction() {
        val database = InMemoryDatabase()

        val employee = Employee(
                id = "1",
                name = "Bob",
                address = "New York",
                paymentMethod = HoldPaycheck,
                paymentClassification = SalariedClassification(20000.0),
                paymentSchedule = MonthlySchedule,
                unionMembership = null
        )

        database.addEmployee(employee)

        val transaction = ChangeEmployeeToCommissionedTransaction(
                empId = "1",
                salary = 10000.0,
                rate = 10.0,
                db = database
        )

        transaction.execute()

        assertEquals(
                expected = CommissionedClassification(commissionRate = 10.0, salary = 10000.0),
                actual = employee.paymentClassification
        )

        assertEquals(
                expected = BiWeeklySchedule,
                actual = employee.paymentSchedule
        )
    }
}