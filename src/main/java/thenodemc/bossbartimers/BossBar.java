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
            exec.scheduleAtFixedRate(this::updateBossBar, 0, 1, TimeUnit.SECONDS);
        } else {
            this.setName(new TextComponentString(title.replace("$time", "")));
        }
    }

    void updateBossBar() {
        if (remaining>0 || remaining==-1) {
            if (remaining>0) {
                remaining -= 1;
            }
            //Force the boss bars to appear at each update, so they reappear even if a new player joins or if players relog
            for (EntityPlayerMP playerMP:BossBarTimers.server.getPlayerList().getPlayers()) {
                for (BossBar bossbar : BossBarTimers.bossbars) {
                    bossbar.addPlayer(playerMP);
                }
            }
        } else {
            exec.shutdownNow();
            for (EntityPlayerMP playerMP:BossBarTimers.server.getPlayerList().getPlayers()) {
                this.removePlayer(playerMP);
            }
            BossBarTimers.removeBar(title,duration,remaining);
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
