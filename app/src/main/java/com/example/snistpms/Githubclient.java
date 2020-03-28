package com.example.snistpms;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Githubclient {
    @GET("users/{username}/repos")
    Call<List<RepoList>> UserRepositories(@Path("username") String userName);

    @GET("repos/{username}/{reponame}/contents")
    Call<List<RepoContentList>> UserRepositoriesContents(@Path("username") String userName, @Path("reponame") String reponame);
}
