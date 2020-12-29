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

  // Timer
  date: string;
  time: string;

  clock: any;
  isTimerDisplay = false;
  timerText: any;

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

  startTimer(): void {
    const countDownDate = new Date(this.date + ' ' + this.time).getTime();
    this.clock = setInterval(() => {
        const now = new Date().getTime();
        const distance = countDownDate - now;
        const days = Math.floor(distance / (1000 * 60 * 60 * 24));
        const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((distance % (1000 * 60)) / 1000);
        this.timerText = {
          days,
          hours,
          minutes,
          seconds
        };
      }
      , 1000);
  }
  stopTimer(): void {
    const days = 0;
    const hours = 0;
    const minutes = 0;
    const seconds = 0;
    this.timerText = {
      days,
      hours,
      minutes,
      seconds
    };
    clearInterval(this.clock);
  }
}
