package payroll.transaction.changeEmployee.changeUnionMembership

import payroll.Database
import payroll.ID
import payroll.models.Employee
import payroll.models.UnionMembership
import payroll.transaction.changeEmployee.ChangeEmployeeTransaction

class PutEmployeeInUnionTransaction(
        empId: ID,
        private val memberId: ID,
        private val dues: Double,
        private val db: Database
): ChangeEmployeeTransaction(empId, db) {
    override fun change(employee: Employee) {
        val member = db.getUnionMember(memberId)

        if (member != null) {
            throw Exception("Member is already in the union");
        } else {
            val membership = UnionMembership(memberId, dues)
            employee.unionMembership = membership

            db.addUnionMember(memberId, employee)
        }
    }
}