package payroll

import payroll.models.Employee

typealias ID = String

interface Database {
    fun addEmployee(employee: Employee)
    fun addUnionMember(memberId: ID, member: Employee)
    fun getEmployee(id: ID): Employee?
    fun getUnionMember(memberId: ID): Employee?
    fun removeEmployee(employee: Employee)
    fun removeUnionMember(memberId: ID)
    fun getEmployees(): Collection<Employee>
}

class InMemoryDatabase: Database {

    private val employees = mutableMapOf<ID, Employee>()
    private val unionMembers = mutableMapOf<ID, Employee>()

    override fun addUnionMember(memberId: ID, member: Employee) {
        unionMembers[memberId] = member
    }

    override fun addEmployee(employee: Employee){
        employees[employee.id] = employee
    }

    override fun getEmployee(id: ID): Employee? {
        return employees[id]
    }

    override fun getUnionMember(memberId: ID): Employee? {
        return unionMembers[memberId]
    }

    override fun removeEmployee(employee: Employee) {
        employees.remove(employee.id)
    }

    override fun removeUnionMember(memberId: ID) {
        unionMembers.remove(memberId)
    }

    override fun getEmployees(): Collection<Employee> {
        return employees.values
    }
}
