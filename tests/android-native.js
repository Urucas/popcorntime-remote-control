'use strict';

var test = new (function(){
	
	var instance = this;
	
	this.port = 4723; 
	this.browser;
	this.env = "as";

	this.checkAppiumIsRunning = function() {
		console.log("Checking Appium is running");
		var appium_url = "http://127.0.0.1:"+this.port+"/wd/hub/status";
		var request = require("request");
		request(appium_url, function (error, response, body) {
			if(error || response.statusCode != 200) {
				instance.error("Appium is not running");
				return;
			}
			console.log("Appium is running, move on!");
			instance.buildApp();
		});
	};

	this.buildApp = function() {
		var sys = require('sys')
		var exec = require('child_process').exec;
		var child;
		console.log("Building android app: ./gradlew app:assembleDebug");
		child = exec("./gradlew app:assembleRelease", function (error, stdout, stderr) {
			if (error !== null) { 
				instance.error('Building error ', error);
				return;
			}
			instance.sendTest();
		});
	};
	
	this.buildEclipseApp = function() {
		
	}

	this.error = function(msg, err) {
		console.error('TEST FAILED! ' + (msg?msg:''), (msg?err:msg));
		process.exit(1);
  }

	this.quit = function() {
		console.log("TEST OK!");
		process.exit(1);
	}

	this.sendTest = function() {
		console.log("Sending test");
		var wd = require('wd'), 
		assert = require('assert'),
		basePath = process.cwd();
	
		// test instance
		var app = wd.remote('localhost', this.port);
		app.init({
			deviceName: 'Android',
			platformName: 'Android',
			platformVersion: '4.4',
			local: true,
			"app": basePath + '/build/app-release.apk',
			"appPackage": "com.urucas.ptremotecontrol", 
			"appActivity": "com.urucas.popcorntimerc.activities.SplashActivity",
			"appWaitActivity": "com.urucas.popcorntimerc.activities.SplashActivity",
			automationName:'Selendroid'
		}, function(err, session, caps) {
			
			// test implementation
			try {
				if (err) instance.error(err);
				else app.setImplicitWaitTimeout(10000, function(err) {
					if (err) instance.error('error setting timeout', err);
						app.elementById('showMoviestt', function(err, el) {
							if(err) instance.error("error finding showMovies", err);
							else{
								el.click(function(err){
									if(err) instance.error("error clicking showMovies", err);
									else { 
										app.quit(); 
										// instance.quit(); 
									};
								});
							}
						})
				});
			}catch(e) {
				instance.error("exception ", e);
			}
		});
	};

	this.run = function(port) {
		if(port != undefined) this.port = port;
		this.checkAppiumIsRunning();
	}
});

if(require.main === module) {
	test.run(4723);
}
