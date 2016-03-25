package octoteam.tahiti.server;

import com.google.common.eventbus.Subscribe;
import octoteam.tahiti.server.event.LoginAttemptEvent;
import octoteam.tahiti.server.event.MessageEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Logging {

    private static final Logger logger = LogManager.getLogger(Logging.class.getName());

    private  int validLoginTimes = 0;
    private  int invalidLoginTimes = 0;
    private  int receivedMessageTimes = 0;
    private  int ignoredMessageTimes = 0;

    //constructor
    public Logging(){

        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(this::loggingForServer,
                60*1000, 60*1000, TimeUnit.MILLISECONDS);

    }

    //count valid/invalid login
    @Subscribe
    public void onLoginAttempt(LoginAttemptEvent event) {
        if (event.getSuccess()) {
            validLoginTimes++;
        } else {
            invalidLoginTimes++;
        }

    }

    //count received/ignored message
    @Subscribe
    private void onMessage(MessageEvent event) {
        if (event.isAuthenticated()) {
            receivedMessageTimes++;
        } else {
            ignoredMessageTimes++;
        }
    }

    //log into server.log and show in console
    private void loggingForServer() {
        Logging.logger.info("server valid login :  " + validLoginTimes + " times");
        Logging.logger.info("server invalid login :  " + invalidLoginTimes + " times");
        Logging.logger.info("server received message :  " + receivedMessageTimes + " times");
        Logging.logger.info("server ignored message :  " + ignoredMessageTimes + " times");
        Logging.logger.info("--------------");
    }


}
