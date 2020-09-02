package io.github.thebusybiscuit.slimefun4.core.services.github;

@FunctionalInterface
interface IssuesTrackerConsumer {

    /**
     * This method is called when the {@link GitHubIssuesTracker} finished loading.
     * 
     * @param issues
     *            The amount of open Issues
     * @param pullRequests
     *            The amount of open Pull Requests
     */
    void accept(int issues, int pullRequests);

}
