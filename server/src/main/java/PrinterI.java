import java.util.Arrays;

public class PrinterI implements Demo.Printer
{
    public int printString(String s, com.zeroc.Ice.Current current) {

        String [] arr = s.split(":");

        if(arr[1].trim().matches("[0-9]+")){
            String fibonacci = fibonacci(Integer.parseInt(arr[1].trim()));
            String[] arrRes = fibonacci.split(",");
            System.out.println(fibonacci);
            return Integer.parseInt(arrRes[arrRes.length-1].trim());
        }else{
            System.out.println(s);
            return 0;
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
}