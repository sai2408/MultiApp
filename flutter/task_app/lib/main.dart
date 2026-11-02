import 'package:flutter/material.dart';

import 'task_api.dart';

void main() => runApp(const TaskApp());

class TaskApp extends StatelessWidget {
  const TaskApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Tasks · Flutter',
      theme: ThemeData(colorSchemeSeed: Colors.indigo, useMaterial3: true),
      home: const TaskScreen(),
    );
  }
}

class TaskScreen extends StatefulWidget {
  const TaskScreen({super.key});

  @override
  State<TaskScreen> createState() => _TaskScreenState();
}

class _TaskScreenState extends State<TaskScreen> {
  final _api = TaskApi();
  final _controller = TextEditingController();
  List<Task> _tasks = [];
  String? _error;

  @override
  void initState() {
    super.initState();
    _run(() async {});
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  /// Runs an API call, then reloads the list; failures show in the error banner.
  Future<void> _run(Future<void> Function() action) async {
    try {
      await action();
      final tasks = await _api.list();
      if (!mounted) return;
      setState(() {
        _tasks = tasks;
        _error = null;
      });
    } catch (e) {
      if (!mounted) return;
      setState(() => _error = "Can't reach ${_api.baseUrl}: $e");
    }
  }

  void _add() {
    final title = _controller.text.trim();
    if (title.isEmpty) return;
    _controller.clear();
    _run(() => _api.create(title));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Tasks · Flutter')),
      body: RefreshIndicator(
        onRefresh: () => _run(() async {}),
        child: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: _controller,
                    decoration: const InputDecoration(hintText: 'What needs doing?'),
                    onSubmitted: (_) => _add(),
                  ),
                ),
                const SizedBox(width: 8),
                FilledButton(onPressed: _add, child: const Text('Add')),
              ],
            ),
            if (_error != null)
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 12),
                child: Text(_error!, style: TextStyle(color: Theme.of(context).colorScheme.error)),
              ),
            const SizedBox(height: 12),
            for (final task in _tasks)
              ListTile(
                key: ValueKey(task.id),
                contentPadding: EdgeInsets.zero,
                leading: Checkbox(
                  value: task.done,
                  onChanged: (v) => _run(() => _api.setDone(task, v ?? false)),
                ),
                title: Text(
                  task.title,
                  style: task.done
                      ? const TextStyle(decoration: TextDecoration.lineThrough, color: Colors.grey)
                      : null,
                ),
                trailing: IconButton(
                  icon: const Icon(Icons.delete_outline),
                  onPressed: () => _run(() => _api.delete(task)),
                ),
              ),
            if (_tasks.isEmpty && _error == null)
              const Padding(
                padding: EdgeInsets.only(top: 24),
                child: Center(child: Text('No tasks yet.')),
              ),
          ],
        ),
      ),
    );
  }
}
