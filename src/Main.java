import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task = taskManager.createTask(new Task("task", "taskDescription", TaskStatus.NEW));

        List<Subtask> subtasks1 = new ArrayList<>();
        subtasks1.add(new Subtask("subtask1", "subtaskdescription1", TaskStatus.NEW));
        subtasks1.add(new Subtask("subtask2", "subtaskdescription2", TaskStatus.DONE));

        Epic epic1 = taskManager.createEpic(new Epic("epic1", "desc1", subtasks1));

        List<Subtask> subtasks2 = new ArrayList<>();
        subtasks2.add(new Subtask("subtask3", "subtaskdescription3", TaskStatus.NEW));

        Epic epic2 = taskManager.createEpic(new Epic("epic2", "desc2", subtasks2));

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());

        task.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task);
        System.out.println(taskManager.getTask(task.getId()));

        List<Subtask> subtasks = taskManager.getEpicSubtasks(epic1.getId());
        System.out.println(subtasks);

        Subtask subtask = subtasks.getFirst();
        subtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask);
        System.out.println(taskManager.getEpic(subtask.getEpicId()));
    }
}
