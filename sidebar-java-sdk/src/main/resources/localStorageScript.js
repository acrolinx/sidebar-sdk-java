/* Copyright (c) 2017 Acrolinx GmbH */

(function() {
  window.acrolinxStorage = {
    getItem: function(key) {
      return getItemP(key);
    },
    removeItem: function(key) {
      removeItemP(key);
    },
    setItem:
    function(key, data) {
      setItemP(key, data);
    }
  }
})();