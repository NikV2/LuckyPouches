package me.nik.luckypouches.utils.reflection;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class ReflectionUtils {

    private static final Map<String, Class<?>> NMS_CLASSES = new HashMap<>();
    private static final Map<String, Class<?>> OBC_CLASSES = new HashMap<>();
    private static final Map<Class<?>, Map<String, Method>> METHODS = new HashMap<>();
    private static final Map<Class<?>, Map<String, Field>> FIELDS = new HashMap<>();
    private static String VERSION;

    public ReflectionUtils() {
    }

    public static void clear() {
        VERSION = null;
        NMS_CLASSES.clear();
        OBC_CLASSES.clear();
        METHODS.clear();
        FIELDS.clear();
    }

    public static Object getHandle(World world) {

        Object nmsWntity = null;

        Method worldGetHandle = getMethod(world.getClass(), "getHandle");

        try {
            nmsWntity = worldGetHandle.invoke(world);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nmsWntity;
    }

    public static String getVersion() {
        if (VERSION == null) {

            String name = Bukkit.getServer().getClass().getPackage().getName();

            VERSION = name.substring(name.lastIndexOf('.') + 1) + ".";
        }

        return VERSION;
    }

    public static Class<?> getNMSClass(final String nmsClassName) {
        if (NMS_CLASSES.containsKey(nmsClassName)) {

            return NMS_CLASSES.get(nmsClassName);
        }

        String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;

        Class<?> clazz;

        try {

            clazz = Class.forName(clazzName);

        } catch (Throwable t) {

            t.printStackTrace();

            return NMS_CLASSES.put(nmsClassName, null);
        }

        NMS_CLASSES.put(nmsClassName, clazz);
        return clazz;
    }

    public static Class<?> getOBCClass(final String obcClassName) {
        if (OBC_CLASSES.containsKey(obcClassName)) {

            return OBC_CLASSES.get(obcClassName);
        }

        String clazzName = "org.bukkit.craftbukkit." + getVersion() + obcClassName;

        Class<?> clazz;

        try {

            clazz = Class.forName(clazzName);

        } catch (Throwable t) {

            t.printStackTrace();

            OBC_CLASSES.put(obcClassName, null);

            return null;
        }

        OBC_CLASSES.put(obcClassName, clazz);

        return clazz;
    }

    public static Object getConnection(final Player player) {

        Method getHandleMethod = getMethod(player.getClass(), "getHandle");

        if (getHandleMethod != null) {
            try {
                Object nmsPlayer = getHandleMethod.invoke(player);

                Field playerConField = getField(nmsPlayer.getClass(), "playerConnection");

                return playerConField.get(nmsPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void sendPacket(final Player player, final Object packet) {

        Object playerConnection = getConnection(player);

        if (playerConnection == null) return;

        Method sendPacketMethod = getMethod(playerConnection.getClass(), "sendPacket", getNMSClass("Packet"));

        if (sendPacketMethod != null) {
            try {
                sendPacketMethod.invoke(playerConnection, packet);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object craftEntity(final Entity entity) {
        try {
            return getMethod(getOBCClass("entity.CraftEntity"), "getHandle").invoke(entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... params) {
        if (!METHODS.containsKey(clazz)) {
            METHODS.put(clazz, new HashMap<>());
        }

        Map<String, Method> methods = METHODS.get(clazz);

        if (methods.containsKey(methodName)) {
            return methods.get(methodName);
        }

        try {
            Method method = clazz.getMethod(methodName, params);

            methods.put(methodName, method);

            METHODS.put(clazz, methods);

            return method;
        } catch (Exception e) {

            e.printStackTrace();

            methods.put(methodName, null);

            METHODS.put(clazz, methods);

            return null;
        }
    }

    public static Field getField(final Class<?> clazz, final String fieldName) {
        if (!FIELDS.containsKey(clazz)) {
            FIELDS.put(clazz, new HashMap<>());
        }

        Map<String, Field> fields = FIELDS.get(clazz);

        if (fields.containsKey(fieldName)) {
            return fields.get(fieldName);
        }

        try {
            Field field = clazz.getField(fieldName);
            fields.put(fieldName, field);
            FIELDS.put(clazz, fields);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            fields.put(fieldName, null);
            FIELDS.put(clazz, fields);
        }

        return null;
    }

    public static Method findMethod(final Class<?> clazz, final int requires, final int excludes, final Class<?> result, final Class<?>... params) {
        int paramsCount = params.length;

        for (Method m : clazz.getMethods()) {

            if (m.getParameterCount() == paramsCount) {

                if (m.getReturnType() == result) {

                    int modifier = m.getModifiers();

                    if ((modifier & requires) == requires && (modifier & excludes) == 0) {

                        if (Arrays.equals(m.getParameterTypes(), params)) {
                            return m;
                        }
                    }
                }
            }
        }

        return null;
    }
}