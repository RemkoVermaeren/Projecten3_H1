(function() {

    'use strict';

    angular.module('hoGentApp').controller('FaqController', FaqController);

    FaqController.$inject = ['$log', 'faqService', 'auth', '$state', '$stateParams'];

    function FaqController($log, faqService, auth, $state, $stateParams) {
        var vm = this;

        vm.faqs = [];
        vm.faq = {};
        vm.getFaqs = getFaqs;
        vm.getFaq = getFaq;
        vm.addFaq = addFaq;
        vm.modifyFaq = modifyFaq;
        vm.deleteFaq = deleteFaq;
        vm.isFaqsEmpty = isFaqsEmpty;
        activate();


        function activate() {
            return getFaqs();
        }
        function getFaqs(){
            return faqService.getAll()
                .then(function(data) {
                    vm.faqs = data.data;
                    return vm.faqs;
                });
        }

        function getFaq(){
            return faqService.get($stateParams.id).then(function(data){
                vm.faq = data;
            });
        }

        function addFaq() {
            if (!vm.faq.question || vm.faq.question === '' || !vm.faq.answer || vm.faq.answer === '') {
                return;
            }
            return faqService.create(vm.faq).then(function(data) {
                vm.faqs.push(data.data);
            }).then($state.go("faqs"));
        }
        function modifyFaq() {
            if (!vm.faq.question || vm.faq.question === '' || !vm.faq.answer || vm.faq.answer === '') {
                return;
            }
            return faqService.update($stateParams.id, vm.faq).then($state.go("faqs"));
        }
        function deleteFaq(faq) {
            return faqService.deleteFaq(faq).then(function(){
              getFaqs();
            });

        }
        function isFaqsEmpty() {
            return vm.faqs.length == 0;
        }
    }



})();
