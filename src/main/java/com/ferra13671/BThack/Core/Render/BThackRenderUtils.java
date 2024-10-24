package com.ferra13671.BThack.Core.Render;

import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public final class BThackRenderUtils implements Mc {

    public static final Matrix4f lastWorldMatrix = new Matrix4f();
    public static final Matrix4f lastProjMatrix = new Matrix4f();
    public static final Matrix4f lastModViewMatrix = new Matrix4f();

    public static Vec3d worldCordsToScreenCords(Vec3d pos) {
        Camera camera = mc.getEntityRenderDispatcher().camera;
        int displayHeight = mc.getWindow().getHeight();
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        Vector3f target = new Vector3f();

        double deltaX = pos.x - camera.getPos().x;
        double deltaY = pos.y - camera.getPos().y;
        double deltaZ = pos.z - camera.getPos().z;

        Vector4f transformedCoordinates = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1.f).mul(lastWorldMatrix);
        Matrix4f matrixProj = new Matrix4f(lastProjMatrix);
        Matrix4f matrixModel = new Matrix4f(lastModViewMatrix);
        matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);

        return new Vec3d(target.x / mc.getWindow().getScaleFactor(), (displayHeight - target.y) / mc.getWindow().getScaleFactor(), target.z);
    }
}
