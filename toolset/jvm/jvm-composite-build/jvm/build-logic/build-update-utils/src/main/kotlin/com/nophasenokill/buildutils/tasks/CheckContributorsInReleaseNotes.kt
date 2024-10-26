
package com.nophasenokill.buildutils.tasks

import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.util.TreeSet


@DisableCachingByDefault(because = "Depends on GitHub API")
abstract class CheckContributorsInReleaseNotes : AbstractCheckOrUpdateContributorsInReleaseNotes() {
    @TaskAction
    fun check() {
        val contributorsInReleaseNotes = TreeSet(getContributorsInReleaseNotes().map { it.login })
        val contributorsFromPullRequests = TreeSet(getContributorsFromPullRequests().map { it.login })
        val contributorsInPullRequestsButNotInReleaseNotes = contributorsFromPullRequests.minus(contributorsInReleaseNotes)

        if (contributorsInPullRequestsButNotInReleaseNotes.isNotEmpty()) {
            error(
                """The contributors in the release notes $releaseNotes don't match the contributors in the PRs.
                Release notes:  $contributorsInReleaseNotes
                Pull requests:  $contributorsFromPullRequests
                Missed in notes:$contributorsInPullRequestsButNotInReleaseNotes

                You can run `GITHUB_TOKEN=<YourGitHubToken> ./gradlew docs:updateContributorsInReleaseNotes --milestone <milestone>` to update the release notes with correct contributors automatically.
                """.trimIndent()
            )
        }
    }
}
