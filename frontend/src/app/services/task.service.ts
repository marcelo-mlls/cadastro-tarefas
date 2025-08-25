import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task, TaskStatus } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:8081/api/tasks';

  constructor(private http: HttpClient) { }

  getTasks(status?: TaskStatus, userId?: number): Observable<Task[]> {
    let params = new HttpParams();
    if (status) {
      params = params.append('status', status);
    }
    if (userId != null) {
      params = params.append('userId', userId.toString());
    }
    return this.http.get<Task[]>(this.apiUrl, { params });
  }

  getTask(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${id}`);
  }

  createTask(task: { titulo: string; descricao?: string; userId: number; dataLimite?: Date }): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, task);
  }

  updateTask(id: number, task: Partial<Task>): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, task, { responseType: 'text' });
  }

  deleteTask(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
