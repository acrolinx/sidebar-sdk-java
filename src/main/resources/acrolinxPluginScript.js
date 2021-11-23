/* Copyright (c) 2017-present Acrolinx GmbH */

(function(){

window.java = {
  log: function(args){
    overwriteJSLoggingInfoP(args);
  },
  error: function(args){
    overwriteJSLoggingErrorP(args);
  }
}

if(window.console && console.log){
    var old = console.log;
    var oldError = console.error;
    console.log = function(){
      old.apply(this, arguments);
      var args = [].slice.apply(arguments);
      var msg = '';
      args.forEach(function(arg){
        msg += JSON.stringify(arg)
      })
      if(window.java) {
        java.log(msg);
      }
    }
    console.error = function(){
      oldError.apply(this, arguments);
      var args = [].slice.apply(arguments);
      var msg = '';
      args.forEach(function(arg){
        msg += JSON.stringify(arg)
      })
      if(window.java) {
        java.error(msg);
      }
  }
}

window.acrolinxPlugin =
{
  requestInit: function(){
    acrolinxSidebar.init(JSON.parse(getInitParamsP()));
  },
  onInitFinished: function(finishResult){
   onInitFinishedNotificationP(JSON.stringify(finishResult));
  },

  requestGlobalCheck: function(options){
    if(options && options.batchCheck === true) {
        runBatchCheck();
    } else {
        if(!canCheck()) {
            acrolinxSidebar.onGlobalCheckRejected();
        } else {
            if (options && options.selection === true) {
                acrolinxSidebar.checkGlobal(getTextP(),
                        {inputFormat: getInputFormatP(), externalContent: JSON.parse(getExternalContentP()), requestDescription: {documentReference: getDocUrlP()}, selection: JSON.parse(getCurrentSelectionRangesP())});
            } else {
                acrolinxSidebar.checkGlobal(getTextP(),
                        {inputFormat: getInputFormatP(), externalContent: JSON.parse(getExternalContentP()), requestDescription: {documentReference: getDocUrlP()}});
            }
        }
    }
  },
  requestBackgroundCheckForDocument: function(documentIdentifier){
     var content = getContentForDocumentP(documentIdentifier);
     var checkOptions = JSON.parse(getCheckOptionsForDocumentP(documentIdentifier));
     acrolinxSidebar.checkDocumentInBackground(documentIdentifier, content, checkOptions);

  },
  openDocumentInEditor: function(documentIdentifier){
     openDocumentInEditorP(documentIdentifier);
  },
  onCheckResult: function(checkResult){
    onCheckResultP(JSON.stringify(checkResult));
  },
  selectRanges: function(checkId, matches){
    selectRangesP(checkId, JSON.stringify(matches));
  },
  replaceRanges: function(checkId, matches){
    replaceRangesP(checkId, JSON.stringify(matches));
  },
  configure: function(acrolinxPluginConfiguration){
    notifyAboutSidebarConfigurationP(JSON.stringify(acrolinxPluginConfiguration));
  },
  download: function(downloadInfo){
    downloadP(JSON.stringify(downloadInfo));
  },
  openWindow: function(openWindowParams){
    openWindowP(JSON.stringify(openWindowParams));
  },
  openLogFile: function(){
    openLogFileP();
  }
}
})();