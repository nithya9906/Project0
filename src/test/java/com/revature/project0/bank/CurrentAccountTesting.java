package com.revature.project0.bank;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
public class CurrentAccountTesting {
	CurrentAccount account;
	@BeforeEach
	  void setUp() throws Exception {
	    account= new CurrentAccount();
	   
	  }
	@AfterEach
	  void tearDown() throws Exception {
	    account=null;
	  }
	@Test
	  void depositTest() {

	    Assertions.assertEquals(6000, account.deposit(80140));
	  }
	@Test
	  void withdrawTest() {

	    Assertions.assertEquals(5500, account.withdraw(80140));
	  }
	
}

