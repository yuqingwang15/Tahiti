package octoteam.tahiti.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Logging {
    public static Logger logger = LogManager.getLogger(Logging.class.getName());

    private static int successfulLoginTimes = 0;
    private static int failedLoginTimes = 0;
    private static int sendMessageTimes = 0;


    //constructor
    public Logging() {

        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(() -> loggingForClient(),
                3, 3, TimeUnit.SECONDS);

    }

    public static void addLoginSucceededTimes() {

        successfulLoginTimes++;
    }

    public static void addLoginFailedTimes() {

        failedLoginTimes++;
    }

    public static void addSendMeassageTimes() {

        sendMessageTimes++;
    }

//    @Subscribe
//    public void onLoginSucceeded(ConnectedEvent event) {
//        successfulLogin++;
//    }
//
//    @Subscribea
//    private void onLoginFailed(DisconnectedEvent event) {
//        failedLogin++;
//    }

    public void loggingForClient() {
        Logging.logger.info("client successful login :  " + successfulLoginTimes + " times");
        Logging.logger.info("client failed login :  " + failedLoginTimes + " times");
        Logging.logger.info("client sent messages :  " + sendMessageTimes + " times");
        Logging.logger.info("--------------");
    }

}
