import { Component, input, effect, signal } from '@angular/core'; // Import 'input'
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Idea, Tag, IdeaLink } from '../../services/idea.service';
import { RichEditorComponent } from '../../shared/rich-editor/rich-editor.component';

@Component({
  selector: 'ideapage', // Make sure the selector matches the tag in landing.html
  standalone: true,
  imports: [CommonModule, FormsModule, RichEditorComponent],
  templateUrl: './ideapage.html',
  styleUrl: './ideapage.css'
})
export class Ideapage {
  // Read-only input from the parent component
  linkedIdea = input<Idea | null>(null);

  // Writable local signal to hold the idea data for editing
  editableIdea = signal<Idea | null>(null);

  constructor() {
    // Effect to clone the input data into the local, writable signal whenever the input changes.
    // This allows the editor to modify editableIdea().text without violating the input signal's immutability.
    effect(() => {
      const idea = this.linkedIdea();
      if (idea) {
        // Deep clone the idea object before setting it, preventing unwanted side effects
        this.editableIdea.set(JSON.parse(JSON.stringify(idea)));
      } else {
        this.editableIdea.set(null);
      }
    });
  }

  // Example method to simulate saving the data back to the backend
  saveIdea() {
    const dataToSave = this.editableIdea();
    if (dataToSave) {
      console.log('Saving updated idea content:', dataToSave.text);
      // In a real application, you would call your idea.service here:
      // this.ideaService.updateIdea(dataToSave).subscribe(...)
      alert('Idea saved successfully (simulated)! Check console for HTML output.');
    }
  }

  clickLinkedIdea(clickedIdea: IdeaLink) {
    console.log('idea clicked:', clickedIdea.id);
  }
}
