
import Talker.CallbackPrx;
import com.zeroc.Ice.Current;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


public class PrinterI implements Talker.Printer {
    private static HashMap<Integer, BigInteger> fibonacciValues;
    private static HashMap<String, CallbackPrx> registerHosts;
    private final ExecutorService pool;
    private final Semaphore semaphore;

    public PrinterI() {
        pool = Executors.newCachedThreadPool();
        semaphore = new Semaphore(1);
        fibonacciValues = new HashMap<>();
        registerHosts = new HashMap<>();
    }

    public void printString(String s, CallbackPrx cl, Current current) {
        pool.execute(new Thread(() -> {

            String[] arr = s.split("<-");


            System.out.println(arr[0].trim());

            if (arr[1].trim().startsWith("register")) {
                String host = arr[1].split(":")[1];
                registerHost(host, cl, current);
            }

            if (arr[1].trim().startsWith("list clients")) {
                listClients(cl);
            }

            if (arr[1].trim().startsWith("to")) {
                String[] split1 = arr[1].split(":");
                String hostname = split1[0].trim().split(" ")[1];

                String message = split1[1].trim();
                sendMessage(hostname, message, cl);
            }

            if (arr[1].trim().startsWith("BC")) {
                String message = arr[1].split(":")[1];
                broadcast(message, cl);
            }

            if (arr[1].trim().startsWith("fib")) {
                String num = arr[1].trim().split(":")[1];
                fibonacciResponse(num, cl);
            }
        }));
    }

    private void listClients(CallbackPrx cl) {
        StringBuilder hosts = new StringBuilder();
        for (String hostname : registerHosts.keySet()) {
            hosts.append(hostname);
        }
        cl.response(hosts.toString());
    }

    private void sendMessage(String hostname, String message, CallbackPrx cl) {
        System.out.println("To:" + hostname);
        if (registerHosts.containsKey(hostname)) {
            registerHosts.get(hostname).response(message);
            cl.response("Mensaje enviado con exito");
        } else {
            cl.response("El hostname que busca no se encuentra disponible");
        }
    }

    private void broadcast(String message, CallbackPrx cl) {
        for (CallbackPrx callback : registerHosts.values()) {
            callback.response(message);
        }
        cl.response("El mensaje fue difundido con exito");
    }

    private void fibonacciResponse(String s, CallbackPrx cl) {

        if (s.matches("\\w*[0-9]+\\w*")) {
            int num = Integer.parseInt(s);

            if (fibonacciValues.containsKey(num)) {
                cl.response(fibonacciValues.get(num).toString());
            } else {
                try {
                    semaphore.acquire();
                    BigInteger fibonacciN = fibonacciHandler(num);
                    cl.response(fibonacciValues.get(num).toString());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                semaphore.release();
            }


        } else {
            System.out.println(s);
            cl.response("La cadena ingresada no es un número");
        }
    }


    public void registerHost(String s, CallbackPrx cl, Current current) {
        if (!registerHosts.containsKey(s)) {
            registerHosts.put(s, cl);
        } else {
            cl.response("Ya se encuentra registrado");
        }
        cl.response("Fue registrado con exito");
    }


    private static String fibonacci(int n) {
        int a = 0, b = 1, c;
        String fibonacci = "";

        for (int i = 0; i < n; i++) {
            if (a > 0) {
                fibonacci += (i != n - 1) ? a + ", " : a;
            } else {
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
        } catch (StackOverflowError e) {
            fibonacciHandler(n - 1000);
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