import com.zeroc.Ice.Current;

import java.math.BigInteger;

public class PrinterI implements Talker.Printer {
    private final Fibonacci fibonacci;

    public PrinterI() {
        fibonacci = new Fibonacci();
        System.out.println("Server started");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
        }));
    }

    public String printString(String s, Current current) {
        Long startTime = System.currentTimeMillis();
        String[] arr = s.split("<-");

        String response = fibonacciResponse(arr[1].trim());

        Long endTime = System.currentTimeMillis();

        System.out.println("-> Requested by: " + arr[0].trim());
        System.out.println("-> Start time = " + startTime + " " + "ms");
        System.out.println("-> End time =" + endTime + " " + "ms");
        System.out.println("-> Response time: " + (endTime - startTime) + " ms");

        return response;
    }
    private String fibonacciResponse(String s) {
        System.out.println("Fibonacci of " + s);

        if (s.matches("\\w*[0-9]+\\w*")) {
            int num = Integer.parseInt(s);

            BigInteger fibonacciN = fibonacci.getFibonacci(num);
            return fibonacciN.toString();
        } else {
            System.out.println(s);
            return "La cadena ingresada no es un número";
        }
    }
}