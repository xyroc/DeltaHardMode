package xiroc.deltahardmode.common.util;

import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Unit;
import xiroc.deltahardmode.DeltaHardMode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class DataReloadListener implements IFutureReloadListener {

    @Override
    public CompletableFuture<Void> reload(IStage stage, IResourceManager resourceManager, IProfiler preparationsProfiler, IProfiler reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return stage.markCompleteAwaitingOthers(Unit.INSTANCE).thenRunAsync(() -> {
            DeltaHardMode.LOGGER.info("Loading data");
            ConfigCache.load(resourceManager);
            DeltaHardMode.LOGGER.info("Finished data loading");
        });
    }
}
