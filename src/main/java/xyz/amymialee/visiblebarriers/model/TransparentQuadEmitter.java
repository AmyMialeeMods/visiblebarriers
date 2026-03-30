//package xyz.amymialee.visiblebarriers.model;
//
//import net.fabricmc.fabric.api.renderer.v1.mesh.*;
//import net.fabricmc.fabric.api.util.TriState;
//import net.minecraft.client.renderer.block.model.BakedQuad;
//import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
//import net.minecraft.core.Direction;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.joml.Vector2f;
//import org.joml.Vector3f;
//import org.joml.Vector3fc;
//
//public record TransparentQuadEmitter(QuadEmitter emitter, float transparency) implements QuadEmitter {
//
//    @Override
//    public QuadEmitter pos(int vertexIndex, float x, float y, float z) {
//        return this.emitter.pos(vertexIndex, x, y, z);
//    }
//
//    @Override
//    public QuadEmitter color(int vertexIndex, int color) {
//        return this.emitter.color(vertexIndex, color);
//    }
//
//    @Override
//    public QuadEmitter uv(int vertexIndex, float u, float v) {
//        return this.emitter.uv(vertexIndex, u, v);
//    }
//
//    @Override
//    public QuadEmitter lightmap(int vertexIndex, int lightmap) {
//        return this.emitter.lightmap(vertexIndex, lightmap);
//    }
//
//    @Override
//    public QuadEmitter normal(int vertexIndex, float x, float y, float z) {
//        return this.emitter.normal(vertexIndex, x, y, z);
//    }
//
//    @Override
//    public QuadEmitter nominalFace(@Nullable Direction face) {
//        return this.emitter.nominalFace(face);
//    }
//
//    @Override
//    public QuadEmitter cullFace(@Nullable Direction face) {
//        return this.emitter.cullFace(face);
//    }
//
//    @Override
//    public QuadEmitter renderLayer(@Nullable ChunkSectionLayer renderLayer) {
//        return this.emitter.renderLayer(ChunkSectionLayer.TRANSLUCENT);
//    }
//
//    @Override
//    public QuadEmitter emissive(boolean emissive) {
//        return this.emitter.emissive(emissive);
//    }
//
//    @Override
//    public QuadEmitter diffuseShade(boolean shade) {
//        return this.emitter.diffuseShade(shade);
//    }
//
//    @Override
//    public QuadEmitter ambientOcclusion(TriState ao) {
//        return this.emitter.ambientOcclusion(ao);
//    }
//
//    @Override
//    public QuadEmitter glint(ItemStackRenderState.@Nullable FoilType glint) {
//        return this.emitter.glint(glint);
//    }
//
//    @Override
//    public QuadEmitter shadeMode(ShadeMode mode) {
//        return this.emitter.shadeMode(mode);
//    }
//
//    @Override
//    public QuadEmitter atlas(QuadAtlas quadAtlas) {
//        return this.emitter.atlas(quadAtlas);
//    }
//
//    @Override
//    public QuadEmitter tintIndex(int tintIndex) {
//        return this.emitter.tintIndex(tintIndex);
//    }
//
//    @Override
//    public QuadEmitter tag(int tag) {
//        return this.emitter.tag(tag);
//    }
//
//    @Override
//    public QuadEmitter copyFrom(QuadView quad) {
//        return this.emitter.copyFrom(quad);
//    }
//
//    @Override
//    public QuadEmitter fromBakedQuad(BakedQuad quad) {
//        return this.emitter.fromBakedQuad(quad);
//    }
//
//    @Override
//    public void pushTransform(QuadTransform transform) {
//        this.emitter.pushTransform(transform);
//    }
//
//    @Override
//    public void popTransform() {
//        this.emitter.popTransform();
//    }
//
//    @Override
//    public QuadEmitter emit() {
//        for (int i = 0; i < 4; i++) {
//            int color = this.emitter.color(i);
//            this.emitter.color(i, (color & 0x00FFFFFF) | ((int)((1.0f - this.transparency) * 255.0f) << 24));
//        }
//        this.emitter.renderLayer(ChunkSectionLayer.TRANSLUCENT);
//        return this.emitter.emit();
//    }
//
//    @Override
//    public float x(int vertexIndex) {
//        return this.emitter.x(vertexIndex);
//    }
//
//    @Override
//    public float y(int vertexIndex) {
//        return this.emitter.y(vertexIndex);
//    }
//
//    @Override
//    public float z(int vertexIndex) {
//        return this.emitter.z(vertexIndex);
//    }
//
//    @Override
//    public float posByIndex(int vertexIndex, int coordinateIndex) {
//        return this.emitter.posByIndex(vertexIndex, coordinateIndex);
//    }
//
//    @Override
//    public Vector3f copyPos(int vertexIndex, @Nullable Vector3f target) {
//        return this.emitter.copyPos(vertexIndex, target);
//    }
//
//    @Override
//    public int color(int vertexIndex) {
//        return this.emitter.color(vertexIndex);
//    }
//
//    @Override
//    public float u(int vertexIndex) {
//        return this.emitter.u(vertexIndex);
//    }
//
//    @Override
//    public float v(int vertexIndex) {
//        return this.emitter.v(vertexIndex);
//    }
//
//    @Override
//    public Vector2f copyUv(int vertexIndex, @Nullable Vector2f target) {
//        return this.emitter.copyUv(vertexIndex, target);
//    }
//
//    @Override
//    public int lightmap(int vertexIndex) {
//        return this.emitter.lightmap(vertexIndex);
//    }
//
//    @Override
//    public boolean hasNormal(int vertexIndex) {
//        return this.emitter.hasNormal(vertexIndex);
//    }
//
//    @Override
//    public float normalX(int vertexIndex) {
//        return this.emitter.normalX(vertexIndex);
//    }
//
//    @Override
//    public float normalY(int vertexIndex) {
//        return this.emitter.normalY(vertexIndex);
//    }
//
//    @Override
//    public float normalZ(int vertexIndex) {
//        return this.emitter.normalZ(vertexIndex);
//    }
//
//    @Override
//    public @Nullable Vector3f copyNormal(int vertexIndex, @Nullable Vector3f target) {
//        return this.emitter.copyNormal(vertexIndex, target);
//    }
//
//    @Override
//    public Vector3fc faceNormal() {
//        return this.emitter.faceNormal();
//    }
//
//    @Override
//    public @NotNull Direction lightFace() {
//        return this.emitter.lightFace();
//    }
//
//    @Override
//    public @Nullable Direction nominalFace() {
//        return this.emitter.nominalFace();
//    }
//
//    @Override
//    public @Nullable Direction cullFace() {
//        return this.emitter.cullFace();
//    }
//
//    @Override
//    public @Nullable ChunkSectionLayer renderLayer() {
//        return this.emitter.renderLayer();
//    }
//
//    @Override
//    public boolean emissive() {
//        return this.emitter.emissive();
//    }
//
//    @Override
//    public boolean diffuseShade() {
//        return this.emitter.diffuseShade();
//    }
//
//    @Override
//    public TriState ambientOcclusion() {
//        return this.emitter.ambientOcclusion();
//    }
//
//    @Override
//    public ItemStackRenderState.@Nullable FoilType glint() {
//        return this.emitter.glint();
//    }
//
//    @Override
//    public ShadeMode shadeMode() {
//        return this.emitter.shadeMode();
//    }
//
//    @Override
//    public QuadAtlas atlas() {
//        return this.emitter.atlas();
//    }
//
//    @Override
//    public int tintIndex() {
//        return this.emitter.tintIndex();
//    }
//
//    @Override
//    public int tag() {
//        return this.emitter.tag();
//    }
//}
