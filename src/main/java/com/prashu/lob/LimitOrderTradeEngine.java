package com.prashu.lob;

import com.prashu.model.Order;
import com.prashu.model.Side;
import com.prashu.model.Trade;
import com.prashu.TradeEngine;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class LimitOrderTradeEngine implements TradeEngine {

    private final static int LEFT_PADDING = 5;
    private final static int RIGHT_PADDING = 10;

    private final PriorityQueue<Order> buyOrders = new PriorityQueue<>();
    private final PriorityQueue<Order> sellOrders = new PriorityQueue<>();

    private final Map<BigDecimal, BigInteger> buyOrdersAggregates = new TreeMap<>(Comparator.reverseOrder());
    private final Map<BigDecimal, BigInteger> sellOrdersAggregates = new TreeMap<>();

    private final List<Trade> tradesExecuted = new ArrayList<>();

    public String executeOrder(Side side, BigInteger quantity, BigDecimal price){
        StringBuilder result = new StringBuilder();

        Order order = createOrder(side, quantity, price);
        result.append("\nBelow order was placed:\n");
        result.append("\t" + order.toString());

        List<Trade> trades = matchOrders();
        result.append("\nTrades executed:\n");
        for(Trade trade : trades){
            tradesExecuted.add(trade);
            result.append(String.format("%" + LEFT_PADDING + "s", "") + trade + "\n");
        }

        String marketDepth = getMarketDepth();
        result.append(marketDepth);

        return result.toString();
    }

    public Order createOrder(Side side, BigInteger quantity, BigDecimal price){
        long creationTime = System.nanoTime();
        Order order = new Order(creationTime, side, quantity, price);
        if(side == Side.Buy){
            if(buyOrdersAggregates.containsKey(price)){
                buyOrdersAggregates.replace(price, buyOrdersAggregates.get(price), buyOrdersAggregates.get(price).add(quantity));
            }else{
                buyOrdersAggregates.put(price, quantity);
            }
            buyOrders.offer(order);
        }else if(side == Side.Sell){
            if(sellOrdersAggregates.containsKey(price)){
                sellOrdersAggregates.replace(price, sellOrdersAggregates.get(price), sellOrdersAggregates.get(price).add(quantity));
            }else{
                sellOrdersAggregates.put(price, quantity);
            }
            sellOrders.offer(order);
        }
        return order;
    }

    public List<Trade> matchOrders(){
        List<Trade> trades = new ArrayList<>();
        while (!buyOrders.isEmpty()
                && !sellOrders.isEmpty()
                && buyOrders.peek().getPrice().compareTo(sellOrders.peek().getPrice()) >= 0
                ){
            Order sellOrder = sellOrders.peek();
            Order buyOrder = buyOrders.peek();

            BigInteger tradeQuantity = buyOrder.getQuantity().min(sellOrder.getQuantity());
            BigDecimal tradePrice = buyOrder.getPrice();

            trades.add(new Trade(System.nanoTime(), tradeQuantity, tradePrice));
            sellOrder.setQuantity(sellOrder.getQuantity().subtract(tradeQuantity));
            buyOrder.setQuantity(buyOrder.getQuantity().subtract(tradeQuantity));

            BigInteger newBuyAggregate = buyOrdersAggregates.get(buyOrder.getPrice()).subtract(tradeQuantity);
            if(newBuyAggregate.compareTo(BigInteger.ZERO) == 0){
                buyOrdersAggregates.remove(buyOrder.getPrice());
            }else{
                buyOrdersAggregates.replace(buyOrder.getPrice(), buyOrdersAggregates.get(buyOrder.getPrice()).subtract(tradeQuantity));
            }

            BigInteger newSellAggregate = sellOrdersAggregates.get(sellOrder.getPrice()).subtract(tradeQuantity);
            if(newSellAggregate.compareTo(BigInteger.ZERO) == 0){
                sellOrdersAggregates.remove(sellOrder.getPrice());
            }else{
                sellOrdersAggregates.replace(sellOrder.getPrice(), sellOrdersAggregates.get(sellOrder.getPrice()).subtract(tradeQuantity));
            }

            if(sellOrder.getQuantity().compareTo(BigInteger.ZERO) == 0){
                sellOrders.poll();
            }

            if(buyOrder.getQuantity().compareTo(BigInteger.ZERO) == 0){
                buyOrders.poll();
            }
        }
        return trades;
    }

    public String getMarketDepth(){
        StringBuilder marketDepth = new StringBuilder();
        marketDepth.append("Market Depth:\n");

        List<String> buys = buyOrdersAggregates.entrySet().stream()
                .map(entry -> entry.getValue() + "@" + entry.getKey())
                .collect(Collectors.toList());

        List<String> sells = sellOrdersAggregates.entrySet().stream()
                .map(entry -> entry.getValue() + "@" + entry.getKey())
                .collect(Collectors.toList());

        int maxSize = (buys.size() > sells.size()) ? buys.size() : sells.size();

        int buysMaxLength = buys.size() > 0 ? buys.stream().map(s -> s.length()).max(Integer::compareTo).get() : 0;
        int rightPadding = RIGHT_PADDING + buysMaxLength;

        marketDepth.append(String.format("%" + LEFT_PADDING + "s",""));
        marketDepth.append(Side.Buy.name());
        marketDepth.append(String.format("%" + (rightPadding - Side.Buy.name().length())  + "s", "|"));
        marketDepth.append(String.format("%" + LEFT_PADDING + "s",""));
        marketDepth.append(Side.Sell.name() + "\n");

        for(int i = 0; i < maxSize; i++){
            String b = "";
            if(i < buys.size()){
                b = buys.get(i);
            }

            String s = "";
            if(i < sells.size()){
                s = sells.get(i);

            }
            marketDepth.append(String.format("%" + LEFT_PADDING + "s",""));
            marketDepth.append(b);
            marketDepth.append(String.format("%" + (rightPadding - b.length()) + "s", "|"));
            marketDepth.append(String.format("%" + LEFT_PADDING + "s",""));
            marketDepth.append(s);
            marketDepth.append("\n");
        }

        marketDepth.append("\n");
        return marketDepth.toString();
    }

    public PriorityQueue<Order> getBuyOrders() {
        return buyOrders;
    }

    public PriorityQueue<Order> getSellOrders() {
        return sellOrders;
    }

    public Map<BigDecimal, BigInteger> getBuyOrdersAggregates() {
        return buyOrdersAggregates;
    }

    public Map<BigDecimal, BigInteger> getSellOrdersAggregates() {
        return sellOrdersAggregates;
    }

    public List<Trade> getTradesExecuted() {
        return tradesExecuted;
    }
}