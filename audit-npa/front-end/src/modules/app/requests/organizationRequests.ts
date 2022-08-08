/**
 * Created by dmihaylov on 26.03.2018.
 */
import { Inject, Injectable } from '@angular/core';
import { Http, RequestOptions, URLSearchParams } from '@angular/http';
import {RequestService} from "../services/requestService";

@Injectable()
export class OrganizationRequests {
    constructor(@Inject(Http) private http: Http,
                @Inject(RequestService) private req: RequestService) {}

    private url: string = this.req.baseUrl;

    public getOrganizations() {
        let fzOrganizations: string = '/directory/fzOrganizations';
        return this.http.get(this.url + fzOrganizations);
    }

    public getSaveOrgsNpa(orgs: number[], idNpa: number) {
        let fzSaveOrgsNpa = "/directory/fzSaveOrgsNpa";
        const myParams = new URLSearchParams();
        myParams.append('idOrgs', orgs.toString());
        if (idNpa != null)
            myParams.append('id', idNpa.toString());
        let options = new RequestOptions({params: myParams });
        return this.http.get(this.url + fzSaveOrgsNpa, options);
    }

    public getOrgsNpa(id: number) {
        let fzOrgsNpa = "/directory/fzOrgsNpa";
        const myParams = new URLSearchParams();
        myParams.append('id', id.toString());
        let options = new RequestOptions({ params: myParams});
        return this.http.get(this.url + fzOrgsNpa, options);
    }


}