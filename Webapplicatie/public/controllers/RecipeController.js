(function() {

    'use strict';

    angular.module('hoGentApp').controller('RecipeController', RecipeController);

    RecipeController.$inject = ['$log', 'recipeService', 'auth', '$state', '$stateParams'];

    function RecipeController($log, recipeService, auth, $state, $stateParams) {
        var vm = this;

        vm.recipes = [];
        vm.recipe = {};
        vm.recipe.allergies = [];
        vm.recipe.food = [];
        vm.recipe.instructions = [];
        vm.getRecipes = getRecipes;
        vm.getRecipe = getRecipe;
        vm.addRecipe = addRecipe;
        vm.modifyRecipe = modifyRecipe;
        vm.deleteRecipe = deleteRecipe;
        activate();


        function activate() {
            return getRecipes();
        }
        function getRecipes(){
            return recipeService.getAll()
                .then(function(data) {
                    vm.recipes = data.data;
                    return vm.recipes;
                });
        }

        function getRecipe(){
            return recipeService.get($stateParams.id).then(function(data){
                vm.recipe = data;
            });
        }

        function addRecipe() {
            //TODO Controle op properties
          /*  if (!vm.faq.question || vm.faq.question === '' || !vm.faq.answer || vm.faq.answer === '') {
                return;
            }*/
            return recipeService.create(vm.recipe).then(function(data) {
                vm.recipes.push(data.data);
            }).then($state.go("recipes"));
        }
        function modifyRecipe() {
            //TODO Controle op properties

            /*if (!vm.faq.question || vm.faq.question === '' || !vm.faq.answer || vm.faq.answer === '') {
                return;
            }*/
            return recipeService.update($stateParams.id, vm.recipe).then($state.go("recipes"));
        }
        function deleteRecipe(recipe) {
            return recipeService.deleteRecipe(recipe).then(function(){
              getRecipes();
            });
        }
    }
})();
