(function(){
    'use strict';

    angular.module('hoGentApp').controller('NavController', NavController);

    NavController.$inject = ['auth', '$state'];

    function NavController(auth, $state){
      var vm = this;

      vm.isLoggedIn = auth.isLoggedIn;
      vm.currentUser = auth.currentUser;

      vm.logOut = logOut;

      function logOut() {
        auth.logOut();
        $state.go('login');
        }

    }
})();
