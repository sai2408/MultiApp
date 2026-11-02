import Foundation

@MainActor
final class TaskViewModel: ObservableObject {
    @Published private(set) var tasks: [TaskItem] = []
    @Published private(set) var error: String?

    private let api = TaskAPI()

    func refresh() async {
        await perform {}
    }

    func add(_ title: String) async {
        await perform { _ = try await self.api.create(title: title) }
    }

    func toggle(_ task: TaskItem) async {
        await perform { _ = try await self.api.update(task, done: !task.done) }
    }

    func delete(_ task: TaskItem) async {
        await perform { try await self.api.delete(task) }
    }

    /// Runs the API call, then reloads the list; network failures surface in `error`.
    private func perform(_ action: () async throws -> Void) async {
        do {
            try await action()
            tasks = try await api.list()
            error = nil
        } catch {
            self.error = "Can't reach \(api.baseURL.absoluteString): \(error.localizedDescription)"
        }
    }
}
