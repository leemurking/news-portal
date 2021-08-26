package dao;

import models.Department;
import models.Employee;
import models.News;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oDepartmentDao implements DepartmentDao {
    private final Sql2o sql2o;

    public Sql2oDepartmentDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Department department) {
        String sql = "INSERT INTO departments(departmentName, description, numberOfEmployees) VALUES (:departmentName, :description, :numberOfEmployees)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(department)
                    .executeUpdate()
                    .getKey();
            department.setId(id);

        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }



    @Override
    public List<Department> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM departments")
                    .executeAndFetch(Department.class);
        }
    }


    @Override
    public void addDepartmentToUser(Department department, Employee employee) {
        String sql = "INSERT INTO departments_employees (departmentId, employeeId) VALUES (:departmentId, :employeeId)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("departmentId", department.getId())
                    .addParameter("employeeId", employee.getId())
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }


    }



    @Override
    public Department findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM departments WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Department.class);
        }
    }


    @Override
    public List<Employee> getAllUsersByDepartment(int departmentId) {
        List<Employee> users = new ArrayList(); //empty list
        String joinQuery = "SELECT userId FROM departments_employees WHERE departmentId = :departmentId";

        try (Connection con = sql2o.open()) {
            List<Integer> allEmployeesIds = con.createQuery(joinQuery)
                    .addParameter("departmentId", departmentId)
                    .executeAndFetch(Integer.class);
            for (Integer userId : allEmployeesIds){
                String usersQuery = "SELECT * FROM employees WHERE id = :userId";
                users.add(
                        con.createQuery(usersQuery)
                                .addParameter("employeeId", userId)
                                .executeAndFetchFirst(Employee.class));
            }
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
        return users;
    }

    @Override
    public void update(int id, String newDepartmentName, String newDescription, String newNumberOfEmployees) {
        String sql = "UPDATE departments SET (departmentName, description, numberOfEmployees) = (:departmentName, :description, :numberOfEmployees) WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("departmentName", newDepartmentName)
                    .addParameter("description", newDescription)
                    .addParameter("numberOfEmployees", newNumberOfEmployees)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from departments WHERE id = :id"; //raw sql
        String deleteJoin = "DELETE from departments_employees WHERE departmentId = :departmentId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            con.createQuery(deleteJoin)
                    .addParameter("departmentId", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }

    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM departments";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }
}
