package uppgift;

import java.math.BigDecimal;
import java.util.Date;

public class paymentReciever implements PaymentReceiver {
    @Override
    public void startPaymentBundle(String accountNumber, Date paymentDate, String currency) {
        System.out.println("\nStarting Payment...");
        System.out.println("Account number: " + accountNumber);
        System.out.println("Payment date: " + paymentDate);
        System.out.println("Using " + currency + " as currency.");
    }

    @Override
    public void payment(BigDecimal amount, String reference) {
        System.out.println("\nAmount: " + amount);
        System.out.println("Reference: " + reference);

    }

    @Override
    public void endPaymentBundle() {
        System.out.println("\nEnding bundle.");
    }
}
