package transaction.addNewEmployee

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.Employee
import payroll.models.HoldPaycheck
import payroll.models.HourlyClassification
import payroll.models.WeeklySchedule
import payroll.transaction.addNewEmployee.AddHourlyEmployeeTransaction
import kotlin.test.assertEquals

class AddHourlyEmployeeTest{

    @Test
    fun addHourlyEmployeeTest() {
        val database = InMemoryDatabase()


        val transaction = AddHourlyEmployeeTransaction(
                empId = "1",
                name = "Bob",
                address = "New York",
                hourlyRate = 10.0,
                database = database
        )

        transaction.execute()

        assertEquals(
                expected = Employee(
                        id = "1",
                        name = "Bob",
                        address = "New York",
                        paymentClassification = HourlyClassification(hourlyRate = 10.0),
                        paymentMethod = HoldPaycheck,
                        unionMembership = null,
                        paymentSchedule = WeeklySchedule
                ),
                actual = database.getEmployee("1")
        )
    }
}