package com.prashu.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Order implements Comparable<Order>{
    private long creationTime;
    private Side side;
    private BigInteger quantity;
    private BigDecimal price;

    public Order(long creationTime, Side side, BigInteger quantity, BigDecimal price) {
        this.creationTime = creationTime;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (getCreationTime() != order.getCreationTime()) return false;
        if (getSide() != null ? !getSide().equals(order.getSide()) : order.getSide() != null) return false;
        if (getQuantity() != null ? !getQuantity().equals(order.getQuantity()) : order.getQuantity() != null)
            return false;
        return getPrice() != null ? getPrice().equals(order.getPrice()) : order.getPrice() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getCreationTime() ^ (getCreationTime() >>> 32));
        result = 31 * result + (getSide() != null ? getSide().hashCode() : 0);
        result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "Created At=" + creationTime +
                ", Side='" + side + '\'' +
                ", Quantity=" + quantity +
                ", Price=" + price +
                '}';
    }

    @Override
    public int compareTo(Order o) {
        if(this.getPrice().compareTo(o.getPrice()) == 0){
            return (int) (this.getCreationTime() - o.creationTime);
        }else if(this.getSide() == Side.Buy){
            return o.getPrice().compareTo(this.getPrice());
        }else if(this.getSide() == Side.Sell){
            return this.getPrice().compareTo(o.getPrice());
        }
        return 0;
    }
}
