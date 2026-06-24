import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

export interface ImgBBResponse {
  data: {
    url: string;
    display_url: string;
  };
  success: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  private apiKey = 'a873949979ef51fc1a85f5673dbedccd';
  private apiUrl = 'https://api.imgbb.com/1/upload';

  constructor(private http: HttpClient) {}

  uploadImage(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('image', file);
    formData.append('key', this.apiKey);

    return this.http.post<ImgBBResponse>(this.apiUrl, formData).pipe(
      map(response => response.data.url)
    );
  }
}
