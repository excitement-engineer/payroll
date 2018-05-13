package transaction.changeUnionMembership

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.changeEmployee.changeUnionMembership.PutEmployeeInUnionTransaction
import kotlin.test.assertEquals
import kotlin.test.assertFails

class PutEmployeeInUnionTransactionTest {
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

        val transaction = PutEmployeeInUnionTransaction(
                empId = "1",
                db = database,
                memberId = "1",
                dues = 10.0
        )

        transaction.execute()

        assertEquals(
                expected = UnionMembership(memberId = "1", dues = 10.0),
                actual = employee.unionMembership
        )

        assertEquals(
                expected = employee,
                actual = database.getUnionMember("1")
        )
    }

    @Test
    fun testEmployeeAlreadyInUnion(){
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

        database.addUnionMember("1", employee)

        val transaction = PutEmployeeInUnionTransaction(
                empId = "1",
                db = database,
                memberId = "1",
                dues = 10.0
        )

        val error = assertFails {
            transaction.execute()
        }

        assertEquals(
                expected = "Member is already in the union",
                actual = error.message
        )
    }
}