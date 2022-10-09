import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Fibonacci {
    private static HashMap<Integer, BigInteger> fibonacciValues;

    public Fibonacci() {
        fibonacciValues = new HashMap<>();
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
        }
        return fibonacciValues.get(n);
    }

    private BigInteger fibonacciFun(int n){
        if (n == 0) {
            fibonacciValues.put(n, BigInteger.ZERO);
            return BigInteger.ZERO;
        }
        if (n == 1) {
            fibonacciValues.put(n, BigInteger.ONE);
            return BigInteger.ONE;
        }

        if (fibonacciValues.containsKey(n)) {
            return fibonacciValues.get(n);
        }

        BigInteger n1 = fibonacciFun(n - 1);
        BigInteger n2 = fibonacciFun(n - 2);

        fibonacciValues.put(n, n1.add(n2));

        return fibonacciValues.get(n);
    }
}
