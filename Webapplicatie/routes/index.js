(function () {

    'use strict';
    var express = require('express');
    var router = express.Router();
    var mongoose = require('mongoose');
    var Restaurant = mongoose.model('Restaurant');
    var Menu = mongoose.model('Menu');
    var User = mongoose.model('User');
    var Faq = mongoose.model('Faq');
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

    //region FAQ ROUTING
    // FAQ : get all faqs
    router.get('/api/faqs', function (req, res, next) {
        Faq.find(function (err, faqs) {
            if (err) {
                return next(err);
            }
            res.json(faqs);
        });
    });
    // FAQ : param for specific faq with parameter ID
    router.param('faq', function (req, res, next, id) {
        var query = Faq.findById(id);
        query.exec(function (err, faq) {
            if (err) {
                return next(err);
            }
            if (!faq) {
                return next(new Error('can\'t find the faq'));
            }
            req.faq = faq;
            return next();
        })
    });
    //FAQ: get specific faq
    router.get('/api/faqs/:faq', function (req, res, next) {
        res.json(req.faq);
    });
    //FAQ: create faq
    router.post('/api/faqs', auth , function (req, res, next) {
        var faq = new Faq(req.body);
        faq.save(function (err, faq) {
            if (err) {
                return next(err);
            }
            res.json(faq);
        })
    });
    //FAQ : delete specific faq
    router.delete('/api/faqs/:faq', auth , function (req, res, next) {
        Faq.remove({
            _id: req.faq._id
        }, function (err, faq) {
            if (err) {
                res.send(err);
            }
            res.json({
                message: 'Faq deleted'
            });
        });
    });
    //FAQ : update specific faq
    router.put('/api/faqs/:faq', auth, function (req, res) {
        Faq.findById(req.faq._id, function (err, faq) {
            if (err) {
                res.send(err);
            }
            faq.question = req.body.question;
            faq.answer = req.body.answer;
            faq.save(function (err, faq) {
                if (err) {
                    res.send(err);
                }
                res.json(faq);
            })
        });
    });
    //endregion

    //region RESTAURANT ROUTING
    router.get('/api/restaurants', function (req, res, next) {
        Restaurant.find(function (err, restaurants) {
            if (err) {
                return next(err);
            }

            res.json(restaurants);
        });
    });

    router.post('/api/restaurants',auth ,function (req, res, next) {
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

    router.get('/api/restaurants/:restaurant', function (req, res, next) {
        req.restaurant.populate('menus', function (err, restaurant) {
            if (err) {
                return next(err);
            }


            res.json(restaurant);
        });

    });

    router.delete('/api/restaurants/:restaurant', auth ,function (req, res, next) {
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
    router.put('/api/restaurants/:restaurant', auth ,function (req, res) {
        Restaurant.findById(req.restaurant._id, function (err, restaurant) {
            if (err) {
                res.send(err);
            }

            restaurant.name = req.body.name;
            restaurant.place = req.body.place;
            restaurant.openingtime = req.body.openingtime;
            restaurant.closingtime = req.body.closingtime;
            restaurant.wheelchairAccess = req.body.wheelchairAccess;
            restaurant.extraInformation = req.body.extraInformation;
            restaurant.capacity = req.body.capacity;

            restaurant.save(function (err) {
                if (err) {
                    res.send(err);
                }
                res.json(restaurant);
            })

        });
    });
    //endregion

    //region MENU ROUTING
    router.delete('/api/restaurants/:restaurant/menus/:menu', auth ,function (req, res, next) {

        Menu.remove({
            _id: req.menu._id

        }, function (err, menu) {

            if (err) {
                res.send(err);
            }
            res.json({
                message: 'Menu deleted'
            });
        });
    });

    // router.delete('/api/restaurants/:restaurant/menus/:menu', function(req, res, next) {
    //     Menu.remove({
    //         _id: req.menu._id
    //     }, function(err, menu) {
    //         if (err) {
    //             res.send(err);
    //         }
    //         res.json({
    //             message: 'Menu deleted'
    //         });
    //     });
    //     Restaurant.findById(req.restaurant._id, function(err, restaurant) {
    //         if (err) {
    //             res.send(err);
    //         }
    //         restaurant.menus.remove({
    //             id: req.menu._id
    //         }, function(err, menu) {
    //             if (err) {
    //                 res.send(err);
    //             }
    //             res.json({
    //                 message: 'Menu inside restaurant deleted'
    //             });
    //         });
    //     });
    // });

    router.param('menu', function (req, res, next, id) {
        var query = Menu.findById(id);

        query.exec(function (err, menu) {
            if (err) {
                return next(err);
            }
            if (!menu) {
                return next(new Error('can\'t find menu'));
            }

            req.menu = menu;
            return next();
        });
    });

    router.post('/api/restaurants/:restaurant/menus', auth ,function (req, res, next) {
        var menu = new Menu(req.body);
        menu.restaurant = req.restaurant;

        menu.save(function (err, menu) {
            if (err) {
                return next(err);
            }

            req.restaurant.menus.push(menu);
            req.restaurant.save(function (err, restaurant) {
                if (err) {
                    return next(err);
                }

                res.json(menu);
            });
        });
    });

    router.get('/api/restaurants/:restaurant/menus', function (req, res, next) {
        Menu.find({
            restaurants: req.restaurant._id
        }, function (err, menus) {
            if (err) {
                return next(err);
            }

            res.json(menus);
        }).populate('restaurants', function (err, restaurant) {
            if (err) {
                return next(err);
            }
        });
    });

    router.get('/api/restaurants/:restaurant/menus/:menu', function (req, res) {
        res.json(req.menu);

    });

    router.put('/api/restaurants/:restaurant/menus/:menu', auth ,function (req, res) {
        Menu.findById(req.menu._id, function (err, menu) {
            if (err) {
                res.send(err);
            }
            menu.category = req.body.category;
            menu.food = req.body.food;
            menu.price = req.body.price;
            menu.allergy = req.body.allergy;
            menu.date = req.body.date;
            menu.restaurants = req.body.restaurants;

            menu.save(function (err) {
                if (err) {
                    res.send(err);
                }
                res.json(menu);
            })

        });
    });
    //endregion

module.exports = router;

})();