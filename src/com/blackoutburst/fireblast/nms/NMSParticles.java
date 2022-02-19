package com.blackoutburst.fireblast.nms;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class NMSParticles {

    public enum ParticleType {
        EXPLOSION_NORMAL,
        EXPLOSION_LARGE,
        EXPLOSION_HUGE,
        FIREWORKS_SPARK,
        WATER_BUBBLE,
        WATER_SPLASH,
        WATER_WAKE,
        SUSPENDED,
        SUSPENDED_DEPTH,
        CRIT,
        CRIT_MAGIC,
        SMOKE_NORMAL,
        SMOKE_LARGE,
        SPELL,
        SPELL_INSTANT,
        SPELL_MOB,
        SPELL_MOB_AMBIENT,
        SPELL_WITCH,
        DRIP_WATER,
        DRIP_LAVA,
        VILLAGER_ANGRY,
        VILLAGER_HAPPY,
        TOWN_AURA,
        NOTE,
        PORTAL,
        ENCHANTMENT_TABLE,
        FLAME,
        LAVA,
        FOOTSTEP,
        CLOUD,
        REDSTONE,
        SNOWBALL,
        SNOW_SHOVEL,
        SLIME,
        HEART,
        BARRIER,
        ITEM_CRACK,
        BLOCK_CRACK,
        BLOCK_DUST,
        WATER_DROP,
        ITEM_TAKE,
        MOB_APPEARANCE
    }

    public static void send(Player player, ParticleType particle, float x, float y, float z, float r, float g, float b, int count) {
        try {
            final Class<?> packetClass = NMS.getClass("PacketPlayOutWorldParticles");
            final Class particlesClass = NMS.getClass("EnumParticle");

            final Enum particleType = Enum.valueOf(particlesClass, particle.toString());

            final Constructor<?> packetConstructor = packetClass.getDeclaredConstructor();
            final Object particlesPacket = packetConstructor.newInstance();

            NMS.setField(particlesPacket, "a", particleType);
            NMS.setField(particlesPacket, "b", x);
            NMS.setField(particlesPacket, "c", y);
            NMS.setField(particlesPacket, "d", z);
            NMS.setField(particlesPacket, "e", r);
            NMS.setField(particlesPacket, "f", g);
            NMS.setField(particlesPacket, "g", b);
            NMS.setField(particlesPacket, "h", 0);
            NMS.setField(particlesPacket, "i", count);
            NMS.setField(particlesPacket, "j", true);

            NMS.sendPacket(player, particlesPacket);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
