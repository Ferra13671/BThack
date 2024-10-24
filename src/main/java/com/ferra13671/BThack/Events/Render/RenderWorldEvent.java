package com.ferra13671.BThack.Events.Render;

import com.ferra13671.BThack.Core.Render.BThackWorldRenderContext;
import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;

public class RenderWorldEvent extends Event {
    private final Phase phase;
    private final BThackWorldRenderContext renderContext;

    public RenderWorldEvent(Phase phase, BThackWorldRenderContext renderContext) {
        this.phase = phase;
        this.renderContext = renderContext;
    }

    public static class Start extends RenderWorldEvent {
        public Start(BThackWorldRenderContext renderContext) {
            super(Phase.START, renderContext);
        }
    }

    public static class AfterSetup extends RenderWorldEvent {
        public AfterSetup(BThackWorldRenderContext renderContext) {
            super(Phase.AFTER_SETUP, renderContext);

        }
    }

    public static class BeforeEntities extends RenderWorldEvent {
        public BeforeEntities(BThackWorldRenderContext renderContext) {
            super(Phase.BEFORE_ENTITIES, renderContext);
        }
    }

    public static class AfterEntities extends RenderWorldEvent {
        public AfterEntities(BThackWorldRenderContext renderContext) {
            super(Phase.AFTER_ENTITIES, renderContext);
        }
    }

    public static class BeforeBlockOutline extends RenderWorldEvent {
        private final HitResult hitResult;

        public BeforeBlockOutline(BThackWorldRenderContext renderContext, @Nullable HitResult hitResult) {
            super(Phase.BEFORE_BLOCK_OUTLINE, renderContext);
            this.hitResult = hitResult;
        }

        public HitResult getHitResult() {
            return this.hitResult;
        }
    }

    public static class BlockOutline extends RenderWorldEvent {
        private final BThackWorldRenderContext blockOutlineContext;

        public BlockOutline(BThackWorldRenderContext renderContext, BThackWorldRenderContext blockOutlineContext) {
            super(Phase.BLOCK_OUTLINE, renderContext);
            this.blockOutlineContext = blockOutlineContext;
        }

        public BThackWorldRenderContext getBlockOutlineContext() {
            return this.blockOutlineContext;
        }
    }

    public static class BeforeDebugRender extends RenderWorldEvent {
        public BeforeDebugRender(BThackWorldRenderContext renderContext) {
            super(Phase.BEFORE_DEBUG_RENDER, renderContext);
        }
    }

    public static class AfterTranslucent extends RenderWorldEvent {
        public AfterTranslucent(BThackWorldRenderContext renderContext) {
            super(Phase.AFTER_TRANSLUCENT, renderContext);
        }
    }

    public static class Last extends RenderWorldEvent {
        public Last(BThackWorldRenderContext renderContext) {
            super(Phase.LAST, renderContext);
        }
    }

    public static class End extends RenderWorldEvent {
        public End(BThackWorldRenderContext renderContext) {
            super(Phase.END, renderContext);
        }
    }



    public Phase getRenderPhase() {
        return this.phase;
    }

    public BThackWorldRenderContext getWorldRenderContext() {
        return this.renderContext;
    }

    public enum Phase {
        START,
        AFTER_SETUP,
        BEFORE_ENTITIES,
        AFTER_ENTITIES,
        BEFORE_BLOCK_OUTLINE,
        BLOCK_OUTLINE,
        BEFORE_DEBUG_RENDER,
        AFTER_TRANSLUCENT,
        LAST,
        END
    }
}
