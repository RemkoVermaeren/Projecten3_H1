(function () {

    'use strict';

    angular.module('hoGentApp').controller('RestaurantController', RestaurantController);

    RestaurantController.$inject = ['$log', 'restaurantService', 'auth', '$state', '$stateParams'];

    function RestaurantController($log, restaurantService, auth, $state, $stateParams) {
        var vm = this;

        vm.restaurants = [];
        vm.restaurant = {};
        vm.getRestaurants = getRestaurants;
        vm.getRestaurant = getRestaurant;
        vm.addRestaurant = addRestaurant;
        vm.modifyRestaurant = modifyRestaurant;
        vm.deleteRestaurant = deleteRestaurant;
        vm.isRestaurantsEmpty = isRestaurantsEmpty;
        activate();

        function activate() {
            return getRestaurants();
        }

        function getRestaurants() {
            return restaurantService.getAll()
                .then(function (data) {
                    vm.restaurants = data.data;
                    return vm.restaurants;
                });
        }

        function getRestaurant() {
            return restaurantService.get($stateParams.id).then(function (data) {
                vm.restaurant = data;
            });
        }

        function addRestaurant() {
             if (!validRestaurant()) {
                 return;
             }
             if(vm.image){
                 return restaurantService.uploadImage(vm.image).success(function (dataImg) {
                     vm.restaurant.picture = dataImg;
                     restaurantService.create(vm.restaurant).success(function (data) {
                         vm.restaurants.push(data.data);
                     })
                 }).then($state.go("restaurants"));
             }else{
                 return  restaurantService.create(vm.restaurant).success(function (data) {
                     vm.restaurants.push(data.data);
                 }).then($state.go("restaurants"));
             }
        }

        function modifyRestaurant() {
            if (!validRestaurant()) {
                $log.log("NOT VALID");

                $log.log(vm.restaurant);
                return;
            }
            if(vm.image){
                $log.log("if");
                $log.log(vm.restaurant);

                return restaurantService.uploadImage(vm.image).success(function(dataImg){
                    vm.restaurant.picture = dataImg;
                    restaurantService.update($stateParams.id, vm.restaurant).then($state.go("restaurants"));
                });
            }else {
                $log.log("else");
                $log.log(vm.restaurant);
                return restaurantService.update($stateParams.id, vm.restaurant).then($state.go("restaurants"));
            }
        }

        function deleteRestaurant(restaurant) {
            return restaurantService.deleteRestaurant(restaurant).then(function () {
                getRestaurants();
            });
        }


        function isRestaurantsEmpty() {
            return vm.restaurants.length === 0;
        }

        function validRestaurant() {
            return (vm.restaurant.name && vm.restaurant.name !== '' &&
                vm.restaurant.veganPoints && vm.restaurant.veganPoints !== '' &&
                vm.restaurant.address && vm.restaurant.address !== '' &&
                vm.restaurant.rating && vm.restaurant.rating !== '' &&
                vm.restaurant.telephoneNumber && vm.restaurant.telephoneNumber !== ''&&
                vm.restaurant.website && vm.restaurant.website !== '' &&
                vm.restaurant.extraInformation && vm.restaurant.extraInformation !== ''
            );
        }
    }
})();
