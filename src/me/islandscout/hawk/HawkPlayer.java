package me.islandscout.hawk;

import me.islandscout.hawk.checks.Check;
import me.islandscout.hawk.utils.PhantomBlock;
import me.islandscout.hawk.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Provides essential and additional tools to analyze players during
 * packet interception. Recommended to use this rather than
 * Bukkit's implementation. Also provides tools for asynchronous
 * threads.
 */
public class HawkPlayer {

    private final UUID uuid;
    private final Map<Check, Double> vl;
    private boolean digging;
    private boolean receiveFlags;
    private boolean online;
    private Player p;
    private boolean teleporting;
    private Location teleportLoc;
    private long lastTeleportTime;
    private Hawk hawk;
    private Location location;
    private Vector velocity;
    private float deltaYaw;
    private float deltaPitch;
    private boolean onGround;
    private boolean flying;
    private int ping;
    private short pingJitter;
    private long lastMoveTime;
    private Set<PhantomBlock> phantomBlocks; //TODO: You'll need to monitor this frequently because I'm sure there will a memory leak here.
                                             //Perhaps have a limit to the amount of PhantomBlocks (16), then clear out old PhantomBlocks.

    HawkPlayer(Player p, Hawk hawk) {
        this.uuid = p.getUniqueId();
        vl = new HashMap<>();
        receiveFlags = true;
        this.p = p;
        this.location = p.getLocation();
        this.onGround = ((Entity)p).isOnGround();
        this.flying = p.isFlying();
        this.hawk = hawk;
        this.ping = ServerUtils.getPing(p);
        this.pingJitter = 0;
        phantomBlocks = new HashSet<>();
    }

    public int getVL(Check check) {
        return (int)(double)vl.getOrDefault(check, 0D);
    }

    public void setVL(Check check, double vl) {
        this.vl.put(check, vl);
    }

    public void incrementVL(Check check) {
        this.vl.put(check, vl.getOrDefault(check, 0D) + 1D);
    }

    public void multiplyVL(Check check, double factor) {
        vl.put(check, vl.getOrDefault(check, 0D) * factor);
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isDigging() {
        return digging;
    }

    //this should really not be public
    public void setDigging(boolean digging) {
        this.digging = digging;
    }

    public boolean canReceiveFlags() {
        return receiveFlags;
    }

    public void setReceiveFlags(boolean status) {
        receiveFlags = status;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isTeleporting() {
        return teleporting;
    }

    //this should really not be public
    public void setTeleporting(boolean status) {
        teleporting = status;
    }

    public Location getTeleportLoc() {
        return teleportLoc;
    }

    public void setTeleportLoc(Location teleportLoc) {
        this.teleportLoc = teleportLoc;
    }

    public long getLastTeleportTime() {
        return lastTeleportTime;
    }

    public void setLastTeleportTime(long lastTeleportTime) {
        this.lastTeleportTime = lastTeleportTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isOnGround() {
        return onGround;
    }

    //this should really not be public
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isFlying() {
        return flying;
    }

    //TODO: Make this really do something in the Bukkit realm.
    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public short getPingJitter() {
        return pingJitter;
    }

    public void setPingJitter(short pingJitter) {
        this.pingJitter = pingJitter;
    }

    public long getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public float getDeltaYaw() {
        return deltaYaw;
    }

    public void setDeltaYaw(float deltaYaw) {
        this.deltaYaw = deltaYaw;
    }

    public float getDeltaPitch() {
        return deltaPitch;
    }

    public void setDeltaPitch(float deltaPitch) {
        this.deltaPitch = deltaPitch;
    }

    public Set<PhantomBlock> getPhantomBlocks() {
        return phantomBlocks;
    }

    public boolean addPhantomBlock(PhantomBlock pBlock) {
        //memory-leak police on duty
        Set<PhantomBlock> oldPBlocks = new HashSet<>();
        for(PhantomBlock loopPBlock : phantomBlocks) {
            if(System.currentTimeMillis() - loopPBlock.getInitTime() > 2000) {
                oldPBlocks.add(loopPBlock);
            }
        }
        phantomBlocks.removeAll(oldPBlocks);
        if(phantomBlocks.size() >= 16)
            return false;
        phantomBlocks.add(pBlock);
        return true;
    }

    //safely kill the connection
    public synchronized void kickPlayer(String reason) {
        online = false;
        Bukkit.getScheduler().scheduleSyncDelayedTask(hawk, new Runnable() {
            @Override
            public void run() {
                p.kickPlayer(reason);
            }
        }, 0L);
    }

    //safely teleport player
    public synchronized void teleportPlayer(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(hawk, new Runnable() {
            @Override
            public void run() {
                p.teleport(location, teleportCause);
            }
        }, 0L);
    }
}