(function(){
    window.liveAdapter = {
        handlePhraseSelection: function(phrase) {
            handlePhraseSelectionP(phrase)
        },
        log: function(message) {
            logP(message)
        },
        closeLivePanel: function() {
            closeLivePanelP()
        }
    };
})();