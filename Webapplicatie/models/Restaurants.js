var mongoose = require('mongoose');

var RestaurantSchema = new mongoose.Schema({
    name: String,
    address: String,
    rating: Number,
    telephoneNumber: String,
    website : String,
    extraInformation: {type: String, default : ""},
    wheelchairAccess: Boolean,
    veganPoints: Number
});


mongoose.model('Restaurant', RestaurantSchema);
