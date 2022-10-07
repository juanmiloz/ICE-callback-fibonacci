import com.zeroc.Ice.Current;

public class CallbackI implements Talker.Callback{

    public void response(String msg, Current current) {
        System.out.println(msg);
    }
}
