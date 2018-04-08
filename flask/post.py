from flask import Flask, request, jsonify, render_template
from flask_sqlalchemy import SQLAlchemy
import uuid
from werkzeug.security import generate_password_hash, check_password_hash
import json
import datetime
import pandas as pd
from flask_cors import CORS
import numpy as np

def get_timestamp():
	return(datetime.datetime.now().strftime("%A, %d. %B %Y %I:%M%p"))

app = Flask(__name__)
CORS(app)

app.config['SECRET_KEY'] = 'thisissecret'
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///checkpost.sqlite3'

db = SQLAlchemy(app)

class Consignment(db.Model):
    consignment_id = db.Column(db.String(13), primary_key=True)
    source = db.Column(db.String(50))
    destination = db.Column(db.String(50))
    weight = db.Column(db.Integer)
    delivery_cost = db.Column(db.Integer)
    sender_name = db.Column(db.String(50))
    sender_number = db.Column(db.Integer)
    sender_address = db.Column(db.String(100))
    receiver_name = db.Column(db.String(50))
    receiver_number = db.Column(db.Integer)
    receiver_address = db.Column(db.String(100))
    booking_date = db.Column(db.String(20))
    package_type = db.Column(db.String(3)) 
    source_pin = db.Column(db.Integer)
    destination_pin = db.Column(db.Integer)


class Scan(db.Model):
    consignment_id = db.Column(db.String(13), primary_key=True)
    scan_timestamps = db.Column(db.String(500))

@app.route('/pkg', methods=['POST'])
def post_pkg():
	data = request.get_json()
	
	new_pkg = Consignment(consignment_id=data['consignment_id'], source=data['source'], destination=data['destination'], weight=data['weight'],
		delivery_cost=data['delivery_cost'], sender_name=data['sender_name'], sender_address=data['sender_address'], receiver_name=data['receiver_name'], 
		receiver_address=data['receiver_address'], package_type=data['package_type'], sender_number=data['sender_number'], receiver_number=data['receiver_number'], 
		source_pin=data['source_pin'],destination_pin=data['destination_pin'],booking_date=get_timestamp())
	
	new_pkg_scan = Scan(consignment_id=data['consignment_id'], scan_timestamps=json.dumps([]))

	db.session.add(new_pkg)
	db.session.add(new_pkg_scan)
	db.session.commit()

	return jsonify({'message':'Package successfully registered'})

@app.route('/pkg/<cons_id>', methods=['GET'])
def get_pkg(cons_id):
	package = Consignment.query.filter_by(consignment_id=cons_id).first()	
	if not package:
		return jsonify({'message':'No package found'})
	package_data = {}
	package_data['consignment_id'] = package.consignment_id
	package_data['source'] = package.source
	package_data['destination'] = package.destination
	package_data['weight'] = package.weight
	package_data['delivery_cost'] = package.delivery_cost
	package_data['sender_name'] = package.sender_name
	package_data['sender_address'] = package.sender_address
	package_data['receiver_name'] = package.receiver_name
	package_data['receiver_address'] = package.receiver_address
	package_data['package_type'] = package.package_type
	package_data['booking_date'] = package.booking_date
	package_data['source_pin'] = package.source_pin
	package_data['destination_pin'] = package.destination_pin
	
	return jsonify({'package':package_data})	

@app.route('/scan/<cons_id>', methods=['POST'])
def scan_pkg(cons_id):
	
	data = request.get_json()

	location = data['location']
	state = data['state']

	package = Scan.query.filter_by(consignment_id=cons_id).first()

	scans = json.loads(package.scan_timestamps)
	
	dump = {}
	dump['location'] = location
	dump['stamp'] = state
	dump['timestamp'] = get_timestamp()

	stamp = scans.append(dump)
	
	package.scan_timestamps = json.dumps(scans)

	db.session.commit()

	return jsonify({'message':'package scanned successfully'})

@app.route('/scan/<cons_id>', methods=['GET'])
def get_status(cons_id):
	package = Scan.query.filter_by(consignment_id=cons_id).first()
	if not package:
		return jsonify({'message':'No package found'})
	output = {}
	output['consignment_id'] = package.consignment_id
	output['scans'] = json.loads(package.scan_timestamps)

	return jsonify({'package':output})


@app.route('/getpkgs', methods=['GET'])
def get_all_pkg():
	packages = Consignment.query.all()
	output = []

	for package in packages:
		package_data = {}
		package_data['consignment_id'] = package.consignment_id
		package_data['source'] = package.source
		package_data['destination'] = package.destination
		package_data['weight'] = package.weight
		package_data['delivery_cost'] = package.delivery_cost
		package_data['sender_name'] = package.sender_name
		package_data['sender_address'] = package.sender_address
		package_data['receiver_name'] = package.receiver_name
		package_data['receiver_address'] = package.receiver_address
		package_data['package_type'] = package.package_type
		package_data['booking_date'] = package.booking_date
		package_data['sender_number'] = package.sender_number
		package_data['receiver_number'] = package.receiver_number
		package_data['source_pin'] = package.source_pin
		package_data['destination_pin'] = package.destination_pin

		package = Scan.query.filter_by(consignment_id=package.consignment_id).first()
		output2 = {}
		output2['scans'] = json.loads(package.scan_timestamps)

		package_data['transit'] = output2

		output.append(package_data)

	return jsonify({'packages':output})

@app.route('/track/src=<src>&dist=<dist>', methods=['GET'])
def test(src, dest):
	
	return []

@app.route('/')
def page():
	return '<div><H1>API USAGE</H1></div><div><H4>/getpkgs</H4></div><div><H4>/pkg</H4></div><div><H4>/scan</H4></div>'

@app.route('/eta/<cons_id>', methods=['GET'])
def get_ETA(cons_id):

	package = Consignment.query.filter_by(consignment_id=cons_id).first()	
	if not package:
		return jsonify({'message':'No package found'})
	
	eta = ETA(package.source_pin, package.destination_pin, package.package_type)

	d = int(round(eta,1))
	h = int(24*(eta*10%10)/10)
	time = str(d) + ' days ' + str(h) + ' hours'
	return jsonify({'ETA': time})

def get_dist(source, destination):
	data = pd.read_csv("Delivery.csv")
	src_lo = data[data['pincode']==source]['longitude'].values[0]
	src_la = data[data['pincode']==source]['latitude'].values[0]

	des_lo = data[data['pincode']==destination]['longitude'].values[0]
	des_la = data[data['pincode']==destination]['latitude'].values[0]

	dist = np.sqrt(np.square(src_lo-des_lo)+np.square(src_la-des_la))

	return dist

def ETA(source, destination, ptype):
	dist = get_dist(source, destination)

	send_mails = pd.read_csv("train.csv")
	send_mails['time'] = (send_mails['EPA']*5 + send_mails['SPA']*3 + send_mails['RPA']*9)*send_mails['distance']

	data = send_mails[['distance', 'EPA', 'SPA', 'RPA']][:120000]
	classes = send_mails['time'][:120000]

	from sklearn.model_selection import train_test_split as ttc

	X_train, X_test, Y_train, Y_test = ttc(data.values, classes.values)

	from sklearn.linear_model import LinearRegression
	clf = LinearRegression()
	clf.fit(X_train, Y_train)

	if(ptype=='EPA'):
		t = clf.predict([[dist,1,0,0]])
	elif(ptype=='SPA'):
		t = clf.predict([[dist,0,1,0]])
	else:
		t = clf.predict([[dist,0,0,1]])


	from scipy.interpolate import interp1d
	m = interp1d([-15,90],[0,2])
	return 1+m(t)[0]


if __name__ == '__main__':
	app.run(host='0.0.0.0', debug=True)