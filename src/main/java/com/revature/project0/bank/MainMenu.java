package com.revature.project0.bank;

//import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.Date;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MainMenu {
	private static final Logger LOGGER = LogManager.getLogger(MainMenu.class);

	public static void main(String[] args) {
		PreparedStatement stmt = null;
		int choice;
		System.out.println("******************WELCOME TO NT BANK***************");
		Connection con = ConnectionUtils.getConnection();
		try {
			do {
				Scanner in = new Scanner(System.in);
				System.out.println("1.CustomerLogin\n2.EmployeeLogin\n3.NewCustomer\n4.Exit from application");
				choice = in.nextInt();
				switch (choice) {
				case 1: {
					System.out.println("Enter user id:");
					int userid = in.nextInt();
					System.out.println("Enter your password:");
					String passwrd = in.next();
					String query = "select userid,passwrd from customer where userid=? and passwrd=?";
					stmt = con.prepareStatement(query);
					stmt.setInt(1, userid);
					stmt.setString(2, passwrd);
					ResultSet rs = stmt.executeQuery();
					if (rs.next()) {
						System.out.println("Logged in Successfully");
						System.out.println("Enter your account no:");
						int acc = in.nextInt();
						String checkQuery = "select accountno from account where userid=?";
						stmt = con.prepareStatement(checkQuery);
						stmt.setInt(1, userid);
						ResultSet check = stmt.executeQuery();
						int actualNo = 0;
						while (check.next()) {
							actualNo = check.getInt(1);
						}
						if (actualNo == acc) {
							String acctype = getAccountDesc(acc);
							Customer usr = new Customer(userid);
							AccountFactory af = new AccountFactory();
							Account accounts = af.getAccountType(acctype, usr);
							int c;
							do {
								System.out.println("Enter your choice:1.Withdraw\n2.Deposit\n3.Transfer\n4.Exit");
								c = in.nextInt();
								switch (c) {
								case 1: {

									float bal = accounts.withdraw(acc);
									if(bal>0.0)
									{
										System.out.println("New Balance:" + bal);
									}
									
								}
									break;
								case 2: {
									float bal = accounts.deposit(acc);
									if(bal>0.0)
									{
										System.out.println("New Balance:" + bal);
									}
									
								}
									break;
								case 3: {
									System.out.println("Transaction");
									System.out.print("Enter the account No to transfer:");
									int accto = in.nextInt();
									System.out.print("Enter amount to transfer:");
									float money = in.nextFloat();
									float bal = accounts.transfer(acc, accto, money);
									System.out.println("New Balance:" + bal);
								}
									break;
								default: {
									System.out.println("Invalid Choice");
								}
								}
							} while (c != 4);

						} else {
							System.out.println("Invalid account no");
						}

					} else if (!rs.next()) {
						System.out.println("Invalid user id or password");
					}

				}
					break;
				case 2: {
					System.out.println("Enter user id:");
					int empid = in.nextInt();
					System.out.println("Enter your password:");
					String passwrd = in.next();
					String query = "select empid,passwrd from employee where empid=? and passwrd=?";
					stmt = con.prepareStatement(query);
					stmt.setInt(1, empid);
					stmt.setString(2, passwrd);
					ResultSet r = stmt.executeQuery();
					if (r.next()) {
						System.out.println("Logged in Successfully");
						String options;
						do {
							System.out.println(
									"Enter your choice:\nA.View Customer details\nB.View Customer's Account details\nC.Delete account\nD.Delete customer details.\nE.View TransactionLog\nF.Exit ");
							options = in.next();
							Employee e = new Employee(empid);
							switch (options) {
							case "A": {
								System.out.println("Enter customer userid:");
								int uid = in.nextInt();
								Customer cust = new Customer(uid);
								cust.display(uid);
							}
								break;
							case "B": {
								System.out.println("Enter account no");
								int aid = in.nextInt();
								String query1 = "select * from account where accountno=?";
								stmt = con.prepareStatement(query1);
								stmt.setInt(1, aid);
								ResultSet rs = stmt.executeQuery();
								while (rs.next()) {
									System.out.println("Acount details:");
									System.out.println("Account No:" + rs.getInt(1));
									System.out.println("Balance:" + rs.getFloat(2));
									System.out.println("Account Type:" + rs.getString(3));
									System.out.println("User ID:" + rs.getInt(4));
								}
							}
								break;
							case "C": {
								System.out.println("Enter account no to delete");
								int aid = in.nextInt();
								e.deleteAccount(aid);
							}
								break;
							case "D": {
								System.out.println("Enter customer id");
								int cid = in.nextInt();
								e.deleteCustomer(cid);
							}
								break;
							case "E": {
								
								System.out.println("******************Transaction LogFile*****************");
								DisplayTransaction.Viewtransactionlogs();	
							}
							break;
							default:
								System.out.println("Invalid option");
							}
						} while (!(options.equalsIgnoreCase("F")));
					} else if (!(r.next())) {
						System.out.println("Invalid user id or password");
					}
				}
				break;
				case 3: {
					int chOp;
					do {
						System.out.println("Choose an account:\n1.Saving Account\n2.Current Account\n");
						System.out.print("Enter your choice:");
						chOp = in.nextInt();
						switch (chOp) {
						case 1: {
							Customer c = new Customer();
							c.getData();
							String query = "insert into customer(first_name,last_name,mobileNumber1,mobileNumber2,dateOfBirth,email,doornumber,locations,pincode,passwrd) values (?,?,?,?,?,?,?,?,?,?)";
							stmt = con.prepareStatement(query);
							stmt.setString(1, c.getFname());
							stmt.setString(2, c.getLname());
							stmt.setLong(3, c.getMobileNumber1());
							stmt.setLong(4, c.getMobileNumber2());
							stmt.setObject(5, c.getDateOfBirth());
							stmt.setString(6, c.getEmail());
							stmt.setString(7, c.getDoorNumber());
							stmt.setString(8, c.getLocation());
							stmt.setInt(9, c.getPincode());
							stmt.setString(10, c.getPassword());
							int r = stmt.executeUpdate();
							if (r > 0) {
								System.out.println("Data Updated Successfully");
								c.setUserId(c.recentUserId());
								SavingAccount account = new SavingAccount(c);
								account.createAccount(c);
							} else {
								System.out.println("Not Updated Successfully");
							}
						}
						break;
						case 2: {
							Customer c1 = new Customer();
							c1.getData();
							String query = "insert into customer(first_name,last_name,mobileNumber1,mobileNumber2,dateOfBirth,email,doornumber,locations,pincode,passwrd) values (?,?,?,?,?,?,?,?,?,?)";
							stmt = con.prepareStatement(query);
							stmt.setString(1, c1.getFname());
							stmt.setString(2, c1.getLname());
							stmt.setLong(3, c1.getMobileNumber1());
							stmt.setLong(4, c1.getMobileNumber2());
							stmt.setObject(5, c1.getDateOfBirth());
							stmt.setString(6, c1.getEmail());
							stmt.setString(7, c1.getDoorNumber());
							stmt.setString(8, c1.getLocation());
							stmt.setInt(9, c1.getPincode());
							stmt.setString(10, c1.getPassword());
							int r = stmt.executeUpdate();
							if (r > 0) {
								System.out.println("Data Updated Successfully");
								c1.setUserId(c1.recentUserId());
								CurrentAccount account = new CurrentAccount(c1);
								account.createAccount(c1);
							} else {
								System.out.println("Not Updated Successfully");
							}
						}
							break;
						default:
							System.out.println("Invalid choice");
						}
					} while (chOp!=3);
				}
					break;
				case 4: {
					System.exit(0);
				}
				default:
					System.out.println("Invalid choice");
				}
			} while (choice > 0 && choice < 4);
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("***************THANKS FOR SELECTING OUR BANK.*******************");
		}
		
	}
	public static String getAccountDesc(int accno) {
		String type = "";
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query = "select accounttype from account where accountno=?";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, accno);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				type = rs.getString(1);
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Some error occured.Please try again or later");
		}
		return type;
	}
	public static Logger getLogger() {
		return LOGGER;
	}

}
