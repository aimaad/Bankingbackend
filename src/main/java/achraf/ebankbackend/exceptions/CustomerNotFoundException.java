package achraf.ebankbackend.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String customerNotFound) {
        super(customerNotFound);
    }
}
