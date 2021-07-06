import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import{PasswordRestService} from '../password-rest.service'
import { RouterserviceService } from '../routerservice.service';
import { Fpassword } from '../fpassword';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  fpass: Fpassword = new Fpassword();
  fpassword: FormGroup;
  username: FormControl;


  constructor( private _snackBar: MatSnackBar, private routerservice: RouterserviceService, private passwordRestService: PasswordRestService, private rute: Router) { }

  ngOnInit() {

    if (sessionStorage.getItem('key') != null) {
      this.routerservice.tohome();
    }
    else {
      this.username = new FormControl('', Validators.required),
        this.fpassword = new FormGroup({
        username: this.username,
 
      });
    }
  }

  pResetSend(username){
    // console.log(username)
    this.fpass.username = this.fpassword.value.username;
    this.passwordRestService.resetLink(this.fpassword.value).subscribe(data => {
  //  Swal.fire("Rest Link Send successfully")
  
    this.rute.navigate(['/login'])},
    Error => {/*Swal.fire("You have Entered invalid email")*/
    // console.log("clicked")
    this._snackBar.open("You have Entered invalid email", "Ok", {
      duration: 2000,
    });
  })
  }
  
  }


