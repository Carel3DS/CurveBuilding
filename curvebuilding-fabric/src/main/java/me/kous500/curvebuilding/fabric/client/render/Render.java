package me.kous500.curvebuilding.fabric.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.function.Consumer;

public abstract class Render {
    static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Logger log = LoggerFactory.getLogger(Render.class);

    /**
     * Executes the rendering process of the action.
     *
     * @param renderThroughWalls Whether to render through walls
     * @param render The render to use
     * @param action The content of the rendering process
     */
    public static <T extends Render> void setRender(Matrix4f matrix, boolean renderThroughWalls, T render, RenderSetAction<T> action) {
        render.setRender(matrix, renderThroughWalls);
        action.run(render);
        render.rendering();
    }

    private static Vec3d transformVec3d(Vec3d in) {
        Camera camera = CLIENT.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();
        return in.subtract(camPos);
    }

    boolean isThroughWalls;
    boolean currentIsBuilding = false;
    boolean said = false;
    BufferBuilder buffer;
    Matrix4f matrix;

    abstract void setRender(Matrix4f matrix, boolean isThroughWalls);

    void rendering() {
        if (!currentIsBuilding) return;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(isThroughWalls ? GL11.GL_ALWAYS : GL11.GL_LEQUAL);

        if(!said){
            said = true;
            if (RenderSystem.getShader() != null){
                log.info("the RenderSystem has a shader");
                log.info(RenderSystem.getShader().toString());
            }else{
                log.warn("There is no shader in the RenderSystem");
            }
        }
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        BufferBuilder.RenderedBuffer builtBuffer = buffer.end();
        if (builtBuffer != null) {
            BufferRenderer.draw(builtBuffer);
        }
        builtBuffer.close();

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();

        currentIsBuilding = false;
    }

    void genericAABBRender(Vec3d start, Vec3d dimensions, Color color, BufferBuilder buffer, RenderAction action) {
        if (!currentIsBuilding) return;

        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Vec3d vec3d = transformVec3d(start);
        Vec3d end = vec3d.add(dimensions);
        float x1 = (float) vec3d.x;
        float y1 = (float) vec3d.y;
        float z1 = (float) vec3d.z;
        float x2 = (float) end.x;
        float y2 = (float) end.y;
        float z2 = (float) end.z;

        Consumer<BufferBuilder> runner = bufferBuilder -> action.run(bufferBuilder, x1, y1, z1, x2, y2, z2, red, green, blue, alpha);
        runner.accept(buffer);
    }

    public interface RenderSetAction<T extends Render> {
        void run(T a);
    }

    interface RenderAction {
        void run(BufferBuilder buffer, float x, float y, float z, float x1, float y1, float z1, float red, float green, float blue, float alpha);
    }
}
