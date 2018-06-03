package com.prashu.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TradeTest {

    @Test
    public void testTrade(){
        Trade trade = new Trade(System.nanoTime(), new BigInteger("100"), new BigDecimal(10.000).setScale(3,BigDecimal.ROUND_DOWN));
        Assert.assertEquals(new BigInteger("100"), trade.getQuantity());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), trade.getPrice());
        Assert.assertEquals("100@10.000", trade.toString());
    }
}
