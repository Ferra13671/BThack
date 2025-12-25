package com.ferra13671.bthack.init;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class InitStage implements IInitStage {
    private final List<IInitStage> subStages = new ArrayList<>();
    private final String name;
    @Getter
    private IInitStage currentSubStage = null;
    private boolean finished = false;

    protected InitStage(String name) {
        this.name = name;
    }

    public static InitStage of(String name) {
        return new InitStage(name) {
            @Override
            public void run() {
            }
        };
    }

    public static InitStage of(String name, Runnable runnable) {
        return new InitStage(name) {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    @Override
    public final void start() {
        assertFinished();

        for (IInitStage stage : this.subStages) {
            this.currentSubStage = stage;
            this.currentSubStage.start();
        }

        this.finished = true;
    }

    public abstract void run();

    @Override
    public IInitStage registerFirst(IInitStage stage) {
        assertFinished();

        this.subStages.addFirst(stage);
        return stage;
    }

    @Override
    public IInitStage registerLast(IInitStage stage) {
        assertFinished();

        this.subStages.addLast(stage);
        return stage;
    }

    @Override
    public IInitStage registerBefore(String requiredStage, IInitStage stage) {
        assertFinished();

        int requiredStagePos = findInternal(requiredStage);

        if (requiredStagePos == -1)
            throw new NullPointerException(String.format("Cannot find required stage '%s'", requiredStage));

        return registerAt(requiredStagePos - 1, stage);
    }

    @Override
    public IInitStage registerAfter(String requiredStage, IInitStage stage) {
        assertFinished();

        int requiredStagePos = findInternal(requiredStage);

        if (requiredStagePos == -1)
            throw new NullPointerException(String.format("Cannot find required stage '%s'", requiredStage));

        return registerAt(requiredStagePos + 1, stage);
    }

    @Override
    public IInitStage registerAt(int pos, IInitStage stage) {
        if (pos < 0 || pos > this.subStages.size() - 1)
            throw new IllegalStateException(String.format("position(%s) must be between %s and %s", pos, 0, this.subStages.size() - 1));

        this.subStages.add(pos, stage);
        return stage;
    }

    @Override
    public IInitStage find(String name) {
        int stagePos = findInternal(name);

        return stagePos == -1 ? null : this.subStages.get(stagePos);
    }

    private int findInternal(String name) {
        for (int i = 0; i < this.subStages.size(); i++) {
            IInitStage stage = this.subStages.get(i);
            if (stage.getName().equals(name))
                return i;
        }

        return -1;
    }

    private void assertFinished() {
        if (isFinished())
            throw new IllegalStateException("InitProvider has finished initializing");
    }

    @Override
    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
