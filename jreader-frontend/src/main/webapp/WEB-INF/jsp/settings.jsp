<div class="add-subscription">
	<form ng-submit="createGroup()">
		<span>Add group</span><input ng-model="newGroupTitle"
			type="text" name="title" placeholder="group name" /><input
			type="image" src="/images/tick_black.png" title="Create group" />
	</form>
	<form ng-submit="subscribe()">
		<span>Add subscription to group</span><select
			ng-model="newSubscription.group"
			ng-options="group.title for group in subscriptionGroups">
		</select><span>from</span><input
			type="text" placeholder="URL" size="80" ng-model="newSubscription.url" /><input
			type="image" src="/images/tick_black.png" title="Subscribe" />
	</form>
</div>

<table id="subscription-settings">
	<tbody class="settings-group" ng-repeat="group in subscriptionGroups">
		<tr class="group-title">
			<td>
				<span class="title" ng-show="!group.editingTitle" ng-click="editTitle(group)">{{group.title}}</span>
				<form class="set-group-title" ng-show="group.editingTitle" ng-click="entitleGroup(group)">
					<input type="text" placeholder="name" ng-model="group.newTitle" />
					<input type="image" src="/images/tick_black.png" title="Change" />
				</form>
			</td>
			<td class="action">
				<form ng-if="group.subscriptions.length <= 0"
					ng-submit="deleteGroup(group.id)">
					<input type="image" src="/images/cross_black.png" title="Delete" />
				</form>
			</td>
			<td class="action">
				<form ng-if="!$first" ng-submit="moveGroupUp(group.id)">
					<input type="image" src="/images/move_up_black.png" title="Move up" />
				</form>
			</td>
			<td class="action">
				<form ng-if="!$last" ng-submit="moveGroupDown(group.id)">
					<input type="image" src="/images/move_down_black.png" title="Move down" />
				</form>
			</td>
		</tr>
		<tr class="settings-item" ng-repeat="subscription in group.subscriptions">
			<td>
				<span class="title" ng-show="!subscription.editingTitle" ng-click="editTitle(subscription)">{{subscription.title}}</span>
				<form class="set-item-title" ng-show="subscription.editingTitle" ng-click="entitleSubscription(group, subscription)">
					<input type="text" placeholder="name" ng-model="subscription.newTitle" />
					<input type="image" src="/images/tick_black.png" title="Change" />
				</form>
			</td>
			<td class="action">
				<form ng-submit="unsubscribe(group.id, subscription.id)">
					<input type="image" src="/images/cross_black.png" title="Unsubscribe" class="unsubscribe-button" />
				</form>
			</td>
			<td class="action">
				<form ng-if="!$first" ng-submit="moveSubscriptionUp(group.id, subscription.id)">
					<input type="image" src="/images/move_up_black.png" title="Move up" />
				</form>
			</td>
			<td class="action">
				<form ng-if="!$last" ng-submit="moveSubscriptionDown(group.id, subscription.id)">
					<input type="image" src="/images/move_down_black.png" title="Move down" />
				</form>
			</td>
		</tr>
	</tbody>
</table>

<div class="add-archive">
	<form ng-submit="createArchive()">
		<span>Add archive</span><input ng-model="newArchiveTitle"
			type="text" name="title" placeholder="archive name" /><input
			type="image" src="/images/tick_black.png" title="Create archive" />
	</form>
</div>

<table id="archive-settings">
	<tbody class="settings-archive" ng-repeat="archive in archives">
		<tr class="group-title">
			<td>
				<span class="title" ng-show="!archive.editingTitle" ng-click="editTitle(archive)">{{archive.title}}</span>
				<form class="set-group-title" ng-show="archive.editingTitle" ng-click="entitleArchive(archive)">
					<input type="text" placeholder="name" ng-model="archive.newTitle" />
					<input type="image" src="/images/tick_black.png" title="Change" />
				</form>
			</td>
			<td class="action">
				<form ng-submit="deleteArchive(archive.id)">
					<input type="image" src="/images/cross_black.png" title="Delete" />
				</form>
			</td>
			<td class="action">
				<form ng-if="!$first" ng-submit="moveArchiveUp(archive.id)">
					<input type="image" src="/images/move_up_black.png" title="Move up" />
				</form>
			</td>
			<td class="action">
				<form ng-if="!$last" ng-submit="moveArchiveDown(archive.id)">
					<input type="image" src="/images/move_down_black.png" title="Move down" />
				</form>
			</td>
		</tr>
	</tbody>
</table>

<form id="export" ng-submit="exportSubscriptions()">
	<input type="submit" value="Export" />
</form>
<textarea id="export-import" ng-model="exportImportJson"></textarea>
<form id="import" ng-submit="importSubscriptions()">
	<input type="submit" value="Import" />
</form>
<textarea id="import-log" ng-model="importLog" ng-show="showImportLog"></textarea>
	