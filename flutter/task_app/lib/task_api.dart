import 'dart:convert';
import 'dart:io' show Platform;

import 'package:flutter/foundation.dart' show kIsWeb;
import 'package:http/http.dart' as http;

class Task {
  final int id;
  final String title;
  final bool done;

  const Task({required this.id, required this.title, required this.done});

  factory Task.fromJson(Map<String, dynamic> json) =>
      Task(id: json['id'] as int, title: json['title'] as String, done: json['done'] as bool);
}

/// HTTP client for the shared REST contract.
class TaskApi {
  // 8083 = Ktor; use any backend port from the root README.
  // The Android emulator reaches your PC at 10.0.2.2; iOS simulator, desktop and web use localhost.
  static String get _host => !kIsWeb && Platform.isAndroid ? '10.0.2.2' : 'localhost';
  static const _port = 8083;

  final Uri baseUrl = Uri.parse('http://$_host:$_port/api/tasks');
  static const _json = {'Content-Type': 'application/json'};

  Uri _item(int id) => baseUrl.replace(path: '${baseUrl.path}/$id');

  Future<List<Task>> list() async {
    final res = _check(await http.get(baseUrl));
    return (jsonDecode(res.body) as List).map((e) => Task.fromJson(e as Map<String, dynamic>)).toList();
  }

  Future<void> create(String title) async {
    _check(await http.post(baseUrl, headers: _json, body: jsonEncode({'title': title})));
  }

  Future<void> setDone(Task task, bool done) async {
    _check(await http.put(_item(task.id), headers: _json, body: jsonEncode({'done': done})));
  }

  Future<void> delete(Task task) async {
    _check(await http.delete(_item(task.id)));
  }

  http.Response _check(http.Response res) {
    if (res.statusCode < 200 || res.statusCode >= 300) {
      throw Exception('${res.request?.method} ${res.request?.url} failed: ${res.statusCode}');
    }
    return res;
  }
}
