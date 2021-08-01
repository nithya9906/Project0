package com.revature.project0.bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
//import java.util.Date;
import java.util.Scanner;

public class Customer {
	private int userId;
	private String fname;
	private String lname;
	private long mobileNumber1;
	private long mobileNumber2;
	private LocalDate dateOfBirth;
	private String email;
	private String doorNumber;
	private String location;
	private int pincode;
	private String password;

	public Customer() {
	}

	public static LocalDate getDateFromString(String string, DateTimeFormatter format) {

		// Convert the String to Date in the specified format
		LocalDate date = LocalDate.parse(string, format);

		// return the converted date
		return date;
	}

	public Customer(String fname, String lname, long mobileNumber1, long mobileNumber2, LocalDate dateOfBirth,
			String email, String doorNumber, String location, int pincode, int userId, String password) {

		this.fname = fname;
		this.lname = lname;
		this.mobileNumber1 = mobileNumber1;
		this.mobileNumber2 = mobileNumber2;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.doorNumber = doorNumber;
		this.location = location;
		this.pincode = pincode;
		this.userId = userId;
		this.password = password;
	}

	public Customer(String fname, String lname, long mobileNumber1, LocalDate dateOfBirth, String email,
			String doorNumber, String location, int pincode, String password) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.mobileNumber1 = mobileNumber1;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.doorNumber = doorNumber;
		this.location = location;
		this.pincode = pincode;
		this.password = password;
	}

	public Customer(String fname, String lname, long mobileNumber1, long mobileNumber2, LocalDate dateOfBirth,
			String email, String doorNumber, String location, int pincode, String password) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.mobileNumber1 = mobileNumber1;
		this.mobileNumber2 = mobileNumber2;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.doorNumber = doorNumber;
		this.location = location;
		this.pincode = pincode;
		this.password = password;
	}

	public Customer(int usrid) {
		userId = usrid;
	}

	public void getData() {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter your first name:");
		fname = in.next();
		System.out.print("Enter your last name:");
		lname = in.next();
		String confirmPassword;
		System.out.print("Enter your password:");
		password = in.next();
		System.out.print("Enter your confirmed password:");
		confirmPassword = in.next();
		if (!(password.equals(confirmPassword))) {
			System.out.println("Mismatched password.Please try again");
			System.out.print("Enter your password:");
			password = in.next();
			System.out.print("Enter your confirmed password:");
			confirmPassword = in.next();
		}
		System.out.print("Enter your mobile number 1:");
		mobileNumber1 = in.nextLong();
		System.out.print("Enter your mobile number 2(if not present,press 0):");
		mobileNumber2 = in.nextLong();
		System.out.print("Enter your date of birth(yyyy-mm-dd):");
		String dob = in.next();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			dateOfBirth = getDateFromString(dob, format);
		} catch (IllegalArgumentException e) {

			System.out.println("Exception: " + e);
		}

		// If the String was unable to be parsed.
		catch (DateTimeParseException e) {

			System.out.println("Exception: " + e);
		}
		System.out.print("Enter your email:");
		email = in.next();
		System.out.print("Enter your door number:");
		doorNumber = in.next();
		System.out.print("Enter your location:");
		location = in.next();
		System.out.print("Enter your pincode:");
		pincode = in.nextInt();
		
		
	}

	@Override
	public String toString() {
		return "Customer [userId=" + userId + ", fname=" + fname + ", lname=" + lname + ", mobileNumber1="
				+ mobileNumber1 + ", mobileNumber2=" + mobileNumber2 + ", dateOfBirth=" + dateOfBirth + ", email="
				+ email + ", doorNumber=" + doorNumber + ", location=" + location + ", pincode=" + pincode
				+ ", password=" + password + "]";
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public long getMobileNumber1() {
		return mobileNumber1;
	}

	public void setMobileNumber1(long mobileNumber1) {
		this.mobileNumber1 = mobileNumber1;
	}

	public long getMobileNumber2() {
		return mobileNumber2;
	}

	public void setMobileNumber2(long mobileNumber2) {
		this.mobileNumber2 = mobileNumber2;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(String doorNumber) {
		this.doorNumber = doorNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public int getUserId() {

		return userId;
	}

	public int recentUserId() {
		Connection con = ConnectionUtils.getConnection();
		// CallableStatement cstmt;
		PreparedStatement stmt = null;
		try {
			String query = "select max(userid) from customer";
			stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				userId = rs.getInt(1);
			} else {
				System.out.println("Account is not created.Try again");
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// System.out.println("Some error occured.Please try again or later");
		}
		return userId;

	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void display(int userid)
	{
		Connection con = ConnectionUtils.getConnection();
		PreparedStatement stmt = null;
		try {
			String query = "select * from customer where userid=?";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, userid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println("User id:" + rs.getInt(1));
				System.out.println("First name:" + rs.getString(2));
				System.out.println("Last name" + rs.getString(3));
				System.out.println("Mobile Number1:" + rs.getLong(4));
				if (mobileNumber2 != 0) {
					System.out.println("Mobile Number2:" + rs.getLong(5));
				}
				System.out.println("Date Of Birth:" + rs.getObject(6));
				System.out.println("Email:" + rs.getString(7));
				System.out.println("Door number:" + rs.getString(8));
				System.out.println("Location:" + rs.getString(9));
				System.out.println("Pincode:" + rs.getInt(10));
			}
			stmt.close();

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// System.out.println("Some error occured.Please try again or later");
		}
	}
	
	public void display() {
		System.out.println("User id:" + userId);
		System.out.println("First name:" + fname);
		System.out.println("Last name" + lname);
		System.out.println("Mobile Number1:" + mobileNumber1);
		if (mobileNumber2 != 0) {
			System.out.println("Mobile Number2:" + mobileNumber2);
		}
		System.out.println("Date Of Birth:" + dateOfBirth);
		System.out.println("Email:" + email);
		System.out.println("Door number:" + doorNumber);
		System.out.println("Location:" + location);
		System.out.println("Pincode:" + pincode);
	}

	public Customer(String fname, String lname, long mobileNumber1, LocalDate dateOfBirth, String email,
			String doorNumber, String location, int pincode, int userId, String password) {

		this.fname = fname;
		this.lname = lname;
		this.mobileNumber1 = mobileNumber1;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.doorNumber = doorNumber;
		this.location = location;
		this.pincode = pincode;
		this.userId = userId;
		this.password = password;
	}

}
