var mongoose = require('mongoose');

var RecipeSchema = new mongoose.Schema({
    name: String,
    veganPoints: Number,
    calories: Number,
    food: [[String]],
    instructions: [String]
});

mongoose.model('Recipe', RecipeSchema);