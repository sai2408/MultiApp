import Foundation

/// Named TaskItem so it doesn't clash with Swift concurrency's `Task`.
struct TaskItem: Codable, Identifiable, Equatable {
    let id: Int
    var title: String
    var done: Bool
}

/// Body for create/update. Nil fields are omitted, so PUT sends only what changed.
private struct TaskRequest: Encodable {
    var title: String?
    var done: Bool?
}

/// URLSession client for the shared REST contract.
struct TaskAPI {
    // iOS simulator shares the Mac's network, so localhost works. 8083 = Ktor; use any backend port.
    var baseURL = URL(string: "http://localhost:8083/api/tasks")!

    func list() async throws -> [TaskItem] {
        try await send(URLRequest(url: baseURL))
    }

    func create(title: String) async throws -> TaskItem {
        try await send(request(baseURL, method: "POST", body: TaskRequest(title: title)))
    }

    func update(_ task: TaskItem, done: Bool) async throws -> TaskItem {
        try await send(request(baseURL.appendingPathComponent("\(task.id)"), method: "PUT", body: TaskRequest(done: done)))
    }

    func delete(_ task: TaskItem) async throws {
        var req = URLRequest(url: baseURL.appendingPathComponent("\(task.id)"))
        req.httpMethod = "DELETE"
        _ = try await data(for: req)
    }

    // MARK: - helpers

    private func request(_ url: URL, method: String, body: TaskRequest) throws -> URLRequest {
        var req = URLRequest(url: url)
        req.httpMethod = method
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.httpBody = try JSONEncoder().encode(body)
        return req
    }

    private func send<T: Decodable>(_ req: URLRequest) async throws -> T {
        try JSONDecoder().decode(T.self, from: try await data(for: req))
    }

    private func data(for req: URLRequest) async throws -> Data {
        let (data, response) = try await URLSession.shared.data(for: req)
        guard let http = response as? HTTPURLResponse, (200..<300).contains(http.statusCode) else {
            throw URLError(.badServerResponse)
        }
        return data
    }
}
