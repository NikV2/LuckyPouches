package me.nik.luckypouches.utils.reflection.bossbar;

import me.nik.luckypouches.utils.ChatUtils;
import me.nik.luckypouches.utils.reflection.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BarSender1_8 implements BarSender {

    private static final Map<UUID, FakeDragon> DRAGONS = new HashMap<>();

    private String text;
    private Player player;
    private float percent;

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void setPercent(float percent) {
        this.percent = percent;
    }

    @Override
    public void setStyle(BarStyle style) {
    }

    @Override
    public void setColor(BarColor color) {
    }

    @Override
    public void show() {
        try {
            FakeDragon fd;

            final UUID uuid = player.getUniqueId();
            Vector vec = player.getLocation().getDirection().normalize().multiply(165);
            Location loc = player.getLocation().add(vec);

            if (DRAGONS.containsKey(uuid)) {
                fd = DRAGONS.get(uuid);
            } else {
                // spawn it 400 blocks above the player so you don't see it die at 0 health
                //fd = new FakeDragon(text, player.getLocation().add(0, 400, 0), percent);
                fd = new FakeDragon(ChatUtils.format(text), loc, percent);
                Object mobPacket = fd.getSpawnPacket();
                ReflectionUtils.sendPacket(player, mobPacket);
                DRAGONS.put(uuid, fd);
            }

            // set the status of the dragon and send the package to the player
            fd.setName(ChatUtils.format(text));
            fd.setHealth(percent);
            Object metaPacket = fd.getMetaPacket(fd.getWatcher());
            Object teleportPacket = fd.getTeleportPacket(loc);
            ReflectionUtils.sendPacket(player, metaPacket);
            ReflectionUtils.sendPacket(player, teleportPacket);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove() {
        if (!DRAGONS.containsKey(player.getUniqueId())) return;
        try {
            ReflectionUtils.sendPacket(player, DRAGONS.get(player.getUniqueId()).getDestroyPacket());
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        DRAGONS.remove(player.getUniqueId());
    }

    private static class FakeDragon {

        private static final int MAX_HEALTH = 200;
        private final int x;
        private final int y;
        private final int z;
        private final Object world;
        private int id;
        private float health;
        private String name;
        private Object dragon;

        public FakeDragon(String name, Location loc, float percent) {
            this.name = name;
            this.x = loc.getBlockX();
            this.y = loc.getBlockY();
            this.z = loc.getBlockZ();
            this.health = percent / 100F * MAX_HEALTH;
            this.world = ReflectionUtils.getHandle(loc.getWorld());
        }

        public void setHealth(float percent) {
            this.health = percent / 100F * MAX_HEALTH;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getSpawnPacket() throws SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {

            Class<?> entityEnderDragon = ReflectionUtils.getNMSClass("EntityEnderDragon");

            // make it so we don't need to update this plugin every time a new bukkit version is used
            dragon = ReflectionUtils.getConstructor(entityEnderDragon, ReflectionUtils.getNMSClass("World")).newInstance(world);

            // in Entity
            ReflectionUtils.getMethod(entityEnderDragon, "setPosition", double.class, double.class, double.class).invoke(dragon, x, y, z);

            ReflectionUtils.getMethod(entityEnderDragon, "setInvisible", boolean.class).invoke(dragon, false);

            // in EntityInsentient
            ReflectionUtils.getMethod(entityEnderDragon, "setCustomName", String.class).invoke(dragon, name);

            // in LivingEntity
            ReflectionUtils.getMethod(entityEnderDragon, "setHealth", float.class).invoke(dragon, health);

            Class<?> entity = ReflectionUtils.getNMSClass("Entity");

            // in Entity
            ReflectionUtils.getField(entity, "motX").set(dragon, 0); // x velocity, double

            // in Entity
            ReflectionUtils.getField(entity, "motY").set(dragon, 0); // y velocity, double

            // in Entity
            ReflectionUtils.getField(entity, "motZ").set(dragon, 0); // z velocity, double

            // in Entity
            this.id = (Integer) ReflectionUtils.getMethod(entityEnderDragon, "getId").invoke(dragon);

            // create an instance of this class using dragon entity

            return ReflectionUtils.getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutSpawnEntityLiving"), ReflectionUtils.getNMSClass("EntityLiving")).newInstance(dragon);
        }

        public Object getDestroyPacket() throws SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException {
            // create a new instance of the class
            return ReflectionUtils.getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutEntityDestroy"), int[].class).newInstance(new int[]{id});
        }

        public Object getMetaPacket(Object watcher) throws SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {

            return ReflectionUtils.getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutEntityMetadata"), int.class, ReflectionUtils.getNMSClass("DataWatcher"), boolean.class).newInstance(id, watcher, true);
        }

        public Object getTeleportPacket(Location loc) throws SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException {
            return ReflectionUtils.getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutEntityTeleport"), int.class, int.class, int.class, int.class, byte.class, byte.class, boolean.class)
                    .newInstance(this.id, loc.getBlockX() * 32, loc.getBlockY() * 32, loc.getBlockZ() * 32,
                            (byte) ((int) loc.getYaw() * 256 / 360), (byte) ((int) loc.getPitch() * 256 / 360), true);
        }

        public Object getWatcher() throws SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException {

            Class<?> dataWatcher = ReflectionUtils.getNMSClass("DataWatcher");

            Object watcher = ReflectionUtils.getConstructor(dataWatcher, ReflectionUtils.getNMSClass("Entity")).newInstance(dragon);
            Method a = ReflectionUtils.getMethod(dataWatcher, "a", int.class, Object.class);

            a.invoke(watcher, 0, (byte) 0x20); // visible, 0 = true, 0x20 = false
            a.invoke(watcher, 6, health); // health
            a.invoke(watcher, 10, ChatUtils.format(name)); // name
            a.invoke(watcher, 11, (byte) 1); // show name, 1 = true, 0 = false
            return watcher;
        }
    }
}