package io.github.thebusybiscuit.slimefun4.core.services;

import io.github.thebusybiscuit.cscorelib2.updater.Updater;
import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;

/**
 * This Class represents our {@link Updater} Service.
 * If enabled, it will automatically connect to https://thebusybiscuit.github.io/builds/
 * to check for updates and to download them automatically.
 *
 * @author TheBusyBiscuit
 */
public class UpdaterService {

    // 汉化版不提供自动更新服务.
    private final SlimefunBranch branch = SlimefunBranch.UNOFFICIAL;

    public UpdaterService() {
    }

    /**
     * This method returns the branch the current build of Slimefun is running on.
     * This can be used to determine whether we are dealing with an official build
     * or a build that was unofficially modified.
     *
     * @return The branch this build of Slimefun is on.
     */
    public SlimefunBranch getBranch() {
        return branch;
    }
}
