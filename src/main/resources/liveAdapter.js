(function(){
    window.liveAdapter = {
        handlePhraseSelection: function(phrase) {
            handlePhraseSelectionP(phrase)
        },
        logInfo: function(message) {
            logInfoP(message)
        },
        logDebug: function(message) {
            logDebugP(message)
        },
        closeLivePanel: function() {
            closeLivePanelP()
        },
        openSidebar: function() {
            openSidebarP()
        }
    };
})();