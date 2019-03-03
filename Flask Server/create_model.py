from nltk.corpus import stopwords
import pandas as pd
import string 
from sklearn.feature_extraction.text import CountVectorizer,TfidfTransformer
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import classification_report
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.externals import joblib

stahpwords = stopwords.words('english')
for i in ['herself','himself','myself']:
	stahpwords.remove(i)

def text_pre_process(message):
	no_punc = [char for char in message if char not in string.punctuation]
	no_punc = ''.join(no_punc)
	return [word for word in no_punc.split() if word.lower() not in stahpwords]

if __name__ == "__main__":
	messages = pd.read_csv('output',sep='\t\t',names=['label','message'])
	msg_train, msg_test, label_train, label_test = train_test_split(messages['message'], messages['label'], test_size=0.2)
	train_data = pd.DataFrame({"label": label_train, "message": msg_train})
	pipeline = Pipeline([
	('bow', CountVectorizer(analyzer=text_pre_process)),  # strings to token integer counts
	('tfidf', TfidfTransformer()),  # integer counts to weighted TF-IDF scores
	('classifier', MultinomialNB()),  # train on TF-IDF vectors w/ Naive Bayes classifier
	])
	clf = pipeline.fit(train_data['message'],train_data['label'])
	predictions = pipeline.predict(msg_test)
	print(classification_report(predictions,label_test))
	joblib.dump(clf, 'suicideClassifier_nb.pkl')


#print("Predicted:" + str(detect_model.predict(tfidf4)[0]))

