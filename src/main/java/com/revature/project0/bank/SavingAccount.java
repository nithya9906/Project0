package com.revature.project0.bank;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class SavingAccount extends Account {
	
	private static final Logger LOGGER = LogManager.getLogger(SavingAccount.class);
	public SavingAccount() {
		super();
	}

	public SavingAccount(Customer c) {
		super(c);
	}

	public void createAccount(Customer c) {
		Scanner in = new Scanner(System.in);
		int accNo=0;
		setAccountType("Saving");
		float amount;
		do {
			System.out.print("Enter your initial amount to invest(Minimum should be Rs.1000):");
			amount = in.nextFloat();
		} while (amount < 1000);
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query = "update account set balance=?, accounttype=? where userid=?";
			stmt = con.prepareStatement(query);
			stmt.setFloat(1, amount);
			stmt.setString(2, "Saving");
			stmt.setInt(3, c.getUserId());
			System.out.println(c.getUserId());
			int rs = stmt.executeUpdate();
			if (rs > 0) {
				System.out.println("Account Created Successfully");
				String query2 = "select accountno from account where userid=?";
				stmt = con.prepareStatement(query2);
				stmt.setInt(1, c.getUserId());
				ResultSet result = stmt.executeQuery();
				while (result.next()) {
					accNo = result.getInt(1);
				}
				AccountDetailsdisplay(accNo);
				c.display();
			} else {
				System.out.println("Account is not created.Try again");
			}
			stmt.close();
			con.close();
		
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Some error occured.Please try again or later");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public float withdraw(int accNo) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now(); 
		System.out.println("Enter the amount to withdraw:");
		Scanner in = new Scanner(System.in);
		float amount = in.nextFloat();
		float currentBalance = getAccBalance(accNo);
		if (currentBalance > amount) {
			currentBalance = currentBalance - amount;
			Connection con = ConnectionUtils.getConnection();
			PreparedStatement stmt = null;
			LOGGER.entry();
			try {
				String query = "update account set balance=? where accountno=?";
				stmt = con.prepareStatement(query);
				stmt.setFloat(1, currentBalance);
				stmt.setInt(2, accNo);
				int rs = stmt.executeUpdate();
				if (rs > 0) {
					System.out.println("Withdrawed Successfully");
					this.setBalance(currentBalance);
					
					LOGGER.info(c.getUserId()+"    "+accNo+"   "+"Withdrawed  "+"Saving  "+dtf.format(now)+" "+amount+" "+currentBalance);
				}
				else {
					LOGGER.error(accNo+"   "+"WithdrawingMoney error "+dtf.format(now)+" "+amount+" "+currentBalance);
					System.out.println("Some error acur.Try again later");
				}
				stmt.close();
				con.close();
				LOGGER.exit();
			} 
			catch (SQLException e) {
				System.out.println("Some error occured.Please try again or later");
			}
		} 
		else {
			System.out.println("Less balance");
		}
		return this.getBalance();
	}

	public float getAccBalance(int accNo) {
		float balance = 0;
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query = "select balance from account where accountno=?";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, accNo);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				balance = rs.getFloat(1);
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Some error occured.Please try again or later");
		}
		return balance;
	}

	

	@SuppressWarnings("deprecation")
	@Override
	public float deposit(int accNo) {
		Scanner in=new Scanner(System.in);
		System.out.println("Enter the amount to deposit:");
		float amount = in.nextFloat();
		if(amount>0)
		{
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now(); 
			float currentBalance = getAccBalance(accNo);
			currentBalance = currentBalance + amount;
			Connection con = ConnectionUtils.getConnection();
			PreparedStatement stmt = null;
			LOGGER.entry();
			try {
				String query = "update account set balance=? where accountno=?";
				stmt = con.prepareStatement(query);
				stmt.setFloat(1, currentBalance);
				stmt.setInt(2, accNo);
				int rs = stmt.executeUpdate();
				if (rs > 0) {
					System.out.println("Deposited Successfully");
					this.setBalance(currentBalance);
					LOGGER.info(c.getUserId()+"    "+accNo+"   "+"Deposited  "+"Saving  "+dtf.format(now)+" "+amount+" "+currentBalance);

				} else {
					System.out.println("Some error acur.Try again later"); 
					LOGGER.error(accNo+"   "+"Depositing error  "+now+" "+amount+" "+currentBalance);
				}
				stmt.close();
				con.close();
				LOGGER.exit();
			} catch (SQLException e) {
				//e.printStackTrace();
				System.out.println("Some error occured.Please try again or later");
			}
		}
		
		return this.getBalance();
	}

	@SuppressWarnings("deprecation")
	@Override
	public float transfer(int from, int to, float amount) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now(); 
		float currentBalance = getAccBalance(from);
		LOGGER.entry();
		if (currentBalance > amount) {
			currentBalance = currentBalance - amount;
			Connection con = ConnectionUtils.getConnection();
			PreparedStatement stmt = null;
			try {
				String query = "update account set balance=? where accountno=?";
				stmt = con.prepareStatement(query);
				stmt.setFloat(1, currentBalance);
				stmt.setInt(2, from);
				int rs = stmt.executeUpdate();
				if (rs > 0) {
					this.setBalance(currentBalance);
					float senderBalance = getAccBalance(to);
					senderBalance = senderBalance + amount;
					stmt = con.prepareStatement(query);
					stmt.setFloat(1, senderBalance);
					stmt.setInt(2, to);
					int result = stmt.executeUpdate();
					if (result > 0) {
						this.setBalance(senderBalance);
						System.out.println("Amount transferred Successfully");
						LOGGER.info(from+"    "+to+"   "+"TransactionMoney  "+dtf.format(now)+" "+amount+" "+senderBalance);
					} else {
						System.out.println("Some error occur.Some taken but not deposited");
						LOGGER.error(from+"    "+to+"   "+"TransactionMoney  error"+now+" "+amount+" "+senderBalance);
					}
				} else {
					System.out.println("Some error acur.Try again later");
				}
				stmt.close();
				con.close();
				LOGGER.exit();
			} catch (SQLException e) {
				e.printStackTrace();
				// System.out.println("Some error occured.Please try again or later");
			}
		} else {
			System.out.println("Less balance");
		}
		return currentBalance;
	}

	public void display() {
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query = "select * from account where accountId=?";
			stmt = con.prepareStatement(query);
			stmt.setInt(3, this.getAccountId());
			// System.out.println(c.getUserId());
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
			//e.printStackTrace();
			System.out.println("Some error occured.Please try again or later");
		}
	}

}
