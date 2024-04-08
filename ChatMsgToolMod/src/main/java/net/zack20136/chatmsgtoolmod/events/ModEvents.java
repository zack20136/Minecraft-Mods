package net.zack20136.chatmsgtoolmod.events;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import net.zack20136.chatmsgtoolmod.ChatMsgToolMod;
import net.zack20136.chatmsgtoolmod.clients.TpsClient;
import net.zack20136.chatmsgtoolmod.commands.TpsCommand;

import java.io.File;

@Mod.EventBusSubscriber(modid = ChatMsgToolMod.MOD_ID)
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
