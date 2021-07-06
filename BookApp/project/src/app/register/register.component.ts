import { Component, OnInit } from '@angular/core';
import { FormGroup ,Validators,FormControl, FormBuilder, FormArray } from '@angular/forms';
import {register} from 'src/app/register';
import { AuthenticationService } from '../authentication.service';
import { RouterserviceService } from '../routerservice.service';
import Swal from 'sweetalert2';
import {MatSnackBar} from '@angular/material/snack-bar';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  register: register = new register();
  registerArray: Array<register> = [];
  // for checkbox
  // ordersData = [
  //   { id: 100, name: 'order 1' },
  //   { id: 200, name: 'order 2' },
  //   { id: 300, name: 'order 3' },
  //   { id: 400, name: 'order 4' }
  // ];
  registerform: FormGroup;
  username: FormControl;
  password: FormControl;
  name : FormControl;
  gender : FormControl;
  city : FormControl;
  image : FormControl;
  genre: FormControl;
  imageURL: string;
  constructor(private _snackBar: MatSnackBar,private authenticateService :AuthenticationService,private routerService :RouterserviceService)
  {
  }
  fileSelected(event) {
    const file = event.target.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = (_event => {
      this.imageURL = reader.result.toString();
      console.log(this.imageURL);
    })
  }
  ngOnInit() {
    if (sessionStorage.getItem('key') != null) {
      this.routerService.tohome();
    }
    else{
    this.username = new FormControl('', Validators.required),
    this.password = new FormControl('', Validators.required),
    this.name = new FormControl('' , Validators.required),
    this.gender = new FormControl('' , Validators.required),
    this.city = new FormControl('' , Validators.required),
    this.image = new FormControl('', Validators.required),
    this.genre = new FormControl('')
      this. registerform= new FormGroup({
      username: this.username,
      password: this.password,
      name: this.name,
      gender: this.gender,
      city: this.city, 
      image:this.image,
      genre : this.genre
    });
  }
  }
  login()
  {
    this.routerService.tologin();
  }
  registerSubmit()
  {
    console.log(this.registerform.value.password);
    this.register.password=this.registerform.value.password;
    this.register.username=this.registerform.value.username;
    this.register.name = this.registerform.value.name;
    this.register.gender= this.registerform.value.gender;
    this.register.city = this.registerform.value.city;
    this.register.image = this.imageURL;
    this.register.genre = this.registerform.value.genre;
    this.registerArray.push(this.register);


    this.authenticateService.addUser(this.register).subscribe((data) => {
      // console.log("inside regsiter angular")
      this.routerService.tologin();
     // Swal.fire('user sucessfully registered.');
     this._snackBar.open("user sucessfully registered.", "Ok", {
      duration: 2000,
    });
 
    },
      error => {
        console.log(error);
       // Swal.fire('user already exist with this email id!!')
       this._snackBar.open("user already exist with this email id!! ", "Ok", {
        duration: 2000,
      });
      });
     

  }
  
}
