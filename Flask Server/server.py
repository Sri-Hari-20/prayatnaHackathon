from flask import Flask, jsonify, request
from pymongo import MongoClient
import os
import base64
from userScraper import user_scraping
from evaluate import eval
import shutil
from create_model import text_pre_process

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
        result = dict()
        result["audio"] = "none"
        result["class"] = "none"
        path = "data/" + analyze_creds["email"]
        if(analyze_creds["audio"] != ""):
            audioString = analyze_creds["audio"]
            audioString = bytes(audioString, encoding="utf-8")
            if not os.path.exists(path):
                os.makedirs(path)
            file = path + "/temp.wav"
            with open(file, "wb+") as f:
                f.write(base64.decodebytes(audioString))
            result["audio"] = eval(file, 1)

        print(analyze_creds["link"])
        if(analyze_creds["link"] != ""):
            user_scraping(analyze_creds["link"], path)

        file = path + "/twitter_output.txt"
        print(analyze_creds["messages"])
        if(analyze_creds["messages"] != ""):
            if not os.path.exists(path):
                os.makedirs(path)
            lines = analyze_creds["messages"].split(";")
            with open(file, "a+") as f:
                for i in lines:
                    f.write(i)
                    f.write("\n")
        if os.path.exists(file):
            result["class"] = eval(file, 2)
            result["class"] = round(result["class"], 2)
        print(result)
        result_creds = jsonify(result)
        shutil.rmtree(path)
        return result_creds
    except Exception as e:
        print(e)
        return "NF"


if __name__ == "__main__":
    app.run(port=8000, debug=True, host="0.0.0.0")
