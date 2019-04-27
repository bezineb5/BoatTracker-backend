import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { NgxQRCodeModule } from 'ngx-qrcode2';
import { HttpClientModule } from '@angular/common/http';
import { ApiModule, BASE_PATH } from '@angular-courageous/api-services';
import { environment } from '../environments/environment';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatIconModule} from '@angular/material/icon';
import {MatTabsModule} from '@angular/material/tabs';
import {MatListModule} from '@angular/material/list';
import {MatButtonModule} from '@angular/material/button';
import {MatChipsModule} from '@angular/material/chips';
import {MatDialogModule} from '@angular/material/dialog';
import {MatSnackBarModule} from '@angular/material/snack-bar';

import { AppComponent, CheckOutDialog } from './app.component';

@NgModule({
  declarations: [
    AppComponent,
    CheckOutDialog
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatSidenavModule,
    MatIconModule,
    MatTabsModule,
    MatListModule,
    MatButtonModule,
    MatChipsModule,
    MatDialogModule,
    MatSnackBarModule,
    LeafletModule.forRoot(),
    NgxQRCodeModule,
    HttpClientModule,
    ApiModule,
  ],
  providers: [{ provide: BASE_PATH, useValue: environment.API_BASE_PATH }],
  bootstrap: [AppComponent],
  entryComponents: [
    CheckOutDialog,
  ],
})
export class AppModule { }
