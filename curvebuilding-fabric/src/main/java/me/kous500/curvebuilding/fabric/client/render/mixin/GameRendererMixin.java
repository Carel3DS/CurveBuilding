package me.kous500.curvebuilding.fabric.client.render.mixin;

import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.kous500.curvebuilding.fabric.client.render.RenderPreview.RenderPosData;

@Mixin(WorldRenderer.class)
public abstract class GameRendererMixin {

    @Inject(at = @At("RETURN"), method =
            "render(" +
                    "Lnet/minecraft/client/util/ObjectAllocator;" +
                    "Lnet/minecraft/client/render/RenderTickCounter;" +
                    "Z" +
                    "Lnet/minecraft/client/render/Camera;" +
                    "Lnet/minecraft/client/render/GameRenderer;" +
                    "Lorg/joml/Matrix4f;" +
                    "Lorg/joml/Matrix4f;" +
                    ")V"
    )
    private void renderer_postWorldRender(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        RenderPosData(matrix4f);
    }
}
