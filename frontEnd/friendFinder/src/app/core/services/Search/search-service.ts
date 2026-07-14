import { Injectable, signal } from '@angular/core';
import type { SimpleUserProfile } from '../user/user-service';

@Injectable({ providedIn: 'root' })
export class SearchService {

  searchResults  = signal<SimpleUserProfile[]>([]);
  isSearchActive = signal<boolean>(false);
  lastQuery      = signal<string>('');

  setResults(results: SimpleUserProfile[], query: string): void {
    this.searchResults.set(results);
    this.lastQuery.set(query);
    this.isSearchActive.set(true);
  }

  clearSearch(): void {
    this.searchResults.set([]);
    this.lastQuery.set('');
    this.isSearchActive.set(false);
  }
}
