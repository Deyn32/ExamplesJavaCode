/**
 * Created by dmihaylov on 14.02.2018.
 */
import { Injectable } from '@angular/core';
import { NavigationEnd, Router, Routes } from '@angular/router';

@Injectable()
export class RouteService {
    public static basePath: string = 'appComp.html';
    public static mergedTrees: string = 'mergedTrees';
}