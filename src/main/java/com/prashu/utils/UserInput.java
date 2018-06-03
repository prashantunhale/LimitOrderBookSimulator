package com.prashu.utils;

import com.prashu.model.Side;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class UserInput {
    public static String validateInput(String s){
        StringBuilder validationResults = new StringBuilder();
        if(s.split("\\s+").length == 3){
            String side = s.split("\\s+")[0];
            validationResults.append(validateSide(side));

            String quantity = s.split("\\s+")[1];
            validationResults.append(validateQuantity(quantity));

            String price =  s.split("\\s+")[2];
            validationResults.append(validatePrice(price));
        }else{
            validationResults.append("Invalid Input. Input should be in the format of [Side (B or S)] [Quantity (Number greater than 0)] [Price (Number greater than 0 to 3 decimal places] for example: B 100 10.1 or S 50 0.25\n");
        }
        return validationResults.toString();
    }

    public static String validateSide(String side){
        if(!side.matches("[BS]")){
            return "Invalid order side : " + side + ". Side should either B or S\n";
        }
        return "";
    }

    public static String validateQuantity(String quantity){
        if(!quantity.matches("[0-9]*") || new BigInteger(quantity).compareTo(BigInteger.ZERO) <= 0){
            return "Invalid quantity : " + quantity + ". Quantity should be a number greater than 0 e.g. 100 or 9995\n";
        }
        return "";
    }

    public static String validatePrice(String price){
        if(!price.matches("[0-9]*\\.?[0-9]?[0-9]?[0-9]?") || new BigDecimal(price).setScale(3, BigDecimal.ROUND_DOWN).compareTo(BigDecimal.ZERO.setScale(3, BigDecimal.ROUND_DOWN)) <= 0){
            return "Invalid price : " + price + ". Price should be any value greater than 0 to 3 decimal places e.g. 10 or 0.225\n";
        }
        return "";
    }

    public static Side extractSide(String input){
        return input.split("\\s+")[0].equals("B") ? Side.Buy : Side.Sell;
    }

    public static BigInteger extractQuantity(String input){
        return new BigInteger(input.split("\\s+")[1]);
    }

    public static BigDecimal extractPrice(String input){
        return new BigDecimal(input.split("\\s+")[2]).setScale(3, RoundingMode.HALF_DOWN);
    }
}
