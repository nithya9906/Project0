package com.revature.project0.bank;

public class AccountFactory {
	
	public Account getAccountType(String accountType,Customer c)
	{
		if(accountType==null)
		{
			return null;
		}
		else if(accountType.equalsIgnoreCase("saving"))
		{
			return new SavingAccount(c);
		}
		else if(accountType.equalsIgnoreCase("current"))
		{
			return new CurrentAccount(c);
		}
		return null;
	}

}
