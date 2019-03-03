from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

def analyse(sentence):
	analyser = SentimentIntensityAnalyzer()
	score = analyser.polarity_scores(sentence)
	return score['compound']   #-0.05 to 0.05 is neutral

if __name__ == "__main__":
	ip = input("Enter a sentence:")
	print("The score is {}".format(analyse(ip)))