package xyz.amymialee.visiblebarriers.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

public record BlockDataComponent(Block block) {
    public static final Codec<Block> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.optionalFieldOf("block", Registries.BLOCK.getDefaultId().toString()).forGetter((block) -> Registries.BLOCK.getId(block).toString())).apply(instance, (id) -> Registries.BLOCK.get(Identifier.of(id))));
    public static final PacketCodec<ByteBuf, Block> PACKET_CODEC = PacketCodec.tuple(Identifier.PACKET_CODEC, Registries.BLOCK::getId, Registries.BLOCK::get);

    public static Block getBlock(@NotNull ItemStack stack) {
        return stack.getOrDefault(VisibleBarriers.BLOCK, Blocks.AIR);
    }
}