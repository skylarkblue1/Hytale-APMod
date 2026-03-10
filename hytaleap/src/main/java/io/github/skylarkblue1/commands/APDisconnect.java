package io.github.skylarkblue1.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;

import java.util.concurrent.CompletableFuture;

public class APDisconnect extends AbstractCommand {

    private final APConnect connectCommand;

    // This just cleanly disconnects from the archipelago server.

    public APDisconnect(APConnect connectCommand) {
        super("apdisconnect", "Disconnect from the Archipelago server");
        this.connectCommand = connectCommand;
    }

    @Override
    protected CompletableFuture<Void> execute(CommandContext cont) {
        if (connectCommand.apClient == null || !connectCommand.apClient.isConnected()) {
            cont.sendMessage(Message.raw("[AP] Not currently connected to Archipelago."));
            return null;
        }

        connectCommand.apClient.disconnect();
        cont.sendMessage(Message.raw("[AP] Disconnected from Archipelago."));

        return null;
    }
}