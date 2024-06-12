package com.zack20136.teleportcommandmod;

import com.zack20136.teleportcommandmod.assets.TpsPosDataFunction;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(TeleportCommandMod.MOD_ID)
public class TeleportCommandMod {
    public static final String MOD_ID = "teleportcommandmod";
    private static final Logger LOGGER = LogManager.getLogger();

    public TeleportCommandMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::enqueueIMC);
        eventBus.addListener(this::processIMC);
        eventBus.addListener(this::setup);
//        eventBus.addListener(this::setupClient);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("teleportcommandmod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello Teleport Command Mod";
        });
    }

    private void processIMC(final InterModProcessEvent event) {

    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        // setup tps coordinates folder
        File folder = new File(TpsPosDataFunction.getCoordinatesFolder());
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

//    private void setupClient(final FMLClientSetupEvent event) {
//
//    }


}