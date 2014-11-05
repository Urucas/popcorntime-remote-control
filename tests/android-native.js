'use strict';

var test = new (function(){
	
	var instance = this;
	
	this.port = 4723; 
	this.browser;
	this.argv = process.argv.slice(2);

	this.checkAppiumIsRunning = function() {
		var appiumRunning = require('appium-running');
		appiumRunning(this.port, function(success){
			if(!success){
				instance.error("Appium is not running");
				return;
			}
			console.log("Appium is running, move on!");
			if(instance.argv.indexOf("--no-build") !== -1) {
				instance.sendTest();
			}else{
				instance.buildApp();
			}
		})
	};

	this.buildApp = function() {
		var sys = require('sys')
		var exec = require('child_process').exec;
		var child;
		// script to build apk with android studio project
		console.log("Building android app: ./gradlew app:assembleDebug");
		child = exec("./gradlew app:assembleDebug", function (error, stdout, stderr) {
			if (error !== null) { 
				instance.error('Building error ', error);
				return;
			}
			instance.sendTest();
		});
	};
	
	this.error = function(msg, err) {
		console.error('TEST FAILED! ' + (msg?msg:''), (err?err:msg));
		if(this.browser) this.browser.quit();
  }

	this.quit = function() {
		console.log("TEST OK!");
		if(this.browser) this.browser.quit();
	}

	this.sendTest = function() {
		console.log("Sending test");
		var wd = require('wd'), 
		assert = require('assert'),
		basePath = process.cwd();
	
		// test instance
		var app = this.browser = wd.remote('localhost', this.port);
		app.init({
			deviceName: 'Android',
			platformName: 'Android',
			platformVersion: '4.4',
			local: true,
			"app": basePath + '/app/build/outputs/apk/app-debug-unaligned.apk',
			"appPackage": "com.urucas.ptremotecontrol", 
			"appActivity": "com.urucas.popcorntimerc.activities.SplashActivity",
			"appWaitActivity": "com.urucas.popcorntimerc.activities.SplashActivity",
			automationName:'Selendroid'
		}, function(err, session, caps) {
			
			// default values
			var conn = {
				host: "192.168.0.100",
				port: 8008,
				user: "popcorn",
				pass: "popcorn"
			};
			
			/*
			var req = require("request");
			req(conn.host+":"+conn.port, function (error, response, body) {
				
			});
			app.quit();
			*/
			// test implementation
			app.setImplicitWaitTimeout(10000, function(err) {
				if(err) instance.error("Error setting implicit timeout", err);
				else app.elementById('menuBtt', function(err, el) {
				if(err) instance.error("Error finding menuBtt element", err);
				else el.click(function(err){
				if(err) instance.error("Error clicking menuBtt");
				else app.elementById('ptHost', function(err, el){
				if(err) instance.error("Error finding ptHost elemen", err);
				else el.clear(function(err){
				if(err) instance.error("Error clearing ptHost value", err);
				else el.type(conn.host, function(err){
				if(err) instance.error("Error typing ptHost value", err);
				else app.elementById('ptPort', function(err, el){
				if(err) instance.error("Error finding ptPort element", err);
				else el.clear(function(err){
				if(err) instance.error("Error clearing ptPort value", err);
				else el.type(conn.port, function(err){
				if(err) instance.error("Error typing ptPort value", err);
				else app.elementById("ptUser", function(err, el){
				if(err) instance.error("Error finding ptUser element", err);
				else el.clear(function(err){
				if(err) instance.error("Error clearing ptUser value", err);
				else el.type(conn.user, function(err){
				if(err) instance.error("Error typing ptUser value", err);
				else app.elementById("ptPass", function(err, el){
				if(err) instance.error("Error finding ptPass element", err);
				else el.clear(function(err){
				if(err) instance.error("Error clearing ptPass value", err);
				else el.type(conn.pass, function(err){
				if(err) instance.error("Error typing ptPass value", err);
				else app.elementById("connectBtt", function(err, el){
				if(err) instance.error("Error finding connectBtt element", err);
				else el.click(function(err){
				if(err) instance.error("Error clicking connectBtt", err);
				else instance.quit();
		
			});});});});});});});});});});});});});});});})});
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
