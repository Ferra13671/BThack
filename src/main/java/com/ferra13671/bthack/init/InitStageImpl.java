package com.ferra13671.bthack.init;

import com.ferra13671.bthack.BThackClient;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class InitStageImpl extends AbstractInitStage {
    protected final List<AbstractInitStage> subStages = new ArrayList<>();
    protected final String name;
    @Getter
    protected AbstractInitStage currentSubStage = null;
    protected boolean finished = false;

    protected InitStageImpl(String name) {
        this.name = name;
    }

    public static InitStageImpl of(String name) {
        return new InitStageImpl(name);
    }

    public static InitStageImpl of(String name, Runnable runnable) {
        return new InitStageImpl(name) {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    @Override
    public final void start() {
        assertFinished();

        BThackClient.getInstance().getLogger().info("Invoking stage:".concat(getStageTree()));

        run();

        for (AbstractInitStage stage : this.subStages) {
            this.currentSubStage = stage;
            this.currentSubStage.start();
        }

        this.finished = true;
    }

    public void run() {}

    @Override
    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getStageTree() {
        return (this.parentStage == null ? "" : this.parentStage.getStageTree()).concat(" -> ").concat(getName());
    }

    @Override
    public AbstractInitStage registerFirst(AbstractInitStage stage) {
        assertFinished();

        this.subStages.addFirst(stage);
        stage.parentStage = this;
        return stage;
    }

    @Override
    public AbstractInitStage registerLast(AbstractInitStage stage) {
        assertFinished();

        this.subStages.addLast(stage);
        stage.parentStage = this;
        return stage;
    }

    @Override
    public AbstractInitStage registerBefore(String requiredStage, AbstractInitStage stage) {
        assertFinished();

        int requiredStagePos = findInternal(requiredStage);

        if (requiredStagePos == -1)
            throw new NullPointerException(String.format("Cannot find required stage '%s'", requiredStage));

        return registerAt(requiredStagePos - 1, stage);
    }

    @Override
    public AbstractInitStage registerAfter(String requiredStage, AbstractInitStage stage) {
        assertFinished();

        int requiredStagePos = findInternal(requiredStage);

        if (requiredStagePos == -1)
            throw new NullPointerException(String.format("Cannot find required stage '%s'", requiredStage));

        return registerAt(requiredStagePos + 1, stage);
    }

    @Override
    public AbstractInitStage registerAt(int pos, AbstractInitStage stage) {
        if (pos < 0 || pos > this.subStages.size() - 1)
            throw new IllegalStateException(String.format("position(%s) must be between %s and %s", pos, 0, this.subStages.size() - 1));

        this.subStages.add(pos, stage);
        stage.parentStage = this;
        return stage;
    }

    @Override
    public AbstractInitStage find(String name) {
        int stagePos = findInternal(name);

        return stagePos == -1 ? null : this.subStages.get(stagePos);
    }

    private int findInternal(String name) {
        for (int i = 0; i < this.subStages.size(); i++) {
            AbstractInitStage stage = this.subStages.get(i);
            if (stage.getName().equals(name))
                return i;
        }

        return -1;
    }

    private void assertFinished() {
        if (isFinished())
            throw new IllegalStateException("InitProvider has finished initializing");
    }
}
