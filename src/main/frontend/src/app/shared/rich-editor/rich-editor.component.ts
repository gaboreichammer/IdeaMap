import { Component, model, input, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { QuillModule } from 'ngx-quill';

@Component({
  selector: 'app-rich-editor',
  standalone: true,
  imports: [CommonModule, FormsModule, QuillModule],
  templateUrl: './rich-editor.component.html',
  styleUrl: './rich-editor.component.css'
})
export class RichEditorComponent {
  content = model<string>('');

  constructor() {}

  logChange(event: any) {
    // Optional: Log changes for debugging or sending events upstream
    console.log('Editor content changed. Delta:', event.delta);
  }
}
