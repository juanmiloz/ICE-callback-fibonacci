import Talker.CallbackPrx;
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

            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Callback");
            com.zeroc.Ice.Object object = new CallbackI();
            com.zeroc.Ice.ObjectPrx objPrx = adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("callback"));
            adapter.activate();
            Talker.CallbackPrx callbackPrx = Talker.CallbackPrx.uncheckedCast(objPrx);

            if (printer == null) {
                throw new Error("Invalid proxy");
            }

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String hostname = "";

                InetAddress iaLocal;
                iaLocal = InetAddress.getLocalHost();
                hostname = iaLocal.getHostName();

                printer.registerHost(hostname, callbackPrx);

                String finalHostname = hostname;

                if (args.length == 0) {
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        System.out.println("Shutting down...");
                        System.out.println(printer.logout(finalHostname));
                    }));
                    menu(printer, hostname, br, callbackPrx);
                }
                test(printer, hostname, args[0], callbackPrx);
                communicator.shutdown();
                br.close();
            } catch (IOException io) {
                System.out.println(io.getMessage());
            }
        }
    }

    private static void test(PrinterPrx printer, String hostname, String arg, CallbackPrx callbackPrx) {
        String request = hostname + "<-" + "Fib:"+ arg;
        printer.printString(request, callbackPrx);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void menu(Talker.PrinterPrx printer, String hostname, BufferedReader br, CallbackPrx callbackPrx) throws IOException {
        String input = "";
        while (true) {
            printMenu();
            input = br.readLine();
            String request = hostname + "<-";
            switch (input) {
                case "1":
                    System.out.println("Ingrese el número del fibonacci que desea calcular");
                    request += "Fib:" + br.readLine();
                    printer.printString(request, callbackPrx);
                    break;
                case "2":
                    request += "Register:" + hostname;
                    printer.printString(request, callbackPrx);
                    break;
                case "3":
                    System.out.println("Ingrese el nombre del host al que desea enviar el mensaje");
                    String host = br.readLine();
                    System.out.println("Ingrese el mensaje que desea enviar");
                    String msg = br.readLine();
                    request += "To " + host + ":" + msg;
                    printer.printString(request, callbackPrx);
                    break;
                case "4":
                    System.out.println("Ingrese el mensaje que desea enviar");
                    String msgTemp = br.readLine();
                    request += "BC" + ":" + msgTemp;
                    printer.printString(request, callbackPrx);
                    break;
                case "5":
                    request += "List clients";
                    printer.printString(request, callbackPrx);
                    break;
                case "6":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
            System.out.println("Pulse cualquier tecla para continuar...");
            br.readLine();
        }
    }

    private static void printMenu() {
        System.out.println("Type the number of the option you want to choose:");
        System.out.println("1. Print fibonacci");
        System.out.println("2. Register");
        System.out.println("3. Send message");
        System.out.println("4. Send message to all");
        System.out.println("5. Print all clients registered");
        System.out.println("6. Exit");
        System.out.print(">");
    }
}