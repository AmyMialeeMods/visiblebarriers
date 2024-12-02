package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.amymialee.visiblebarriers.access.EntityRenderStateAccess;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateAccess {

    @Unique
    private Entity entity;

    @Override
    public void visiblebarriers$setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity visiblebarriers$getEntity() {
        return this.entity;
    }

}
