package amymialee.visiblebarriers;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.InfestedBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class VisibleBarriersCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = literal("visiblebarriers")
                .requires((player) -> player.hasPermissionLevel(4))

                .then(literal("lock")
                        .then(argument("password", StringArgumentType.string())
                                .then(argument("radius", IntegerArgumentType.integer(0, 200))
                                        .executes(VisibleBarriersCommand::lock))))

                .then(literal("unlock")
                        .then(argument("radius", IntegerArgumentType.integer(0, 200))
                                .executes(VisibleBarriersCommand::unlock)))

                .then(literal("infest")
                        .then(argument("radius", IntegerArgumentType.integer(0, 200))
                                .executes(VisibleBarriersCommand::infest)))

                .then(literal("extirpate")
                        .then(argument("radius", IntegerArgumentType.integer(0, 200))
                                .executes(VisibleBarriersCommand::extirpate)));

        dispatcher.register(literalArgumentBuilder);
    }

    private static int lock(CommandContext<ServerCommandSource> context) {
        int count = 0;
        Entity source = context.getSource().getEntity();
        World world = source.world;
        String password = StringArgumentType.getString(context, "password");
        int radius = IntegerArgumentType.getInteger(context, "radius");
        BlockPos pos = source.getBlockPos();
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos2 = pos.add(x, y, z);
                    BlockEntity blockEntity = world.getBlockEntity(pos2);
                    if (blockEntity instanceof LockableContainerBlockEntity) {
                        NbtCompound compound = new NbtCompound();
                        blockEntity.writeNbt(compound);
                        if (Objects.equals(compound.getString("Lock"), "")) {
                            count++;
                        }
                        compound.putString("Lock", password);
                        blockEntity.readNbt(compound);
                    }
                }
            }
        }
        source.sendSystemMessage((new LiteralText(count + " containers locked.").formatted(Formatting.GRAY, Formatting.ITALIC)), source.getUuid());
        return 1;
    }

    private static int unlock(CommandContext<ServerCommandSource> context) {
        int count = 0;
        Entity source = context.getSource().getEntity();
        World world = source.world;
        int radius = IntegerArgumentType.getInteger(context, "radius");
        BlockPos pos = source.getBlockPos();
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos2 = pos.add(x, y, z);
                    BlockEntity blockEntity = world.getBlockEntity(pos2);
                    if (blockEntity instanceof LockableContainerBlockEntity) {
                        NbtCompound compound = new NbtCompound();
                        blockEntity.writeNbt(compound);
                        if (!Objects.equals(compound.getString("Lock"), "")) {
                            count++;
                        }
                        compound.remove("Lock");
                        blockEntity.readNbt(compound);
                    }
                }
            }
        }
        source.sendSystemMessage((new LiteralText(count + " containers unlocked.").formatted(Formatting.GRAY, Formatting.ITALIC)), source.getUuid());
        return 1;
    }

    private static int infest(CommandContext<ServerCommandSource> context) {
        int count = 0;
        Entity source = context.getSource().getEntity();
        World world = source.world;
        int radius = IntegerArgumentType.getInteger(context, "radius");
        BlockPos pos = source.getBlockPos();
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos2 = pos.add(x, y, z);
                    BlockState blockState = world.getBlockState(pos2);
                    if (InfestedBlock.isInfestable(blockState)) {
                        count++;
                        world.setBlockState(pos2, InfestedBlock.fromRegularState(blockState));
                    }
                }
            }
        }
        source.sendSystemMessage((new LiteralText(count + " blocks infested.").formatted(Formatting.GRAY, Formatting.ITALIC)), source.getUuid());
        return 1;
    }

    private static int extirpate(CommandContext<ServerCommandSource> context) {
        int count = 0;
        Entity source = context.getSource().getEntity();
        World world = source.world;
        int radius = IntegerArgumentType.getInteger(context, "radius");
        BlockPos pos = source.getBlockPos();
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos2 = pos.add(x, y, z);
                    BlockState blockState = world.getBlockState(pos2);
                    if (blockState.getBlock() instanceof InfestedBlock) {
                        count++;
                        world.setBlockState(pos2, ((InfestedBlock) blockState.getBlock()).toRegularState(blockState));
                    }
                }
            }
        }
        source.sendSystemMessage((new LiteralText(count + " blocks extirpated.").formatted(Formatting.GRAY, Formatting.ITALIC)), source.getUuid());
        return 1;
    }
}