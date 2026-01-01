package me.kous500.curvebuilding.fabric.client.render;

import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.*;

public class FilledRender extends Render {
    private static final FilledRender FILLED_RENDER = new FilledRender();

    public static FilledRender getInstance() {
        return FILLED_RENDER;
    }

    @Override
    void setRender(Matrix4f matrix, boolean isThroughWalls) {
        this.matrix = matrix;
        this.buffer = Tessellator.getInstance().getBuffer();
        this.buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        this.isThroughWalls = isThroughWalls;
        currentIsBuilding = true;
    }

    /**
     * Draws a filled block.
     *
     * @param color      Fill color
     * @param start      Start coordinates
     * @param dimensions Size
     */
    public void renderFilled(Color color, Vec3d start, Vec3d dimensions) {
        genericAABBRender(
                start,
                dimensions,
                color,
                buffer,
                (buffer, x1, y1, z1, x2, y2, z2, red, green, blue, alpha) -> {
                    buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha);

                    buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha);

                    buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha);

                    buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha);

                    buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha);

                    buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha);
                }
        );
    }
}
