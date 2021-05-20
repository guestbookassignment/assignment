import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EntryListService } from './admin.service';


@Component({
  selector: 'app-list-entry',
  templateUrl: './list-entry.component.html',
  styleUrls: ['./list-entry.component.scss'],
  providers: [EntryListService]
})
export class ListEntryComponent implements OnInit {
  tableData: Array<any>;

  /* pagination Info */
  pageSize = 10;
  pageNumber = 1;

  constructor(private _tablesDataService: EntryListService, private router: Router) { }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this._tablesDataService.getAllEntries().then(
      data=> {
        this.tableData = data;
      },
      error=>{
        if(error["status"] != 404) {
          alert("No access zone!");
          this.router.navigate(['/login']);
        }
      }
    )
  }

  pageChanged(pN: number): void {
    this.pageNumber = pN;
  }

  approveEntry(index, id) {
    this._tablesDataService.approveEntry(id).subscribe(
      data=> {
        this.tableData[index].approved = true;
      },
      error=>{
        alert("Something went wrong");
      }
    )
  }

  deleteEntry(index, id) {
    this._tablesDataService.deleteEntry(id).subscribe(
      data=> {
        this.tableData[index].deleted = true;
      },
      error=>{
        alert("Something went wrong");
      }
    )
  }

}
