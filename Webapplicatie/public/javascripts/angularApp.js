(function () {

    'use strict';

    angular.module('hoGentApp').config(hogentAppState);
    hogentAppState.$inject = ['$stateProvider', '$urlRouterProvider'];

    function hogentAppState($stateProvider, $urlRouterProvider) {
        $stateProvider.state('home', {
            url: '/home',
            templateUrl: '/templates/home.html',
            controller: 'MainController',
            controllerAs: 'ctrl',
            onEnter: ['$state', 'auth', function ($state, auth) {
                if (!auth.isLoggedIn()) {
                    $state.go('login');
                }
            }]
            })
            .state('recipes', {
                url: '/recipes',
                templateUrl: '/templates/recipes.html',
                controller: 'RecipeController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('recipe', {
                url: '/recipes/{id}',
                templateUrl: '/templates/recipe.html',
                controller: 'RecipeController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('newrecipe', {
                url: '/newrecipe',
                templateUrl: '/templates/newrecipe.html',
                controller: 'RecipeController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('modifyrecipe', {
                url: '/recipes/{id}/edit',
                templateUrl: '/templates/modifyrecipe.html',
                controller: 'RecipeController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('blogs', {
                url: '/blogs',
                templateUrl: '/templates/blogs.html',
                controller: 'BlogController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('blog', {
                url: '/blogs/{id}',
                templateUrl: '/templates/blog.html',
                controller: 'BlogController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('newblog', {
                url: '/newblog',
                templateUrl: '/templates/newblog.html',
                controller: 'BlogController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('modifyblog', {
                url: '/blogs/{id}/edit',
                templateUrl: '/templates/modifyblog.html',
                controller: 'BlogController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('login', {
                url: '/login',
                templateUrl: '/templates/login.html',
                controller: 'AuthController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (auth.isLoggedIn()) {
                        $state.go('home');
                    }
                }]
            })
            .state('register', {
                url: '/register',
                templateUrl: '/templates/register.html',
                controller: 'AuthController',
                controllerAs: 'ctrl'
            })
            .state('restaurants', {
                url: '/restaurants',
                templateUrl: '/templates/restaurants.html',
                controller: 'RestaurantController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('restaurant', {
                url: '/restaurants/{id}',
                templateUrl: '/templates/restaurant.html',
                controller: 'RestaurantController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('newrestaurant', {
                url: '/newrestaurant',
                templateUrl: '/templates/newrestaurant.html',
                controller: 'RestaurantController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('modifyrestaurant', {
                url: '/restaurants/{id}/edit',
                templateUrl: '/templates/modifyrestaurant.html',
                controller: 'RestaurantController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('changepassword', {
                url: '/changepassword',
                templateUrl: '/templates/changepassword.html',
                controller: 'AuthController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            })
            .state('users', {
                url: '/users',
                templateUrl: '/templates/users.html',
                controller: 'AuthController',
                controllerAs: 'ctrl',
                onEnter: ['$state', 'auth', function ($state, auth) {
                    if (!auth.isLoggedIn()) {
                        $state.go('login');
                    }
                }]
            });
        $urlRouterProvider.otherwise('home');
    }
})();
