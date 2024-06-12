package com.zack20136.teleportcommandmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.zack20136.teleportcommandmod.assets.CommonFunction;
import com.zack20136.teleportcommandmod.assets.TpsFunction;
import com.zack20136.teleportcommandmod.assets.TpsPosData;
import com.zack20136.teleportcommandmod.assets.TpsPosDataFunction;
import net.minecraft.command.CommandSource;

import java.util.Map;

public class Commands {
    public static void init(CommandDispatcher<CommandSource> dispatcher){
        back(dispatcher);
        tps(dispatcher);
    }
    private static void back(CommandDispatcher<CommandSource> dispatcher) {
        // back
        LiteralArgumentBuilder<CommandSource> command_back = net.minecraft.command.Commands.literal("back");
        command_back.executes((command) -> {
            return CommonFunction.back(command.getSource());
        });

        // back command register
        dispatcher.register(command_back);
    }

    private static void tps(CommandDispatcher<CommandSource> dispatcher) {

        LiteralArgumentBuilder<CommandSource> literalargumentbuilder = net.minecraft.command.Commands.literal("tps");

        // list
        literalargumentbuilder.then(net.minecraft.command.Commands.literal("list").executes((command) -> {
            return TpsFunction.showList(command.getSource());
        }));

        // set
        literalargumentbuilder.then(net.minecraft.command.Commands.literal("set").then(net.minecraft.command.Commands.argument("name", StringArgumentType.string()).executes((command) -> {
            return TpsFunction.setPos(command.getSource(), StringArgumentType.getString(command, "name"), "");
        }).then(net.minecraft.command.Commands.argument("desc", StringArgumentType.greedyString()).executes((command) -> {
            return TpsFunction.setPos(command.getSource(), StringArgumentType.getString(command, "name"), StringArgumentType.getString(command, "desc"));
        }))));

        // remove
        literalargumentbuilder.then(net.minecraft.command.Commands.literal("rm").then(net.minecraft.command.Commands.argument("name", StringArgumentType.string())
                .suggests((context, builder) -> {
                    Map<String, TpsPosData> tpsPosData = TpsPosDataFunction.loadCoordinates(context.getSource().getPlayerOrException().getUUID());
                    for (String name : tpsPosData.keySet()) {
                        if (name.startsWith(builder.getRemaining().toLowerCase())) {
                            builder.suggest(name);
                        }
                    }
                    return builder.buildFuture();
                })
                .executes((command) -> {
                    return TpsFunction.removePos(command.getSource(), StringArgumentType.getString(command, "name"));
                })));

        // teleport
        literalargumentbuilder.then(net.minecraft.command.Commands.argument("name", StringArgumentType.string())
                .suggests((context, builder) -> {
                    Map<String, TpsPosData> tpsPosData = TpsPosDataFunction.loadCoordinates(context.getSource().getPlayerOrException().getUUID());
                    for (String name : tpsPosData.keySet()) {
                        if (name.startsWith(builder.getRemaining().toLowerCase())) {
                            builder.suggest(name);
                        }
                    }
                    return builder.buildFuture();
                })
                .executes((command) -> {
                    return TpsFunction.teleport(command.getSource(), StringArgumentType.getString(command, "name"));
                }));

        // tps command register
        dispatcher.register(literalargumentbuilder);
    }
}
