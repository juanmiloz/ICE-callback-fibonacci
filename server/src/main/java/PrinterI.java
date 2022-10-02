
import java.math.BigInteger;
import java.util.HashMap;

public class PrinterI implements Demo.Printer {
    private static HashMap<Integer, BigInteger> fibonacciValues;

    public PrinterI() {
        fibonacciValues = new HashMap<>();
    }

    public String printString(String s, com.zeroc.Ice.Current current) {

        String [] arr = s.split(":");

        if (arr[1].trim().matches("\\w*[0-9]+\\w*")) {
            int num = Integer.parseInt(arr[1].trim());
            String fibonacci = fibonacci(num);
            BigInteger fibonacciN = fibonacciHandler(num);
            StringBuilder response = new StringBuilder();
            for (int i = 0; i <= num; i++) {
                response.append(fibonacciHandler(i)).append("\n");
            }
            System.out.println(arr[0]+": " + response);
            return fibonacciN.toString();
        } else {
            System.out.println(s);
            return "The string is not a number";
        }
    }

    private static String fibonacci(int n){
        int a = 0, b = 1, c;
        String fibonacci = "";

        for(int i = 0; i < n; i ++){
            if(a > 0){
                fibonacci += (i != n-1)?a + ", ":a;
            }else{
                fibonacci += ", Ha llegado al maximo valor posible previo al desbordamiento";
                i = n;
            }
            c = a + b;
            a = b;
            b = c;
        }
        return fibonacci;
    }

    private BigInteger fibonacciHandler(int n) {
        try {
            fibonacciFun(n);
        }catch (StackOverflowError e){
            fibonacciHandler(n-1000);
            fibonacciHandler(n);
        }
        return fibonacciValues.get(n);
    }

    private BigInteger fibonacciFun(int n) {
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

        fibonacciValues.put(n, fibonacciFun(n - 1).add(fibonacciFun(n - 2)));

        return fibonacciValues.get(n);
    }
}