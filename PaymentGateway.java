import java.util.*;

class PaymentGateway {
    private Map<String, Set<String>> clients; // Stores clients and their supported paymodes
    private Map<Bank, Double> banks; // Stores banks and their success percentages

    public PaymentGateway() {
        clients = new HashMap<>();
        banks = new HashMap<>();
    }

    public void addClient(String client) {
        clients.put(client, new HashSet<>());
    }

    public void removeClient(String client) {
        clients.remove(client);
    }

    public boolean hasClient(String client) {
        return clients.containsKey(client);
    }

    public Set<String> listSupportedPaymodes(String client) {
        if (client == null) {
            return new HashSet<>(clients.values());
        } else {
            return clients.getOrDefault(client, new HashSet<>());
        }
    }

    public void addSupportForPaymode(String client, String paymode) {
        Set<String> supportedPaymodes = clients.getOrDefault(client, new HashSet<>());
        supportedPaymodes.add(paymode);
        clients.put(client, supportedPaymodes);
    }

    public void removePaymode(String paymode) {
        for (Set<String> supportedPaymodes : clients.values()) {
            supportedPaymodes.remove(paymode);
        }
    }

    public Map<Bank, Double> showDistribution() {
        return banks;
    }

    public boolean makePayment(String client, String paymode, Map<String, Object> paymentDetails) {
        if (!clients.containsKey(client) || !clients.get(client).contains(paymode)) {
            return false;
        }

        // Check if bank supports the payment mode
        Bank bank = getBankForPaymode(paymode);
        if (bank == null) {
            return false;
        }

        // Perform the payment using the bank
        boolean success = processPaymentWithBank(bank, paymentDetails);
        return success;
    }

    private Bank getBankForPaymode(String paymode) {
        for (Bank bank : banks.keySet()) {
            if (bank.supportedPaymodes.contains(paymode)) {
                return bank;
            }
        }
        return null;
    }

    private boolean processPaymentWithBank(Bank bank, Map<String, Object> paymentDetails) {
        // Mock bank's response (random success or failure)
        boolean success = new Random().nextBoolean();
        return success;
    }
}

class Bank {
    private String name;
    private double successPercentage;
    private Set<String> supportedPaymodes;

    public Bank(String name, double successPercentage) {
        this.name = name;
        this.successPercentage = successPercentage;
        this.supportedPaymodes = new HashSet<>();
    }

    public void addSupportedPaymode(String paymode) {
        supportedPaymodes.add(paymode);
    }

    public void removeSupportedPaymode(String paymode) {
        supportedPaymodes.remove(paymode);
    }
}

// Example usage
public class Main {
    public static void main(String[] args) {
        PaymentGateway pg = new PaymentGateway();

        // Create banks
        Bank hdfc = new Bank("HDFC", 0.3);
        Bank icici = new Bank("ICICI", 0.7);
        pg.banks.put(hdfc, hdfc.successPercentage);
        pg.banks.put(icici, icici.successPercentage);

        // Add clients
        pg.addClient("Flipkart");

        // Set supported paymodes for clients
        pg.addSupportForPaymode("Flipkart", "UPI");
        pg.addSupportForPaymode("Flipkart", "Credit/Debit Card");
        pg.addSupportForPaymode("Flipkart", "Netbanking");

        // Make a payment
        Map<String, Object> paymentDetails = new HashMap<>();
        paymentDetails.put("paymode", "Credit/Debit Card");
        Map<String, Object> cardDetails = new HashMap<>();
        cardDetails.put("number", "1234567890123456");
        cardDetails.put("expiry", "12/24");
        cardDetails.put("cvv", "123");
        paymentDetails.put("cardDetails", cardDetails);
        boolean success = pg.makePayment("Flipkart", (String) paymentDetails.get("paymode"), paymentDetails);
        System.out.println("Payment success: " + success);
    }
}
