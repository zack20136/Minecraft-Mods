package com.zack20136.teleportcommandmod.events;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.zack20136.teleportcommandmod.TeleportCommandMod;
import com.zack20136.teleportcommandmod.clients.TpsClient;
import com.zack20136.teleportcommandmod.commands.TpsCommand;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = TeleportCommandMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new TpsCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onClientsRegister(ClientChatEvent event) throws CommandSyntaxException {
        new TpsClient(event);
    }
}
