package com.ferra13671.bthack.init;

public interface IInitStage {

    void start();

    IInitStage registerFirst(IInitStage stage);

    IInitStage registerLast(IInitStage stage);

    IInitStage registerBefore(String requiredStage, IInitStage stage);

    IInitStage registerAfter(String requiredStage, IInitStage stage);

    IInitStage registerAt(int pos, IInitStage stage);

    IInitStage find(String name);

    boolean isFinished();

    String getName();
}
