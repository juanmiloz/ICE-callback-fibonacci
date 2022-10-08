
import Talker.CallbackPrx;
import com.zeroc.Ice.Current;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;


public class PrinterI implements Talker.Printer {
    private static HashMap<String, CallbackPrx> registerHosts;
    private final ThreadPoolExecutor pool;
    private final Fibonacci fibonacci;
    private final Semaphore semaphore;

    public PrinterI() {
        pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        semaphore = new Semaphore(1);
        fibonacci = new Fibonacci(semaphore);
        registerHosts = new HashMap<>();
        System.out.println("Server started");
        System.out.println("Thread number active = " + pool.getActiveCount());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            pool.shutdown();
        }));
    }

    public void printString(String s, CallbackPrx cl, Current current) {
        pool.execute(new Thread(() -> {
            Long startTime = System.currentTimeMillis();
            String[] arr = s.split("<-");

            System.out.println(arr[0].trim());

            if (arr[1].trim().startsWith("Register")) {
                String host = arr[1].split(":")[1];
                registerHost(host, cl, current);
            }

            if (arr[1].trim().startsWith("List clients")) {
                listClients(cl);
            }

            if (arr[1].trim().startsWith("To")) {
                String[] split1 = arr[1].split(":");
                String hostname = split1[0].trim().split(" ")[1];

                String message = split1[1].trim();
                sendMessage(hostname, message, cl);
            }

            if (arr[1].trim().startsWith("BC")) {
                String message = arr[1].split(":")[1];
                broadcast(message, cl);
            }

            if (arr[1].trim().startsWith("Fib")) {
                String num = arr[1].trim().split(":")[1];
                fibonacciResponse(num, cl);
            }

            Long endTime = System.currentTimeMillis();
            System.out.println("start time = " +startTime + " " + "ms");
            System.out.println("end time =" + endTime + " " + "ms");
            System.out.println("Response time: " + (endTime - startTime) + " ms");
            Thread thread = Thread.currentThread();
            System.out.println("Thread " + thread.getId() + " finished");
            System.out.println("thread number" + pool.getActiveCount());
        }));
    }

    private void listClients(CallbackPrx cl) {
        String hosts = "";
        hosts = String.join("\n", registerHosts.keySet());
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

            BigInteger fibonacciN = fibonacci.getFibonacci(num);
            cl.response(fibonacciN.toString());
        } else {
            System.out.println(s);
            cl.response("La cadena ingresada no es un número");
        }
    }


    public void registerHost(String s, CallbackPrx cl, Current current) {
        if (!registerHosts.containsKey(s)) {
            try {
                semaphore.acquire();
                registerHosts.put(s, cl);
                semaphore.release();
                cl.response("El host se ha registrado con exito");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            cl.response("Ya se encuentra registrado");
        }
    }

    public String logout(String s, Current current) {
        if (!registerHosts.containsKey(s)) {
            return "No se encuentra registrado";
        }

        try {
            semaphore.acquire();
            registerHosts.remove(s);
            semaphore.release();
            System.out.println("El host "+s+" se ha desconectado con exito");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Se ha desconectado con exito";
    }
}