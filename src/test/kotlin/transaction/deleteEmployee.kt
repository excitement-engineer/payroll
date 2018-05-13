package transaction

import org.junit.Before
import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.Employee
import payroll.models.HoldPaycheck
import payroll.models.MonthlySchedule
import payroll.models.SalariedClassification
import payroll.transaction.DeleteEmployeeTransaction
import kotlin.test.assertEquals
import kotlin.test.assertFails

class DeleteEmployeeTransactionTest {

    val database = InMemoryDatabase()

    @Before
    fun setup() {
        val employee = Employee(
                id = "1",
                name = "Bob",
                address = "New York",
                paymentMethod = HoldPaycheck,
                paymentClassification = SalariedClassification(80_000.0),
                paymentSchedule =MonthlySchedule,
                unionMembership = null
        )

        database.addEmployee(employee)
    }

    @Test
    fun testTransaction() {
        val transaction = DeleteEmployeeTransaction(
                empId = "1",
                db = database
        )

        transaction.execute()

        assertEquals(null, database.getEmployee("1"))
    }


    @Test
    fun testErrorUnknownEmpID() {

        val transaction = DeleteEmployeeTransaction(
                empId = "2",
                db = database
        )

        val error = assertFails {
            transaction.execute()
        }

        assertEquals(
                "No employee for id 2",
                error.message
        )

    }
}