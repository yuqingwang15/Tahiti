package octoteam.tahiti.client.ui;

import com.google.common.eventbus.EventBus;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import octoteam.tahiti.client.event.UIOnLoginCommandEvent;

import java.io.IOException;
import java.util.Arrays;

public class Renderer {

    private Screen screen;

    private UtopiaMultiWindowTextGUI gui;

    private EventBus eventBus = new EventBus();

    private Store store = new Store();

    public Renderer() throws IOException, InterruptedException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        gui = new UtopiaMultiWindowTextGUI(new SeparateTextGUIThread.Factory(), screen);
        gui.setEOFWhenNoWindows(false);
        ((AsynchronousTextGUIThread) gui.getGUIThread()).start();

        initLoginDialog();
        initLoginStateDialog();
    }

    private void initLoginDialog() {
        Panel panel = new Panel();
        panel.setLayoutManager(
                new GridLayout(2)
                        .setBottomMarginSize(1)
                        .setTopMarginSize(1)
                        .setLeftMarginSize(3)
                        .setRightMarginSize(3)
        );

        panel.addComponent(new Label("Username"));
        TextBox txtUsername = new TextBox().addTo(panel);

        panel.addComponent(new Label("Password"));
        TextBox txtPassword = new TextBox().addTo(panel);

        panel.addComponent(new EmptySpace(new TerminalSize(0, 0)));

        new Button("Login", () -> {
            eventBus.post(new UIOnLoginCommandEvent(txtUsername.getText(), txtPassword.getText()));
        }).addTo(panel);

        BasicWindow dialog = new BasicWindow("Login to Tahiti");
        dialog.setComponent(panel);
        dialog.setHints(Arrays.asList(Window.Hint.CENTERED));

        store.init("login.dialog.visible", false);
        store.observe("login.dialog.visible", v -> {
            gui.addOrRemoveWindow((Boolean) v, dialog);
            gui.updateScreen();
            return null;
        });
    }

    private void initLoginStateDialog() {
        Panel panel = new Panel();
        panel.setLayoutManager(
                new GridLayout(1)
                        .setBottomMarginSize(1)
                        .setTopMarginSize(1)
                        .setLeftMarginSize(3)
                        .setRightMarginSize(3)
        );

        Label label = new Label("");
        panel.addComponent(label);
        BasicWindow dialog = new BasicWindow();
        dialog.setComponent(panel);
        dialog.setHints(Arrays.asList(Window.Hint.CENTERED));

        store.init("login.statedialog.visible", false);
        store.init("login.statedialog.text", "");
        store.observe("login.statedialog.text", v -> {
            label.setText((String) v);
            return null;
        });
        store.observe("login.statedialog.visible", v -> {
            gui.addOrRemoveWindow((Boolean) v, dialog);
            gui.updateScreen();
            return null;
        });
    }

    public void shutdown() throws IOException {
        AsynchronousTextGUIThread guiThread = (AsynchronousTextGUIThread) gui.getGUIThread();
        guiThread.stop();
        screen.stopScreen();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void actionShowMessageDialog(String title, String text, MessageDialogButton... buttons) {
        MessageDialog.showMessageDialog(gui, title, text, buttons);
    }

    public void actionShowLoginDialog() {
        store.update(() -> {
            store.put("login.dialog.visible", true);
        });
    }

    public void actionHideLoginDialog() {
        store.update(() -> {
            store.put("login.dialog.visible", false);
        });
    }

    public void actionShowLoginStateDialog(String text) {
        store.update(() -> {
            store.put("login.statedialog.text", text);
            store.put("login.statedialog.visible", true);
        });
    }

    public void actionHideLoginStateDialog() {
        store.update(() -> {
            store.put("login.statedialog.visible", false);
        });
    }

}
