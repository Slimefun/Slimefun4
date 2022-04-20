package io.github.thebusybiscuit.slimefun4.core.services.github;

@FunctionalInterface
interface IssuesCallback {

    /**
     * This method is called when the {@link GitHubIssuesConnector} finished loading.
     * 
     * @param issues
     *            The amount of open Issues
     * @param pullRequests
     *            The amount of open Pull Requests
     */
    void accept(int issues, int pullRequests);

}
