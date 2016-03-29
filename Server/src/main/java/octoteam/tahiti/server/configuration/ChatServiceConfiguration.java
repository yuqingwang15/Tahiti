package octoteam.tahiti.server.configuration;

import com.google.common.base.MoreObjects;

/**
 * 包括服务端监听的ip地址以及端口<br><br>
 * host : String<br>
 * port : int<br>
 */
public class ChatServiceConfiguration {

    private String bindHost;

    private int bindPort;

    public String getBindHost() {
        return bindHost;
    }

    public void setBindHost(String bindHost) {
        this.bindHost = bindHost;
    }

    public int getBindPort() {
        return bindPort;
    }

    public void setBindPort(int bindPort) {
        this.bindPort = bindPort;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bindHost", bindHost)
                .add("bindPort", bindPort)
                .toString();
    }
}

