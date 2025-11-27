import { Component } from '@angular/core';

import { FormsModule } from '@angular/forms';

@Component({
  selector: 'idea-group-new',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './ideaGroupNew.html',
  styleUrl: './ideaGroupNew.css'
})
export class IdeaGroupNew {
    ideaGroupName: string = '';

    saveIdeaGroup(): void {
        console.log('Saving idea group with name:', this.ideaGroupName);
    }
}
