package com.qspiders.eshopping.testscripts;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qspiders.eshopping.genericlibs.ExcelLibrary;
import com.qspiders.eshopping.pages.DressesPage;
import com.qspiders.eshopping.pages.OrderDetailsPage;
import com.qspiders.eshopping.pages.ProductDetailsPage;

public class TC002 extends BaseTest {
	@Test(description="Add the Product to ODP and verify it is displayed in ODP")
	public void testProductInODP() {
		//Reading data from Excel
		String sheetName = "TC002";
		String menuLinkName=ExcelLibrary.getStringData(sheetName, 1, 0);
		String productId=ExcelLibrary.getStringData(sheetName, 1, 1).split("\\.")[0];
		int quantity=(int)ExcelLibrary.getNumericData(sheetName, 1, 2);
		boolean increase=ExcelLibrary.getBooleanData(sheetName, 1, 3);
		String size=ExcelLibrary.getStringData(sheetName, 1, 4);
		String colorName=ExcelLibrary.getStringData(sheetName, 1, 5);
				
		//Using in App
		DressesPage dressPage=(DressesPage)homePage.clickOnMenu(menuLinkName);
		ProductDetailsPage productDetailsPage = dressPage.clickOnProduct(productId);
		OrderDetailsPage orderDetailsPage = productDetailsPage.addItemToKart(quantity, increase, size, colorName);
		Assert.assertTrue(orderDetailsPage.isProductDisplayed(productId));
		orderDetailsPage.deleteProduct(productId);
		Assert.assertFalse(orderDetailsPage.isProductDisplayed(productId));
	}
}
