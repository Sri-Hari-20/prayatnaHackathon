from sklearn.externals import joblib
from nltk.corpus import stopwords
import pandas as pd
import string 
from sklearn.feature_extraction.text import CountVectorizer,TfidfTransformer
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import classification_report
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.externals import joblib
from performance import text_pre_process

def predict_svm(message):
	clf = joblib.load('suicideClassifier_svm.pkl')
	return str(clf.predict([message])[0])

if __name__ == "__main__":
	message = input("Enter a sample text:")
	print("Prediction: " + predict_svm(message))

	