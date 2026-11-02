package com.multiapp.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    TaskScreen()
                }
            }
        }
    }
}

@Composable
fun TaskScreen(vm: TaskViewModel = viewModel()) {
    var newTitle by remember { mutableStateOf("") }

    Column(
        Modifier
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Sai's Tasks", style = MaterialTheme.typography.headlineMedium)

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                placeholder = { Text("What needs doing?") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            Button(
                onClick = {
                    vm.add(newTitle.trim())
                    newTitle = ""
                },
                enabled = newTitle.isNotBlank(),
            ) { Text("Add") }
        }

        vm.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            TextButton(onClick = vm::refresh) { Text("Retry") }
        }

        LazyColumn {
            items(vm.tasks, key = { it.id }) { task ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = task.done, onCheckedChange = { vm.toggle(task) })
                    Text(
                        task.title,
                        modifier = Modifier.weight(1f),
                        textDecoration = if (task.done) TextDecoration.LineThrough else null,
                        color = if (task.done) Color.Gray else Color.Unspecified,
                    )
                    TextButton(onClick = { vm.delete(task) }) { Text("Delete") }
                }
                HorizontalDivider()
            }
        }
    }
}
