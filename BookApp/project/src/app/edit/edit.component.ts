import { Component, OnInit } from '@angular/core';
import { edit } from '../edit';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { RouterserviceService } from '../routerservice.service';
import { AuthenticationService } from '../authentication.service';
import Swal from 'sweetalert2';
import { UpdateDTO } from './edit/edit.model';
import { PasswordRestService } from '../password-rest.service';
import { Router } from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css'],
  providers:[PasswordRestService]
})
export class EditComponent implements OnInit {
  edit: edit = new edit();
  editArray: Array<edit> = [];

  editform: FormGroup;
  name:string;
  gender:string;
  city:string;
  updateDetails:UpdateDTO;
  constructor(private _snackBar: MatSnackBar,private authenticateService :AuthenticationService,private routerService :RouterserviceService,public passwordservice:PasswordRestService, private rute: Router) {}
   
  username = sessionStorage.getItem("name");
  usergender = sessionStorage.getItem("gender");
  usercity = sessionStorage.getItem("city");
  userId = sessionStorage.getItem("id");
  image : FormControl;
  genre: FormControl;
  imageURL: string;
  id : number;

  fileSelected(event) {
    const file = event.target.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = (_event => {
      this.imageURL = reader.result.toString();
      console.log(this.imageURL);
    })
  }

PopulateDetails (){
 this.name = this.username;
 this.gender = this.usergender;
 this.city= this.usercity;

}
   
  ngOnInit() {
    this.PopulateDetails();

  }
 
  UpdateProfile(){
 this.updateDetails = new UpdateDTO ();
 this.updateDetails.name = this.name;
 this.updateDetails.city = this.city;
 this.updateDetails.gender = this.gender;
  this.passwordservice.updateUserDetails(this.userId,this.updateDetails).subscribe(x => {
  if(x !== null){
  //  Swal.fire('User Updated Suceesfully','success');
  this._snackBar.open('User Updated Suceesfully','success', {
    duration: 2000,
  });
    this.rute.navigate(['/home'])
  }
  else{
    //Swal.fire('Failed To Update User' ,'error');
    this._snackBar.open('Failed To Update User' ,'error', {
      duration: 2000,
    });
  }
})

  }



}
