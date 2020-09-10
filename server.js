const express = require('express')
const bcrypt = require('bcryptjs');
const app = express()
const bodyParser = require('body-parser')
const nodemailer = require('nodemailer')
app.use(bodyParser.urlencoded({
    extended: true
}))
const mongoClient = require('mongodb').MongoClient
const port = process.env.PORT || 3000
ObjectId = require('mongodb').ObjectId
var path = require('path');
const {
    raw
} = require('body-parser');

//const url = "mongodb+srv://techrebels:techrebels@.in@techrebels.iajzj.mongodb.net/test"
const url = "mongodb://localhost:27017"
mongoClient.connect(url, {
    useNewUrlParser: true,
    useUnifiedTopology: true
}, (err, db) => {
    if (err) {
        console.log("Error in connecting to mongodb")
    } else {
        console.log("Connected")
        const myDb = db.db('hacksagonnodejs')
        const collection = myDb.collection('user')
        const collectiontiming = myDb.collection('timing')
        const collectionissue = myDb.collection('healthissue')
        const collectiondoctor = myDb.collection('doctor')

	app.get('/', (req, res) => {
            res.statusCode = 200;
            res.setHeader('Content-Type', 'text/html');
            res.end('<h1> Smart City </h1>');
        })


        app.post('/register', (req, res) => {

            const hash = bcrypt.hashSync(req.body.password, 10)

            const newUser = {

                Name: req.body.name,
                Email_id: req.body.email,
                Password: hash,
                Phone: req.body.phone,
                City: req.body.city,
                Village: req.body.village

            }
            const query = {
                Email_id: newUser.Email_id
            }
            collection.findOne(query, (err, result) => {
                if (result == null) {
                    collection.insertOne(newUser, (err, result) => {
                        res.status(200).send()

                    })
                } else {
                    res.status(400).send()

                }
            })

        })


        app.post('/registerdoctor', (req, res) => {

            const hash = bcrypt.hashSync(req.body.password, 10)

            const newUser = {

                Name: req.body.name,
                Email_id: req.body.email,
                Password: hash,
                Phone: req.body.phone,
                City: req.body.city,
                Village: req.body.village,
                Certificate: req.body.certificate

            }
            const query = {
                Email_id: newUser.Email_id
            }
            collectiondoctor.findOne(query, (err, result) => {
                if (result == null) {
                    collectiondoctor.insertOne(newUser, (err, result) => {
                        res.status(200).send()

                    })
                } else {
                    res.status(400).send()

                }
            })

        })


        app.post('/issueinsert', (req, res) => {

            const newIssue = {

                Name: req.body.name,
                Age: req.body.age,
                Village: req.body.village,
                City: req.body.city,
                Symptoms: req.body.symptoms,
                Landmark: req.body.landmark,
                Medicalintake: req.body.intake,
                Injury: req.body.injury,
                Date: req.body.date

            }
            collectionissue.insertOne(newIssue, (err, result) => {
                if (err)
                    res.status(404).send()
                else
                    res.status(200).send()

            })

        })

        app.post('/inserttime', (req, res) => {

            const newTime = {

                Doctor_name: req.body.name,
                Village: req.body.village,
                City: req.body.city,
                Specilist: req.body.specilities,
                Date: req.body.date,
                Datedisplay: req.body.datedisplay,
                Time: req.body.time

            }
            collectiontiming.insertOne(newTime, (err, result) => {
                if (err)
                    res.status(404).send()
                else
                    res.status(200).send()

            })

        })

        app.post('/gettime', (req, res) => {

            collectiontiming.find({
                Village: req.body.village,
                City: req.body.city,
                Date: req.body.date
            }).toArray(function (err, result) {
                if (err) {
                    throw err
                    res.status(400)
                } else {
                    res.status(200).jsonp(result)
                    //  res.json(result)
                }
            })
        })

        app.post('/getissue', (req, res) => {

            collectionissue.find({
                Village: req.body.village,
                City: req.body.city,
                Date: req.body.date
            }).toArray(function (err, result) {
                if (err) {
                    res.status(400)
                } else {
                    // res.json(result)
                    res.status(200).json(result)

                }
            })
        })

        app.post('/logindoctor', (req, res) => {
            const query = {
                Email_id: req.body.email

            }

            collectiondoctor.findOne(query, (err, result) => {
                if (result != null) {
                    var hash = result.Password;
                    const rs = bcrypt.compareSync(req.body.password, hash)
                    if (rs) {
                        res.status(200).send()
                    } else {
                        res.status(404).send()
                    }

                } else {
                    res.status(400).send()
                }
            })
        })



        app.post('/login', (req, res) => {
            const query = {
                Email_id: req.body.email

            }

            collection.findOne(query, (err, result) => {
                if (result != null) {
                    var hash = result.Password;
                    const rs = bcrypt.compareSync(req.body.password, hash)
                    if (rs) {
                        res.status(200).send()
                    } else {
                        res.status(404).send()
                    }

                } else {
                    res.status(400).send()
                }
            })
        })

        app.post('/resetpassword', (req, res) => {
                    const hash = bcrypt.hashSync(req.body.password, 10)

                    const User = {
                        $set: {
                            Password: hash
                        }
                    }
                    collection.updateOne({
                        Email_id: req.body.email
                    }, User, (err, result2) => {
                        if (err) {
                            res.status(400).send();
                        }
                        res.status(200).send()

                    }
            )
        })

        app.post('/verifyemail', (req, res) => {
            var email = req.body.email
            var code = req.body.code

            var transporter = nodemailer.createTransport({
                service: 'gmail',
                auth: {
                    user: 'techrebelshacksagon@gmail.com',
                    pass: 'ivvnacngqygzlbxx'
                }
            });

            var mailOptions = {
                from: 'techrebelshacksagon@gmail.com',
                to: email,
                subject: 'Email verification Code',
                text: code
            };
            transporter.sendMail(mailOptions, function (error, info) {
                if (error) {
                    res.status(404).send()
                    console.log(error);
                } else {
                    res.status(200).send()
                    console.log('Email sent: ' + info.response);
                }
            });

        })

        app.post('/forgotpass', (req, res) => {
            var email = req.body.email
            var code = req.body.code

            const query = {
                Email_id: email
            }
            collection.findOne(query, (err, result) => {
                if (result == null) {
                   res.status(400).send()
                } else {
                    var transporter = nodemailer.createTransport({
                        service: 'gmail',
                        auth: {
                            user: 'techrebelshacksagon@gmail.com',
                            pass: 'ivvnacngqygzlbxx'
                        }
                    });

                    var mailOptions = {
                        from: 'techrebelshacksagon@gmail.com',
                        to: email,
                        subject: 'Forgot Email Verification Code',
                        text: code
                    };
                    transporter.sendMail(mailOptions, function (error, info) {
                        if (error) {
                            res.status(404).send()
                            console.log(error);
                        } else {
                            res.status(200).send()
                            console.log('Email sent Sucessfully ' + info.response);
                        }
                    });

                }
            })
        })

    }

})



app.use(express.json())
app.listen(port, () => {

    console.log("Listening on port 3000..")

})
