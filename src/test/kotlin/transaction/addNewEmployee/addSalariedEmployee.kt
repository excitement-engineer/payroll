package transaction.addNewEmployee

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.addNewEmployee.AddSalariedEmployeeTransaction
import kotlin.test.assertEquals

class AddSalariedEmployeeTest{

    @Test
    fun addHourlyEmployeeTest() {
        val database = InMemoryDatabase()


        val transaction = AddSalariedEmployeeTransaction(
                empId = "1",
                name = "Bob",
                address = "New York",
                salary = 80_000.0,
                database = database
        )

        transaction.execute()

        assertEquals(
                expected = Employee(
                        id = "1",
                        name = "Bob",
                        address = "New York",
                        paymentClassification = SalariedClassification(salary = 80_000.0),
                        paymentMethod = HoldPaycheck,
                        unionMembership = null,
                        paymentSchedule = MonthlySchedule
                ),
                actual = database.getEmployee("1")
        )
    }
}