# Limit Order Book Simulator
A Light-weight market simulator to test simple algorithmic trading
strategies. It maintains a limit order book for the stock and
report back every time the limit order book generates a trade.

### Assumptions
- Supports only one stock
- Only buy and sell orders (no short sells or buy-to-cover orders)
- No support to revisions or cancellations of orders submitted
- Order should be in the format: [Side] [Quantity] [Price]
- Side: Single character {B,S}, B for a buy order, S for a sell order
- Quantity: any value greater than 0
- Price: any value greater than zero to 3 decimal places
- Stock's lot size is 1

### Pre-requisites
- Java 8 (JDK 1.8.0_161 or equivalent)
- Maven

### Build And Run Instructions
Compile and package up the simulator:
```
$ mvn clean package
$ java -jar F:\workspace\LimitOrderBookSimulator\target\LimitOrderBookSimulator-1.0-SNAPSHOT-jar-with-dependencies.jar 
```

### For example
```
##### Limit Order Book Simulator #####

Enter order or type 'exit' to exit the simulator:
B 100 9.9

Below order was placed:
        Order{Created At=20052542440667, Side='Buy', Quantity=100, Price=9.900}
Trades executed:
Market Depth:
     Buy               |     Sell
     100@9.900         |


Enter order or type 'exit' to exit the simulator:
B 1000 10

Below order was placed:
        Order{Created At=20058672596030, Side='Buy', Quantity=1000, Price=10.000}
Trades executed:
Market Depth:
     Buy                 |     Sell
     1000@10.000         |
     100@9.900           |


Enter order or type 'exit' to exit the simulator:
S 100 10.1

Below order was placed:
        Order{Created At=20067446846357, Side='Sell', Quantity=100, Price=10.100}
Trades executed:
Market Depth:
     Buy                 |     Sell
     1000@10.000         |     100@10.100
     100@9.900           |


Enter order or type 'exit' to exit the simulator:
S 50 9.9

Below order was placed:
        Order{Created At=20074713803007, Side='Sell', Quantity=50, Price=9.900}
Trades executed:
     50@10.000
Market Depth:
     Buy                |     Sell
     950@10.000         |     100@10.100
     100@9.900          |


Enter order or type 'exit' to exit the simulator:
exit
```