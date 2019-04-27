import { Component, Inject } from '@angular/core';
import { latLng, tileLayer, marker, Layer, icon, divIcon } from 'leaflet';
import { MatSnackBar, MatDialog, MAT_DIALOG_DATA } from '@angular/material';
import { timer } from 'rxjs';
import { ItemsService, AssignationsService, LocationsService, InventoryItem, Assignation, Location } from '@angular-courageous/api-services'

export interface CheckOutDialogData {
  secret: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  inventory: InventoryItem[] = [];
  assignations: Map<string, Assignation> = new Map();
  locations: Map<string, Location> = new Map();

  homeCoordinates = latLng(42.3721578, -71.0516434);
  title = 'courageous-tracking';

  refreshRate = 10; // In seconds

  mapOptions = {
    layers: [
      tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
      }),
      tileLayer("https://tileservice.charts.noaa.gov/tiles/50000_1/{z}/{x}/{y}.png", {
        attribution: '&copy NOAA'
      }),
    ],
    zoom: 15,
    center: this.homeCoordinates,
  };
  mapCenter = this.homeCoordinates;
  mapMarkers: Layer[] = [];

  constructor(private itemsService: ItemsService,
    private assignationsService: AssignationsService,
    private locationsService: LocationsService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog) { }

  ngOnInit(): void {
    this.refresh();
    this.addMarkers();
    this.refreshTimer();
  }

  refresh(): void {
    this.updateInventory();
    this.updateAssignations();
    this.updateLocations();
  }

  centerHome(): void {
    this.mapCenter = this.homeCoordinates;
  }

  updateInventory(): void {
    this.itemsService.getAllItems().subscribe((e) => {
      console.log(e);
      // Sort the items by name
      this.inventory = e.sort((a, b) => { return a.name.localeCompare(b.name) });
    });
  }

  updateAssignations(): void {
    this.assignationsService.getAllAssignations().subscribe((e) => {
      console.log(e);
      // Convert the array to a map, indexed by the item ID
      let newAssignations: Map<string, Assignation> = new Map();
      e.forEach((v) => newAssignations.set(v.inventoryItemId, v));
      this.assignations = newAssignations;
    });
  }

  updateLocations(): void {
    this.locationsService.getAllLocations().subscribe((e) => {
      console.log(e);
      // Convert the array to a map, indexed by the item ID
      let newLocations: Map<string, Location> = new Map();
      e.forEach((v) => newLocations.set(v.inventoryItemId, v));
      this.locations = newLocations;
      this.addMarkers();
    });
  }

  assignItem(uuid: string): void {
    let newAssignation: Assignation = {
      "inventoryItemId": uuid,
    };

    this.assignationsService.addAssignation(newAssignation).subscribe((e) => {
      console.log(e);
      // Refresh the assignations
      this.updateAssignations();

      // Display a dialog with the barcode to scan by the user
      this.dialog.open(CheckOutDialog, {
        data: {
          secret: e.secret
        }
      });
    });
  }

  releaseItem(uuid: string): void {
    // Get the item with the UUID
    let item = this.getItemWithUUID(uuid);

    this.assignationsService.deleteAssignation(uuid).subscribe((e) => {
      console.log(e);
      this.updateAssignations();

      if (item) {
        this.snackBar.open('Checked-in: ' + item.name, "OK", {
          duration: 1500
        });
      }
    });
  }

  getItemWithUUID(uuid: string): InventoryItem {
    return this.inventory.find((v) => { return v.id.localeCompare(uuid) == 0 })
  }

  addMarkers(): void {
    let newMarkers: Layer[] = [];

    // Base location marker: "home"
    newMarkers.push(marker(this.homeCoordinates,
      {
        icon: divIcon({
          html: '<i class="material-icons md-24">home</i>',
          className: 'my-div-icon',
          iconAnchor: [12, 24],
        })
      }));

    // Marker for the items
    this.locations.forEach((loc) => {
      let itemId = loc.inventoryItemId;
      let mark = marker([loc.latitude, loc.longitude],
        {
          icon: divIcon({
            html: '<i class="material-icons md-24">person_pin</i>',
            className: 'my-div-icon',
            iconAnchor: [12, 24],
          })
        });

      mark.bindPopup((m) => {
        let item = this.getItemWithUUID(itemId);
        let assi = this.assignations.get(itemId);

        let itemName = (item) ? item.name : "Unknown";
        let userName = (assi) ? assi.userName : "Unknown user";
        let userPhone = (assi) ? assi.userPhone : "Unknown phone number";

        return '<b>' + userName + '</b><br>' + itemName + '<br><a href="tel:' + userPhone + '">' + userPhone + "</a>";
      });

      newMarkers.push(mark);
    });

    this.mapMarkers = newMarkers;
  }

  refreshTimer() {
    let refreshRateMs = this.refreshRate * 1000;
    const source = timer(refreshRateMs, refreshRateMs);
    source.subscribe(val => {
      this.refresh();
    });
  }
}


@Component({
  selector: 'check-out-dialog',
  templateUrl: 'check-out-dialog.html',
})
export class CheckOutDialog {
  constructor(@Inject(MAT_DIALOG_DATA) public data: CheckOutDialogData) { }
}
