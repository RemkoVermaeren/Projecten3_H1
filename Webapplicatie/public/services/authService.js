(function() {

    'use strict';

    angular.module('hoGentApp').factory('auth', authService);

    authService.$inject = ['$http', '$window', '$log'];

    function authService($http, $window, $log) {
        var auth = {};
        var service = {
            saveToken: saveToken,
            getToken: getToken,
            isLoggedIn: isLoggedIn,
            currentUser: currentUser,
            register: register,
            logIn: logIn,
            logOut: logOut,
            changePassword: changePassword,
            getAll: getAll,
            deleteUser: deleteUser
        };
        return service;

        function getAll() {
            return $http.get('/api/users').success(function(data) {
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
          $log.log("Service: " + user.username);
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
               /* var payload = angular.fromJson($window.atob(data.token.split('.')[1]));
                $log.log(payload);
                if(!payload.isAdmin){
                    return "not an admin";
                }*/
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
