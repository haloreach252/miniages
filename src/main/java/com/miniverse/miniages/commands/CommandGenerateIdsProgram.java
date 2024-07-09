package com.miniverse.miniages.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.FileWriter;
import java.io.IOException;

public class CommandGenerateIdsProgram extends CommandBase {

    @Override
    public String getName() {
        return "generateidsprogram";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/generateidsprogram - Generates a list of all ids usable by the Miniverse CT Helper program";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        // Setup our json stuff
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray blockArray = new JsonArray();
        JsonArray itemArray = new JsonArray();

        for (ResourceLocation key : Block.REGISTRY.getKeys()) {
            Block block = Block.REGISTRY.getObject(key);
            String blockName = block.getLocalizedName();
            String blockRegName = key.getResourceDomain() + ":" + key.getResourcePath();

            blockArray.add(getJsonForObject(blockName, blockRegName));
        }

        for (ResourceLocation key : Item.REGISTRY.getKeys()) {
            Item item = Item.REGISTRY.getObject(key);
            String itemName = I18n.format(item.getUnlocalizedName() + ".name");
            String itemReg = key.getResourceDomain() + ":" + key.getResourcePath();

            itemArray.add(getJsonForObject(itemName, itemReg));
        }

        JsonObject root = new JsonObject();
        root.add("blocks", blockArray);
        root.add("items", itemArray);

        try (FileWriter writer = new FileWriter("generateids.json")) {
            gson.toJson(root, writer);
            sender.sendMessage(new TextComponentString("Generated generateids.json!"));
        } catch (IOException e) {
            sender.sendMessage(new TextComponentString("Failed to generate generateids.json"));
            e.printStackTrace();
        }
    }

    private JsonObject getJsonForObject(String name, String registry) {
        JsonObject temp = new JsonObject();

        temp.addProperty("name", name);
        temp.addProperty("registry", registry);
        temp.addProperty("meta", 0);

        return temp;
    }

}
