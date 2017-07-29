var mongoose = require('mongoose');

var RecipeSchema = new mongoose.Schema({
    name: String,
    veganPoints: Number,
    calories: Number,
    food: [[String]]
});

mongoose.model('Recipe', RecipeSchema);