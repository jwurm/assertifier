package de.jenswurm.assertifier;

import java.text.SimpleDateFormat;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import de.jenswurm.assertifier.testbeans.Customer;
import de.jenswurm.assertifier.testbeans.Product;
import de.jenswurm.assertifier.testbeans.Purchase;

public class JSONAssertifierTest {

	@Test
	public void test() {
		// build a more or less complex object structure to perform asserts on
		Customer customer = buildTestData();

		/*
		 * The generated asserts are complete, any changes to the values in the
		 * class will make the test fail. If any new properties are created and
		 * set to non-null or a previously null property is given a value, then
		 * the test will notice this. Yet at the same time, despite of the
		 * complex object structure, the tested structures are easy to read,
		 * unlike chained getter calls.
		 * 
		 * It's also very easy to see legitimate changes in the tested beans
		 * after changes in the code - just copy and paste the new generated
		 * asserts into the code and do a diff with the previous version with
		 * version control system, it will highlight all the changes to be
		 * reviewed in a very user friendly way.
		 * 
		 * The disadvantages that i see to this tool are these: It's not real
		 * test driven development (we need a bean with data in it, we can't
		 * write tests before the code), but only can be used to expand tests
		 * once the code to be tested has already been written. Also, renaming a
		 * property may become a bit more work as the IDE refactoring doesn't
		 * necessarily work correctly if there are other tests with other
		 * classes that contain a property of the same name. Also, there must
		 * not be any circular references in the object graph.
		 */

		// generate asserts - these are outputted at the console and can be
		// copied and pasted into the test class
		new JSONAssertifier().assertify(customer, "customer");

		// so here are the generated asserts...the statements 
		String[] customerJSON = new JSONAssertifier().marshalAsJSON(customer);
		int customerIndex = 0;
		Assert.assertEquals("{", customerJSON[customerIndex++]);
		Assert.assertEquals("  givenName : John", customerJSON[customerIndex++]);
		Assert.assertEquals("  id : 1", customerJSON[customerIndex++]);
		Assert.assertEquals("  purchases : [ {", customerJSON[customerIndex++]);
		Assert.assertEquals("    date : 2016-03-23 00:00:00", customerJSON[customerIndex++]);
		Assert.assertEquals("    id : 23", customerJSON[customerIndex++]);
		Assert.assertEquals("    numberOfUnits : 1", customerJSON[customerIndex++]);
		Assert.assertEquals("    product : {", customerJSON[customerIndex++]);
		Assert.assertEquals("      id : 123", customerJSON[customerIndex++]);
		Assert.assertEquals("      name : Lenovo Thinkpad T450", customerJSON[customerIndex++]);
		Assert.assertEquals("      price : 1200.0", customerJSON[customerIndex++]);
		Assert.assertEquals("    }", customerJSON[customerIndex++]);
		Assert.assertEquals("  }, {", customerJSON[customerIndex++]);
		Assert.assertEquals("    date : 2016-02-06 00:00:00", customerJSON[customerIndex++]);
		Assert.assertEquals("    id : 65", customerJSON[customerIndex++]);
		Assert.assertEquals("    numberOfUnits : 1", customerJSON[customerIndex++]);
		Assert.assertEquals("    product : {", customerJSON[customerIndex++]);
		Assert.assertEquals("      id : 98", customerJSON[customerIndex++]);
		Assert.assertEquals("      name : Samsung Galaxy S7", customerJSON[customerIndex++]);
		Assert.assertEquals("      price : 750.0", customerJSON[customerIndex++]);
		Assert.assertEquals("    }", customerJSON[customerIndex++]);
		Assert.assertEquals("  } ]", customerJSON[customerIndex++]);
		Assert.assertEquals("  surname : Doe", customerJSON[customerIndex++]);
		Assert.assertEquals("}", customerJSON[customerIndex++]);

		/*
		 * This is what the alternative of classic asserts would look like.
		 * It's a lot of work to type and not really easy to read and maintain
		 * once we go down into purchases and products. Also it's easy to forget
		 * attributes and new attributes that are added to the beans don't
		 * automatically get noticed to be missing in the tests.
		 * 
		 * Also note the hassle of doing asserts on Doubles or Dates.
		 */
		Assert.assertEquals("Doe", customer.getSurname());
		Assert.assertEquals("John", customer.getGivenName());
		Assert.assertEquals(1, customer.getId());
		Assert.assertEquals(23, customer.getPurchases().get(0).getId());
		Assert.assertEquals(1, customer.getPurchases().get(0).getNumberOfUnits());
		Assert.assertEquals("2016-03-23",
				new SimpleDateFormat("yyyy-MM-dd").format(customer.getPurchases().get(0).getDate()));
		Assert.assertEquals(123, customer.getPurchases().get(0).getProduct().getId());
		Assert.assertEquals("Lenovo Thinkpad T450", customer.getPurchases().get(0).getProduct().getName());
		Assert.assertEquals(Double.valueOf(1200),
				Double.valueOf(customer.getPurchases().get(0).getProduct().getPrice()));
		Assert.assertEquals(65, customer.getPurchases().get(1).getId());
		Assert.assertEquals(1, customer.getPurchases().get(1).getNumberOfUnits());
		Assert.assertEquals("2016-02-06",
				new SimpleDateFormat("yyyy-MM-dd").format(customer.getPurchases().get(1).getDate()));
		Assert.assertEquals(98, customer.getPurchases().get(1).getProduct().getId());
		Assert.assertEquals("Samsung Galaxy S7", customer.getPurchases().get(1).getProduct().getName());
		Assert.assertEquals(Double.valueOf(750),
				Double.valueOf(customer.getPurchases().get(1).getProduct().getPrice()));
	}

	private Customer buildTestData() {
		Customer customer = new Customer("John", "Doe");
		customer.setId(1);
		Product thinkpad = new Product("Lenovo Thinkpad T450", 1200d);
		thinkpad.setId(123);
		Product galaxys7 = new Product("Samsung Galaxy S7", 750d);
		galaxys7.setId(98);

		Purchase purchase = new Purchase();
		purchase.setId(23);
		purchase.setProduct(thinkpad);
		purchase.setDate(LocalDateTime.parse("2016-03-23").toDate());
		purchase.setNumberOfUnits(1);
		customer.getPurchases().add(purchase);

		purchase = new Purchase();
		purchase.setId(65);
		purchase.setProduct(galaxys7);
		purchase.setDate(LocalDateTime.parse("2016-02-06").toDate());
		purchase.setNumberOfUnits(1);
		customer.getPurchases().add(purchase);

		return customer;
	}
}
