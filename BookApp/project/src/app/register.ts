import { FormBuilder } from '@angular/forms';

export class register
{
    password:string;
    username:string;
    name: string;
    gender: string;
    city: string;
    image:any;
    genre:any;
  repeatpassword: any;
    constructor(){
        this.username='';
        this.password='';
        this.name='';
        this.gender='';
        this.city='';
        this.image='';
        this.genre='';
        this.repeatpassword='';
    }

    
}