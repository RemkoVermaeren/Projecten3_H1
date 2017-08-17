(function () {

    'use strict';

    var mongoose = require('mongoose');
    var cloudinary = require('cloudinary');
    var express = require('express');

    cloudinary.config({
        cloud_name: 'douskchks',
        api_key: '552883666323728',
        api_secret: 'RhDl-TvAXIiaPkeBWOHY8OcCwr8'
    });

    var router = express.Router();

    var Restaurant = mongoose.model('Restaurant');
    var User = mongoose.model('User');
    var Recipe = mongoose.model('Recipe');
    var Challenge = mongoose.model('Challenge');
    var Blog = mongoose.model('Blog');
    var passport = require('passport');
    var jwt = require('express-jwt');
    var auth = jwt({
        secret: 'SECRET',
        userProperty: 'payload'
    });

    /* GET home page. */
    router.get('/', function (req, res, next) {
        res.render('index', {
            title: 'Express'
        });
    });

    /* Upload images */
    router.post('/api/upload/image', function (req, res, next) {
        cloudinary.v2.uploader.upload(req.body.data, {
            resource_type: "image"
        }, function (error, result) {
            if (error) {
                return console.log(error);
            }
            console.log(result.url);
            res.send(result.url);
        });
    });

    //region RESTAURANT ROUTING
    router.get('/api/restaurants', function (req, res, next) {
        Restaurant.find(function (err, restaurants) {
            if (err) {
                return next(err);
            }

            res.json(restaurants);
        });
    });

    router.post('/api/restaurants', auth, function (req, res, next) {
        var restaurant = new Restaurant(req.body);
        //restaurant.author = req.payload.;
        restaurant.save(function (err, restaurant) {
            if (err) {
                return next(err);
            }

            res.json(restaurant);
        });
    });

    router.param('restaurant', function (req, res, next, id) {
        var query = Restaurant.findById(id);

        query.exec(function (err, restaurant) {
            if (err) {
                return next(err);
            }
            if (!restaurant) {
                return next(new Error('can\'t find restaurant'));
            }

            req.restaurant = restaurant;
            return next();
        });
    });

    router.get('/api/restaurants/:restaurant', function (req, res) {
        res.json(req.restaurant);
    });

    router.delete('/api/restaurants/:restaurant', auth, function (req, res, next) {
        Restaurant.remove({
            _id: req.restaurant._id
        }, function (err, restaurant) {
            if (err) {
                res.send(err);
            }
            res.json({
                message: 'Restaurant deleted'
            });
        });
    });
    router.put('/api/restaurants/:restaurant', auth, function (req, res) {
        Restaurant.findById(req.restaurant._id, function (err, restaurant) {
            if (err) {
                res.send(err);
            }
            restaurant.name = req.body.name;
            restaurant.picture = req.body.picture;
            restaurant.address = req.body.address;
            restaurant.rating = req.body.rating;
            restaurant.wheelchairAccess = req.body.wheelchairAccess;
            restaurant.extraInformation = req.body.extraInformation;
            restaurant.veganPoints = req.body.veganPoints;
            restaurant.website = req.body.website;
            restaurant.telephoneNumber = req.body.telephoneNumber;
            restaurant.save(function (err) {
                if (err) {
                    res.send(err);
                }
                res.json(restaurant);
            })

        });
    });
    //endregion


    // region Recipe ROUTING
    // Recipe : get all recipes
    router.get('/api/recipes', function (req, res, next) {
        Recipe.find(function (err, recipes) {
            if (err) {
                return next(err);
            }
            res.json(recipes);
        });
    });
    // Recipe : param for specific recipe with parameter ID
    router.param('recipe', function (req, res, next, id) {
        var query = Recipe.findById(id);
        query.exec(function (err, recipe) {
            if (err) {
                return next(err);
            }
            if (!recipe) {
                return next(new Error('can\'t find the recipe'));
            }
            req.recipe = recipe;
            return next();
        })
    });
    //Recipe : get specific recipe
    router.get('/api/recipes/:recipe', function (req, res, next) {
        res.json(req.recipe);
    });
    //Recipe : create recipe
    router.post('/api/recipes', auth, function (req, res, next) {
        var recipe = new Recipe(req.body);
        recipe.save(function (err, recipe) {
            if (err) {
                return next(err);
            }
            res.json(recipe);
        })
    });
    //Recipe : delete specific recipe
    router.delete('/api/recipes/:recipe', auth, function (req, res, next) {
        Recipe.remove({
            _id: req.recipe._id
        }, function (err, recipe) {
            if (err) {
                res.send(err);
            }
            res.json({
                message: 'Recipe deleted'
            });
        });
    });
    //Recipe : update specific recipe
    router.put('/api/recipes/:recipe', auth, function (req, res) {
        Recipe.findById(req.recipe._id, function (err, recipe) {
            if (err) {
                res.send(err);
            }
            recipe.name = req.body.name;
            recipe.veganPoints = req.body.veganPoints;
            recipe.calories = req.body.calories;
            recipe.food = req.body.food;
            recipe.difficulty = req.body.difficulty;
            recipe.time = req.body.time;
            recipe.allergies = req.body.allergies;
            recipe.picture = req.body.picture;
            recipe.type = req.body.type;
            recipe.instructions = req.body.instructions;
            recipe.save(function (err, recipe) {
                if (err) {
                    res.send(err);
                }
                console.log("Saved recipe");
                console.log(recipe);
                res.json(recipe);
            })
        });
    });
    //endregion

    // region Blog ROUTING
    // Blog : get all blogs
    router.get('/api/blogs', function (req, res, next) {
        Blog.find(function (err, blogs) {
            if (err) {
                return next(err);
            }
            res.json(blogs);
        });
    });
    // Blog : param for specific blog with parameter ID
    router.param('blog', function (req, res, next, id) {
        var query = Blog.findById(id);
        query.exec(function (err, blog) {
            if (err) {
                return next(err);
            }
            if (!blog) {
                return next(new Error('can\'t find the blog'));
            }
            req.blog = blog;
            return next();
        })
    });
    //Blog : get specific blog
    router.get('/api/blogs/:blog', function (req, res, next) {
        res.json(req.blog);
    });
    //Blog : create blog
    router.post('/api/blogs', auth, function (req, res, next) {
        var blog = new Blog(req.body);
        blog.save(function (err, blog) {
            if (err) {
                return next(err);
            }
            res.json(blog);
        })
    });
    //Blog : delete specific blog
    router.delete('/api/blogs/:blog', auth, function (req, res, next) {
        Blog.remove({
            _id: req.blog._id
        }, function (err, blog) {
            if (err) {
                res.send(err);
            }
            res.json({
                message: 'Recipe deleted'
            });
        });
    });
    //Blog : update specific blog
    router.put('/api/blogs/:blog', auth, function (req, res) {
        Blog.findById(req.blog._id, function (err, blog) {
            if (err) {
                res.send(err);
            }
            blog.name = req.body.name;
            blog.veganPoints = req.body.veganPoints;
            blog.author = req.body.author;
            blog.picture = req.body.picture;
            blog.date = req.body.date;
            blog.website = req.body.website;
            blog.save(function (err, blog) {
                if (err) {
                    res.send(err);
                }
                res.json(blog);
            })
        });
    });
    //endregion


    //Challenges routing
    router.get('/api/challenges', function (req, res, next) {
        Challenge.find(function (err, challenges) {
            if (err) {
                return next(err);
            }

            res.json(challenges);
        });
    });

    module.exports = router;

})();
