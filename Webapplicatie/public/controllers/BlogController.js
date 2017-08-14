(function () {

    'use strict';

    angular.module('hoGentApp').controller('BlogController', BlogController);

    BlogController.$inject = ['$log', 'blogService', 'auth', '$state', '$stateParams'];

    function BlogController($log, blogService, auth, $state, $stateParams) {
        var vm = this;

        vm.blogs = [];
        vm.blog = {};
        vm.getBlogs = getBlogs;
        vm.getBlog = getBlog;
        vm.addBlog = addBlog;
        vm.modifyBlog = modifyBlog;
        vm.deleteBlog = deleteBlog;
        vm.isBlogsEmpty = isBlogsEmpty;
        activate();


        function activate() {
            return getBlogs();
        }

        function getBlogs() {
            return blogService.getAll()
                .then(function (data) {
                    vm.blogs = data.data;
                    return vm.blogs;
                });
        }

        function getBlog() {
            return blogService.get($stateParams.id).then(function (data) {
                vm.blog = data;
            });
        }

        function addBlog() {
            if (!validBlog()) {
                return;
            }
            return blogService.create(vm.blog).then(function (data) {
                vm.blogs.push(data.data);
            }).then($state.go("blogs"));
        }

        function modifyBlog() {
            if (!validBlog()) {
                return;
            }
            return blogService.update($stateParams.id, vm.blog).then($state.go("blogs"));
        }

        function deleteBlog(blog) {
            return blogService.deleteBlog(blog).then(function () {
                getBlogs();
            });

        }

        function isBlogsEmpty() {
            return vm.blogs.length === 0;
        }

        function validBlog() {
            return (
                vm.blog.name && vm.blog.name !== '' &&
                vm.blog.veganPoints && vm.blog.veganPoints !== '' &&
                vm.blog.author && vm.blog.author !== '' &&
                vm.blog.website && vm.blog.website !== '' &&
                vm.blog.date && vm.blog.date !== ''
            )
        }
    }


})();
