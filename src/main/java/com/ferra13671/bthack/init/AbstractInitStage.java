package com.ferra13671.bthack.init;

import lombok.Getter;

public abstract class AbstractInitStage {
    @Getter
    protected AbstractInitStage parentStage;

    public abstract boolean isFinished();

    public abstract String getName();

    public abstract String getStageTree();

    public abstract void start();

    public abstract AbstractInitStage registerFirst(AbstractInitStage stage);

    public abstract AbstractInitStage registerLast(AbstractInitStage stage);

    public abstract AbstractInitStage registerBefore(String requiredStage, AbstractInitStage stage);

    public abstract AbstractInitStage registerAfter(String requiredStage, AbstractInitStage stage);

    public abstract AbstractInitStage registerAt(int pos, AbstractInitStage stage);

    public abstract AbstractInitStage find(String name);
}
