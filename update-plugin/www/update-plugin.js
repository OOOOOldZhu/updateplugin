var exec = require('cordova/exec');

exports.check = function (arg0, success, error) {
    console.log('update-plugin.js中 exports.check() . .')
    exec(success, error, 'Updater', 'check', [arg0]);
};
