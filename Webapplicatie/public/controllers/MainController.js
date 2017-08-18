(function() {

    'use strict';

    angular.module('hoGentApp').controller('MainController', MainController);

    MainController.$inject = ['$log', 'restaurantService','blogService','recipeService', 'auth', '$state', '$stateParams'];

    function MainController($log, restaurantService,blogService,recipeService, auth, $state, $stateParams) {
        var vm = this;

        //vm.isLoggedIn = authService.isLoggedIn;
        vm.restaurants = [];
        vm.blogs = [];
        vm.recipes = [];
        vm.users = [];
        vm.getRestaurants = getRestaurants;
        vm.getUsers = getUsers;
        vm.getBlogs = getBlogs;
        vm.getRecipes = getRecipes;

        activate();


        function activate() {
            getRestaurants();
            getRecipes();
            getUsers();
            getBlogs();
        }
        function getRestaurants() {
            return restaurantService.getAll()
                .then(function(data) {
                    vm.restaurants = data.data;
                    return vm.restaurants;
                });
        }

        function getBlogs() {
            return blogService.getAll()
                .then(function(data) {
                    vm.blogs = data.data;
                    return vm.blogs;
                });
        }
        function getRecipes() {
            return recipeService.getAll()
                .then(function(data) {
                    vm.recipes = data.data;
                    return vm.recipes;
                });
        }

        function getUsers() {
            return auth.getAll()
                .then(function(data) {
                    vm.users = data.data;
                    return vm.users;
                });
        }
    }

})();
