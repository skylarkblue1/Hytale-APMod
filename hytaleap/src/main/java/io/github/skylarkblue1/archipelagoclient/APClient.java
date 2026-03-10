package io.github.skylarkblue1.archipelagoclient;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import io.github.archipelagomw.Client;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class APClient extends Client {

    private final List<CommandSender> subscribers = new CopyOnWriteArrayList<>();

    // add... the archipelago stream? I think
    public void addSubscriber(CommandSender sender) {
        subscribers.add(sender);
    }

    // Send messages from the archipelago stream
    public void broadcastToSubscribers(String message) {
        Message msg = Message.raw(message);
        for (CommandSender sender : subscribers) {
            sender.sendMessage(msg);
        }
    }

    // Idk if this even works lmao
    @Override
    public void onError(Exception e) {
        System.err.println("[AP] Error: " + e.getMessage());
    }

    // this needs to work still. Currently on hard/non-user-initiated disconnection there is absolutely 0 messages.
    @Override
    public void onClose(String reason, int code) {
        System.out.println("[AP] Connection closed: " + reason);
        subscribers.clear();
        Client.client = null;
    }
}