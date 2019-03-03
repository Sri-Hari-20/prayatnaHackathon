import speech_recognition as sr 


def recognize(file_name):
	r = sr.Recognizer()
	AUDIO_FILE = (file_name)
	with sr.AudioFile(AUDIO_FILE) as source:
		audio = r.record(source)
	try:
		return r.recognize_google(audio)
	except sr.UnknownValueError:
		print("Google Speech Recognition could not understand audio") 
	except sr.RequestError as e:
		print("Could not request results from Google SpeechRecognition service; {0}".format(e)) 