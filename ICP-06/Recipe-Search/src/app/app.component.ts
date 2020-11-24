import { Component } from '@angular/core';
import {HttpClient, HttpClientModule} from '@angular/common/http';

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
  appId = 'dd7d20b9';
  appKey = 'eaf4b5b351249770ee7d65b7f434ab20';
  // Search Result indices
  start = '0';
  end = '10';

  // tslint:disable-next-line:typedef
  search(){
    // Generate URL to make request to.
    let searchUrl = 'https://api.edamam.com/search';
    searchUrl += '?q=' + this.recipe;
    searchUrl += '&app_id=' + this.appId;
    searchUrl += '&app_key=' + this.appKey;
    searchUrl += '&from=' + this.start;
    searchUrl += '&to=' + this.end;

    // output URL for testing
    console.log(searchUrl);

    // Make Request
    this.httpClient.get<any>(searchUrl).subscribe(data => {
      console.log(data.hits);
      this.hits = data.hits;
      console.log(this.hits);
    });
  }
}
