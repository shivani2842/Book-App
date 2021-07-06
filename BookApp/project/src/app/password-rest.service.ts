import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Fpassword } from './fpassword';
import { Resetpassword } from './resetpassword';


@Injectable({
  providedIn: 'root'
})
export class PasswordRestService {
  tohome: any;

  constructor( private httpClient: HttpClient) { }

  resetLink(username:Fpassword){
    return this.httpClient.post(`http://localhost:8080/api/users/forget-password` , username);
  }

  validateLink(linkId){
    return this.httpClient.get(`http://localhost:8080/api/users/validate-link/` + linkId)
  }

  resetPassword(resetPassword:Resetpassword){
    return this.httpClient.post(`http://localhost:8080/api/users/set-password`, resetPassword)
  }

  updateUserDetails(id,details:any){
    return this.httpClient.put(`http://localhost:8080/api/users/` + id ,details)
  }
}
