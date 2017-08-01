var mongoose = require('mongoose');

var RecipeSchema = new mongoose.Schema({
    name: String,
    veganPoints: Number,
    calories: Number,
    food: [String],
    difficulty: String,
    time : Number,
    allergies : [String],
    picture: String,
    type: String,
    instructions: [String]
});

mongoose.model('Recipe', RecipeSchema);