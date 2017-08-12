(function() {

    'use strict';

    angular.module('hoGentApp').controller('RecipeController', RecipeController);

    RecipeController.$inject = ['$log', 'recipeService', 'auth', '$state', '$stateParams'];

    function RecipeController($log, recipeService, auth, $state, $stateParams) {
        var vm = this;

        vm.recipes = [];
        vm.recipe = {};
        vm.recipe.allergies = [];
        vm.addAllergie = addAllergie;
        vm.removeAllergie = removeAllergie;
        vm.recipe.food = [];
        vm.addFood = addFood;
        vm.removeFood = removeFood;
        vm.recipe.instructions = [];
        vm.addInstruction = addInstruction;
        vm.removeInstruction = removeInstruction;
        vm.getRecipes = getRecipes;
        vm.getRecipe = getRecipe;
        vm.addRecipe = addRecipe;
        vm.modifyRecipe = modifyRecipe;
        vm.deleteRecipe = deleteRecipe;
        activate();


        function activate() {
            return getRecipes();
        }

        function addAllergie(){
            //var nrOfAllergies = vm.recipe.allergies.length + 1;
            vm.recipe.allergies.push("");
        }
        function removeAllergie(index){
            //var nrOfAllergies = vm.recipe.allergies.count + 1;
            vm.recipe.allergies.splice(index,1);
        }
        function addFood(){
            //var nrOfAllergies = vm.recipe.allergies.length + 1;
            vm.recipe.food.push("");
        }
        function removeFood(index){
            //var nrOfAllergies = vm.recipe.allergies.count + 1;
            vm.recipe.food.splice(index,1);
        }
        function addInstruction(){
            //var nrOfAllergies = vm.recipe.allergies.length + 1;
            vm.recipe.instructions.push("");
        }
        function removeInstruction(index){
            //var nrOfAllergies = vm.recipe.allergies.count + 1;
            vm.recipe.instructions.splice(index,1);
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
            return recipeService.uploadImage(vm.image).success(function(dataImg) {
                vm.recipe.picture = dataImg;
                return recipeService.create(vm.recipe).success(function(data) {
                    vm.recipes.push(data.data);
                }).then($state.go("recipes"));
            });
        }
        function modifyRecipe() {
            //TODO Controle op properties

            /*if (!vm.faq.question || vm.faq.question === '' || !vm.faq.answer || vm.faq.answer === '') {
                return;
            }*/
            if(vm.image){
                recipeService.uploadImage(vm.image).success(function(dataImg){
                    vm.recipe.picture = dataImg;
                    $log.log("New img url : "+ dataImg);
                })
            }
            $log.log("Update volgend recipe");
            $log.log(vm.recipe);
            return recipeService.update($stateParams.id, vm.recipe).then($state.go("recipes"));

        }
        function deleteRecipe(recipe) {
            return recipeService.deleteRecipe(recipe).then(function(){
              getRecipes();
            });
        }

    }
})();
