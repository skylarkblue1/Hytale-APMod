package io.github.skylarkblue1;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChains;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.io.adapter.PacketFilter;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;

import io.github.skylarkblue1.commands.APConnect;
import io.github.skylarkblue1.commands.APDisconnect;
import io.github.skylarkblue1.events.APChatListener;
import io.github.skylarkblue1.events.ExampleEvent;

import java.util.Arrays;
import javax.annotation.Nonnull;

public class HytaleAP extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static Player ply = null;

    public HytaleAP(@Nonnull JavaPluginInit init) {
        super(init);
    }

    protected void setup() {
        PacketAdapters.registerInbound((PacketFilter) (handler, packet) -> {

// This whole if statement doesn't trigger anymore.
//            if (ply != null) {
//                ply.sendMessage(Message.raw("Reading Packet"));
//                if (packet.getClass().getSimpleName().equals("ClientMovement") || packet.getClass().getSimpleName().equals("Pong")) {
//                    return false;
//                } else  {
//                    ply.sendMessage(Message.raw(packet.getClass().getSimpleName()));
//                }
//            }
            if (packet.getId() == 290) {
                SyncInteractionChains chainsPacket = (SyncInteractionChains)packet;
                return Arrays.stream(chainsPacket.updates).anyMatch((up) -> {
                    if (up.interactionType == InteractionType.Use && up.data != null && up.data.blockPosition != null) {
                        World w = Universe.get().getWorld(Universe.get().getPlayer(handler.getAuth().getUuid()).getWorldUuid());
                        BlockType bl = w.getBlockType(up.data.blockPosition.x, up.data.blockPosition.y, up.data.blockPosition.z);
                        return bl != null && bl.getBench() != null && bl.getBench().getTierLevel(2) != null; // If bench has a higher tier level than 1, block it from opening
                    } else {
                        return false;
                    }
                });
            } else {
                return false;
            }
        });
        PacketAdapters.registerOutbound((PacketFilter) (handler, packet) -> {
            String handlerName = handler.getClass().getSimpleName();
            String packetName = packet.getClass().getSimpleName();

// This breaks with the new FTU memories screen - unknown if it's still broken if there's a custom UI that opsns but will need a hell of a lot more testing. Likely, just use mixins to change memories.

//    if (packet.getId() == 218 || packet.getId() == 216 || packet.getId() == 200) { // Blocks memories page from opening
//        ((HytaleLogger.Api)LOGGER.atInfo()).log("[" + handlerName + "] Sent packet id=" + packet.getId() + ": " + packetName);
//        if (packet instanceof CustomPage) {
//            CustomPage pagePacket = (CustomPage)packet;
//            ((HytaleLogger.Api)LOGGER.atInfo()).log("[" + handlerName + "] Sent CustomPage: " + pagePacket.key);
//            if (pagePacket.key.equalsIgnoreCase("com.hypixel.hytale.builtin.adventure.memories.page.MemoriesPage")) {
//                ((HytaleLogger.Api)LOGGER.atInfo()).log("[" + handlerName + "] Memories custom page!!");
//                return true;
//            }
//        }
//    }

            return false;
        });

        PacketAdapters.registerOutbound((PacketFilter) (handler, packet) -> false);

        // Register the archipelago commands
        APConnect connectCommand = new APConnect();
        getCommandRegistry().registerCommand(connectCommand);
        getCommandRegistry().registerCommand(new APDisconnect(connectCommand));

        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);

        // Register the archipelago chat listener
        APChatListener chatListener = new APChatListener(connectCommand);
        this.getEventRegistry().registerAsyncGlobal(
                PlayerChatEvent.class,
                future -> future.thenApply(event -> {
                    chatListener.onPlayerChat(event);
                    return event;
                })
        );

    }
}
