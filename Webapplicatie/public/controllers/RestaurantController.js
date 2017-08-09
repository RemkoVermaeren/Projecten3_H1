(function () {

    'use strict';

    angular.module('hoGentApp').controller('RestaurantController', RestaurantController);

    RestaurantController.$inject = ['$log', 'restaurantService', 'auth', '$state', '$stateParams'];

    function RestaurantController($log, restaurantService, auth, $state, $stateParams) {
        var vm = this;

        //vm.isLoggedIn = authService.isLoggedIn;
        vm.restaurants = [];
        vm.restaurant;
        vm.getRestaurants = getRestaurants;
        vm.getRestaurant = getRestaurant;
        vm.addRestaurant = addRestaurant;
        vm.modifyRestaurant = modifyRestaurant;
        vm.deleteRestaurant = deleteRestaurant;
        vm.convertDate = convertDate;
        vm.isRestaurantsEmpty = isRestaurantsEmpty;

        activate();


        function activate() {
            return getRestaurants().then(function () {
                $log.log("Restaurants were retrieved");
            });
        }

        function getRestaurants() {
            return restaurantService.getAll()
                .then(function (data) {
                    $log.log("getRestaurants in RestaurantController was called");
                    vm.restaurants = data.data;
                    return vm.restaurants;
                });
        }

        function getRestaurant() {
            return restaurantService.get($stateParams.id).then(function (data) {
                vm.restaurant = data;
                convertDate(vm.restaurant);
                $log.log(vm.restaurant);
            });
        }


        function addRestaurant() {
             if (!vm.restaurant.name || vm.restaurant.name === '') {
                 return;
             }
            //TODO Controle op empty
            restaurantService.uploadImage(vm.image).success(function (dataImg) {
                vm.restaurant.picture = dataImg;
                console.log(vm.restaurant);
                return restaurantService.create(vm.restaurant).success(function (data) {
                    $log.log(data);
                    vm.restaurants.push(data.data);
                })
            }).then($state.go("home"));
        }

        function modifyRestaurant() {
            $log.log("modifyRestaurant in MainController was called");
            if (!vm.restaurant.name || vm.restaurant.name === '') {
                return;
            }
            if(vm.image != null){
                restaurantService.uploadImage(vm.image).success(function(dataImg){
                    vm.restaurant.picture = dataImg;
                })
            }
            return restaurantService.update($stateParams.id, vm.restaurant).then($state.go("home"));
        }

        function deleteRestaurant(restaurant) {
            return restaurantService.deleteRestaurant(restaurant).then(function () {
                getRestaurants();
            });
        }

        function convertDate(restaurant) {
            var openingtime = vm.restaurant.openingtime;
            var closingtime = vm.restaurant.closingtime;
            vm.restaurant.openingtime = new Date(openingtime);
            vm.restaurant.closingtime = new Date(closingtime);
        }

        function isRestaurantsEmpty() {
            return vm.restaurants.length == 0;
        }
    }



})();