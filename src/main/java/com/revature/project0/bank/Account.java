package com.revature.project0.bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Account {
	private int accountId;
	private float balance;
	private String accountType;
	protected Customer c;

	Account() {
	}
	Account(int accid)
	{
		accountId=accid;
	}
	Account(Customer c1) {
		c = c1;
	}

	public Account(int accountId,String accType,float balance) {
		super();
		this.accountId = accountId;
		this.balance = balance;
		this.accountType = accType;
	}
	
	public String accType(int uid) {
		String type = "";
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query = "select accounttype from account a inner join customer c on a.userid=c.?";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, uid);
			// System.out.println(c.getUserId());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				// System.out.println("Account Type:"+rs.getString(1));
				type = rs.getString(1);

			}
			stmt.close();

			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			// System.out.println("Some error occured.Please try again or later");
		}
		return type;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Customer getC() {
		return c;
	}

	public void setC(Customer c) {
		this.c = c;
	}

	public abstract void createAccount(Customer c);

	public abstract float withdraw(int a);

	public abstract float deposit(int a);

	public abstract float transfer(int sentid, int receiveid, float amount);

	public float getAccBalance(int accNo) {
		float balance = 0;
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query = "select balance from account where accountno=?";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, accNo);
			// System.out.println(c.getUserId());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				// System.out.println("Account Type:"+rs.getString(1));
				balance = rs.getFloat(1);

			}
			stmt.close();

			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			// System.out.println("Some error occured.Please try again or later");
		}
		return balance;
	}
	public String checkAccount(int aid) {
		String type = "";
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query = "select accountno,accounttype from account where accountno=?";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, aid);
			// System.out.println(c.getUserId());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				// System.out.println("Account Type:"+rs.getString(1));
				type = rs.getString(2);

			}
			stmt.close();

			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			// System.out.println("Some error occured.Please try again or later");
		}
		return type;
	}

	public void AccountDetailsdisplay(int accNo) {
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query = "select * from account where accountno=?";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, accNo);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println("Acount details:");
				System.out.println("Account No:" + rs.getInt(1));
				System.out.println("Balance:" + rs.getFloat(2));
				System.out.println("Account Type:" + rs.getString(3));
				System.out.println("User ID:" + rs.getInt(4));
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
