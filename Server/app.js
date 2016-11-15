var express = require('express');
var bodyParser = require('body-parser');
var multer = require('multer'); // v1.0.5
var upload = multer(); // for parsing multipart/form-data
var app = express();
var bd = require('./config.js')
const METRICS_FACT_TABLES ={units_sold:['fact_sale'] };
app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({ extended: true })); // for parsing application/x-www-form-urlencoded

bd.client.connect();

function runQuery(req, res, qr){
	var qry = bd.client.query(qr).then(function(result){
		res.send(result.rows);
	},
	function(error){
		console.log(error);
	});
}

app.post('/insertScore', upload.array(), function(req, res){
	console.log(req.body);
	var sql = req.body;
	var text = "INSERT INTO scores (USER_NAME,SCORE) VALUES (" + "'" + sql.name + "'" + "," + sql.score + ")"
	console.log(text)
	runQuery(req,res,text);
});
app.get('/top100', function (req, res) {
	runQuery(req, res, 'SELECT * FROM scores ORDER BY score DESC LIMIT 100')
});

// Return the number of users that are with higher score the the user, so the user's position is the returned by this function+ 1
app.get('/getuserposition/:score', function (req, res) { 
	runQuery(req, res, "SELECT COUNT(score) FROM scores WHERE score >= " + req.params.score)
});


app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});
app.get('/closeConn', function (req, res) {
	bd.client.end();
	process.exit(1);
});