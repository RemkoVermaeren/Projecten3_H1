var mongoose = require('mongoose');

var ChallengeSchema = new mongoose.Schema({
    name: String,
    description: String,
    picture: String,
    date: { type: Date, default: Date.now},
    amountOfLikes: {type: Number, default: 0},
    veganScore: Number,
    isCompleted: Boolean,
    createdBy: {
    type: String,
        ref: 'User'
    },
    likedBy: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    }]
});

mongoose.model('Challenge', ChallengeSchema);
