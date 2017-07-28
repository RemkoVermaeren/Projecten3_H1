var mongoose = require('mongoose');

var FaqSchema = new mongoose.Schema({
    question: String,
    answer: String
});

mongoose.model('Faq', FaqSchema);