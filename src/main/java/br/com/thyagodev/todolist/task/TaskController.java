package br.com.thyagodev.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thyagodev.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository iTaskRepository;

    @PostMapping("/")
    public ResponseEntity create (@RequestBody TaskModel taskModel, HttpServletRequest request) {
        taskModel.setUserId((UUID) request.getAttribute("userId"));

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskModel.getStartAt()) 
            || currentDate.isAfter(taskModel.getEndAt())
            || taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong date");
        }

        var createdTask = iTaskRepository.save(taskModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/")
    public List<TaskModel> getAll(HttpServletRequest request){
        var tasks = this.iTaskRepository.findByUserId((UUID) request.getAttribute("userId"));
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request){
        var userId = (UUID) request.getAttribute("userId");
        var task = this.iTaskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not exists");
        }
    
        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not owned by user");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var updatedTask = this.iTaskRepository.save(task);

        return ResponseEntity.ok().body(updatedTask);
    }
    
}
