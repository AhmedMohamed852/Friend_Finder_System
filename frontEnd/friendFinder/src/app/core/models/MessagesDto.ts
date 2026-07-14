import {UserSimpleDto} from './user-simple-dto';

export interface MessagesDto {
  id?: number;              // READ_ONLY — جاي من الباك إند
  content: string;
  sender?: UserSimpleDto;   // READ_ONLY — جاي من الباك إند
  receiver?: UserSimpleDto; // READ_ONLY — جاي من الباك إند
  isRead?: boolean;
  lastTime: string;
}
