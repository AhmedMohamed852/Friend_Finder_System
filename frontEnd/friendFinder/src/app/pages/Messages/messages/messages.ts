import {
  Component, OnInit, OnDestroy, signal, computed, inject, ChangeDetectorRef
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Subject, interval, catchError, of, takeUntil, forkJoin } from 'rxjs';
import { AuthService } from '../../../core/services/auth/auth';
import { MessagesDto } from '../../../core/models/MessagesDto';
import { UserService } from '../../../core/services/user/user-service';
import { MessagesService } from '../../../core/services/Messages/message-service';
import { FriendshipService } from '../../../core/services/Friendship/friendship-service';

interface Conversation {
  userId:         number;
  firstName:      string;
  lastName:       string;
  profilePicture: string;
  lastMessage:    string;
  unreadCount:    number;
}

@Component({
  selector: 'app-messages',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './messages.html',
  styleUrl: './messages.css'
})
export class MessagesComponent implements OnInit, OnDestroy {

  private messagesService   = inject(MessagesService);
  private authService       = inject(AuthService);
  private userService       = inject(UserService);
  private friendshipService = inject(FriendshipService);
  private route             = inject(ActivatedRoute);
  private router            = inject(Router);
  private cdr               = inject(ChangeDetectorRef);
  private destroy$          = new Subject<void>();

  currentUserId  = 0;
  conversations  = signal<Conversation[]>([]);
  activeMessages = signal<MessagesDto[]>([]);
  activeUser     = signal<Conversation | null>(null);

  searchQuery    = '';
  newMessage     = '';
  loadingInbox   = false;
  loadingChat    = false;
  sendingMessage = false;
  errorInbox     = '';
  errorChat      = '';
  errorSend      = '';

  filteredConversations = computed(() => {
    const q = this.searchQuery.toLowerCase().trim();
    if (!q) return this.conversations();
    return this.conversations().filter(c =>
      `${c.firstName} ${c.lastName}`.toLowerCase().includes(q)
    );
  });

  getTotalUnread(): number {
    return this.conversations().reduce((sum, c) => sum + c.unreadCount, 0);
  }

  filterConversations(): void {
    const key = this.searchQuery.trim();

    if (!key) {
      this.loadInbox();
      return;
    }

    this.loadingInbox = true;
    this.errorInbox = '';

    this.friendshipService.search(key).pipe(
      takeUntil(this.destroy$),
      catchError(err => {
        this.errorInbox = err.error?.message || 'Error occurred while searching.';
        this.loadingInbox = false;
        this.cdr.detectChanges();
        return of([]);
      })
    ).subscribe(users => {
      const searchAsConversations: Conversation[] = users.map(user => ({
        userId: user.id,
        firstName: user.firstName,
        lastName: user.lastName,
        profilePicture: user.profilePicture || 'assets/default-avatar.png',
        lastMessage: 'Tap to start a new conversation',
        unreadCount: 0
      }));

      this.conversations.set(searchAsConversations);
      this.loadingInbox = false;
      this.cdr.detectChanges();
    });
  }

  // ── Lifecycle ─────────────────────────────────────────────
  ngOnInit(): void {
    const userId = this.authService.getCurrentUserId();

    if (userId > 0) {
      this.currentUserId = userId;
      this.init();
    } else {
      this.authService.fetchCurrentUser().subscribe({
        next: () => {
          this.currentUserId = this.authService.getCurrentUserId();
          this.init();
        },
        error: () => {
          this.errorInbox = 'Could not load user session. Please log in again.';
          this.cdr.detectChanges();
        }
      });
    }
  }

  private init(): void {
    this.loadInbox();

    const urlUserId = this.route.snapshot.params['userId'];
    if (urlUserId) this.openConversationById(+urlUserId);

    interval(10000).pipe(takeUntil(this.destroy$)).subscribe(() => {
      if (!this.searchQuery.trim()) {
        this.refreshInbox();
      }
      if (this.activeUser()) this.refreshActiveChat();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ── INBOX ─────────────────────────────────────────────────
  loadInbox(): void {
    this.loadingInbox = true;
    this.errorInbox   = '';

    forkJoin({
      inbox: this.messagesService.getInbox(this.currentUserId).pipe(catchError(() => of([]))),
      sent:  this.messagesService.getSentMessages(this.currentUserId).pipe(catchError(() => of([])))
    }).subscribe({
      next: ({ inbox, sent }) => {
        this.buildConversations([...inbox, ...sent]);
        this.loadingInbox = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorInbox   = err.error?.message || 'Failed to load conversations.';
        this.loadingInbox = false;
        this.cdr.detectChanges();
      }
    });
  }

  refreshInbox(): void {
    forkJoin({
      inbox: this.messagesService.getInbox(this.currentUserId).pipe(catchError(() => of([]))),
      sent:  this.messagesService.getSentMessages(this.currentUserId).pipe(catchError(() => of([])))
    }).subscribe(({ inbox, sent }) => {
      this.buildConversations([...inbox, ...sent]);
      this.cdr.detectChanges();
    });
  }

  private buildConversations(messages: MessagesDto[]): void {
    const map = new Map<number, Conversation>();

    messages.forEach(msg => {
      const other = msg.sender?.id === this.currentUserId ? msg.receiver : msg.sender;
      if (!other) return;

      const key = other.id;

      if (!map.has(key)) {
        map.set(key, {
          userId:         other.id,
          firstName:      other.firstName,
          lastName:       other.lastName,
          profilePicture: other.profilePicture || 'assets/default-avatar.png',
          lastMessage:    msg.content,
          unreadCount:    (!msg.isRead && msg.receiver?.id === this.currentUserId) ? 1 : 0
        });
      } else {
        const conv = map.get(key)!;
        conv.lastMessage = msg.content;
        if (!msg.isRead && msg.receiver?.id === this.currentUserId) {
          conv.unreadCount++;
        }
      }
    });

    this.conversations.set(Array.from(map.values()));
  }

  // ── OPEN CONVERSATION ─────────────────────────────────────
  openConversation(conv: Conversation): void {
    this.activeUser.set(conv);
    this.errorChat   = '';
    this.loadingChat = true;
    this.router.navigate(['/messages', conv.userId], { replaceUrl: true });

    this.messagesService.getConversation(this.currentUserId, conv.userId).pipe(
      catchError(err => {
        this.errorChat   = err.error?.message || 'Failed to load messages.';
        this.loadingChat = false;
        this.cdr.detectChanges();
        return of([]);
      })
    ).subscribe(msgs => {
      this.activeMessages.set(msgs);
      this.loadingChat = false;
      this.cdr.detectChanges();
      this.scrollToBottom();

      msgs
        .filter(msg => !msg.isRead && msg.receiver?.id === this.currentUserId && msg.id)
        .forEach(msg => {
          this.messagesService.markAsRead(msg.id!).pipe(catchError(() => of(null))).subscribe();
        });

      this.refreshInbox();
    });
  }

  clearActive(): void {
    this.activeUser.set(null);
    this.activeMessages.set([]);
    this.router.navigate(['/messages'], { replaceUrl: true });
  }

  viewProfile(userId: number): void {
    this.router.navigate(['/profile', userId]);
  }

  private openConversationById(userId: number): void {
    setTimeout(() => {
      const existing = this.conversations().find(c => c.userId === userId);
      if (existing) {
        this.openConversation(existing);
      } else {
        this.userService.getUser(userId).subscribe({
          next: user => {
            const conv: Conversation = {
              userId,
              firstName:      user.firstName,
              lastName:       user.lastName,
              profilePicture: user.profilePicture || 'assets/default-avatar.png',
              lastMessage:    '',
              unreadCount:    0
            };
            this.activeUser.set(conv);
            this.loadChat(userId);
          },
          error: () => { this.errorChat = 'Could not load user info.'; }
        });
      }
    }, 800);
  }

  // ── CHAT ──────────────────────────────────────────────────
  loadChat(otherUserId: number): void {
    this.loadingChat = true;
    this.errorChat   = '';
    this.activeMessages.set([]);

    this.messagesService.getConversation(this.currentUserId, otherUserId).pipe(
      catchError(err => {
        this.errorChat   = err.error?.message || 'Failed to load messages.';
        this.loadingChat = false;
        this.cdr.detectChanges();
        return of([]);
      })
    ).subscribe(msgs => {
      this.activeMessages.set(msgs);
      this.loadingChat = false;
      this.cdr.detectChanges();
      this.scrollToBottom();
    });
  }

  refreshActiveChat(): void {
    const user = this.activeUser();
    if (!user) return;
    this.messagesService.getConversation(this.currentUserId, user.userId).pipe(
      catchError(() => of([]))
    ).subscribe(msgs => {
      this.activeMessages.set(msgs);
      this.cdr.detectChanges();
    });
  }

  // ── SEND ──────────────────────────────────────────────────
  sendMessage(): void {
    const user = this.activeUser();
    if (!user || !this.newMessage.trim() || this.sendingMessage) return;

    this.sendingMessage = true;
    this.errorSend      = '';
    const content       = this.newMessage.trim();
    this.newMessage     = '';

    this.messagesService.sendMessage(user.userId, { content }).pipe(
      catchError(err => {
        this.errorSend      = err.error?.message || 'Failed to send. Please try again.';
        this.sendingMessage = false;
        this.newMessage     = content;
        this.cdr.detectChanges();
        return of(null);
      })
    ).subscribe(msg => {
      if (msg) {
        this.activeMessages.update(msgs => [...msgs, msg]);
        this.sendingMessage = false;

        const currentConvs = this.conversations();
        const exists = currentConvs.find(c => c.userId === user.userId);
        if (exists) {
          exists.lastMessage = content;
          this.conversations.set([...currentConvs]);
        } else {
          this.conversations.update(convs => [{
            userId:         user.userId,
            firstName:      user.firstName,
            lastName:       user.lastName,
            profilePicture: user.profilePicture,
            lastMessage:    content,
            unreadCount:    0
          }, ...convs]);
        }

        this.cdr.detectChanges();
        this.scrollToBottom();
      }
    });
  }

  onEnter(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  // ── DELETE ────────────────────────────────────────────────
  deleteMessage(msgId: number): void {
    this.messagesService.deleteMessage(msgId).pipe(
      catchError(err => {
        this.errorChat = err.error?.message || 'Could not delete message.';
        this.cdr.detectChanges();
        return of(null);
      })
    ).subscribe(() => {
      this.activeMessages.update(msgs => msgs.filter(m => m.id !== msgId));
      this.refreshInbox();
      this.cdr.detectChanges();
    });
  }

  isMine(msg: MessagesDto): boolean {
    return msg.sender?.id === this.currentUserId;
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      const el = document.querySelector('.chat-messages');
      if (el) el.scrollTop = el.scrollHeight;
    }, 50);
  }
}
