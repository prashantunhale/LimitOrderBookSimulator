package com.prashu;

import com.prashu.lob.LimitOrderTradeEngine;
import com.prashu.utils.UserInput;

import java.util.Scanner;

public class Simulator {
    public static void main(String[] args) {
        TradeEngine engine = new LimitOrderTradeEngine();
        Scanner scanner = new Scanner(System.in);
        startSimulator(engine, scanner);
    }

    public static void startSimulator(TradeEngine engine, Scanner scanner) {
        System.out.println("##### Limit Order Book Simulator #####\n");
        while(true){
            System.out.println("Enter order or type 'exit' to exit the simulator:");
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("exit")){
                break;
            }
            String inputValidation = UserInput.validateInput(input);
            if(!inputValidation.isEmpty()){
                System.out.println(inputValidation);
                continue;
            }
            String output = engine.executeOrder(UserInput.extractSide(input), UserInput.extractQuantity(input), UserInput.extractPrice(input));
            System.out.println(output);
        }
    }
}
