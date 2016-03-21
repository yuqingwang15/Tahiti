package octoteam.tahiti.client.ui;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import octoteam.tahiti.client.TahitiClient;
import octoteam.tahiti.client.event.ConnectErrorEvent;
import octoteam.tahiti.client.event.ConnectedEvent;
import octoteam.tahiti.client.event.UIOnLoginCommandEvent;
import octoteam.tahiti.protocol.SocketMessageProtos.Message;

public class Reactor {

    private TahitiClient client;

    private Renderer renderer;

    private String loginUsername;
    private String loginPassword;

    public Reactor(TahitiClient client, Renderer renderer) {
        this.client = client;
        this.renderer = renderer;
    }

    void login() {
        client.login(loginUsername, loginPassword, (msg) -> {
            renderer.hideLoginStateDialog();
            if (msg.getStatus() == Message.StatusCode.PASSWORD_INCORRECT) {
                renderer.showMessageDialog("Login failed", "Incorrect password");
            } else if (msg.getStatus() == Message.StatusCode.USERNAME_NOT_FOUND) {
                renderer.showMessageDialog("Login failed", "Username not found");
            } else if (msg.getStatus() == Message.StatusCode.SUCCESS) {
                renderer.showMessageDialog("Login success", "Success!");
                renderer.hideLoginDialog();
            }
            return null;
        });
    }

    @Subscribe
    public void onConnected(ConnectedEvent event) {
        renderer.showLoginStateDialog("Log in...");
        try {
            login();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onConnectError(ConnectErrorEvent event) {
        renderer.hideLoginStateDialog();
        renderer.showMessageDialog("Login failed", "Cannot connect to server");
    }

    @Subscribe
    public void onLoginCommand(UIOnLoginCommandEvent event) {
        loginUsername = event.getUsername();
        loginPassword = event.getPassword();
        if (client.isConnected()) {
            login();
        } else {
            client.connectAsync();
        }
        renderer.showLoginStateDialog("Connecting to server...");
    }

}
