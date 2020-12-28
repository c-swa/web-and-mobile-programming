import { Component } from '@angular/core';
import { Tasks } from './Tasks';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  // To Do List
  title = 'To Do List & Timer';

  public tasks: Array<{taskName: string, isComplete: boolean}> = [];

  public newTask;

  task = '';

  // Timer
  days: any;
  hours: any;
  minutes: any;
  seconds: any;

  createNewTask(): void{
    if ( this.newTask.taskName !== ''){
      this.tasks.push({
        taskName: this.newTask,
        isComplete: false
      });
    }
  }

  // Mark an item on the list as complete.
  markComplete(index: number): void {
    const task = this.tasks[index];
    if ( task ){
      task.isComplete = !task.isComplete;
    }
  }

  deleteTask(index: number): void {
    this.tasks.splice(index,  1);
  }

  startTimer(endDate: string ): void {
    const countdownTime = new Date(endDate).getTime();
    setInterval(( ) => {
        const currentTime = new Date().getTime();
        const timeRemaining = countdownTime - currentTime;
        const daysRemaining = Math.floor(timeRemaining / (1000 * 60 * 60 * 24));
        const hoursRemaining = Math.floor((timeRemaining % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutesRemaining = Math.floor((timeRemaining % (1000 * 60 * 60)) / (1000 * 60));
        const secondsRemaining = Math.floor((timeRemaining % (1000 * 60)) / 1000);
        this.days = daysRemaining;
        this.hours = hoursRemaining;
        this.minutes = minutesRemaining;
        this.seconds = secondsRemaining;
      }
      , 1000);
  }
}
