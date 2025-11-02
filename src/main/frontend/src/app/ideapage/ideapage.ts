import { Component, input } from '@angular/core'; // Import 'input'
import { CommonModule } from '@angular/common';
import { Idea, Tag } from '../services/idea.service';

@Component({
  selector: 'ideapage', // Make sure the selector matches the tag in landing.html
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ideapage.html',
  styleUrl: './ideapage.css'
})
export class Ideapage {
  linkedIdea = input<Idea | null>(null);
}
