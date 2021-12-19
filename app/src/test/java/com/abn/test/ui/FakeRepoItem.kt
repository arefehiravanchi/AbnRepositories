package com.abn.test.ui

import com.abn.test.data.local.model.GitRepo
import com.abn.test.data.remote.response.RepoResponse
import com.abn.test.data.remote.response.mapToDbModel
import com.google.gson.Gson

class FakeRepoItem {
    companion object {
        fun getFakeRepoItem(): GitRepo {
            val json = "{\n" +
                    "    \"id\": 276828556,\n" +
                    "    \"node_id\": \"MDEwOlJlcG9zaXRvcnkyNzY4Mjg1NTY=\",\n" +
                    "    \"name\": \"airflow\",\n" +
                    "    \"full_name\": \"abnamrocoesd/airflow\",\n" +
                    "    \"private\": false,\n" +
                    "    \"owner\": {\n" +
                    "      \"login\": \"abnamrocoesd\",\n" +
                    "      \"id\": 15876397,\n" +
                    "      \"node_id\": \"MDEyOk9yZ2FuaXphdGlvbjE1ODc2Mzk3\",\n" +
                    "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/15876397?v=4\",\n" +
                    "      \"gravatar_id\": \"\",\n" +
                    "      \"url\": \"https://api.github.com/users/abnamrocoesd\",\n" +
                    "      \"html_url\": \"https://github.com/abnamrocoesd\",\n" +
                    "      \"followers_url\": \"https://api.github.com/users/abnamrocoesd/followers\",\n" +
                    "      \"following_url\": \"https://api.github.com/users/abnamrocoesd/following{/other_user}\",\n" +
                    "      \"gists_url\": \"https://api.github.com/users/abnamrocoesd/gists{/gist_id}\",\n" +
                    "      \"starred_url\": \"https://api.github.com/users/abnamrocoesd/starred{/owner}{/repo}\",\n" +
                    "      \"subscriptions_url\": \"https://api.github.com/users/abnamrocoesd/subscriptions\",\n" +
                    "      \"organizations_url\": \"https://api.github.com/users/abnamrocoesd/orgs\",\n" +
                    "      \"repos_url\": \"https://api.github.com/users/abnamrocoesd/repos\",\n" +
                    "      \"events_url\": \"https://api.github.com/users/abnamrocoesd/events{/privacy}\",\n" +
                    "      \"received_events_url\": \"https://api.github.com/users/abnamrocoesd/received_events\",\n" +
                    "      \"type\": \"Organization\",\n" +
                    "      \"site_admin\": false\n" +
                    "    },\n" +
                    "    \"html_url\": \"https://github.com/abnamrocoesd/airflow\",\n" +
                    "    \"description\": \"Apache Airflow - A platform to programmatically author, schedule, and monitor workflows\",\n" +
                    "    \"fork\": true,\n" +
                    "    \"url\": \"https://api.github.com/repos/abnamrocoesd/airflow\",\n" +
                    "    \"forks_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/forks\",\n" +
                    "    \"keys_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/keys{/key_id}\",\n" +
                    "    \"collaborators_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/collaborators{/collaborator}\",\n" +
                    "    \"teams_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/teams\",\n" +
                    "    \"hooks_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/hooks\",\n" +
                    "    \"issue_events_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/issues/events{/number}\",\n" +
                    "    \"events_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/events\",\n" +
                    "    \"assignees_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/assignees{/user}\",\n" +
                    "    \"branches_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/branches{/branch}\",\n" +
                    "    \"tags_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/tags\",\n" +
                    "    \"blobs_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/git/blobs{/sha}\",\n" +
                    "    \"git_tags_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/git/tags{/sha}\",\n" +
                    "    \"git_refs_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/git/refs{/sha}\",\n" +
                    "    \"trees_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/git/trees{/sha}\",\n" +
                    "    \"statuses_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/statuses/{sha}\",\n" +
                    "    \"languages_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/languages\",\n" +
                    "    \"stargazers_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/stargazers\",\n" +
                    "    \"contributors_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/contributors\",\n" +
                    "    \"subscribers_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/subscribers\",\n" +
                    "    \"subscription_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/subscription\",\n" +
                    "    \"commits_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/commits{/sha}\",\n" +
                    "    \"git_commits_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/git/commits{/sha}\",\n" +
                    "    \"comments_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/comments{/number}\",\n" +
                    "    \"issue_comment_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/issues/comments{/number}\",\n" +
                    "    \"contents_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/contents/{+path}\",\n" +
                    "    \"compare_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/compare/{base}...{head}\",\n" +
                    "    \"merges_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/merges\",\n" +
                    "    \"archive_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/{archive_format}{/ref}\",\n" +
                    "    \"downloads_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/downloads\",\n" +
                    "    \"issues_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/issues{/number}\",\n" +
                    "    \"pulls_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/pulls{/number}\",\n" +
                    "    \"milestones_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/milestones{/number}\",\n" +
                    "    \"notifications_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/notifications{?since,all,participating}\",\n" +
                    "    \"labels_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/labels{/name}\",\n" +
                    "    \"releases_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/releases{/id}\",\n" +
                    "    \"deployments_url\": \"https://api.github.com/repos/abnamrocoesd/airflow/deployments\",\n" +
                    "    \"created_at\": \"2020-07-03T06:47:39Z\",\n" +
                    "    \"updated_at\": \"2020-07-03T06:47:42Z\",\n" +
                    "    \"pushed_at\": \"2020-07-10T05:41:56Z\",\n" +
                    "    \"git_url\": \"git://github.com/abnamrocoesd/airflow.git\",\n" +
                    "    \"ssh_url\": \"git@github.com:abnamrocoesd/airflow.git\",\n" +
                    "    \"clone_url\": \"https://github.com/abnamrocoesd/airflow.git\",\n" +
                    "    \"svn_url\": \"https://github.com/abnamrocoesd/airflow\",\n" +
                    "    \"homepage\": \"https://airflow.apache.org/\",\n" +
                    "    \"size\": 73362,\n" +
                    "    \"stargazers_count\": 0,\n" +
                    "    \"watchers_count\": 0,\n" +
                    "    \"language\": null,\n" +
                    "    \"has_issues\": false,\n" +
                    "    \"has_projects\": true,\n" +
                    "    \"has_downloads\": true,\n" +
                    "    \"has_wiki\": false,\n" +
                    "    \"has_pages\": false,\n" +
                    "    \"forks_count\": 0,\n" +
                    "    \"mirror_url\": null,\n" +
                    "    \"archived\": false,\n" +
                    "    \"disabled\": false,\n" +
                    "    \"open_issues_count\": 0,\n" +
                    "    \"license\": {\n" +
                    "      \"key\": \"apache-2.0\",\n" +
                    "      \"name\": \"Apache License 2.0\",\n" +
                    "      \"spdx_id\": \"Apache-2.0\",\n" +
                    "      \"url\": \"https://api.github.com/licenses/apache-2.0\",\n" +
                    "      \"node_id\": \"MDc6TGljZW5zZTI=\"\n" +
                    "    },\n" +
                    "    \"allow_forking\": true,\n" +
                    "    \"is_template\": false,\n" +
                    "    \"topics\": [],\n" +
                    "    \"visibility\": \"public\",\n" +
                    "    \"forks\": 0,\n" +
                    "    \"open_issues\": 0,\n" +
                    "    \"watchers\": 0,\n" +
                    "    \"default_branch\": \"master\"\n" +
                    "  }"

            return Gson().fromJson(json, RepoResponse::class.java).mapToDbModel()
        }
    }

}