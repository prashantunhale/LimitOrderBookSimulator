package com.prashu.lob;

import com.prashu.model.Side;
import com.prashu.model.Trade;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class LimitOrderTradeEngineTest {

    @Test
    public void testBuyOrdersPrioritiesByPrice(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.createOrder(Side.Buy, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Buy, new BigInteger("100"), new BigDecimal(11).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Buy, new BigInteger("100"), new BigDecimal(12).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertEquals(new BigDecimal(12).setScale(3, BigDecimal.ROUND_DOWN), engine.getBuyOrders().poll().getPrice());
        Assert.assertEquals(new BigDecimal(11).setScale(3, BigDecimal.ROUND_DOWN), engine.getBuyOrders().poll().getPrice());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), engine.getBuyOrders().poll().getPrice());
    }

    @Test
    public void testBuyOrdersPrioritiesByTime(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.createOrder(Side.Buy, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Buy, new BigInteger("500"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Buy, new BigInteger("800"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertEquals(new BigInteger("100"), engine.getBuyOrders().poll().getQuantity());
        Assert.assertEquals(new BigInteger("500"), engine.getBuyOrders().poll().getQuantity());
        Assert.assertEquals(new BigInteger("800"), engine.getBuyOrders().poll().getQuantity());
    }

    @Test
    public void testSellOrdersPrioritiesByPrice(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.createOrder(Side.Sell, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Sell, new BigInteger("100"), new BigDecimal(11).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Sell, new BigInteger("100"), new BigDecimal(12).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), engine.getSellOrders().poll().getPrice());
        Assert.assertEquals(new BigDecimal(11).setScale(3, BigDecimal.ROUND_DOWN), engine.getSellOrders().poll().getPrice());
        Assert.assertEquals(new BigDecimal(12).setScale(3, BigDecimal.ROUND_DOWN), engine.getSellOrders().poll().getPrice());
    }

    @Test
    public void testSellOrdersPrioritiesByTime(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.createOrder(Side.Sell, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Sell, new BigInteger("500"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Sell, new BigInteger("800"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertEquals(new BigInteger("100"), engine.getSellOrders().poll().getQuantity());
        Assert.assertEquals(new BigInteger("500"), engine.getSellOrders().poll().getQuantity());
        Assert.assertEquals(new BigInteger("800"), engine.getSellOrders().poll().getQuantity());
    }

    @Test
    public void testBuyOrdersAggregate(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.createOrder(Side.Buy, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Buy, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Buy, new BigInteger("100"), new BigDecimal(20).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertEquals(2, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(new BigInteger("200"), engine.getBuyOrdersAggregates().get(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN)));
        Assert.assertEquals(new BigInteger("100"), engine.getBuyOrdersAggregates().get(new BigDecimal(20).setScale(3, BigDecimal.ROUND_DOWN)));
    }

    @Test
    public void testSellOrdersAggregate(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.createOrder(Side.Sell, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Sell, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Sell, new BigInteger("100"), new BigDecimal(20).setScale(3, BigDecimal.ROUND_DOWN));

        Assert.assertEquals(2, engine.getSellOrdersAggregates().size());
        Assert.assertEquals(new BigInteger("200"), engine.getSellOrdersAggregates().get(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN)));
        Assert.assertEquals(new BigInteger("100"), engine.getSellOrdersAggregates().get(new BigDecimal(20).setScale(3, BigDecimal.ROUND_DOWN)));
    }

    @Test
    public void testExactOrderMatching(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.createOrder(Side.Sell, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Buy, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        List<Trade> trades = engine.matchOrders();
        Assert.assertEquals(1, trades.size());
        Assert.assertEquals(new BigInteger("100"), trades.get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), trades.get(0).getPrice());
        Assert.assertEquals(0, engine.getBuyOrders().size());
        Assert.assertEquals(0, engine.getSellOrders().size());
        Assert.assertEquals(0, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(0, engine.getSellOrdersAggregates().size());
        String expectedMarketDepth = "Market Depth:\n" +
                                     "     Buy      |     Sell\n" +
                                     "\n";
        Assert.assertEquals(expectedMarketDepth, engine.getMarketDepth());
    }

    @Test
    public void testNoOrderMatching(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.createOrder(Side.Sell, new BigInteger("100"), new BigDecimal(10.250).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Buy, new BigInteger("100"), new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN));
        List<Trade> trades = engine.matchOrders();
        Assert.assertEquals(0, trades.size());
        Assert.assertEquals(1, engine.getBuyOrders().size());
        Assert.assertEquals(1, engine.getSellOrders().size());
        Assert.assertEquals(1, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(new BigInteger("100"), engine.getBuyOrdersAggregates().get(new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN)));
        Assert.assertEquals(new BigInteger("100"), engine.getSellOrdersAggregates().get(new BigDecimal(10.250).setScale(3, BigDecimal.ROUND_DOWN)));
        String expectedMarketDepth = "Market Depth:\n" +
                                     "     Buy                |     Sell\n" +
                                     "     100@10.000         |     100@10.250\n" +
                                     "\n";
        Assert.assertEquals(expectedMarketDepth, engine.getMarketDepth());
    }

    @Test
    public void testPartialOrderMatching(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.createOrder(Side.Sell, new BigInteger("100"), new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN));
        engine.createOrder(Side.Buy, new BigInteger("70"), new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN));
        List<Trade> trades = engine.matchOrders();
        Assert.assertEquals(1, trades.size());
        Assert.assertEquals(new BigInteger("70"), trades.get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), trades.get(0).getPrice());
        Assert.assertEquals(0, engine.getBuyOrders().size());
        Assert.assertEquals(1, engine.getSellOrders().size());
        Assert.assertEquals(1, engine.getSellOrdersAggregates().size());
        Assert.assertEquals(new BigInteger("30"), engine.getSellOrdersAggregates().get(new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN)));
        String expectedMarketDepth = "Market Depth:\n" +
                                     "     Buy      |     Sell\n" +
                                     "              |     30@10.000\n" +
                                     "\n";
        Assert.assertEquals(expectedMarketDepth, engine.getMarketDepth());
    }

    @Test
    public void testExactTradeExecuted(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.executeOrder(Side.Buy, new BigInteger("100"), new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN));
        engine.executeOrder(Side.Sell, new BigInteger("100"), new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertEquals(1, engine.getTradesExecuted().size());
        Assert.assertEquals(new BigInteger("100"), engine.getTradesExecuted().get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(0).getPrice());
    }

    @Test
    public void testPartialTradesExecuted(){
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.executeOrder(Side.Sell, new BigInteger("50"), new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN));
        engine.executeOrder(Side.Sell, new BigInteger("50"), new BigDecimal(9.900).setScale(3, BigDecimal.ROUND_DOWN));
        engine.executeOrder(Side.Buy, new BigInteger("70"), new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN));
        engine.executeOrder(Side.Buy, new BigInteger("10"), new BigDecimal(9.900).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertEquals(2, engine.getTradesExecuted().size());
        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(0).getPrice());
        Assert.assertEquals(new BigInteger("20"), engine.getTradesExecuted().get(1).getQuantity());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(1).getPrice());
        Assert.assertEquals(1, engine.getBuyOrders().size());
        Assert.assertEquals(1, engine.getSellOrders().size());
        Assert.assertEquals(1, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(1, engine.getSellOrdersAggregates().size());
        Assert.assertEquals(new BigInteger("30"), engine.getSellOrdersAggregates().get(new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN)));
        Assert.assertEquals(new BigInteger("10"), engine.getBuyOrdersAggregates().get(new BigDecimal(9.900).setScale(3, BigDecimal.ROUND_DOWN)));
    }

    @Test
    public void testNoTradeExecuted() {
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.executeOrder(Side.Sell, new BigInteger("100"), new BigDecimal(10.250).setScale(3, BigDecimal.ROUND_DOWN));
        engine.executeOrder(Side.Buy, new BigInteger("100"), new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertEquals(0, engine.getTradesExecuted().size());
        Assert.assertEquals(1, engine.getBuyOrders().size());
        Assert.assertEquals(1, engine.getSellOrders().size());
        Assert.assertEquals(1, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(1, engine.getSellOrdersAggregates().size());
    }

    @Test
    public void testMultipleTradeExecuted() {
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.executeOrder(Side.Buy, new BigInteger("50"), new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN));
        engine.executeOrder(Side.Buy, new BigInteger("100"), new BigDecimal(10.000).setScale(3, BigDecimal.ROUND_DOWN));
        engine.executeOrder(Side.Buy, new BigInteger("100"), new BigDecimal(9.900).setScale(3, BigDecimal.ROUND_DOWN));
        engine.executeOrder(Side.Sell, new BigInteger("200"), new BigDecimal(9.900).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertEquals(3, engine.getTradesExecuted().size());
        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(0).getPrice());

        Assert.assertEquals(new BigInteger("100"), engine.getTradesExecuted().get(1).getQuantity());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(1).getPrice());

        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(2).getQuantity());
        Assert.assertEquals(new BigDecimal(9.9).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(2).getPrice());

        Assert.assertEquals(1, engine.getBuyOrders().size());
        Assert.assertEquals(0, engine.getSellOrders().size());
        Assert.assertEquals(1, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(0, engine.getSellOrdersAggregates().size());
        Assert.assertEquals(new BigInteger("50"), engine.getBuyOrdersAggregates().get(new BigDecimal(9.900).setScale(3, BigDecimal.ROUND_DOWN)));
    }

    @Test
    public void testPartialSellExecuted() {
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.executeOrder(Side.Sell, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.executeOrder(Side.Buy, new BigInteger("50"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertEquals(1, engine.getTradesExecuted().size());
        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(0).getPrice());
        Assert.assertEquals(0, engine.getBuyOrders().size());
        Assert.assertEquals(1, engine.getSellOrders().size());
        Assert.assertEquals(0, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(1, engine.getSellOrdersAggregates().size());
    }

    @Test
    public void testPartialBuyExecuted() {
        LimitOrderTradeEngine engine = new LimitOrderTradeEngine();
        engine.executeOrder(Side.Sell, new BigInteger("50"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        engine.executeOrder(Side.Buy, new BigInteger("100"), new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN));
        Assert.assertEquals(1, engine.getTradesExecuted().size());
        Assert.assertEquals(new BigInteger("50"), engine.getTradesExecuted().get(0).getQuantity());
        Assert.assertEquals(new BigDecimal(10).setScale(3, BigDecimal.ROUND_DOWN), engine.getTradesExecuted().get(0).getPrice());
        Assert.assertEquals(1, engine.getBuyOrders().size());
        Assert.assertEquals(0, engine.getSellOrders().size());
        Assert.assertEquals(1, engine.getBuyOrdersAggregates().size());
        Assert.assertEquals(0, engine.getSellOrdersAggregates().size());
    }

}
