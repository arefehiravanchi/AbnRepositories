package com.abn.test.data.remote

import com.abn.test.data.remote.response.RepoResponse
import com.abn.test.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Constants.API_REPOSITORIES)
    suspend fun getRepositories(
        @Query("page") page: Int = Constants.FIRST_PAGE_OFFSET,
        @Query("per_page") perPage: Int = Constants.PAGE_SIZE
    ): Response<List<RepoResponse>>

}