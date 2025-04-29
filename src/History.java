import java.util.ArrayList;
import java.util.List;
public class History {
    private List<String> logs;

    public History() {
        this.logs = new ArrayList<>();
    }

    public void addLog(int transactionNumber, List<Product> items, int totalPrice) {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction #").append(transactionNumber).append(":\n");
        for (Product item : items) {
            sb.append(" - ").append(item.getName())
              .append(" x ").append(item.getQuantity())
              .append(" @ ").append(item.getPrice())
              .append(" = ").append(item.getQuantity() * item.getPrice()).append(" THB\n");
        }
        sb.append("Total: ").append(totalPrice).append(" THB\n");
        sb.append("----------------------");
        logs.add(sb.toString());
    }

    public List<String> getLogs() {
        return logs;
    }

    public void printHistory() {
        if (logs.isEmpty()) {
            System.out.println("No transaction history.");
        } else {
            for (String log : logs) {
                System.out.println(log);
            }
        }
    }
}

