package com.exnihilo.inout.services;

/**
 * Describe the blocked cell phone number.
 * @author nboussedra
 *
 */
public class PhoneNumber {

	private String number ; 
	
	private boolean blocked ; 
	
	private boolean smsBlocked ;
	
	private String blockRules ;

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the blocked
	 */
	public boolean isBlocked() {
		return blocked;
	}

	/**
	 * @param blocked the blocked to set
	 */
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	/**
	 * @return the smsBlocked
	 */
	public boolean isSmsBlocked() {
		return smsBlocked;
	}

	/**
	 * @param smsBlocked the smsBlocked to set
	 */
	public void setSmsBlocked(boolean smsBlocked) {
		this.smsBlocked = smsBlocked;
	}

	/**
	 * @return the blockRules
	 */
	public String getBlockRules() {
		return blockRules;
	}

	/**
	 * @param blockRules the blockRules to set
	 */
	public void setBlockRules(String blockRules) {
		this.blockRules = blockRules;
	}
	
	
	
}
