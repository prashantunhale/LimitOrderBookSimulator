package com.prashu.utils;

import com.prashu.model.Side;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UserInputTest {

    @Test
    public void testValidInput(){
        Assert.assertTrue(UserInput.validateInput("B 10 10.0").isEmpty());
        Assert.assertTrue(UserInput.validateInput("B      10           10.0").isEmpty());
        Assert.assertTrue(UserInput.validateInput("S 10 10.0").isEmpty());
        Assert.assertTrue(UserInput.validateInput("S      10           10.0").isEmpty());
        Assert.assertTrue(UserInput.validateInput("B 1000000 10").isEmpty());
        Assert.assertTrue(UserInput.validateInput("S 1000000 10").isEmpty());
        Assert.assertTrue(UserInput.validateInput("B 1000000 10.125").isEmpty());
        Assert.assertTrue(UserInput.validateInput("S 1000000 10.125").isEmpty());
        Assert.assertTrue(UserInput.validateInput("B 29501295295 0.005").isEmpty());
        Assert.assertTrue(UserInput.validateInput("S 29501295295 0.005").isEmpty());
    }

    @Test
    public void testInvalidInput(){
        Assert.assertFalse(UserInput.validateInput("B 0 10.0").isEmpty());
        Assert.assertFalse(UserInput.validateInput("S 0 10.0").isEmpty());
        Assert.assertFalse(UserInput.validateInput("b 10 10.0").isEmpty());
        Assert.assertFalse(UserInput.validateInput("s 10 10.0").isEmpty());
        Assert.assertFalse(UserInput.validateInput("B").isEmpty());
        Assert.assertFalse(UserInput.validateInput("S").isEmpty());
        Assert.assertFalse(UserInput.validateInput("B 10.000 10.000").isEmpty());
        Assert.assertFalse(UserInput.validateInput("S BS 10").isEmpty());
    }

    @Test
    public void testValidateSide(){
        Assert.assertTrue(UserInput.validateSide("S").isEmpty());
        Assert.assertTrue(UserInput.validateSide("B").isEmpty());
        Assert.assertFalse(UserInput.validateSide("s").isEmpty());
        Assert.assertFalse(UserInput.validateSide("b").isEmpty());
        Assert.assertFalse(UserInput.validateSide("0").isEmpty());
        Assert.assertFalse(UserInput.validateSide("1.12345").isEmpty());
    }

    @Test
    public void testValidateQuantity(){
        Assert.assertTrue(UserInput.validateQuantity("1").isEmpty());
        Assert.assertTrue(UserInput.validateQuantity("100000").isEmpty());
        Assert.assertTrue(UserInput.validateQuantity("99999999999").isEmpty());
        Assert.assertTrue(UserInput.validateQuantity("99").isEmpty());
        Assert.assertFalse(UserInput.validateQuantity("s").isEmpty());
        Assert.assertFalse(UserInput.validateQuantity("b").isEmpty());
        Assert.assertFalse(UserInput.validateQuantity("0").isEmpty());
        Assert.assertFalse(UserInput.validateQuantity("1.12345").isEmpty());
    }


    @Test
    public void testValidatePrice(){
        Assert.assertTrue(UserInput.validatePrice("10.000").isEmpty());
        Assert.assertTrue(UserInput.validatePrice("9.125").isEmpty());
        Assert.assertTrue(UserInput.validatePrice("0.005").isEmpty());
        Assert.assertFalse(UserInput.validatePrice("0.000").isEmpty());
        Assert.assertFalse(UserInput.validatePrice("0").isEmpty());
        Assert.assertFalse(UserInput.validatePrice("1.12345").isEmpty());
    }

    @Test
    public void testExtract(){
        String validInput = "B 10 10.1";
        Assert.assertEquals(Side.Buy, UserInput.extractSide(validInput));
        Assert.assertEquals(new BigInteger("10"), UserInput.extractQuantity(validInput));
        Assert.assertEquals(new BigDecimal("10.100").setScale(3, BigDecimal.ROUND_DOWN), UserInput.extractPrice(validInput));

        String multiSpacedValidInput = "S    10              10.1";
        Assert.assertEquals(Side.Sell, UserInput.extractSide(multiSpacedValidInput));
        Assert.assertEquals(new BigInteger("10"), UserInput.extractQuantity(multiSpacedValidInput));
        Assert.assertEquals(new BigDecimal("10.100").setScale(3, BigDecimal.ROUND_DOWN), UserInput.extractPrice(multiSpacedValidInput));
    }
}
