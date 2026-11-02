import SwiftUI

struct ContentView: View {
    @StateObject private var vm = TaskViewModel()
    @State private var newTitle = ""

    var body: some View {
        NavigationStack {
            List {
                Section {
                    HStack {
                        TextField("What needs doing?", text: $newTitle)
                            .onSubmit(add)
                        Button("Add", action: add)
                            .disabled(newTitle.trimmingCharacters(in: .whitespaces).isEmpty)
                    }
                }

                if let error = vm.error {
                    Section {
                        Text(error).foregroundStyle(.red)
                    }
                }

                Section {
                    ForEach(vm.tasks) { task in
                        HStack {
                            Button {
                                Task { await vm.toggle(task) }
                            } label: {
                                Image(systemName: task.done ? "checkmark.circle.fill" : "circle")
                            }
                            .buttonStyle(.borderless)

                            Text(task.title)
                                .strikethrough(task.done)
                                .foregroundStyle(task.done ? .secondary : .primary)
                        }
                    }
                    .onDelete { offsets in
                        let doomed = offsets.map { vm.tasks[$0] }
                        Task { for task in doomed { await vm.delete(task) } }
                    }
                }
            }
            .navigationTitle("Tasks · Swift")
            .refreshable { await vm.refresh() }
            .task { await vm.refresh() }
        }
    }

    private func add() {
        let title = newTitle.trimmingCharacters(in: .whitespaces)
        guard !title.isEmpty else { return }
        newTitle = ""
        Task { await vm.add(title) }
    }
}

#Preview {
    ContentView()
}
