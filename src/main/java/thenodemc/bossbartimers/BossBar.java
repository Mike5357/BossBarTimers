package thenodemc.bossbartimers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BossBar extends BossInfoServer {

    public BossBar(ITextComponent nameIn, Color colorIn, Overlay overlayIn) {
        super(nameIn, colorIn, overlayIn);
    }

    public BossInfo.Color color;
    public int duration;
    public int remaining;
    public String title;

    ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

    public BossBar(BossInfo.Color color, int duration, String title) {
        super(new TextComponentString(title), color, BossInfo.Overlay.PROGRESS);
        this.color = color;
        this.duration = duration;
        this.remaining = duration;
        this.title = title;

        if (duration!= -1) {
            exec.scheduleAtFixedRate(this::decrement, 0, 1, TimeUnit.SECONDS);
        } else {
            this.setName(new TextComponentString(title.replace("$time", "")));
        }
    }

    void decrement() {
        if (remaining >0) {
            remaining -= 1;
        } else {
            exec.shutdownNow();
            for (EntityPlayerMP playerMP:BossBarTimers.server.getPlayerList().getPlayers()) {
                this.removePlayer(playerMP);
            }
            BossBarTimers.removeBar(title,duration,remaining);
            //BossBarTimers.logger.error("Bar attempting to be removed: " + title + "D: " + duration+ " R: " + remaining);
            //for (BossBar bar:BossBarTimers.bossbars) {
            //    BossBarTimers.logger.error("Existing Boss Bars:");
            //    BossBarTimers.logger.error(BossBarTimers.bossbars.get(BossBarTimers.bossbars.indexOf(bar)).title + "D: " + BossBarTimers.bossbars.get(BossBarTimers.bossbars.indexOf(bar)).duration+ " R: " + BossBarTimers.bossbars.get(BossBarTimers.bossbars.indexOf(bar)).remaining);
            //}
        }
        this.setPercent((float)remaining/duration);

        //hours = remaining / 3600;
        //minutes = (remaining % 3600) / 60;
        //seconds = remaining % 60;

        if ((remaining/3600) >0) {
            this.setName(new TextComponentString(title.replace("$time", String.format("%02d:%02d:%02d", remaining / 3600, (remaining % 3600) / 60, remaining % 60))));
        } else if (remaining>=0) {
            this.setName(new TextComponentString(title.replace("$time", String.format("%02d:%02d", (remaining % 3600) / 60, remaining % 60))));
        }
    }

}
