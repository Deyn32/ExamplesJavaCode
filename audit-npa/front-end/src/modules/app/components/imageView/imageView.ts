/**
 * Created by dmihaylov on 28.02.2018.
 */
import {Component, OnInit, Input} from '@angular/core';

@Component({
    selector: 'imgview-app',
    templateUrl: './imageView.html'
})

export class ImageViewComponent implements OnInit {
    constructor() {}

    ngOnInit() {

    }

    @Input() htmlText: string;

}