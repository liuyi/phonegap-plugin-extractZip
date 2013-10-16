// JavaScript Document
function ExtractZip(){
 
	
}

ExtractZip.extract=function(file,to,win,fail){
	 
	cordova.exec(onSuccess, onFailed, "ExtractZip", "extract", [file,to]);
	   
	function onSuccess(msg){
	 
		 if(win!=null) win(file,to);
   }
   
   function onFailed(msg){
	 
		  if(fail!=null) fail(msg);
   }
 }
 
 module.exports=ExtractZip

