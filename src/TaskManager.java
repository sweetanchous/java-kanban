import java.util.*;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();

    private int idCounter = 0;

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Task createTask(Task task) {
        int id = idCounter++;
        task.setId(id);
        tasks.put(id, task);

        return task;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public Collection<Epic> getEpics() {
        return epics.values();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Epic createEpic(Epic epic) {
        int id = idCounter++;
        epic.setId(id);

        for (Subtask subtask : epic.getSubtasks()) {
            int subtaskId = idCounter++;
            subtask.setId(subtaskId);
            subtask.setEpicId(epic.getId());
            subtasks.put(subtaskId, subtask);
        }

        epics.put(id, epic);
        updateEpicStatus(id);

        return epic;
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        }
    }

    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        for (Subtask subtask : epic.getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
    }

    public Collection<Subtask> getSubtasks() {
        return subtasks.values();
    }

    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
        }
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtasks().remove(subtask);
                updateEpicStatus(subtask.getEpicId());
            }
        }
    }

    public Subtask createSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return null;
        }

        int id = idCounter++;
        subtask.setId(id);
        subtasks.put(id, subtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasks().add(subtask);
        updateEpicStatus(subtask.getEpicId());

        return subtask;
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    public List<Subtask> getEpicSubtasks(int epicId) {
        if (epics.containsKey(epicId)) {
            return epics.get(epicId).getSubtasks();
        }
        return new ArrayList<>();
    }

    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        List<Subtask> subtaskList = epic.getSubtasks();
        epic.setStatus(computeEpicStatus(subtaskList));

        epics.put(epicId, epic);
    }

    public TaskStatus computeEpicStatus(List<Subtask> subtaskList) {
        if (subtaskList == null || subtaskList.isEmpty()) {
            return TaskStatus.NEW;
        }

        int counter = 0;

        for (Subtask sub : subtaskList) {
            if (sub.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                return TaskStatus.IN_PROGRESS;
            } else if (sub.getStatus().equals(TaskStatus.NEW)) {
                counter++;
            }
        }

        if (counter == 0) {
            return TaskStatus.DONE;
        } else if (subtaskList.size() == counter) {
            return TaskStatus.NEW;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }
}
