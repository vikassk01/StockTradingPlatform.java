import java.util.*;
class Stock {
    private String symbol;
    private double price;
    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
    public String getSymbol() {
        return symbol;
    }
    public double getPrice() {
        return price;
    }
    public void updatePrice() {
        Random rand = new Random();
        double change = (rand.nextDouble() - 0.5) * 5; // Random price fluctuation
        this.price += change;
        if (this.price < 1) this.price = 1; // Ensuring stock price doesn't go negative
    }
    @Override
    public String toString() {
        return symbol + ": Rs" + String.format("%.2f", price);
    }
}
class Portfolio {
    private Map<String, Integer> holdings;
    private double balance;
    public Portfolio(double initialBalance) {
        this.holdings = new HashMap<>();
        this.balance = initialBalance;
    }
    public double getBalance() {
        return balance;
    }
    public void buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cost > balance) {
            System.out.println("Insufficient funds to buy " + quantity + " shares of " + stock.getSymbol());
            return;
        }
        holdings.put(stock.getSymbol(), holdings.getOrDefault(stock.getSymbol(), 0) + quantity);
        balance -= cost;
        System.out.println("Bought " + quantity + " shares of " + stock.getSymbol());
    }
    public void sellStock(Stock stock, int quantity) {
        if (!holdings.containsKey(stock.getSymbol()) || holdings.get(stock.getSymbol()) < quantity) {
            System.out.println("Not enough shares to sell!");
            return;
        }
        holdings.put(stock.getSymbol(), holdings.get(stock.getSymbol()) - quantity);
        balance += stock.getPrice() * quantity;
        if (holdings.get(stock.getSymbol()) == 0) {
            holdings.remove(stock.getSymbol());
        }
        System.out.println("Sold " + quantity + " shares of " + stock.getSymbol());
    }
    public void displayPortfolio(Map<String, Stock> market) {
        System.out.println("\nPortfolio:");
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            Stock stock = market.get(entry.getKey());
            double stockValue = stock.getPrice() * entry.getValue();
            System.out.println(entry.getKey() + " - " + entry.getValue() + " shares | Value: Rs" + String.format("%.2f", stockValue));
        }
        System.out.println("Cash Balance: Rs" + String.format("%.2f", balance));
    }
}
class Market {
    private Map<String, Stock> stocks;
    public Market() {
        this.stocks = new HashMap<>();
        stocks.put("AAPL", new Stock("AAPL", 150.00));
        stocks.put("GOOGL", new Stock("GOOGL", 2800.00));
        stocks.put("TSLA", new Stock("TSLA", 700.00));
        stocks.put("AMZN", new Stock("AMZN", 3500.00));
    }
    public void updateMarket() {
        for (Stock stock : stocks.values()) {
            stock.updatePrice();
        }
    }
    public void displayMarket() {
        System.out.println("\nMarket Prices:");
        for (Stock stock : stocks.values()) {
            System.out.println(stock);
        }
    }
    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }
    public Map<String, Stock> getStocks() {
        return stocks;
    }
}
public class StockTradingApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Market market = new Market();
        Portfolio portfolio = new Portfolio(10000.00);
        while (true) {
            market.updateMarket();
            market.displayMarket();
            portfolio.displayPortfolio(market.getStocks());
            System.out.println("\nOptions:");
            System.out.println("1. Buy Stock");
            System.out.println("2. Sell Stock");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                System.out.print("Enter stock symbol to buy: ");
                String symbol = scanner.next().toUpperCase();
                Stock stock = market.getStock(symbol);
                if (stock == null) {
                    System.out.println("Invalid stock symbol!");
                    continue;
                }
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                portfolio.buyStock(stock, quantity);
            } else if (choice == 2) {
                System.out.print("Enter stock symbol to sell: ");
                String symbol = scanner.next().toUpperCase();
                Stock stock = market.getStock(symbol);
                if (stock == null) {
                    System.out.println("Invalid stock symbol!");
                    continue;
                }
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                portfolio.sellStock(stock, quantity);
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }
        scanner.close();
    }
}
