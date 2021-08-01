package com.revature.project0.bank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DisplayTransaction{
	public static void Viewtransactionlogs() {
		  File file = new File("C:\\mylogs\\BankApp1.log");
		  BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			System.out.println("THE TRANSACTIONS LOG DETAILS: ");
			while ((st = br.readLine()) != null)
			    System.out.println(st);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	

}
