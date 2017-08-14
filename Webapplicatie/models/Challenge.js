var mongoose = require('mongoose');

var ChallengeSchema = new mongoose.Schema({
    name: String,
    description: String,
    picture: String,
    date: { type: Date, default: Date.now},
    amountOfLikes: Number,
    veganScore: Number,
    isCompleted: Boolean
});

mongoose.model('Challenge', ChallengeSchema);


// Todo : Methode voor likes + 1 en likes - 1
