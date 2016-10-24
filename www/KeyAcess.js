

cordova.define("KeyAccessDemo.KeyAcess", function(require, exports, module) {
               var exec = require('cordova/exec');


               exports.storeMethod = function(arg0, success, error) {
                var val=arg0.value;
               var key=arg0.key;
               console.log("val"+arg0.value);
               exec(success, error, "KeyAcess", "storeMethod", [val,key]);
               };
               exports.getMethod = function(arg0, success, error) {
               var key=arg0.key;
               console.log("arg0.key"+arg0.key);
               exec(success, error, "KeyAcess", "getMethod", [key]);
               };
               exports.deleteMethod = function(arg0, success, error) {
               var key=arg0.key;
               console.log("arg0.key"+arg0.key);
               exec(success, error, "KeyAcess", "deleteMethod", [key]);
               };

               exports.geneKeyPair = function(arg0, success, error) {
               exec(success, error, "KeyAcess", "geneKeyPair", [arg0]);
               };

               exports.geneSigning = function(arg0, success, error) {
               var strToenc=arg0.strToEnc;
               var keyPub=arg0.keyToEnc;
               exec(success, error, "KeyAcess", "geneSigning", [strToenc,keyPub]);
               };


               });



cordova.define("KeyAccessDemo.myplugin.pankanis.KeyAcess",function(require,exports,module){

var exec = require('cordova/exec');
 exports.generateKey = function(arg0, success, error) {
                          exec(success, error, "KeyAcess", "generateKey", [arg0]);
                            };

 exports.generateSig = function(arg0, success, error) {
 var key=arg0.key;
 var text=arg0.value;
 exec(success, error, "KeyAcess", "generateSig", [key,text]);
 };

 exports.validateSig = function(arg0, success, error) {
var sig=arg0.key;
var val=arg0.val;
var unique=arg0.uniqueKey;
 exec(success, error, "KeyAcess", "validateSig", [sig,val,unique]);
                                                                                    };

});
