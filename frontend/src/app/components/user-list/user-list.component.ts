import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  userForm: FormGroup;
  editingUser: User | null = null;
  message: string | null = null;
  messageType: 'success' | 'error' = 'success';

  constructor(
    private userService: UserService,
    private fb: FormBuilder
  ) {
    this.userForm = this.fb.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe(data => {
      this.users = data;
    });
  }

  onEdit(user: User): void {
    this.editingUser = user;
    this.userForm.setValue({
      nome: user.nome,
      email: user.email
    });
    window.scrollTo(0, 0); // Rola a página para o topo para ver o formulário
  }

  onDelete(id: number): void {
    if (confirm('Tem certeza que deseja excluir este usuário?')) {
      this.userService.deleteUser(id).subscribe({
        next: (response) => {
          this.showMessage(response, 'success');
          this.loadUsers();
        },
        error: (err) => this.showMessage(err.error, 'error')
      });
    }
  }

  onSubmit(): void {
    if (this.userForm.invalid) {
      return;
    }

    const userData = this.userForm.value;

    if (this.editingUser) {
      // Atualizando usuário
      this.userService.updateUser(this.editingUser.id, userData).subscribe({
        next: (response) => {
          this.showMessage(response, 'success');
          this.loadUsers();
          this.onCancel();
        },
        error: (err) => this.showMessage(err.error, 'error')
      });
    } else {
      // Criando novo usuário
      this.userService.createUser(userData).subscribe({
        next: (response) => {
          this.showMessage(response, 'success');
          this.loadUsers();
          this.onCancel();
        },
        error: (err) => this.showMessage(err.error, 'error')
      });
    }
  }

  onCancel(): void {
    this.editingUser = null;
    this.userForm.reset();
  }

  showMessage(msg: string, type: 'success' | 'error'): void {
    this.message = msg;
    this.messageType = type;
    setTimeout(() => this.message = null, 5000); // A mensagem desaparece após 5 segundos
  }
}