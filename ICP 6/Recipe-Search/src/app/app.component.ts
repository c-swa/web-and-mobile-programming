import { Component } from '@angular/core';
import {HttpClient, HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private httpClient: HttpClient) {
  }

  title = 'Recipe-Search';
  // Search request:
  recipe = '';

  // Search Results:
  hits = [];

  // API keys/necessary bits:
  app_id = "dd7d20b9";
  app_key = "eaf4b5b351249770ee7d65b7f434ab20";
  // Search Result indices
  start = '0';
  end = '10';

  search(){
    // Generate URL to make request to.
    let search_url = 'https://api.edamam.com/search';
    search_url += "?q=" + this.recipe;
    search_url += "&app_id=" + this.app_id;
    search_url += "&app_key=" +this.app_key;
    search_url += "&from=" + this.start;
    search_url += "&to=" +this.end;

    // output URL for testing
    console.log(search_url);

    // Make Request
    this.httpClient.get<any>(search_url).subscribe(data=>{
      console.log(data.hits);
      this.hits = data.hits;
      console.log(this.hits);
    })
  }
}
