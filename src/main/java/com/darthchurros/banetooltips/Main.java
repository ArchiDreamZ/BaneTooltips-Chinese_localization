/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.Mod
 *  cpw.mods.fml.common.Mod$EventHandler
 *  cpw.mods.fml.common.event.FMLInitializationEvent
 *  cpw.mods.fml.common.eventhandler.EventBus
 *  cpw.mods.fml.common.eventhandler.EventPriority
 *  cpw.mods.fml.common.eventhandler.SubscribeEvent
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.player.ItemTooltipEvent
 */
package com.darthchurros.banetooltips;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

@Mod(modid="bane-tooltips", version="1.0.1", dependencies="required-after:lotr;")
public class Main {
    public static final String MODID = "bane-tooltips";
    public static final String VERSION = "1.0.1";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack weapon = event.itemStack;
        NBTTagCompound tag = weapon.getTagCompound();
        if (tag != null && tag.hasKey("LOTREnchProgress")) {
            if (tag.hasKey("LOTREnch")) {
                NBTTagList modifiers = tag.getTagList("LOTREnch", 8);
                for (int i = 0; i < modifiers.tagCount(); ++i) {
                    if (!modifiers.getStringTagAt(i).substring(0, 4).equals("bane")) continue;
                    return;
                }
            }
            NBTTagList progress = tag.getTagList("LOTREnchProgress", 10);
            int kills = 0;
            int killsRequired = 1;
            String baneType = "";
            for (int i = 0; i < progress.tagCount(); ++i) {
                int tempKillsRequired;
                int tempKills = progress.getCompoundTagAt(i).getInteger("Kills");
                if (!((double)tempKills / (double)(tempKillsRequired = progress.getCompoundTagAt(i).getInteger("KillsRequired")) > (double)kills / (double)killsRequired)) continue;
                kills = tempKills;
                killsRequired = tempKillsRequired;
                baneType = progress.getCompoundTagAt(i).getString("Name");
            }
            event.toolTip.add(baneType.substring(4) + "克星: 已杀" + kills + "，共需" + killsRequired);
        }
    }
}

