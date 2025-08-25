export enum TaskStatus {
  PENDENTE = 'PENDENTE',
  EM_ANDAMENTO = 'EM_ANDAMENTO',
  CONCLUIDO = 'CONCLUIDO'
}

export interface Task {
  id: number;
  titulo: string;
  descricao?: string;
  status: TaskStatus;
  dataCriacao: Date;
  dataLimite?: Date;
  userId: number;
}