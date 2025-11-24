import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'idea-group-new',
  standalone: true,
  imports: [CommonModule, FormsModule],
    templateUrl: './ideaGroupNew.html',
    styleUrl: './ideaGroupNew.css'
})
export class IdeaGroupNew {
    ideaGroupName: string = '';

                saveIdeaGroup(): void {
        console.log('Saving idea group with name:', this.ideaGroupName);
    }
}
