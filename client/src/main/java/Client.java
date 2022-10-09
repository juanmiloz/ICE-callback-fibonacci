import Talker.PrinterPrx;

import java.io.*;
import java.net.InetAddress;


public class Client {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client", extraArgs)) {
            Talker.PrinterPrx twoWay = Talker.PrinterPrx.checkedCast(
                    communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);
            Talker.PrinterPrx printer = twoWay.ice_twoway();

            if (printer == null) {
                throw new Error("Invalid proxy");
            }

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String hostname = "";

                InetAddress iaLocal;
                iaLocal = InetAddress.getLocalHost();
                hostname = iaLocal.getHostName();

                if (args.length == 0) {
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        System.out.println("Shutting down...");
                    }));
                    menu(printer, hostname, br);
                }
                test(printer, hostname, args[0]);
                communicator.shutdown();
                br.close();
            } catch (IOException io) {
                System.out.println(io.getMessage());
            }
        }
    }

    private static void test(PrinterPrx printer, String hostname, String arg) {
        String request = hostname + "<-"+ arg;
        System.out.println(printer.printString(request));
    }

    private static void menu(Talker.PrinterPrx printer, String hostname, BufferedReader br) throws IOException {
        String input = "";
        while (!input.equalsIgnoreCase("exit")) {
            System.out.println("Ingrese exit para terminar el programa");
            input = br.readLine();

            String request = hostname + "<-";

            System.out.println("Ingrese el número del fibonacci que desea calcular");
            request += "Fib:" + br.readLine();
            System.out.println(printer.printString(request));
        }
        br.readLine();
    }
}