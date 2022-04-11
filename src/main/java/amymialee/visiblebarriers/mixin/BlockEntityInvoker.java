package amymialee.visiblebarriers.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockEntity.class)
public interface BlockEntityInvoker {
//    @Invoker("writeNbt")
//    void writeNbt(NbtCompound nbt);
}
