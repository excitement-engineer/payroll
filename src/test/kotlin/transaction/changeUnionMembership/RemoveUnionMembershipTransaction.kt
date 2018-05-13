package transaction.changeUnionMembership

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.changeEmployee.changeUnionMembership.PutEmployeeInUnionTransaction
import payroll.transaction.changeEmployee.changeUnionMembership.RemoveUnionMembershipTransaction
import kotlin.test.assertEquals

class RemoveUnionMembershipTransactionTest {
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
                unionMembership = UnionMembership(memberId = "1", dues = 10.0)
        )

        database.addEmployee(employee)

        val transaction = RemoveUnionMembershipTransaction(
                empId = "1",
                db = database
        )

        transaction.execute()

        assertEquals(
                expected = null,
                actual = employee.unionMembership
        )

        assertEquals(
                expected = null,
                actual = database.getUnionMember("1")
        )
    }

}