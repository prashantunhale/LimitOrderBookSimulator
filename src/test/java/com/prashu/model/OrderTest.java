package com.prashu.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class OrderTest {

    @Test
    public void testOrder(){
        Order buyOrder = new Order(System.nanoTime(), Side.Buy, new BigInteger("100"), new BigDecimal(10.1).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertEquals("Order side is Buy", Side.Buy, buyOrder.getSide());
        Assert.assertEquals("Order quantity is 100", new BigInteger("100"), buyOrder.getQuantity());
        Assert.assertEquals("Order price is 10.1", new BigDecimal(10.1).setScale(3, BigDecimal.ROUND_DOWN), buyOrder.getPrice());

        Order sellOrder = new Order(System.nanoTime(), Side.Sell, new BigInteger("500"), new BigDecimal(9.9).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertEquals("Order side is Sell", Side.Sell, sellOrder.getSide());
        Assert.assertEquals("Order quantity is 500", new BigInteger("500"), sellOrder.getQuantity());
        Assert.assertEquals("Order price is 9.9", new BigDecimal(9.9).setScale(3, BigDecimal.ROUND_DOWN), sellOrder.getPrice());
    }

    @Test
    public void testOrderEquality(){
        Order buyOrderAt10100 = new Order(System.nanoTime(), Side.Buy, new BigInteger("100"), new BigDecimal(10.100).setScale(3, BigDecimal.ROUND_DOWN));
        Order buyOrderAt1010 = new Order(buyOrderAt10100.getCreationTime(), Side.Buy, new BigInteger("100"), new BigDecimal(10.100).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertTrue("Both orders are equal", buyOrderAt1010.equals(buyOrderAt10100));

        Order sellOrderAt9900 = new Order(System.nanoTime(), Side.Sell, new BigInteger("500"), new BigDecimal(9.900).setScale(3, BigDecimal.ROUND_DOWN));
        Order sellOrderAt99 = new Order(sellOrderAt9900.getCreationTime(), Side.Sell, new BigInteger("500"), new BigDecimal(9.9).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertTrue("Both orders are equal", sellOrderAt9900.equals(sellOrderAt99));
    }

    @Test
    public void testOrderInEquality(){
        Order buyOrderAt10100 = new Order(System.nanoTime(), Side.Buy, new BigInteger("100"), new BigDecimal(10.100).setScale(3, BigDecimal.ROUND_DOWN));
        Order buyOrderAt10150 = new Order(buyOrderAt10100.getCreationTime(), Side.Buy, new BigInteger("100"), new BigDecimal(10.150).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertFalse("Both orders are not equal, price is different",buyOrderAt10150.equals(buyOrderAt10100));

        Order buyOrderAt10100SomeTimeLater = new Order(System.nanoTime(), Side.Buy, new BigInteger("100"), new BigDecimal(10.100).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertFalse("Both orders are not equal, order creation time differs",buyOrderAt10150.equals(buyOrderAt10100SomeTimeLater));

        Order sellOrderAt10100 = new Order(buyOrderAt10100.getCreationTime(), Side.Sell, new BigInteger("100"), new BigDecimal(10.100).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertFalse("Both orders are not equal, order side differs",buyOrderAt10150.equals(sellOrderAt10100));
    }

    @Test
    public void testBuyOrdersComparison(){
        Order buyOrder100At10 = new Order(System.nanoTime(), Side.Buy, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        Order buyOrder500At10 = new Order(System.nanoTime(), Side.Buy, new BigInteger("500"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertTrue("buyOrder100At10 should be given preference because of earlier creation time", buyOrder100At10.compareTo(buyOrder500At10) < 0);

        Order buyOrder100At11 = new Order(System.nanoTime(), Side.Buy, new BigInteger("100"), new BigDecimal(11).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertTrue("buyOrder100At11 should be given preference because of higher price bid", buyOrder100At11.compareTo(buyOrder100At10) < 0);
    }

    @Test
    public void testSellOrdersComparison(){
        Order sellOrder100At10 = new Order(System.nanoTime(), Side.Sell, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        Order sellOrder500At10 = new Order(System.nanoTime(), Side.Sell, new BigInteger("500"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertTrue("sellOrder100At10 should be given preference because of earlier creation time", sellOrder100At10.compareTo(sellOrder500At10) < 0);

        Order sellOrder100At11 = new Order(System.nanoTime(), Side.Sell, new BigInteger("100"), new BigDecimal(11).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertTrue("sellOrder100At10 should be given preference because of lower price ask", sellOrder100At10.compareTo(sellOrder100At11) < 0);
    }
}
