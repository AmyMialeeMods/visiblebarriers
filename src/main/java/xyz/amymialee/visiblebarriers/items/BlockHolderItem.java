package xyz.amymialee.visiblebarriers.items;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

import java.util.List;

public class BlockHolderItem extends Item {
    public BlockHolderItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var actionResult = this.place(new ItemPlacementContext(context));
        return !actionResult.isAccepted() && context.getStack().contains(DataComponentTypes.CONSUMABLE) ? super.use(context.getWorld(), context.getPlayer(), context.getHand()) : actionResult;
    }

    @Override
    public Text getName(ItemStack stack) {
        return BlockDataComponent.getBlock(stack).getName();
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        BlockDataComponent.getBlock(stack).appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public boolean shouldShowOperatorBlockWarnings(ItemStack stack, @Nullable PlayerEntity player) {
        if (player == null || player.getPermissionLevel() < 2) return false;
        var nbtComponent = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
        if (nbtComponent == null) return false;
        var blockEntityType = nbtComponent.getRegistryValueOfId(player.getWorld().getRegistryManager(), RegistryKeys.BLOCK_ENTITY_TYPE);
        return blockEntityType != null && blockEntityType.canPotentiallyExecuteCommands();
    }

    public static ItemStack from(@NotNull BlockState state) {
        var result = VisibleBarriers.BLOCK_HOLDER_ITEM.getDefaultStack().mialib$set(VisibleBarriers.BLOCK, state.getBlock());
        var blockStateComponent = BlockStateComponent.DEFAULT;
        for (var property : state.getProperties()) blockStateComponent = blockStateComponent.with(property, state);
        return result.mialib$set(DataComponentTypes.BLOCK_STATE, blockStateComponent);
    }

    public ActionResult place(@NotNull ItemPlacementContext context) {
        if (!context.canPlace()) return ActionResult.FAIL;
        var state = this.getPlacementState(context);
        if (!context.getWorld().setBlockState(context.getBlockPos(), state, Block.REDRAW_ON_MAIN_THREAD | Block.NOTIFY_LISTENERS, 0)) return ActionResult.FAIL;
        var pos = context.getBlockPos();
        var world = context.getWorld();
        var player = context.getPlayer();
        var stack = context.getStack();
        var prevState = world.getBlockState(pos);
        if (prevState.isOf(state.getBlock())) {
            prevState = this.placeFromNbt(pos, world, stack, prevState);
            writeNbtToBlockEntity(world, player, pos, stack);
            var blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null) {
                blockEntity.readComponents(stack);
                blockEntity.markDirty();
            }
            prevState.getBlock().onPlaced(world, pos, prevState, player, stack);
            if (player instanceof ServerPlayerEntity serverPlayer) Criteria.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
        }
        var group = prevState.getSoundGroup();
        world.playSound(player, pos, prevState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, (group.getVolume() + 1.0F) / 2.0F, group.getPitch() * 0.8F);
        world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(player, prevState));
        stack.decrementUnlessCreative(1, player);
        return ActionResult.SUCCESS;
    }

    protected BlockState getPlacementState(@NotNull ItemPlacementContext context) {
        var block = BlockDataComponent.getBlock(context.getStack());
        var blockState = block.getPlacementState(context);
        if (blockState != null) return blockState;
        return block.getDefaultState();
    }

    private BlockState placeFromNbt(BlockPos pos, World world, @NotNull ItemStack stack, BlockState state) {
        var blockStateComponent = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
        if (blockStateComponent.isEmpty()) return state;
        var blockState = blockStateComponent.applyToState(state);
        if (blockState != state) world.setBlockState(pos, blockState, Block.REDRAW_ON_MAIN_THREAD | Block.NOTIFY_LISTENERS, 0);
        return blockState;
    }

    public static void writeNbtToBlockEntity(@NotNull World world, @Nullable PlayerEntity player, BlockPos pos, ItemStack stack) {
        if (world.isClient) return;
        var nbtComponent = stack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT);
        if (nbtComponent.isEmpty()) return;
        var blockEntityType = nbtComponent.getRegistryValueOfId(world.getRegistryManager(), RegistryKeys.BLOCK_ENTITY_TYPE);
        if (blockEntityType == null) return;
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null) return;
        var type = blockEntity.getType();
        if (type != blockEntityType) return;
        if (!type.canPotentiallyExecuteCommands() || player != null && player.isCreativeLevelTwoOp()) nbtComponent.applyToBlockEntity(blockEntity, world.getRegistryManager());
    }
}