import { Component, input, effect, signal, output } from '@angular/core'; // Import 'input'

import { FormsModule } from '@angular/forms';
import { Idea, Tag, IdeaLink } from '../../services/idea.service';
import { RichEditorComponent } from '../../shared/rich-editor/rich-editor.component';

@Component({
  selector: 'ideapage', // Make sure the selector matches the tag in landing.html
  standalone: true,
  imports: [FormsModule, RichEditorComponent],
  templateUrl: './ideapage.html',
  styleUrl: './ideapage.css'
})
export class Ideapage {
  // Read-only input from the parent component
  linkedIdea = input<Idea | null>(null);

  // **1. Define the output signal**
  // Emits the ID of the IdeaLink when clicked
  ideaClicked = output<string>();

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

  /**
   * Saves the currently edited idea.
   *
   * This method currently simulates saving by logging the idea content and showing an alert.
   * In a real application, replace this with a call to the idea service to persist changes.
   */
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
    this.ideaClicked.emit(clickedIdea.id)
  }
}
