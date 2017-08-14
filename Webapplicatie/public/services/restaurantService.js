(function () {
    'use strict';

    angular.module('hoGentApp').factory('restaurantService', restaurantService);

    restaurantService.$inject = ['$log', '$http', 'auth'];

    function restaurantService($log, $http, auth) {


        var service = {
            getAll: getAll,
            create: create,
            get: get,
            update: update,
            deleteRestaurant: deleteRestaurant,
            uploadImage: uploadImage
        };
        return service;

        ////////////

        function getAll() {
            return $http.get('/api/restaurants').success(function (data) {
                return data;
            });
        }

        function create(restaurant) {
            return $http.post('/api/restaurants', restaurant, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function (data) {
                return data;
            });
        }

        function get(id) {
            return $http.get('/api/restaurants/' + id).then(function (res) {
                return res.data;
            });
        }

        function update(id, restaurant) {
            $log.log("update in restaurantService was called");
            return $http.put('/api/restaurants/' + id, restaurant, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function (data) {
                return data;
            });

        }

        function deleteRestaurant(restaurant) {
            return $http.delete('/api/restaurants/' + restaurant._id, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).then(function (res) {
                return res.data;
            })
        }

        function uploadImage(image) {
            $log.log(image);
            return $http.post('/api/upload/image', image).success(function (data) {
                return data;
            });
        }
    }
})();
