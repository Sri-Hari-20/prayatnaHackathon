from flask import Flask, jsonify, request
from pymongo import MongoClient
import os
import base64

client = MongoClient('localhost', 27017)
database = client.ReachOut

app = Flask(__name__)

@app.route('/')
def home_function():
    return "This is the home route request on a different one!"

@app.route('/register', methods = ["POST"])
def register_function():
    try:
        creds = request.get_json()
        collection = database.register
        collection.insert_one(creds)
        return "Done"
    except:
        return "Failed"

@app.route('/login', methods = ["POST"])
def login_function():
    try:
        log_creds = request.get_json()
        search = {"email" : log_creds["email"]}
        collection = database.register
        search_creds = collection.find_one(search)
        if search_creds is not None:
            if(log_creds["password"] == search_creds["password"]):
                return search_creds["name"]
            else:
                return "NF"
        else:
            return "NF"
    except:
        return "NF"

@app.route('/analyze', methods = ["POST"])
def analyze_function():
    try:
        analyze_creds = request.get_json()
        path = "audio/" + analyze_creds["email"]
        audioString = analyze_creds["audio"]
        audioString = bytes(audioString, encoding="utf-8")
        if not os.path.exists(path):
            os.makedirs(path)
        file = path + "/temp.wav"
        with open(file, "wb+") as f:
            f.write(base64.decodebytes(audioString))
        return "Done"
    except Exception as e:
        print(e)
        return "NF"


if __name__ == "__main__":
    app.run(port=8000, debug=True, host="0.0.0.0")
