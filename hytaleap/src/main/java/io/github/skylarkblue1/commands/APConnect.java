package io.github.skylarkblue1.commands;

import io.github.skylarkblue1.archipelagoclient.APClient;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;

import io.github.skylarkblue1.events.APEventListener;

import java.util.concurrent.CompletableFuture;

public class APConnect extends AbstractCommand {

    private final RequiredArg<String> domainArg;
    private final RequiredArg<Integer> portArg;
    private final RequiredArg<String> slotArg;

    public static String slot;

    public APClient apClient;

    public APConnect() {
        super("apconnect", "Connect to an Archipelago server");

        domainArg = withRequiredArg("domain", "The domain part of the URL. Eg: archipelago.gg", ArgTypes.STRING);
        portArg   = withRequiredArg("port", "The port number for the Archipelago server", ArgTypes.INTEGER);
        slotArg   = withRequiredArg("slot", "Your slot's name. Make sure it is your Hytale slot", ArgTypes.STRING);
    }

    @Override
    protected CompletableFuture<Void> execute(CommandContext cont) {
        String domain = cont.get(domainArg);
        int port = cont.get(portArg);
        slot = cont.get(slotArg);

        // Check if already connected, if yes disconnect
        if (apClient != null && apClient.isConnected()) {
            cont.sendMessage(Message.raw("[AP] Already connected. Disconnecting first..."));
            apClient.disconnect();
            apClient = null;
        }

        apClient = new APClient();
        apClient.setName(slot);
        apClient.setGame("Hytale");

        // Register AP event listener for incoming messages
        apClient.getEventManager().registerListener(new APEventListener(apClient));

        // Subscribe the connecting player to AP messages
        if (cont.isPlayer()) {
            apClient.addSubscriber(cont.sender());
        }

        try {
            apClient.connect(domain + ":" + port);
            cont.sendMessage(Message.raw("[AP] Connection initiated! Use /apdisconnect to disconnect."));
        } catch (Exception e) {
            cont.sendMessage(Message.raw("[AP] Failed to connect: " + e.getMessage()));
        }

        return null;
    }
}