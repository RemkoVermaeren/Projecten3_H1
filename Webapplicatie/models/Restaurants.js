var mongoose = require('mongoose');

var RestaurantSchema = new mongoose.Schema({
    name: String,
    place: String,
    openingtime: {
        type: Date,
        default: Date.now
    },
    closingtime: {
        type: Date,
        default: Date.now
    },
    extraInformation: {type: String, default : ""},
    wheelchairAccess: Boolean,
    menus: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Menu'
    }],
    currentVisitors: {type: Number, default: 0},
    capacity: {type: Number, default: 0}
});


mongoose.model('Restaurant', RestaurantSchema);
