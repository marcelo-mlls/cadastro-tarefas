import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { TaskService } from '../../services/task.service';
import { UserService } from '../../services/user.service';
import { Task, TaskStatus } from '../../models/task.model';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  users: User[] = [];
  taskForm: FormGroup;
  filterForm: FormGroup;
  editingTask: Task | null = null;
  message: string | null = null;
  messageType: 'success' | 'error' = 'success';
  
  TaskStatus = TaskStatus;

  constructor(
    private taskService: TaskService,
    private userService: UserService,
    private fb: FormBuilder
  ) {
    this.taskForm = this.fb.group({
      titulo: ['', Validators.required],
      descricao: [''],
      userId: [null, Validators.required],
      dataLimite: ['']
    });

    this.filterForm = this.fb.group({
      status: [''],
      userId: ['']
    });
  }

  ngOnInit(): void {
    this.loadInitialData();
    this.filterForm.valueChanges.subscribe(() => {
      this.applyFilters();
    });
  }

  loadInitialData(): void {
    this.loadTasks();
    this.loadUsers();
  }

  loadTasks(): void {
    const { status, userId } = this.filterForm.value;
    this.taskService.getTasks(status || undefined, userId || undefined).subscribe(data => {
      this.tasks = data;
    });
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe(data => {
      this.users = data;
    });
  }

  applyFilters(): void {
    this.loadTasks();
  }

  onEdit(task: Task): void {
    this.editingTask = task;
    this.taskForm.patchValue({
      titulo: task.titulo,
      descricao: task.descricao,
      userId: task.userId,
      dataLimite: task.dataLimite ? new Date(task.dataLimite).toISOString().substring(0, 16) : ''
    });
    window.scrollTo(0, 0);
  }

  onDelete(id: number): void {
    if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          this.showMessage('Tarefa excluída com sucesso.', 'success');
          this.loadTasks();
        },
        error: (err) => this.showMessage(err.error?.message || 'Erro ao excluir tarefa.', 'error')
      });
    }
  }

  onSubmit(): void {
    if (this.taskForm.invalid) {
      return;
    }

    const taskData = this.taskForm.value;

    if (this.editingTask) {
      this.taskService.updateTask(this.editingTask.id, taskData).subscribe({
        next: (response) => {
          this.showMessage(response, 'success');
          this.loadTasks();
          this.onCancel();
        },
        error: (err) => this.showMessage(err.error, 'error')
      });
    } else {
      this.taskService.createTask(taskData).subscribe({
        next: () => {
          this.showMessage('Tarefa criada com sucesso.', 'success');
          this.loadTasks();
          this.onCancel();
        },
        error: (err) => this.showMessage(err.error?.message || 'Erro ao criar tarefa.', 'error')
      });
    }
  }
  
  updateTaskStatus(task: Task, event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    const newStatus = selectElement.value as TaskStatus;

    this.taskService.updateTask(task.id, { status: newStatus }).subscribe({
      next: (response) => {
        this.showMessage(response, 'success');
        this.loadTasks();
      },
      error: (err) => {
        this.showMessage(err.error, 'error');
        this.loadTasks();
      }
    });
  }

  getUserName(userId: number): string {
    const user = this.users.find(u => u.id === userId);
    return user ? user.nome : 'Usuário não encontrado';
 }

  onCancel(): void {
    this.editingTask = null;
    this.taskForm.reset();
  }

  showMessage(msg: string, type: 'success' | 'error'): void {
    this.message = msg;
    this.messageType = type;
    setTimeout(() => this.message = null, 5000);
  }
}
