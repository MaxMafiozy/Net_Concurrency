/**
 * Created by Сергеев on 17.03.2017.
 */
public class PA implements Runnable {
    public static Channel  channel;
    public void run() {
                while (true) {
                     Runnable session = channel.take();
                      Thread thread = new Thread(session);
                        thread.start();
                    }
           }
    public PA(Channel channel){
        this.channel=channel;
    }
}
