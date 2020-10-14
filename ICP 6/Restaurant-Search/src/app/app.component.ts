import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Restaurant-Search';

  // Constructor for the HttpClient library - allows app to call the API.
  constructor(private httpClient: HttpClient) {
  }

  // Variables for application needs:
  search_food = '';
  search_location = '';

  // API keys
  client_id = '1FVLN3QHG2QHRYCYHESF01NTZW4TVBGLM1B4GMP5AQJB43JY';
  client_secret = 'D1SQD1245YMYLAJNEYCWHOZR5ZUPBCS3S2K0DGOA4HQ43OUQ';

  // Default settings for search values
  radius = '10000'; // in meters (default set for 10km distance)
  version = '20201011'; // default set to today's date
  quantity = '10';

  // Array outputting results from API call.
  venues = [];

  search(){
    // Sets up the URL of the API call
    let search_url = 'https://api.foursquare.com/v2/venues/search';
    search_url += '?near=' + this.search_location;
    search_url += '&radius=' + this.radius + '&limit=' + this.quantity;
    search_url += '&query=' + this.search_food;
    search_url += '&client_id=' + this.client_id + '&client_secret=' + this.client_secret;
    search_url += '&v=' + this.version;

    console.log(search_url);

    // Calls the API for a data retrieval.
    this.httpClient.get<any>(search_url).subscribe(data => {
      console.log(data.response.venues);
      this.venues = data.response.venues;
      console.log(this.venues);
    });

  }

}
