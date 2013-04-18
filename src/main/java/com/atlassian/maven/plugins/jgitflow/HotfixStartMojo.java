package com.atlassian.maven.plugins.jgitflow;

import com.atlassian.maven.plugins.jgitflow.exception.JGitFlowReleaseException;
import com.atlassian.maven.plugins.jgitflow.manager.FlowReleaseManager;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @since version
 */
@Mojo(name = "hotfix-start", aggregator = true)
public class HotfixStartMojo extends AbstractJGitFlowMojo
{
    /**
     * Whether to automatically assign submodules the parent version. If set to false, the user will be prompted for the
     * version of each submodules.
     *
     */
    @Parameter( defaultValue = "false", property = "autoVersionSubmodules" )
    private boolean autoVersionSubmodules = false;

    /**
     * Whether to allow SNAPSHOT dependencies. Default is to fail when finding any SNAPSHOT.
     *
     */
    @Parameter( defaultValue = "false", property = "allowSnapshots" )
    private boolean allowSnapshots = false;

    /**
     * Default version to use when preparing a release
     *
     */
    @Parameter( property = "releaseVersion" )
    private String releaseVersion;

    @Parameter( defaultValue = "true", property = "updateDependencies" )
    private boolean updateDependencies = true;

    @Component(hint = "hotfix")
    FlowReleaseManager releaseManager;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        ReleaseContext ctx = new ReleaseContext(getBasedir());
        ctx.setAutoVersionSubmodules(autoVersionSubmodules)
           .setInteractive(getSettings().isInteractiveMode())
           .setDefaultReleaseVersion(releaseVersion)
           .setAllowSnapshots(allowSnapshots)
           .setUpdateDependencies(updateDependencies)
           .setFlowInitContext(getFlowInitContext().getJGitFlowContext());

        try
        {
            releaseManager.start(ctx,getReactorProjects());
        }
        catch (JGitFlowReleaseException e)
        {
            throw new MojoExecutionException("Error starting hotfix: " + e.getMessage(),e);
        }
    }
}