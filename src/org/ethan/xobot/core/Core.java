package org.ethan.xobot.core;


import org.ethan.xobot.data.Server;
import org.ethan.xobot.handlers.XOBotScriptList;
import org.ethan.xobot.handlers.XOBotScriptRip;
import org.ethan.xobot.handlers.XOBotServerRip;

import java.util.Map;

public class Core {

    /**
     * Use any registered username & password
     * <p>
     * Select your output directory & handler
     * <p>
     * Run & let it do rest...
     *
     * MAKE SURE ACCOUNT HAS SCRIPTS TO RIP BEFORE YOU RUN, DUMBASSES.
     */
    private final String username = "";
    private final String password = "";
    private final String outputDir = "C:\\Users\\itset\\Desktop\\Parabot Scripting\\Outputs";
    private boolean ripServer = false;
    private boolean ripScripts = true;

    public Core() {
        System.out.println("XOBotRipper has been started.");

        if (ripServer) {
            ripServer(Server.ALORA);
        }
        if (ripScripts) {
            ripScripts(Server.IKOV);
        }

    }

    public static void main(String[] args) {
        new Core();
    }

    private void ripServer(Server server) {
        new XOBotServerRip(username, password, server.getServerHash(), server.getName(), outputDir);
    }

    private void ripScripts(Server server) {
        for (Map.Entry<Integer, String> entry : getScriptList(server).entrySet()) {
            new XOBotScriptRip(username, password, entry.getKey(), entry.getValue(), outputDir);
        }
    }

    private Map<Integer, String> getScriptList(Server server) {
        return new XOBotScriptList().scriptMap(username, password, server);
    }
}
