package transaction.addNewEmployee

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.BiWeeklySchedule
import payroll.models.CommissionedClassification
import payroll.models.Employee
import payroll.models.HoldPaycheck
import payroll.transaction.addNewEmployee.AddCommissionedEmployeeTransaction
import kotlin.test.assertEquals

class AddCommissionedEmployeeTransactionTest {


    @Test
    fun addEmployee() {
        val database = InMemoryDatabase()


        val transaction = AddCommissionedEmployeeTransaction(
                empId = "1",
                name = "Bob",
                address = "New York",
                salary = 80_000.0,
                commissionRate = 10.0,
                database = database
        )

        transaction.execute()

        assertEquals(
                expected = Employee(
                        id = "1",
                        name = "Bob",
                        address = "New York",
                        paymentClassification = CommissionedClassification(10.0, 80_000.0),
                        paymentMethod = HoldPaycheck,
                        unionMembership = null,
                        paymentSchedule = BiWeeklySchedule
                ),
                actual = database.getEmployee("1")
        )
    }
}