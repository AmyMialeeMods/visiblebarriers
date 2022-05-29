package amymialee.visiblebarriers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class VisibleBarriers implements ModInitializer {
    public final static String MOD_ID = "visiblebarriers";
    protected static boolean visible = false;
    protected static boolean visibleAir = false;

    public static final ItemGroup VISIBLE_BARRIERS = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "visible_barriers"))
            .icon(() -> new ItemStack(Items.BARRIER))
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
    public void onInitialize() {}

    public static boolean isVisible() {
        return visible;
    }

    public static boolean isVisibleAir() {
        return visibleAir;
    }
}