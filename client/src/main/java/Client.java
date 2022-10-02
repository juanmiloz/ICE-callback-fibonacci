import java.io.*;


public class Client{
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args,"config.client",extraArgs)){
            //com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SimplePrinter:default -p 10000");
            Demo.PrinterPrx twoWay = Demo.PrinterPrx.checkedCast(
                    communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);
            //Demo.PrinterPrx printer = Demo.PrinterPrx.checkedCast(base);
            Demo.PrinterPrx printer = twoWay.ice_twoway();

            if(printer == null) {
                throw new Error("Invalid proxy");
            }

            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String input = "", answer = "", hostname = "";

                while(!input.equalsIgnoreCase("exit")){
                    hostname = f("hostname");
                    input = br.readLine();
                    answer = hostname +": "+ input;
                    String ans = printer.printString(answer);
                    System.out.println("Respuesta:"+ans);
                }

                br.close();
            }catch(IOException io){
                System.err.println("Ocurrio un error");
            }
        }
    }

    public static String f(String m){
        String str = null, output = "";

        InputStream s;
        BufferedReader r;

        try {
            Process p = Runtime.getRuntime().exec(m);

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((str = br.readLine()) != null){
                output += str /*+ System.getProperty("line.separator")*/;
            }
            br.close();
        }
        catch(Exception ex) {
        }
        /*output.replaceAll("\n","");*/
        return output;
    }
}