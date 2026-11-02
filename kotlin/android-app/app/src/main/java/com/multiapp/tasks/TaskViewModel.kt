package com.multiapp.tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val api = TaskApi.create(BuildConfig.API_BASE_URL)

    var tasks by mutableStateOf<List<Task>>(emptyList())
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        refresh()
    }

    fun refresh() = launchAndReload { }

    fun add(title: String) = launchAndReload { api.create(TaskRequest(title = title)) }

    fun toggle(task: Task) = launchAndReload { api.update(task.id, TaskRequest(done = !task.done)) }

    fun delete(task: Task) = launchAndReload { api.delete(task.id) }

    /** Runs the API call, then reloads the list; network failures surface in [error]. */
    private fun launchAndReload(action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                action()
                tasks = api.list()
                error = null
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                error = "Can't reach ${BuildConfig.API_BASE_URL} (${e.message})"
            }
        }
    }
}
