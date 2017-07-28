(function() {
    'use strict';

    angular.module('hoGentApp').factory('menuService', menuService);

    menuService.$inject = ['$log', '$http', 'auth'];

    function menuService($log, $http, auth) {
        var restoid;
        var service = {
            getAll: getAll,
            get:get,
            create: create,
            update: update,
            deleteMenu: deleteMenu,
            getRestaurants: getRestaurants

        };
        return service;

        ////////////

        function getAll(id) {
            if (id !== restoid && angular.isDefined(id)) {
                restoid = id;
            }
            return $http.get('/api/restaurants/' + restoid + '/menus').success(function(data) {
                return data;


            });
        }
        function create(menu) {
            return $http.post('/api/restaurants/' + restoid + '/menus', menu, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function(data) {
                return data;
            });
        }
        function get(id) {
            return $http.get('/api/restaurants/' + restoid + '/menus/' + id).then(function(res) {
                return res.data;

            });
        }
        function update(id, menu) {
            $log.log("update in menuService was called");
            return $http.put('/api/restaurants/' + restoid + '/menus/' + id, menu, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function(data) {
                return data;
            });

        }
        function deleteMenu(menu) {
          $log.log(restoid);
            return $http.delete('/api/restaurants/' + restoid + '/menus/' + menu._id, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function(data) {
                return data;
            });
        }
        function getRestaurants() {
          return $http.get('/api/restaurants').success(function(data) {
              return data;

          });
        }
    }
})
();
