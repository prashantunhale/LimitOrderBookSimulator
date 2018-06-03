package com.prashu.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Trade {
    private long creationTime;
    private BigInteger quantity;
    private BigDecimal price;

    public Trade(long creationTime, BigInteger quantity, BigDecimal price) {
        this.creationTime = creationTime;
        this.quantity = quantity;
        this.price = price;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trade)) return false;

        Trade trade = (Trade) o;

        if (getCreationTime() != trade.getCreationTime()) return false;
        if (getQuantity() != null ? !getQuantity().equals(trade.getQuantity()) : trade.getQuantity() != null)
            return false;
        return getPrice() != null ? getPrice().equals(trade.getPrice()) : trade.getPrice() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getCreationTime() ^ (getCreationTime() >>> 32));
        result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return quantity + "@" + price;
    }
}
