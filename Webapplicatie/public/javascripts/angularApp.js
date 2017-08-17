(function() {

    'use strict';

    angular.module('hoGentApp').config(hogentAppState);
    hogentAppState.$inject = ['$stateProvider', '$urlRouterProvider'];
    function hogentAppState($stateProvider, $urlRouterProvider) {
        $stateProvider.state('home', {
            url: '/home',
            templateUrl: '/home.html',
            controller: 'MainController',
            controllerAs: 'ctrl',
            resolve: {
                restaurantPromise: ['restaurantService', function(restaurantService) {
                    return restaurantService.getAll();
                }]
            },
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('restaurants', {
            url: '/restaurants',
            templateUrl: '/restaurants.html',
            controller: 'RestaurantController',
            controllerAs: 'ctrl'
        }).state('recipes', {
                url: '/recipes',
                templateUrl: '/recipes.html',
                controller: 'RecipeController',
                controllerAs : 'ctrl'
            }
        ).state('newrecipe', {
            url: '/newrecipe',
            templateUrl: '/newrecipe.html',
            controller: 'RecipeController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('modifyrecipe', {
            url: '/recipes/{id}/edit',
            templateUrl: '/modifyrecipe.html',
            controller: 'RecipeController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('blogs', {
                url: '/blogs',
                templateUrl: '/blogs.html',
                controller: 'BlogController',
                controllerAs : 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
            }
        ).state('newblog', {
            url: '/newblog',
            templateUrl: '/newblog.html',
            controller: 'BlogController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('modifyblog', {
            url: '/blogs/{id}/edit',
            templateUrl: '/modifyblog.html',
            controller: 'BlogController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('login', {
            url: '/login',
            templateUrl: '/login.html',
            controller: 'AuthController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (auth.isLoggedIn()) {
                    $state.go('home');
                }
            }]
        }).state('register', {
            url: '/register',
            templateUrl: '/register.html',
            controller: 'AuthController',
            controllerAs: 'ctrl'
        }).state('newrestaurant', {
            url: '/newrestaurant',
            templateUrl: '/newrestaurant.html',
            controller: 'RestaurantController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('newmenu', {
            url: '/{id}/newmenu',
            templateUrl: '/newmenu.html',
            controller: 'MenuController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('modifyrestaurant', {
            url: '/restaurants/{id}/edit',
            templateUrl: '/modifyrestaurant.html',
            controller: 'RestaurantController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('modifymenu', {
            url: '/menus/{id}',
            templateUrl: '/modifymenu.html',
            controller: 'MenuController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('changepassword', {
            url: '/changepassword',
            templateUrl: '/changepassword.html',
            controller: 'AuthController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('users', {
            url: '/users',
            templateUrl: '/users.html',
            controller: 'AuthController',
            controllerAs: 'ctrl',
            resolve: {
                users: ['auth', function(auth) {
                    return auth.getAll();
                }]
              },
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
          }).state('restaurant', {
            url: '/restaurants/{id}',
            templateUrl: '/restaurant.html',
            controller: 'RestaurantController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('recipe', {
            url: '/recipes/{id}',
            templateUrl: '/recipe.html',
            controller: 'RecipeController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        }).state('blog', {
            url: '/blogs/{id}',
            templateUrl: '/blog.html',
            controller: 'BlogController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
        });
        $urlRouterProvider.otherwise('home');
    }
})();
