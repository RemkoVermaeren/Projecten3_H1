(function() {

    'use strict';

    var express = require('express');
    var router = express.Router();
    var mongoose = require('mongoose');
    var passport = require('passport');
    var User = mongoose.model('User');
    var Challenge = mongoose.model('Challenge');
    var jwt = require('express-jwt');
    var auth = jwt({
        secret: 'SECRET',
        userProperty: 'payload'
    });



    //region AUTHENTICATION ROUTING
    router.post('/register', function(req, res, next) {
        if (!req.body.username || !req.body.password) {
            return res.status(400).json({
                message: 'Please fill out all fields'
            });
        }
        var user = new User();
        user.username = req.body.username;
        user.setPassword(req.body.password);
        user.name = req.body.name;
        user.surName = req.body.surName;
        user.dateOfCreation = req.body.dateOfCreation;
        user.isAdmin = req.body.isAdmin;
        user.fullName = req.body.name + " " + req.body.surName;
        user.image = "";
        user.totalVeganScore = 0;
        user.save(function(err) {
            if (err) {
                if (err.name === 'MongoError' && err.code === 11000) {
                    //Duplicate username
                    return res.status(500).send({
                        success: false,
                        message: 'User already exists'
                    });
                }
                return res.status(500).send(err);
            }
            return res.json({
                token: user.generateJWT(),
                userid : user._id
            });
        });
    });

    router.post('/login', function(req, res, next) {
        if (!req.body.username || !req.body.password) {
            return res.status(400).json({
                message: 'Please fill out all fields'
            });
        }
        passport.authenticate('local', function(err, user, info) {
            if (err) {
                return next(err);
            }
            if (user) {
                return res.json({
                    token: user.generateJWT(),
                    userid : user._id
                });
            } else {
                return res.status(401).json(info);
            }
        })(req, res, next);
    });
    //endregion

    //region USERS ROUTING
    router.put('/changepassword', auth, function(req, res) {
        User.findById(req.payload._id, function(err, user) {
            if (err) {
                res.send(err);
            }
            user.setPassword(req.body.password);
            user.save(function(err) {
                if (err) {
                    res.send(err);
                }
                res.json(user);
            })
        });
    });

    router.get('/', function(req, res, next) {
        User.find(function(err, users) {
            if (err) {
                return next(err);
            }

            res.json(users);
        });
    });
    router.get('/nonadmins', function(req, res, next) {
        User.find({'isAdmin': 'false'},function(err, users) {
            if (err) {
                return next(err);
            }

            res.json(users);
        });
    });
    router.get('/:user', function(req, res, next) {
        res.json(req.user);
    });


    router.put('/:user', function (req, res) {
        User.findById(req.user._id, function (err, user) {
            if (err) {
                res.send(err);
            }
            user.username = req.body.username;
            user.name = req.body.name;
            user.surName = req.body.surName;
            user.fullName = req.body.name + " " + req.body.surName;
            user.image = req.body.image;

            user.save(function(err) {
                if (err) {
                    if (err.name === 'MongoError' && err.code === 11000) {
                        //Duplicate username
                        return res.status(500).send({
                            success: false,
                            message: 'User already exists'
                        });
                    }
                    return res.status(500).send(err);
                }
                return res.json(user);
            });
        });
    });
    router.get('/:user/followers', function(req, res, next) {
        var query = User.where('_id').in(req.user.followingUsers);
        query.exec(function (err, users) {
            if (err) {
                return next(err);
            }
            if (!users) {
                return next(new Error('can\'t find the users'));
            }
            return res.json(users);
        });
    });

    router.param('user', function(req, res, next, id) {
        var query = User.findById(id);

        query.exec(function(err, user) {
            if (err) {
                return next(err);
            }
            if (!user) {
                return next(new Error('can\'t find user'));
            }

            req.user = user;
            return next();
        });
    });
    router.param('userfollow', function(req, res, next, id) {
        var query = User.findById(id);

        query.exec(function(err, user) {
            if (err) {
                return next(err);
            }
            if (!user) {
                return next(new Error('can\'t find user'));
            }

            req.userfollow = user;
            return next();
        });
    });
    router.post('/:user/followers/add/:userfollow',function(req,res){
        User.findById(req.user._id, function (err, user) {
            if (err) {
                res.send(err);
            }
            // TODO : Indien problemen met users, ligt het wss hieraan
            user.followingUsers.addToSet(req.userfollow);
            user.save(function (err) {
                if (err) {
                    res.send(err);
                }
                res.json(user);
            })
        });
    });
    router.post('/:user/followers/remove/:userfollow' ,function(req,res){
        User.findById(req.user._id, function (err, user) {
            if (err) {
                res.send(err);
            }
            user.followingUsers.pull(req.userfollow._id);
            user.save(function (err) {
                if (err) {
                    res.send(err);
                }
                res.json(user);
            })
        });
    });

    router.delete('/:user', auth, function(req, res, next) {
        User.remove({
            _id: req.user._id
        }, function(err, user) {
            if (err) {
                res.send(err);
            }
            res.json({
                message: 'User deleted'
            });
        });
    });
    //endregion

    router.get('/:user/challenges', function(req,res){
       User.findById(req.user._id).populate('challenges').exec(function(err,user){
        res.json(user.challenges);
       });
    });
   router.post('/:user/challenges/', function (req, res, next) {
        var challenge = new Challenge(req.body);
        challenge.save(function (err, faq) {
            if (err) {
                return next(err);
            }
            //res.json(faq);
            //next();
        });
        console.log(challenge);
        User.findById(req.user._id, function (err, user) {
            if (err) {
                res.send(err);
            }
            user.challenges.addToSet(challenge);
            if(challenge.isCompleted)
               user.totalVeganScore += challenge.veganScore;
            user.save(function (err) {
                if (err) {
                    res.send(err);
                }
                res.json(user);
            });
        });
    });

    module.exports = router;

})();
