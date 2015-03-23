var formsActive=false;
var formsClosed=false;
var backToModulePage=false;
function blurFormsApplet() {
  document.getElementById("forms_applet").style.visibility="hidden";
}
function unblurFormsApplet() {
  if ((formsActive)&&(!(formsClosed))) {
    document.getElementById("forms_applet").style.visibility="visible";
  }
}
function callModule(menuOption, Module) {
  document.getElementById("homePage").style.visibility="hidden";
  blurFormsApplet();
  formsActive=false;
  document.getElementById("formsPage").style.visibility="hidden";
  document.getElementById("modulePage").style.visibility="visible";
  document.getElementById("iframe").src=Module;
  backToModulePage=true;
}
function callForm(menuOption,Interface,Raadplegen,Parm1,Parm2,Parm3) {
	var iframe = document.getElementById("iframeId|window2|HelloWorld");
	if (!iframe) {
		return;
	}
	var innerDoc = iframe.contentWindow.document;
	if (!innerDoc) {
		return;
	}
	var formsApplet = innerDoc.getElementById("forms_applet");
	if (!formsApplet) {
		return;
	}
	formsApplet.focus();
	formsApplet.raiseEvent("CALL_FORM", Interface+";"+Raadplegen+";"+Parm1+";"+Parm2+";"+Parm3);
};
function showHomePage() {
  clearMessage();
  blurFormsApplet();
  document.getElementById("formsPage").style.visibility="hidden";
  formsActive=false;
  backToModulePage=false;
  document.getElementById("homePage").style.visibility="visible";
  document.getElementById("modulePage").style.visibility="hidden";
}
function formsEvent(eventname) {
  clearMessage();
  if (eventname=="READY") {
  } else if (eventname=="ERROR") {
     alert("De forms applet is niet correct gestart");
  } else if (eventname=="CLOSE") {
     formsClosed=true;
     blurFormsApplet();
     if (backToModulePage) {
       document.getElementById("homePage").style.visibility="hidden";
       document.getElementById("formsPage").style.visibility="hidden";
       document.getElementById("modulePage").style.visibility="visible";
       formsActive=false;
     }
  }
}
function formKey(doKey) {
  clearMessage();
  document.getElementById('forms_applet').raiseEvent("DO_KEY",doKey);
}
function formMenu(menuOption) {
  clearMessage();
  document.getElementById('forms_applet').raiseEvent("MENU",menuOption);
}
function formTrigger(triggerName) {
  clearMessage();
  document.getElementById('forms_applet').raiseEvent("TRIGGER",triggerName);
}
function showMessage(msg) {
  document.getElementById("message").innerHTML=msg;
}
function clearMessage() {
  if (document.getElementById("message"))
    document.getElementById("message").innerHTML="&nbsp";
}