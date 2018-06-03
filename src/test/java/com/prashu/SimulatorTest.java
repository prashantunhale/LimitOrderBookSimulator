package com.prashu;

import com.prashu.lob.LimitOrderTradeEngine;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

public class SimulatorTest {
    @Test
    public void testStartSimulator(){
        String userInputs = "B 50 10.0\nS 50 10.0\nexit";
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        Scanner scanner = new Scanner(userInputs);
        Simulator.startSimulator(engine, scanner);
        Assert.assertEquals(1, engine.getTradesExecuted().size());
        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10.0).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(0).getPrice());
    }

    @Test
    public void testRandomScenario1(){
        String userInputs = "B 100 9.9\nB 1000 10\nS 100 10.2\nS 50 9.9\nexit";
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        Scanner scanner = new Scanner(userInputs);
        Simulator.startSimulator(engine, scanner);
        Assert.assertEquals(1, engine.getTradesExecuted().size());
        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10.0).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(0).getPrice());
        Assert.assertEquals(2, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(1, engine.getSellOrdersAggregates().size());
    }

    @Test
    public void testRandomScenario2(){
        String userInputs = "B 50 10\nB 50 10\nS 100 10\nexit";
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        Scanner scanner = new Scanner(userInputs);
        Simulator.startSimulator(engine, scanner);
        Assert.assertEquals(2, engine.getTradesExecuted().size());
        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10.0).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(0).getPrice());
        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(1).getQuantity());
        Assert.assertEquals(new BigDecimal(10.0).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(1).getPrice());
        Assert.assertEquals(0, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(0, engine.getSellOrdersAggregates().size());
    }

    @Test
    public void testRandomScenario3(){
        String userInputs = "B 50 10\nB 100 10\nB 100 9.9\nS 200 9.9\nexit";
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        Scanner scanner = new Scanner(userInputs);
        Simulator.startSimulator(engine, scanner);
        Assert.assertEquals(3, engine.getTradesExecuted().size());
        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10.0).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(0).getPrice());
        Assert.assertEquals(new BigInteger("100"), engine.getTradesExecuted().get(1).getQuantity());
        Assert.assertEquals(new BigDecimal(10.0).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(1).getPrice());
        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(2).getQuantity());
        Assert.assertEquals(new BigDecimal(9.9).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(2).getPrice());
        Assert.assertEquals(1, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(0, engine.getSellOrdersAggregates().size());
    }
}
