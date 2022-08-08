/**
 * Created by dmihaylov on 26.10.2017.
 */
import { DataTreeFile } from './dataTreeFile';

export class ModelTreeFile {
    constructor() {
        this.tree = new DataTreeFile();
    }
    public tree: DataTreeFile;
    public uncertainties: Object =
    {
        header: '',
        name: ''
    };
}
