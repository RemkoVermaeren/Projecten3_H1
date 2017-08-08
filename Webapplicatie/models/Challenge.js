var mongoose = require('mongoose');

var ChallengeSchema = new mongoose.Schema({
    name: String,
    description: String,
    picture: String,
    date: { type: Date, default: Date.now},
    amountOfLikes: Number,
    veganScore: Number
    // isCompleted: Boolean
});

mongoose.model('Challenge', ChallengeSchema);


// Todo : Property voor persoon die gechallenged word, methode voor likes + 1 en likes - 1