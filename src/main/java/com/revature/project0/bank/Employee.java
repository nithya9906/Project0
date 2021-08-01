package com.revature.project0.bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
	private int empId;
	private String firstName;
	private String lastName;
	private String designation;
	private float salary;
	private long mobilePhone;
	private String password;
	protected Account a;
	public void deleteAccount(int accNo)
	{
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query2 = "delete from account where balance=? and  accountno=?";
			stmt = con.prepareStatement(query2);
			stmt.setFloat(1,0);
			stmt.setInt(2, accNo);
			int rs = stmt.executeUpdate();
			if (rs > 0) {
				System.out.println("Account deleted successfully");
			} else {
				System.out.println("Not deleted.It is valid account having balance");
			}
		}
		catch(SQLException e)
		{
			//e.printStackTrace();
			System.out.println("Some error occur....");
		}
	}
	public void deleteCustomer(int uid)
	{
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query2 = "delete from account where userid=?";
			stmt = con.prepareStatement(query2);
			stmt.setInt(1, uid);
			int rs = stmt.executeUpdate();
			if (rs > 0) {
				System.out.println("Customer Account closed successfully");
			} 
			String query3 = "delete from customer where userid=?";
			stmt = con.prepareStatement(query3);
			stmt.setInt(1, uid);
			int r = stmt.executeUpdate();
			if (r > 0) {
				System.out.println("Customer details deleted successfully");
			} else {
				System.out.println("Not deleted");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			//System.out.println("Some error occur....");
		}
		
	}
	public Employee(int eid)
	{
		this.empId=eid;
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public float getSalary() {
		return salary;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
	public long getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(long mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Account getA() {
		return a;
	}
	public void setA(Account a) {
		this.a = a;
	}
	public Employee(int empId, String firstName, String lastName, String designation, float salary, long mobilePhone,
			String password, Account a) {
		super();
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.designation = designation;
		this.salary = salary;
		this.mobilePhone = mobilePhone;
		this.password = password;
		this.a = a;
	}
}
	