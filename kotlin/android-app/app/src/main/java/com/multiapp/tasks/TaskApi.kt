package com.multiapp.tasks

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class Task(val id: Long, val title: String, val done: Boolean)

/** Null fields are omitted by Gson, so PUT sends only what changed. */
data class TaskRequest(val title: String? = null, val done: Boolean? = null)

/** Retrofit client for the shared REST contract. */
interface TaskApi {

    @GET("api/tasks")
    suspend fun list(): List<Task>

    @POST("api/tasks")
    suspend fun create(@Body body: TaskRequest): Task

    @PUT("api/tasks/{id}")
    suspend fun update(@Path("id") id: Long, @Body body: TaskRequest): Task

    @DELETE("api/tasks/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>

    companion object {
        fun create(baseUrl: String): TaskApi = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApi::class.java)
    }
}
