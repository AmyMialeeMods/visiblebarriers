package amymialee.visiblebarriers;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class VisibleBarriers implements ModInitializer {
    public static String MODID = "visiblebarriers";

    public static boolean visible = false;
    public static boolean visible_air = false;
    public static VisibleBarriersConfig config = null;

    public static final ItemGroup EXTRA_ITEMS = FabricItemGroupBuilder.create(
                    new Identifier(MODID, "extra_items"))
            .icon(() -> new ItemStack(Items.COMMAND_BLOCK))
            .appendItems(stacks -> {
                stacks.add(new ItemStack(Items.COMMAND_BLOCK));
                stacks.add(new ItemStack(Items.CHAIN_COMMAND_BLOCK));
                stacks.add(new ItemStack(Items.REPEATING_COMMAND_BLOCK));
                stacks.add(new ItemStack(Items.STRUCTURE_BLOCK));
                stacks.add(new ItemStack(Items.JIGSAW));
                stacks.add(new ItemStack(Items.SPAWNER));
                stacks.add(new ItemStack(Items.BARRIER));
                stacks.add(new ItemStack(Items.STRUCTURE_VOID));
                stacks.add(new ItemStack(Items.LIGHT));

                stacks.add(new ItemStack(Items.DEBUG_STICK));
                stacks.add(new ItemStack(Items.KNOWLEDGE_BOOK));
                stacks.add(new ItemStack(Items.DRAGON_EGG));
                stacks.add(new ItemStack(Items.COMMAND_BLOCK_MINECART));
                stacks.add(new ItemStack(Items.FIREWORK_ROCKET));
                stacks.add(new ItemStack(Items.FIREWORK_STAR));
                stacks.add(new ItemStack(Items.SUSPICIOUS_STEW));
            })
            .build();

    @Override
    public void onInitialize() {
        config = VisibleBarriersConfig.load();
    }
}
