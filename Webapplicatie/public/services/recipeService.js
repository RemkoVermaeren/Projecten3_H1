(function() {
    'use strict';

    angular.module('hoGentApp').factory('recipeService', recipeService);

    recipeService.$inject = ['$log', '$http', 'auth'];

    function recipeService($log, $http, auth) {

        var service = {
            getAll: getAll,
            create: create,
            get: get,
            update: update,
            deleteRecipe: deleteRecipe,
            uploadImage: uploadImage
        };
        return service;

        function getAll() {
            return $http.get('/api/recipes').success(function(data) {
                return data;

            });
        }
        function create(recipe) {
            return $http.post('/api/recipes', recipe, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function(data) {
                return data;
            });
        }
        function get(id) {
            return $http.get('/api/recipes/' + id).then(function(res) {
                return res.data;
            });
        }
        function update(id, recipe) {
            return $http.put('/api/recipes/' + id, recipe, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function(data) {
                return data;
            });

        }
        function deleteRecipe(recipe) {
            return $http.delete('/api/recipes/' + recipe._id, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).then(function(res) {
                return res.data;
            })
        }
        function uploadImage(image) {
            return $http.post('api/upload/image', image).success(function (data) {
                return data;
            });
        }
    }
})();
