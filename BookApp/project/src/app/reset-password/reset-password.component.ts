import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PasswordRestService } from '../password-rest.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Resetpassword } from '../resetpassword'
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  passwordRestService: any;
  constructor(private _snackBar: MatSnackBar,private route: ActivatedRoute, private resetService: PasswordRestService, private httpClient: HttpClient, private rute: Router) { }

  resetpassword: Resetpassword = new Resetpassword();
  resetPasswordForm: FormGroup;
  password: FormControl;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.validate(id);

    if (sessionStorage.getItem('key') != null) {
      this.resetService.tohome();
    }
    else {
      this.password = new FormControl('', Validators.required),
        this.resetPasswordForm = new FormGroup({
          password: this.password,
        });
    }
  }


  validate(linkId) {
    this.resetService.validateLink(linkId).subscribe((response) => {
      if (response !== null) {
        localStorage.setItem("id", String(response));
        // console.log("##########", localStorage.getItem("id"));
      }
    }
      , error => {
        // console.log("error")
       // Swal.fire('Your link has expired please check!');
       this._snackBar.open("Your link has expired please check!", "Ok", {
        duration: 2000,
      });
        this.rute.navigate(['/forget-password'])
      }
    )

  }

  reSetPass() {
    // console.log(username)
    console.log(this.resetPasswordForm.value['password']);
    this.resetpassword.password = this.resetPasswordForm.value['password'];
    this.resetpassword.userId = localStorage.getItem("id");
    this.resetService.resetPassword(this.resetpassword).subscribe(data => {
     // Swal.fire("Password Rest  successfully")
     this._snackBar.open("Password Rest  successfully", "Ok", {
      duration: 2000,
    });
 
      this.rute.navigate(['/login'])
    },
      Error => {
       // Swal.fire("Your link has expired please check!");
       this._snackBar.open("Your link has expired please check!", "Ok", {
        duration: 2000,
      });
        this.rute.navigate(['/forget-password'])
      });
    //  console.log("clicked")

  }


}
