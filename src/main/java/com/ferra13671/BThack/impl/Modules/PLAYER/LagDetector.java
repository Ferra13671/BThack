package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Events.PacketEvent;
import com.ferra13671.BThack.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LagDetector extends Module {
    public static boolean rubberBandDetected = false;


    int red1 = new Color(255,0,0).hashCode();
    int red2 = new Color(255,150,150).hashCode();
    int red = new Color(255,0,0).hashCode();
    int changeColorTimeout = 0;

    private String lagText = "";
    long timeoutMillis = (long)(3 * 1000.0f);

    private final Ticker lastPacketTimer = new Ticker();
    public static final Ticker lastRubberBandTimer = new Ticker();


    public LagDetector() {
        super("LagDetector",
                "lang.module.LagDetector",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );
    }


    @EventSubscriber
    public void onOverlay(RenderHudPostEvent e) {
        if (!mc.isIntegratedServerRunning()) {
            if (lagText.isEmpty())
                return;

            BThackRender.drawString(lagText, (mc.getWindow().getScaledWidth() / 2f) - (mc.textRenderer.getWidth(lagText) / 2f), (mc.getWindow().getScaledHeight() / 2f) + 20, red);
        }
    }

    @EventSubscriber
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;
        if (mc.isIntegratedServerRunning()) return;

        if (lastPacketTimer.passed(timeoutMillis)) {
            if (isOffline()) {
                lagText = LanguageSystem.translate("lang.module.LagDetector.offlineInternet");
            } else {
                lagText = LanguageSystem.translate("lang.module.LagDetector.serverNotResponding");
            }
        } else {
            if (!lagText.isEmpty()) lagText = "";
        }
        if (!lastRubberBandTimer.passed(50)) {
            if (!rubberBandDetected)
                rubberBandDetected = true;

            ChatUtils.sendMessage(LanguageSystem.translate("lang.module.LagDetector.rubberBandDetected"));
        }

        if (changeColorTimeout >= 30) {
            red = red == red1 ? red2 : red1;
            changeColorTimeout = 0;
        } else changeColorTimeout++;
    }

    @EventSubscriber
    public void onReceivePackets(PacketEvent.Receive e) {
        lastPacketTimer.reset();

        if (!rubberBandDetected || !(e.getPacket() instanceof PlayerMoveC2SPacket packet))
            return;

        double dist = new Vec3d(packet.x, packet.y, packet.z).subtract(mc.player.getPos()).length();
        Vec2f rotVec =  new Vec2f(packet.yaw - mc.player.yaw, packet.pitch - mc.player.pitch);
        double rotationDiff = Math.sqrt(rotVec.x * rotVec.x + rotVec.y * rotVec.y);

        if (0.5 <= dist && dist <= 64 || rotationDiff > 1.0)
            lastRubberBandTimer.reset();
    }

    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof LoginHelloC2SPacket){
            lastPacketTimer.reset(69420L);
            lastRubberBandTimer.reset(-69420L);
        }
    }



    private long timeOut;
    private boolean lastReturn = false;

    private boolean isOffline() {
        if (timeOut != 0) {
            timeOut--;
            return lastReturn;
        }
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("1.1.1.1", 80), 100);
            lastReturn = false;
            timeOut = 20;
            return false;
        } catch (IOException e) {
            lastReturn = true;
            timeOut = 20;
            return true; // Either timeout or unreachable or failed DNS lookup.
        }
    }
}
