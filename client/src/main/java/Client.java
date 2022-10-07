import java.io.*;


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
            Talker.CallbackPrx callPrx = Talker.CallbackPrx.uncheckedCast(objPrx);

            if (printer == null) {
                throw new Error("Invalid proxy");
            }

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("./data/time.txt", true));
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String input = "", answer = "", hostname = "";
                hostname = f("hostname");

                printer.registerHost(hostname, callPrx);

                while (!input.equalsIgnoreCase("exit")) {
                    input = br.readLine();
                    answer = hostname + "<- " + input;
                    long start = System.currentTimeMillis();
                    printer.printString(answer, callPrx);
                    long end = System.currentTimeMillis();
                    String time = (end - start) + "";

                    bw.write("n = "  + "\n" + "Inicio: " + start + "\nDuración: " + time);
                }
                bw.close();
                br.close();
            } catch (IOException io) {
                System.out.println(io.getMessage());
            }

            communicator.waitForShutdown();
        }
    }

    public static String f(String m) {
        String str = null, output = "";

        InputStream s;
        BufferedReader r;

        try {
            Process p = Runtime.getRuntime().exec(m);

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((str = br.readLine()) != null) {
                output += str /*+ System.getProperty("line.separator")*/;
            }
            br.close();
        } catch (Exception ex) {
        }
        /*output.replaceAll("\n","");*/
        return output;
    }
}