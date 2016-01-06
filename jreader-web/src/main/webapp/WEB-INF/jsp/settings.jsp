<form id="group-form" class="form-inline" data-ng-submit="createGroup()">
	<div class="form-group">
		<label for="new-group-title">Create group entitled</label>
		<input id="new-group-title" class="form-control" data-ng-model="newGroupTitle" type="text" name="title" placeholder="title" />
	</div>
	<button type="submit" class="btn btn-default" title="Create group">
		<span class="glyphicon glyphicon-ok"></span>
	</button>
</form>

<form id="subscription-form" class="form-inline" data-ng-submit="subscribe()">
	<div class="form-group">
		<label for="new-subscription-url">Subscribe to</label>
		<input id="new-subscription-url" type="text" placeholder="URL" class="form-control" data-ng-model="newSubscription.url" />
	</div>
	<p class="form-control-static">in group</p>
	<div class="form-group">
		<div class="dropdown">
			<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
				<span class="title">{{newSubscription.group.title}}</span><span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li data-ng-repeat="group in groups.items" data-ng-click="newSubscription.group=group">
					<a tabindex="-1" href="">{{group.title}}</a>
				</li>
			</ul>
		</div>
	</div>
	<div class="form-group">
		<button type="submit" class="btn btn-default" title="Subscribe">
			<span class="glyphicon glyphicon-ok"></span>
		</button>
	</div>
</form>

<table id="subscription-settings" class="table">
	<tbody class="settings-group" data-ng-repeat="group in groups.items">
		<tr class="group-title">
			<td>
				<span class="title" data-ng-show="!group.editingTitle" data-ng-click="editTitle(group)">{{group.title}}</span>
				<form class="form-inline" data-ng-show="group.editingTitle" data-ng-submit="entitleGroup(group)">
					<input class="form-control" type="text" placeholder="title" data-ng-model="group.newTitle" />
					<button type="submit" class="btn btn-default" title="Update">
						<span class="glyphicon glyphicon-ok"></span>
					</button>
				</form>
			</td>
			<td>
				<form class="form" data-ng-if="group.subscriptions.length <= 0" data-ng-submit="deleteGroup(group.id)">
					<button type="submit" class="btn btn-default" title="Delete">
						<span class="glyphicon glyphicon-remove"></span>
					</button>
				</form>
			</td>
			<td>
				<form class="form" data-ng-if="!$first" data-ng-submit="moveGroupUp(group.id)">
					<button type="submit" class="btn btn-default" title="Move up">
						<span class="glyphicon glyphicon-arrow-up"></span>
					</button>
				</form>
			</td>
			<td>
				<form class="form" data-ng-if="!$last" data-ng-submit="moveGroupDown(group.id)">
					<button type="submit" class="btn btn-default" title="Move down">
						<span class="glyphicon glyphicon-arrow-down"></span>
					</button>
				</form>
			</td>
		</tr>
		<tr class="settings-item" data-ng-repeat="subscription in group.subscriptions">
			<td class="title">
				<span data-ng-show="!subscription.editingTitle" data-ng-click="editTitle(subscription)">{{subscription.title}}</span>
				<form class="form-inline set-item-title" data-ng-show="subscription.editingTitle" data-ng-submit="entitleSubscription(group, subscription)">
					<input class="form-control" type="text" placeholder="name" data-ng-model="subscription.newTitle" />
					<button type="submit" class="btn btn-default" title="Update">
						<span class="glyphicon glyphicon-ok"></span>
					</button>
				</form>
			</td>
			<td>
				<form class="form" data-ng-submit="unsubscribe(group.id, subscription.id)">
					<button type="submit" class="btn btn-default" title="Unsubscribe">
						<span class="glyphicon glyphicon-remove"></span>
					</button>
				</form>
			</td>
			<td>
				<form class="form" data-ng-if="!$first" data-ng-submit="moveSubscriptionUp(group.id, subscription.id)">
					<button type="submit" class="btn btn-default" title="Move up">
						<span class="glyphicon glyphicon-arrow-up"></span>
					</button>
				</form>
			</td>
			<td>
				<form class="form" data-ng-if="!$last" data-ng-submit="moveSubscriptionDown(group.id, subscription.id)">
					<button type="submit" class="btn btn-default" title="Move down">
						<span class="glyphicon glyphicon-arrow-down"></span>
					</button>
				</form>
			</td>
		</tr>
	</tbody>
</table>

<form class="form-inline" data-ng-submit="createArchive()">
	<div class="form-group">
		<label for="new-archive-title">Create archive entitled</label>
		<input class="form-control" type="text" id="new-archive-title" name="title" placeholder="title" data-ng-model="newArchiveTitle" />
	</div>
	<button type="submit" class="btn btn-default" title="Create archive">
		<span class="glyphicon glyphicon-ok"></span>
	</button>
</form>

<table class="table">
	<tbody class="settings-group" data-ng-repeat="archive in archives">
		<tr class="settings-item">
			<td class="title">
				<span data-ng-show="!archive.editingTitle" data-ng-click="editTitle(archive)">{{archive.title}}</span>
				<form class="form-inline set-group-title" data-ng-show="archive.editingTitle" data-ng-click="entitleArchive(archive)">
					<input class="form-control" type="text" placeholder="name" data-ng-model="archive.newTitle" />
					<button type="submit" class="btn btn-default" title="Update">
						<span class="glyphicon glyphicon-ok"></span>
					</button>
				</form>
			</td>
			<td>
				<form class="form" data-ng-submit="deleteArchive(archive.id)">
					<button type="submit" class="btn btn-default" title="Delete">
						<span class="glyphicon glyphicon-remove"></span>
					</button>
				</form>
			</td>
			<td>
				<form class="form" data-ng-if="!$first" data-ng-submit="moveArchiveUp(archive.id)">
					<button type="submit" class="btn btn-default" title="Move up">
						<span class="glyphicon glyphicon-arrow-up"></span>
					</button>
				</form>
			</td>
			<td>
				<form class="form" data-ng-if="!$last" data-ng-submit="moveArchiveDown(archive.id)">
					<button type="submit" class="btn btn-default" title="Move down">
						<span class="glyphicon glyphicon-arrow-down"></span>
					</button>
				</form>
			</td>
		</tr>
	</tbody>
</table>

<form class="form-inline" data-ng-submit="exportSubscriptions()">
	<button type="submit" class="btn btn-default" title="Export">Export</button>
</form>

<textarea id="export-import" class="form-control" rows="10" data-ng-model="exportImportJson"></textarea>

<form class="form-inline" data-ng-submit="importSubscriptions()">
	<button type="submit" class="btn btn-default" title="Import">Import</button>
</form>

<textarea id="import-log" class="form-control" rows="10" data-ng-model="importLog" data-ng-show="showImportLog"></textarea>
	