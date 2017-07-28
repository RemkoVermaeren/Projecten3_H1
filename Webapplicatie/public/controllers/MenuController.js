(function() {
    'use strict';

    angular.module('hoGentApp').controller('MenuController', MenuController);

    MenuController.$inject = ['$log', 'menuService', 'auth', '$stateParams', '$state'];

    function MenuController($log, menuService, auth, $stateParams, $state) {

        var vm = this;
        var restoid;
        vm.menus = [];
        vm.restaurants = [];
        vm.data = {
            model: null,
            availableOptions: [{
                value: "None",
                name: "None"
            }, {
                value: 'Gluten',
                name: 'Gluten'
            }, {
                value: "Schaaldieren",
                name: "Schaaldieren"
            }, {
                value: "Eieren",
                name: "Eieren"
            }, {
                value: "Vis",
                name: "Vis"
            }, {
                value: "Aardnoten",
                name: "Aardnoten"
            }, {
                value: "Soya",
                name: "Soya"
            }, {
                value: "Melk(inclusief lactose)",
                name: "Melk(inclusief lactose)"
            }, {
                value: "Noten",
                name: "Noten"
            }, {
                value: "Selderij en aanverwanten",
                name: "Selderij en aanverwanten"
            }, {
                value: "Mosterd en aanverwanten",
                name: "Mosterd en aanverwanten"
            }, {
                value: "Sesamzaad en aanverwanten",
                name: "Sesamzaad en aanverwanten"
            }, {
                value: "Zwaveldioxide/sulfiet",
                name: "Zwaveldioxide/sulfiet"
            }, {
                value: "Lupine en aanverwanten",
                name: "Lupine en aanverwanten"
            }, {
                value: "Weekdieren en aanverwanten",
                name: "Weekdieren en aanverwanten"
            }, ]
        };
        vm.dataRestaurants;
        vm.menu;
        vm.getMenus = getMenus;
        vm.getMenu = getMenu;
        vm.addMenu = addMenu;
        vm.modifyMenu = modifyMenu;
        vm.deleteMenu = deleteMenu;
        vm.restoId = $stateParams.id;
        vm.convertDate = convertDate;
        vm.getAllergys = getAllergys;
        vm.getRestaurants = getRestaurants;
        vm.isMenusEmpty = isMenusEmpty;
        vm.restaurant;



        activate();

        function activate() {
            return getMenus().then(function() {
                getRestaurants();
            });
        }

        function getMenus() {
            return menuService.getAll(restoid)
                .then(function(data) {
                    vm.menus = data.data;
                    return vm.menus;
                });
        }

        function getMenu() {
            return menuService.get($stateParams.id).then(function(data) {
                vm.menu = data;
                convertDate(vm.menu);
            });
        }

        function addMenu() {
            var restaurantId = vm.restoId;
            $log.log("Datarestos: " + vm.dataRestaurants.model);
            $log.log("Category: " + vm.category);
            $log.log("Allergy: " + vm.data.model);
            $log.log("addMenu in MenuController was called");
            return menuService.create({
                category: vm.category,
                food: vm.food,
                price: vm.price,
                restaurants: vm.dataRestaurants.model,
                allergy: vm.data.model,
                date: vm.date
            }).then(function(data) {
                $log.log(data);
                $state.go("restaurants", {
                    id: restaurantId
                })
            });
        }

        function modifyMenu() {
            if (!vm.menu.category || vm.menu.category === '' || !vm.menu.food || vm.menu.food === '' ||  !vm.menu.date) {
                return;
            }
            vm.menu.allergy = vm.data.model;
            vm.menu.restaurants = vm.dataRestaurants.model;
            return menuService.update($stateParams.id, vm.menu).then($state.go("home"));
        }

        function deleteMenu(menu) {
            $log.log(menu);
            return menuService.deleteMenu(menu).then(function() {
                getMenus();
            });
        }

        /*  function restoId() {
            var executed = false;
            return function() {
                if (!executed) {
                    restoid = $stateParams.id;
                    executed = true;
                }
            }
        };
*/
        function convertDate(menu) {
            var date = vm.menu.date;
            vm.menu.date = new Date(date);
        }

        function getAllergys() {
            return vm.allergy;
        }

        function getRestaurants() {
            return menuService.getRestaurants()
                .then(function(data) {
                    vm.restaurants = data.data;
                    vm.dataRestaurants = {
                        model: null,
                        availableOptions: vm.restaurants
                    };
                    data.data.some(function(value, index, _restaurants) {
                        $log.log("Resto R: " + value.name);
                        if (value._id === vm.restoId) {
                            vm.restaurant = value;
                            $log.log("Huidig restaurant: " + vm.restaurant.name);
                            return true;
                        }
                    });
                    //data.data.forEach(function(r){
                    //  $log.log("Resto R: " + r.name);
                    //  if(r._id === vm.restoId){
                    //    vm.restaurant = r;
                    //    $log.log("Huidig restaurant: " + vm.restaurant.name);
                    //    return vm.restaurant;
                    //  }
                    //});
                    //for(r in data.data){
                    //  $log.log("R= "+ r);
                    //  if(r._id === vm.restoId){
                    //    vm.restaurant = r;
                    //    $log.log("RESTO: "+vm.restaurant)
                    //    return vm.restaurant;
                    //  }
                    //}
                    return vm.restaurants;
                });
        }

        function isMenusEmpty() {
            return vm.menus.length == 0;
        }



    }


})();
