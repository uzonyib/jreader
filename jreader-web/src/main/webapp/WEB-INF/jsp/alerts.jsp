<div id="alerts-container" class="col-lg-4 col-md-6 col-sm-8 col-xs-10">
	<div data-ng-repeat="alert in alertService.alerts" class="alert alert-danger" role="alert" data-animate-alert>
	  <span class="glyphicon glyphicon-exclamation-sign"></span>
	  <span>{{::alert.message}}</span>
	</div>
</div>