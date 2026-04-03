package xyz.amymialee.visiblebarriers.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.*;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;

public record TransparentQuadEmitter(QuadEmitter emitter, float transparency) implements QuadEmitter {

    @Override
    public @NonNull QuadEmitter pos(int vertexIndex, float x, float y, float z) {
        return this.emitter.pos(vertexIndex, x, y, z);
    }

    @Override
    public @NonNull QuadEmitter color(int vertexIndex, int color) {
        return this.emitter.color(vertexIndex, color);
    }

    @Override
    public @NonNull QuadEmitter uv(int vertexIndex, float u, float v) {
        return this.emitter.uv(vertexIndex, u, v);
    }

    @Override
    public @NonNull QuadEmitter lightmap(int vertexIndex, int lightmap) {
        return this.emitter.lightmap(vertexIndex, lightmap);
    }

    @Override
    public @NonNull QuadEmitter normal(int vertexIndex, float x, float y, float z) {
        return this.emitter.normal(vertexIndex, x, y, z);
    }

    @Override
    public @NonNull QuadEmitter nominalFace(@Nullable Direction face) {
        return this.emitter.nominalFace(face);
    }

    @Override
    public @NonNull QuadEmitter cullFace(@Nullable Direction face) {
        return this.emitter.cullFace(face);
    }

    @Override
    public @NonNull QuadEmitter chunkLayer(@NonNull ChunkSectionLayer chunkSectionLayer) {
        return this.emitter.chunkLayer(chunkSectionLayer);
    }

    @Override
    public @NonNull QuadEmitter itemRenderType(@NonNull RenderType renderType) {
        return this.emitter.itemRenderType(renderType);
    }

    @Override
    public @NonNull QuadEmitter emissive(boolean emissive) {
        return this.emitter.emissive(emissive);
    }

    @Override
    public @NonNull QuadEmitter diffuseShade(boolean shade) {
        return this.emitter.diffuseShade(shade);
    }

    @Override
    public @NonNull QuadEmitter ambientOcclusion(@NonNull TriState ao) {
        return this.emitter.ambientOcclusion(ao);
    }

    @Override
    public @NonNull QuadEmitter foilType(@Nullable ItemStackRenderState.FoilType foilType) {
        return this.emitter.foilType(foilType);
    }

    @Override
    public @NonNull QuadEmitter shadeMode(@NonNull ShadeMode mode) {
        return this.emitter.shadeMode(mode);
    }

    @Override
    public @NonNull QuadEmitter animated(boolean b) {
        return this.emitter.animated(b);
    }

    @Override
    public @NonNull QuadEmitter atlas(@NonNull QuadAtlas quadAtlas) {
        return this.emitter.atlas(quadAtlas);
    }

    @Override
    public @NonNull QuadEmitter tintIndex(int tintIndex) {
        return this.emitter.tintIndex(tintIndex);
    }

    @Override
    public @NonNull QuadEmitter tag(int tag) {
        return this.emitter.tag(tag);
    }

    @Override
    public @NonNull QuadEmitter copyFrom(@NonNull QuadView quad) {
        return this.emitter.copyFrom(quad);
    }

    @Override
    public @NonNull QuadEmitter fromBakedQuad(@NonNull BakedQuad quad) {
        return this.emitter.fromBakedQuad(quad);
    }

    @Override
    public @NonNull QuadEmitter clear() {
        return this.emitter.clear();
    }

    @Override
    public void pushTransform(@NonNull QuadTransform transform) {
        this.emitter.pushTransform(transform);
    }

    @Override
    public void popTransform() {
        this.emitter.popTransform();
    }

    @Override
    public void buffer(int i, @NonNull VertexConsumer vertexConsumer) {
        this.emitter.buffer(i, vertexConsumer);
    }

    @Override
    public void buffer(int i, PoseStack.@NonNull Pose pose, @NonNull VertexConsumer vertexConsumer) {
        this.emitter.buffer(i, pose, vertexConsumer);
    }

    @Override
    public @NonNull QuadEmitter emit() {
        for (int i = 0; i < 4; i++) {
            int color = this.emitter.color(i);
            this.emitter.color(i, (color & 0x00FFFFFF) | ((int)((1.0f - this.transparency) * 255.0f) << 24));
        }
        this.emitter.chunkLayer(ChunkSectionLayer.TRANSLUCENT);
        return this.emitter.emit();
    }

    @Override
    public float x(int vertexIndex) {
        return this.emitter.x(vertexIndex);
    }

    @Override
    public float y(int vertexIndex) {
        return this.emitter.y(vertexIndex);
    }

    @Override
    public float z(int vertexIndex) {
        return this.emitter.z(vertexIndex);
    }

    @Override
    public float posByIndex(int vertexIndex, int coordinateIndex) {
        return this.emitter.posByIndex(vertexIndex, coordinateIndex);
    }

    @Override
    public @NonNull Vector3f copyPos(int vertexIndex, @Nullable Vector3f target) {
        return this.emitter.copyPos(vertexIndex, target);
    }

    @Override
    public int color(int vertexIndex) {
        return this.emitter.color(vertexIndex);
    }

    @Override
    public float u(int vertexIndex) {
        return this.emitter.u(vertexIndex);
    }

    @Override
    public float v(int vertexIndex) {
        return this.emitter.v(vertexIndex);
    }

    @Override
    public @NonNull Vector2f copyUv(int vertexIndex, @Nullable Vector2f target) {
        return this.emitter.copyUv(vertexIndex, target);
    }

    @Override
    public int lightmap(int vertexIndex) {
        return this.emitter.lightmap(vertexIndex);
    }

    @Override
    public boolean hasNormal(int vertexIndex) {
        return this.emitter.hasNormal(vertexIndex);
    }

    @Override
    public float normalX(int vertexIndex) {
        return this.emitter.normalX(vertexIndex);
    }

    @Override
    public float normalY(int vertexIndex) {
        return this.emitter.normalY(vertexIndex);
    }

    @Override
    public float normalZ(int vertexIndex) {
        return this.emitter.normalZ(vertexIndex);
    }

    @Override
    public @Nullable Vector3f copyNormal(int vertexIndex, @Nullable Vector3f target) {
        return this.emitter.copyNormal(vertexIndex, target);
    }

    @Override
    public @NonNull Vector3fc faceNormal() {
        return this.emitter.faceNormal();
    }

    @Override
    public @NotNull Direction lightFace() {
        return this.emitter.lightFace();
    }

    @Override
    public @Nullable Direction nominalFace() {
        return this.emitter.nominalFace();
    }

    @Override
    public @Nullable Direction cullFace() {
        return this.emitter.cullFace();
    }

    @Override
    public @NonNull ChunkSectionLayer chunkLayer() {
        return this.emitter.chunkLayer();
    }

    @Override
    public @NonNull RenderType itemRenderType() {
        return this.emitter.itemRenderType();
    }

    @Override
    public boolean emissive() {
        return this.emitter.emissive();
    }

    @Override
    public boolean diffuseShade() {
        return this.emitter.diffuseShade();
    }

    @Override
    public @NonNull TriState ambientOcclusion() {
        return this.emitter.ambientOcclusion();
    }

    @Override
    public @Nullable ItemStackRenderState.FoilType foilType() {
        return this.emitter.foilType();
    }

    @Override
    public @NonNull ShadeMode shadeMode() {
        return this.emitter.shadeMode();
    }

    @Override
    public boolean animated() {
        return false;
    }

    @Override
    public @NonNull QuadAtlas atlas() {
        return this.emitter.atlas();
    }

    @Override
    public int tintIndex() {
        return this.emitter.tintIndex();
    }

    @Override
    public int tag() {
        return this.emitter.tag();
    }
}
