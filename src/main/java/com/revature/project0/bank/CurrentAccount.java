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

public class CurrentAccount extends Account {
	private static final Logger LOGGER = LogManager.getLogger(CurrentAccount.class.getName());
	CurrentAccount() {
		super();
	}
	public CurrentAccount(int accId,String acctype,float bal)
	{
		super(accId,acctype,bal);
	}
	public CurrentAccount(Customer c) {
		super(c);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void createAccount(Customer c) {
		Scanner in = new Scanner(System.in);
		setAccountType("Current");
		float amount;
		int accNo=0;
		System.out.print("Enter your initial amount to invest(Minimum should be Rs.1000):");
		amount = in.nextFloat();
		if (amount < 1000) {
			System.out.println("Amount is less than 1000.");
			System.out.print("Enter your initial amount to invest(Minimum should be Rs.1000):");
			amount = in.nextFloat();
		}
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		LOGGER.entry();
		try {
			String query = "update account set balance=?, accounttype=? where userid=?";
			stmt = con.prepareStatement(query);
			stmt.setFloat(1, amount);
			stmt.setString(2, "Current");
			stmt.setInt(3, c.getUserId());
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
			LOGGER.exit();
		} catch (SQLException e) {
			System.out.println("Some error has occured...");
			//e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public float withdraw(int acc) {
		System.out.println("Enter the amount to withdraw:");
		Scanner in = new Scanner(System.in);
		float amount = in.nextFloat();
		float currentBalance = getAccBalance(acc);
		LOGGER.entry();
		if (currentBalance > amount) {
			currentBalance = currentBalance - amount;
			Connection con = ConnectionUtils.getConnection();
			PreparedStatement stmt = null;
			try {
				String query = "update account set balance=? where accountno=?";
				stmt = con.prepareStatement(query);
				stmt.setFloat(1, currentBalance);
				stmt.setInt(2, acc);
				int rs = stmt.executeUpdate();
				if (rs > 0) {
					System.out.println("Withdrawed Successfully");
					this.setBalance(currentBalance);
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					LocalDateTime now = LocalDateTime.now(); 
					LOGGER.info("AccountNo:"+"    "+acc+"   "+"Withdrawed  "+"Saving  "+dtf.format(now)+" "+amount+" "+currentBalance);
					
				} else {
					System.out.println("Some error acur.Try again later");
				}
				stmt.close();
				con.close();
				LOGGER.entry();
			} catch (SQLException e) {
				//e.printStackTrace();
			     System.out.println("Some error occured.Please try again or later");
			}
		} else {
			System.out.println("Less balance");
		

		}
		return this.getBalance();
	}

	@SuppressWarnings("deprecation")
	@Override
	public float deposit(int acc) {
		Scanner in=new Scanner(System.in);
		System.out.println("Enter the amount to deposit:");
		float amount = in.nextFloat();
		if(amount>0)
		{
			float currentBalance = getAccBalance(acc);
			currentBalance = currentBalance + amount;
			Connection con = ConnectionUtils.getConnection();
			PreparedStatement stmt = null;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now(); 
			LOGGER.entry();
			try {
				String query = "update account set balance=? where accountno=?";
				stmt = con.prepareStatement(query);
				stmt.setFloat(1, currentBalance);
				stmt.setInt(2, acc);
				int rs = stmt.executeUpdate();
				if (rs > 0) {
					System.out.println("Deposited Successfully");
					this.setBalance(currentBalance);
					LOGGER.info("AccountNo:"+"    "+acc+"   "+"Deposited  "+"Saving  "+dtf.format(now)+" "+amount+" "+currentBalance);
				} else {
					System.out.println("Some error acur.Try again later");
					LOGGER.error(acc+"   "+"Depositing error  "+dtf.format(now)+" "+amount+" "+currentBalance);
				}
				stmt.close();
				con.close();
				LOGGER.exit();
			} catch (SQLException e) {
				e.printStackTrace();
				// System.out.println("Some error occured.Please try again or later");
			}
		}
		
		return this.getBalance();
	}

	@SuppressWarnings("deprecation")
	@Override
	public float transfer(int from, int to, float amount) {
		float currentBalance = getAccBalance(from);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now(); 
		if (currentBalance > amount) {
			currentBalance = currentBalance - amount;
			Connection con = ConnectionUtils.getConnection();
			PreparedStatement stmt = null;
			LOGGER.entry();
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
						LOGGER.error(from+"    "+to+"   "+"TransactionMoney  "+dtf.format(now)+" "+amount+" "+senderBalance);
					}
				} else {
					System.out.println("Some error occurs.Try again later");
				}
				stmt.close();
				con.close();
			} catch (SQLException e) {
				//e.printStackTrace();
				System.out.println("Some error occurs.Try again later");
				
			}
			LOGGER.exit();
		} else {
			System.out.println("Less balance");
		}
		return currentBalance;
	}

}
