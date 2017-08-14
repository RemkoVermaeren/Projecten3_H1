(function() {
    'use strict';

    angular.module('hoGentApp').factory('blogService', blogService);

    blogService.$inject = ['$log', '$http', 'auth'];

    function blogService($log, $http, auth) {

        var service = {
            getAll: getAll,
            create: create,
            get: get,
            update: update,
            deleteBlog: deleteBlog
        };
        return service;

        function getAll() {
            return $http.get('/api/blogs').success(function(data) {
                return data;

            });
        }
        function create(blog) {
            return $http.post('/api/blogs', blog, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function(data) {
                return data;
            });
        }
        function get(id) {
            return $http.get('/api/blogs/' + id).then(function(res) {
                return res.data;
            });
        }
        function update(id, blog) {
            return $http.put('/api/blogs/' + id, blog, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function(data) {
                return data;
            });

        }
        function deleteBlog(blog) {
            return $http.delete('/api/blogs/' + blog._id, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).then(function(res) {
                return res.data;
            })
        }
    }
})();
