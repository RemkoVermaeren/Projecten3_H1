(function() {
    'use strict';

    angular.module('hoGentApp').factory('faqService', faqService);

    faqService.$inject = ['$log', '$http', 'auth'];

    function faqService($log, $http, auth) {

        var service = {
            getAll: getAll,
            create: create,
            get: get,
            update: update,
            deleteFaq: deleteFaq
        };
        return service;

        function getAll() {
            return $http.get('/api/faqs').success(function(data) {
                return data;

            });
        }
        function create(faq) {
            return $http.post('/api/faqs', faq, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function(data) {
                return data;
            });
        }
        function get(id) {
            return $http.get('/api/faqs/' + id).then(function(res) {
                return res.data;
            });
        }
        function update(id, faq) {
            return $http.put('/api/faqs/' + id, faq, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).success(function(data) {
                return data;
            });

        }
        function deleteFaq (faq) {
            return $http.delete('/api/faqs/' + faq._id, {
                headers: {
                    Authorization: 'Bearer ' + auth.getToken()
                }
            }).then(function(res) {
                return res.data;
            })
        }
    }
})();
