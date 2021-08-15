package thenodemc.bossbartimers;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import thenodemc.bossbartimers.command.BBTCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Mod(
        modid = BossBarTimers.MOD_ID,
        name = BossBarTimers.MOD_NAME,
        version = BossBarTimers.VERSION,
        serverSideOnly = true,
        acceptableRemoteVersions = "*"
)
public class BossBarTimers {

    public static final String MOD_ID = "bossbartimers";
    public static final String MOD_NAME = "Boss Bar Timers";
    public static final String VERSION = "1.0-SNAPSHOT";

    public static org.apache.logging.log4j.Logger logger;
    public static MinecraftServer server;

    /* This is the instance of your mod as created by Forge. It will never be null.*/
    @Mod.Instance(MOD_ID)
    public static BossBarTimers INSTANCE;
    public static List<BossBar> bossbars = new ArrayList<>();

    /* This is the first initialization event. Register tile entities here. The registry events below will have fired prior to entry to this method.*/
    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    /* This is the second initialization event. Register custom recipes*/
    @EventHandler
    public void init(FMLInitializationEvent event) {
        server = FMLCommonHandler.instance().getMinecraftServerInstance();
        ICommandManager commandManager = server.getCommandManager();
        ServerCommandManager manager = ((ServerCommandManager) commandManager);
        addCommands(manager);
    }

    public static void addCommands(ServerCommandManager manager) {
        manager.registerCommand(new BBTCommand());
    }

    /* This is the final initialization event. Register actions from other mods here*/
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    public static void removeBar(String name, int duration, int remaining) {
        for (BossBar bar : bossbars) {
            if (bar.title.contains(name) && bar.remaining == remaining && bar.duration == duration) {
                for (EntityPlayerMP playerMP : server.getPlayerList().getPlayers()) {
                    bar.removePlayer(playerMP);
                }
                bar.exec.shutdownNow();
                bossbars.remove(bar);
            }
        }
    }

}