/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.tigerspice.client;

public class StockPrice {

	private String symbol;
	  private double price;
	  private double change;

	  public StockPrice() {
	  }

	  public StockPrice(String symbol, double price, double change) {
	    this.symbol = symbol;
	    this.price = price;
	    this.change = change;
	  }

	  public String getSymbol() {
	    return this.symbol;
	  }

	  public double getPrice() {
	    return this.price;
	  }

	  public double getChange() {
	    return this.change;
	  }

	  public double getChangePercent() {
	    return 100.0 * this.change / this.price;
	  }

	  public void setSymbol(String symbol) {
	    this.symbol = symbol;
	  }

	  public void setPrice(double price) {
	    this.price = price;
	  }

	  public void setChange(double change) {
	    this.change = change;
	  }
}
