<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="col-lg-2 col-md-2"></div>
	<div class="col-lg-10 col-md-10 collapse navbar-collapse">
    	<ul class="nav navbar-nav" data-ng-controller="EntriesNavbarCtrl" data-ng-show="active">
    		<li class="btn-group">
				<button type="button" class="btn btn-default navbar-btn" title="Mark all as read" data-ng-click="markAllRead()">
					<span class="glyphicon glyphicon-ok"></span>
				</button>
			</li>
			<li class="btn-group">
				<button type="button" class="btn btn-default navbar-btn" title="Refresh" data-ng-click="refreshEntries()">
					<span class="glyphicon glyphicon-refresh"></span>
				</button>
			</li>
			<li class="btn-group" data-toggle="buttons">
				<label class="btn btn-default navbar-btn" title="All" data-ng-class="{active: viewService.entryFilter.selection == 'all'}" data-ng-click="setSelection('all')">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-th-list"></span>
		  		</label>
		  		<label class="btn btn-default navbar-btn" title="Unread" data-ng-class="{active: viewService.entryFilter.selection == 'unread'}" data-ng-click="setSelection('unread')">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-unchecked"></span>
		  		</label>
		  		<label class="btn btn-default navbar-btn" title="Starred" data-ng-class="{active: viewService.entryFilter.selection == 'starred'}" data-ng-click="setSelection('starred')">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-star"></span>
		  		</label>
			</li>
			<li class="btn-group" data-toggle="buttons">
				<label class="btn btn-default navbar-btn" title="Ascending" data-ng-click="setAscendingOrder(true)" data-ng-class="{active: viewService.entryFilter.ascendingOrder}">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-down"></span>
		  		</label>
		  		<label class="btn btn-default navbar-btn" title="Descending" data-ng-click="setAscendingOrder(false)" data-ng-class="{active: !viewService.entryFilter.ascendingOrder}">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-up"></span>
		  		</label>
			</li>
			<li data-ng-show="!loading">
				<p class="navbar-text">Items displayed:&nbsp;<span>{{entries.length}}</span></p>
			</li>
			<li data-ng-show="loading">
				<p class="navbar-text">Loading...</p>
			</li>
		</ul>
    	<ul class="nav navbar-nav" data-ng-controller="ArchivesNavbarCtrl" data-ng-show="active">
			<li class="btn-group">
				<button type="button" class="btn btn-default navbar-btn" title="Refresh" data-ng-click="refreshEntries()">
					<span class="glyphicon glyphicon-refresh"></span>
				</button>
			</li>
			<li class="btn-group" data-toggle="buttons">
				<label class="btn btn-default navbar-btn" title="Ascending" data-ng-click="setAscendingOrder(true)" data-ng-class="{active: viewService.archiveFilter.ascendingOrder}">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-down"></span>
		  		</label>
		  		<label class="btn btn-default navbar-btn" title="Descending" data-ng-click="setAscendingOrder(false)" data-ng-class="{active: !viewService.archiveFilter.ascendingOrder}">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-up"></span>
		  		</label>
			</li>
			<li data-ng-show="!loading">
				<p class="navbar-text">Items displayed:&nbsp;<span>{{entries.length}}</span></p>
			</li>
			<li data-ng-show="loading">
				<p class="navbar-text">Loading...</p>
			</li>
		</ul>
	</div>
</nav>