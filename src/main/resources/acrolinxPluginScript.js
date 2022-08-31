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
      var args = [].slice.apply(arguments);
      var msg = '';
      args.forEach(function(arg){
        msg += JSON.stringify(arg)
      })
      if(window.java) {
        java.error(msg);
      }
      oldError.apply(this, arguments);
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
               runCheckGlobalP("withCheckSelection")
            } else {
               runCheckGlobalP("withoutCheckSelection")
            }
        }
    }
  },
  requestCheckForDocumentInBatch: function(documentIdentifier){
     requestCheckForDocumentInBatchP(documentIdentifier)
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
  },
  parseAllClusters: function(clusters) {
    reuseTestFunction(clusters);
  },
  reusePrefixSearch: function(prefix) {
    reusePrefixSearchP(prefix);
  },
  onReusePrefixSearchFinished: function(result) {
    onReusePrefixSearchFinishedP(result);
  }
}
})();