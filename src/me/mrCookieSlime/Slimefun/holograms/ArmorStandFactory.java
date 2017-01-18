package me.mrCookieSlime.Slimefun.holograms;

import java.lang.reflect.Method;

import me.mrCookieSlime.CSCoreLibPlugin.general.Reflection.ReflectionUtils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class ArmorStandFactory {

	public static ArmorStand createHidden(Location l) {
		ArmorStand armorstand = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
		armorstand.setVisible(false);
		armorstand.setGravity(false);
		armorstand.setBasePlate(false);
		armorstand.setCustomNameVisible(true);
		armorstand.setRemoveWhenFarAway(false);
		try {
			Object nmsEntity = armorstand.getClass().getMethod("getHandle").invoke(armorstand);
            if (ReflectionUtils.getVersion().startsWith("v1_9_")) {
            	try {
            		ReflectionUtils.setFieldValue(nmsEntity, "bz", 2039583);
            	} catch (IllegalArgumentException x) {
            		ReflectionUtils.setFieldValue(nmsEntity, "bA", 2039583);
            	}
            }
            else if (ReflectionUtils.getVersion().startsWith("v1_10_")) {
            	try {
            		ReflectionUtils.setFieldValue(nmsEntity, "bB", 2039583);
            	} catch (IllegalArgumentException x) {
            		ReflectionUtils.setFieldValue(nmsEntity, "bA", true);
            	}
            }
            else if (ReflectionUtils.getVersion().startsWith("v1_11_")) {
				try {
					ReflectionUtils.setFieldValue(nmsEntity, "bA", 2039583);
				} catch (IllegalArgumentException x) {
					ReflectionUtils.setFieldValue(nmsEntity, "bB", true);
				}
			}
            else {
            	Method method = nmsEntity.getClass().getMethod("getNBTTag");
            	Object tag = method.invoke(nmsEntity);
                if(tag == null) {
                    tag = ReflectionUtils.getClass("NBTTagCompound").newInstance();
                }
                method = nmsEntity.getClass().getMethod("c", ReflectionUtils.getClass("NBTTagCompound"));
                method.invoke(nmsEntity, tag);

                tag.getClass().getMethod("setBoolean", String.class, boolean.class).invoke(tag, "Invulnerable", true);
                nmsEntity.getClass().getMethod("f", ReflectionUtils.getClass("NBTTagCompound")).invoke(nmsEntity, tag);

                tag.getClass().getMethod("setInt", String.class, int.class).invoke(tag, "DisabledSlots", 2039583);
                nmsEntity.getClass().getMethod("a", ReflectionUtils.getClass("NBTTagCompound")).invoke(nmsEntity, tag);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
		return armorstand;
	}

	public static ArmorStand createSmall(Location l, ItemStack item, EulerAngle arm, float yaw) {
		l.setYaw(yaw);
		ArmorStand armorstand = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
		armorstand.getEquipment().setItemInMainHand(item);
		armorstand.setVisible(false);
		armorstand.setGravity(false);
		armorstand.setSmall(true);
		armorstand.setArms(true);
		armorstand.setRightArmPose(arm);
		armorstand.setBasePlate(false);
		armorstand.setRemoveWhenFarAway(false);
		try {
			Object nmsEntity = armorstand.getClass().getMethod("getHandle").invoke(armorstand);
            if (ReflectionUtils.getVersion().startsWith("v1_9_")) {
            	try {
            		ReflectionUtils.setFieldValue(nmsEntity, "bz", 2039583);
            	} catch(IllegalArgumentException x) {
            		ReflectionUtils.setFieldValue(nmsEntity, "bA", 2039583);
            	}
            }
            else if (ReflectionUtils.getVersion().startsWith("v1_10_")) {
            	try {
            		ReflectionUtils.setFieldValue(nmsEntity, "bB", 2039583);
            	} catch (IllegalArgumentException x) {
            		ReflectionUtils.setFieldValue(nmsEntity, "bA", true);
            	}
            }
            else if (ReflectionUtils.getVersion().startsWith("v1_11_")) {
				try {
					ReflectionUtils.setFieldValue(nmsEntity, "bA", 2039583);
				} catch (IllegalArgumentException x) {
					ReflectionUtils.setFieldValue(nmsEntity, "bB", true);
				}
			}
            else {
            	Method method = nmsEntity.getClass().getMethod("getNBTTag");
            	Object tag = method.invoke(nmsEntity);
                if(tag == null) {
                    tag = ReflectionUtils.getClass("NBTTagCompound").newInstance();
                }
                method = nmsEntity.getClass().getMethod("c", ReflectionUtils.getClass("NBTTagCompound"));
                method.invoke(nmsEntity, tag);

                tag.getClass().getMethod("setBoolean", String.class, boolean.class).invoke(tag, "Invulnerable", true);
                nmsEntity.getClass().getMethod("f", ReflectionUtils.getClass("NBTTagCompound")).invoke(nmsEntity, tag);

                tag.getClass().getMethod("setInt", String.class, int.class).invoke(tag, "DisabledSlots", 2039583);
                nmsEntity.getClass().getMethod("a", ReflectionUtils.getClass("NBTTagCompound")).invoke(nmsEntity, tag);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
		return armorstand;
	}

	public static ArmorStand createSmall(Location l, ItemStack head, float yaw) {
		l.setYaw(yaw);
		ArmorStand armorstand = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
		armorstand.getEquipment().setHelmet(head);
		armorstand.setVisible(false);
		armorstand.setGravity(false);
		armorstand.setSmall(true);
		armorstand.setBasePlate(false);
		armorstand.setRemoveWhenFarAway(false);
		try {
			Object nmsEntity = armorstand.getClass().getMethod("getHandle").invoke(armorstand);
            if (ReflectionUtils.getVersion().startsWith("v1_9_")) {
            	try {
            		ReflectionUtils.setFieldValue(nmsEntity, "bz", 2039583);
            	} catch(IllegalArgumentException x) {
            		ReflectionUtils.setFieldValue(nmsEntity, "bA", 2039583);
            	}
            }
            else {
            	Method method = nmsEntity.getClass().getMethod("getNBTTag");
            	Object tag = method.invoke(nmsEntity);
                if(tag == null) {
                    tag = ReflectionUtils.getClass("NBTTagCompound").newInstance();
                }
                method = nmsEntity.getClass().getMethod("c", ReflectionUtils.getClass("NBTTagCompound"));
                method.invoke(nmsEntity, tag);

                tag.getClass().getMethod("setBoolean", String.class, boolean.class).invoke(tag, "Invulnerable", true);
                nmsEntity.getClass().getMethod("f", ReflectionUtils.getClass("NBTTagCompound")).invoke(nmsEntity, tag);

                tag.getClass().getMethod("setInt", String.class, int.class).invoke(tag, "DisabledSlots", 2039583);
                nmsEntity.getClass().getMethod("a", ReflectionUtils.getClass("NBTTagCompound")).invoke(nmsEntity, tag);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
		return armorstand;
	}

}
