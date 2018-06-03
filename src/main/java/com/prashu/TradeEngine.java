package com.prashu;

import com.prashu.model.Side;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface TradeEngine {

    String executeOrder(Side side, BigInteger quantity, BigDecimal price);

}
