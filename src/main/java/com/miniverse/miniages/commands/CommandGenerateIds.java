package com.miniverse.miniages.commands;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CommandGenerateIds extends CommandBase {

    @Override
    public String getName() {
        return "generateids";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/generateids";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        try (FileWriter writer = new FileWriter("block_item_list.txt")) {
            writer.write("Block and Item List\n\n");

            writer.write("Blocks:\n");
            for (ResourceLocation key : Block.REGISTRY.getKeys()) {
                Block block = Block.REGISTRY.getObject(key);
                String blockName = block.getLocalizedName();
                writer.write(String.format("%s:%s - %s (Block)\n", key.getResourceDomain(), key.getResourcePath(), blockName
                ));
            }

            writer.write("\n\n\nItems:\n");

            for (ResourceLocation key : Item.REGISTRY.getKeys()) {
                Item item = Item.REGISTRY.getObject(key);
                String itemName = item.getUnlocalizedName() + ".name";
                String itemNameFixed = I18n.format(itemName);
                writer.write(String.format("%s:%s - %s (Item)\n", key.getResourceDomain(), key.getResourcePath(), itemNameFixed));
            }

            sender.sendMessage(new TextComponentString("Generated block_item_list.txt"));
        } catch (IOException e) {
            sender.sendMessage(new TextComponentString("Failed to generate block_item_list.txt"));
            e.printStackTrace();
        }
    }

}
