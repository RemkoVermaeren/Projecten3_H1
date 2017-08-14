var mongoose = require('mongoose');

var BlogSchema = new mongoose.Schema({
    name: String,
    veganPoints: Number,
    author: String,
    website: String,
    date: Date,
    picture: String
});

mongoose.model('Blog', BlogSchema);