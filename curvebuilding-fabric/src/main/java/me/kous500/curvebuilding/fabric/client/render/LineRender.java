package me.kous500.curvebuilding.fabric.client.render;

import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.*;

public class LineRender extends Render {
    private static final LineRender LINE_RENDER = new LineRender();

    public static LineRender getInstance() {
        return LINE_RENDER;
    }

    @Override
    void setRender(Matrix4f matrix, boolean isThroughWalls) {
        this.matrix = matrix;
        this.buffer = Tessellator.getInstance().getBuffer();
        this.buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        this.isThroughWalls = isThroughWalls;
        currentIsBuilding = true;
    }

    /**
     * Draws crossed lines inside a cuboid.
     *
     * @param color      Line color
     * @param start      Start coordinates
     * @param dimensions Size
     */
    public void addCrossing(Color color, Vec3d start, Vec3d dimensions) {
        Vec3d end = start.add(dimensions);
        double x1 = start.x;
        double y1 = start.y;
        double z1 = start.z;
        double x2 = end.x;
        double y2 = end.y;
        double z2 = end.z;

        addLine(color, new Vec3d(x1, y1, z1), new Vec3d(x2, y2, z2));
        addLine(color, new Vec3d(x1, y1, z2), new Vec3d(x2, y2, z1));
        addLine(color, new Vec3d(x1, y2, z1), new Vec3d(x2, y1, z2));
        addLine(color, new Vec3d(x2, y1, z1), new Vec3d(x1, y2, z2));
    }

    /**
     * Draws the outline of the block.
     *
     * @param color      Line color
     * @param start      Start coordinates
     * @param dimensions Size
     */
    public void addOutline(Color color, Vec3d start, Vec3d dimensions) {
        genericAABBRender(
                start,
                dimensions,
                color,
                buffer,
                (buffer, x1, y1, z1, x2, y2, z2, red, green, blue, alpha) -> {
                    buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha);

                    buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha);

                    buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha);

                    buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha);

                    buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha);

                    buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha);
                }
        );
    }

    /**
     * Draws a line.
     *
     * @param color    Line color
     * @param start    Start coordinates
     * @param end      End coordinates
     */
    public void addLine(Color color, Vec3d start, Vec3d end) {
        if (!currentIsBuilding) return;

        genericAABBRender(
                start,
                end.subtract(start),
                color,
                buffer,
                (buffer, x, y, z, x1, y1, z1, red, green, blue, alpha) -> {
                    buffer.vertex(matrix, x, y, z).color(red, green, blue, alpha);
                    buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha);
                }
        );
    }
}