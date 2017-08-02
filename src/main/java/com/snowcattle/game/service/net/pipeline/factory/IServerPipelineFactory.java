package com.snowcattle.game.service.net.pipeline.factory;


import com.snowcattle.game.service.net.pipeline.IServerPipeLine;

/**
 * Created by jiangwenping on 17/2/14.
 */
public interface IServerPipelineFactory {
    public IServerPipeLine createServerPipeLine();
}
