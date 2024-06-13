package com.zack20136.teleportcommandmod.events;

import com.zack20136.teleportcommandmod.TeleportCommandMod;
import com.zack20136.teleportcommandmod.assets.CommonFunction;
import com.zack20136.teleportcommandmod.clients.ClientChatCommands;
import com.zack20136.teleportcommandmod.commands.Commands;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Mod.EventBusSubscriber(modid = TeleportCommandMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        Commands.init(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onCommandExecuted(CommandEvent event) {
        CommonFunction.checkSetBackByCommandEvent(event);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientChat(ClientChatEvent event) {
        ClientChatCommands.tps(event);
    }
}
