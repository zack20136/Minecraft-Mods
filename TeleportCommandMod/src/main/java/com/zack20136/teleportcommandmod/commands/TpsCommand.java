package com.zack20136.teleportcommandmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.zack20136.teleportcommandmod.assets.TpsFunction;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import com.zack20136.teleportcommandmod.assets.TpsPosData;
import com.zack20136.teleportcommandmod.assets.TpsPosDataFunction;

import java.util.HashMap;
import java.util.Map;

public class TpsCommand {
    private Map<String, TpsPosData> tpsPosData = new HashMap<>();

    public TpsCommand(CommandDispatcher<CommandSource> dispatcher) {

        LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("tps");

        // list
        literalargumentbuilder.then(Commands.literal("list").executes((command) -> {
            return TpsFunction.showList(command.getSource());
        }));

        // set
        literalargumentbuilder.then(Commands.literal("set").then(Commands.argument("name", StringArgumentType.string()).executes((command) -> {
            return TpsFunction.setPos(command.getSource(), StringArgumentType.getString(command, "name"), "");
        }).then(Commands.argument("desc", StringArgumentType.greedyString()).executes((command) -> {
            return TpsFunction.setPos(command.getSource(), StringArgumentType.getString(command, "name"), StringArgumentType.getString(command, "desc"));
        }))));

        // remove
        literalargumentbuilder.then(Commands.literal("rm").then(Commands.argument("name", StringArgumentType.string())
            .suggests((context, builder) -> {
                tpsPosData = TpsPosDataFunction.loadCoordinates(context.getSource().getPlayerOrException().getUUID());
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
        literalargumentbuilder.then(Commands.argument("name", StringArgumentType.string())
            .suggests((context, builder) -> {
                tpsPosData = TpsPosDataFunction.loadCoordinates(context.getSource().getPlayerOrException().getUUID());
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

        // back     not finish yet
        LiteralArgumentBuilder<CommandSource> command_back = Commands.literal("back");
        command_back.executes((command) -> {
            return TpsFunction.back(command.getSource());
        });
        dispatcher.register(command_back);
    }
}
