(function() {

    'use strict';

    angular.module('hoGentApp').factory('auth', authService);

    authService.$inject = ['$http', '$window', '$log'];

    function authService($http, $window, $log) {
        var auth = {};
        var service = {
            saveToken: saveToken,
            getToken: getToken,
            getUser: getUser,
            isLoggedIn: isLoggedIn,
            currentUser: currentUser,
            register: register,
            logIn: logIn,
            logOut: logOut,
            changePassword: changePassword,
            getAll: getAll,
            getAllUsers: getAllUsers,
            deleteUser: deleteUser
        };
        return service;

        function getAll() {
            return $http.get('/api/users').success(function(data) {
                return data;

            });
        }
        function getUser(id) {
            return $http.get('/api/users/{id}').success(function(data) {
                return data;

            });
        }
        function getAllUsers(){
            return $http.get('/api/users/nonadmins').success(function(data) {
                return data;
            });
        }

        function saveToken(token) {
            $window.localStorage['hogent-app-token'] = token;
        }
        function getToken() {
            return $window.localStorage['hogent-app-token'];
        }
        function isLoggedIn() {
            var token = getToken();

            if (token) {
                var payload = angular.fromJson($window.atob(token.split('.')[1]));
                return payload.exp > Date.now() / 1000;
            } else {
                return false;
            }
        }
        function currentUser() {
            if (isLoggedIn()) {
                var token = getToken();
                var payload = angular.fromJson($window.atob(token.split('.')[1]));
                return payload.username;
            }
        }
        function register(user) {
            return $http.post('/api/users/register', user, {
                headers: {
                    Authorization: 'Bearer ' + getToken()
                }
            });
        }
        function logIn(user) {
            return $http.post('api/users/login', user, {
                headers: {
                    Authorization: 'Bearer ' + getToken()
                }
            }).success(function(data) {
                saveToken(data.token);
            }).error(function(err){
              return err;
            });
        }
        function logOut(){
            $window.localStorage.removeItem('hogent-app-token');
        }
        function changePassword(user){
          return $http.put("/changepassword",user, {
              headers: {
                  Authorization: 'Bearer ' + getToken()
              }
          });
        }
        function deleteUser(user){
            return $http.delete('/api/users/' + user._id, {
                headers: {
                    Authorization: 'Bearer ' + getToken()
                }
            }).then(function(res) {
                return res.data;
            })
        }

    }
})();
