var mongoose = require('mongoose');

var MenuSchema = new mongoose.Schema({
  category: String,
  food: String,
  price: {type: Number, default: 3.8},
  restaurants: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Restaurant' }],
  allergy: [String],
  date: Date
});


mongoose.model('Menu', MenuSchema);
