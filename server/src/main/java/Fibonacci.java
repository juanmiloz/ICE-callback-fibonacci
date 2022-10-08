import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Fibonacci {
    private static HashMap<Integer, BigInteger> fibonacciValues;
    private final Semaphore semaphore;

    public Fibonacci(Semaphore semaphore) {
        fibonacciValues = new HashMap<>();
        this.semaphore = semaphore;
    }

    public BigInteger getFibonacci(int n){
        return fibonacciHandler(n);
    }

    private BigInteger fibonacciHandler(int n) {
        try {
            fibonacciFun(n);
        } catch (StackOverflowError e) {
            fibonacciHandler(n - 1000);
            fibonacciHandler(n);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error with semaphore at fibonacciHandler");
        }
        return fibonacciValues.get(n);
    }

    private BigInteger fibonacciFun(int n) throws InterruptedException {
        if (n == 0) {
            semaphore.acquire();
            fibonacciValues.put(n, BigInteger.ZERO);
            semaphore.release();
            return BigInteger.ZERO;
        }
        if (n == 1) {
            semaphore.acquire();
            fibonacciValues.put(n, BigInteger.ONE);
            semaphore.release();
            return BigInteger.ONE;
        }

        if (fibonacciValues.containsKey(n)) {
            return fibonacciValues.get(n);
        }

        BigInteger n1 = fibonacciFun(n - 1);
        BigInteger n2 = fibonacciFun(n - 2);

        semaphore.acquire();
        fibonacciValues.put(n, n1.add(n2));
        semaphore.release();
        return fibonacciValues.get(n);
    }
}
