(function () {

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
        vm.addAllergie = addAllergie;
        vm.removeAllergie = removeAllergie;
        vm.addFood = addFood;
        vm.removeFood = removeFood;
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

        function addAllergie() {
            vm.recipe.allergies.push("");
        }

        function removeAllergie(index) {
            vm.recipe.allergies.splice(index, 1);
        }

        function addFood() {
            vm.recipe.food.push("");
        }

        function removeFood(index) {
            vm.recipe.food.splice(index, 1);
        }

        function addInstruction() {
            vm.recipe.instructions.push("");
        }

        function removeInstruction(index) {
            vm.recipe.instructions.splice(index, 1);
        }

        function getRecipes() {
            return recipeService.getAll()
                .then(function (data) {
                    vm.recipes = data.data;
                    return vm.recipes;
                });
        }

        function getRecipe() {
            return recipeService.get($stateParams.id).then(function (data) {
                vm.recipe = data;
            });
        }

        function addRecipe() {
            if (!validRecipe()) {
                return;
            }
            if (vm.image) {
                return recipeService.uploadImage(vm.image).success(function (dataImg) {
                    vm.recipe.picture = dataImg;
                    recipeService.create(vm.recipe).success(function (data) {
                        vm.recipes.push(data.data);
                    }).then($state.go("recipes"));
                });
            }
            return recipeService.create(vm.recipe).success(function (data) {
                vm.recipes.push(data.data);
            }).then($state.go("recipes"));

        }

        function modifyRecipe() {
            if (!validRecipe()) {
                return;
            }
            if (vm.image) {
               return recipeService.uploadImage(vm.image).success(function (dataImg) {
                    vm.recipe.picture = dataImg;
                    recipeService.update($stateParams.id, vm.recipe).then($state.go("recipes"));
                });
            } else {
                return recipeService.update($stateParams.id, vm.recipe).then($state.go("recipes"));
            }
        }

        function deleteRecipe(recipe) {
            return recipeService.deleteRecipe(recipe).then(function () {
                getRecipes();
            });
        }

        function validRecipe() {
            return (vm.recipe.name && vm.recipe.name !== '' &&
                vm.recipe.veganPoints && vm.recipe.veganPoints !== '' &&
                vm.recipe.calories && vm.recipe.calories !== '' &&
                vm.recipe.difficulty && vm.recipe.difficulty !== '' &&
                vm.recipe.time && vm.recipe.time !== '' &&
                vm.recipe.type && vm.recipe.type !== '' &&
                vm.recipe.food &&
                vm.recipe.instructions &&
                vm.recipe.allergies)
            }
    }
})();
