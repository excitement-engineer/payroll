package payroll.transaction.changeEmployee.changeUnionMembership

import payroll.Database
import payroll.ID
import payroll.models.Employee
import payroll.transaction.changeEmployee.ChangeEmployeeTransaction

class RemoveUnionMembershipTransaction(
        empId: ID,
        private val db: Database
): ChangeEmployeeTransaction(empId, db) {
    override fun change(employee: Employee) {
        val membership = employee.unionMembership

        if (membership != null) {
            db.removeUnionMember(membership.memberId)
            employee.unionMembership = null
        }
    }
}