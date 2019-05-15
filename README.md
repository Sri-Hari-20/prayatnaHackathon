# prayatnaHackathon
Code used for prayatna hackathon

### Summary
This is an Android application for the prediction of suicidal tendencies of a person based on one (or more) of the following sources
1. Their twitter ID (Only public tweets using Web Scraping)
2. Copy pasting their conversational messages
3. Uploading the recorded conversation. (One person voice to text and followed by classification as of now)

#### Note
<p>The application only acts as an interface for the user while all the information uploaded will be analyzed and classified server side using Support Vector Machine Classifier and Naive Bayes Classifier (Combined for resepctable accuracy of prediction). The files are only stored for the duration of the analysis only and are removed after said classification results for the user.</p>
    
### Components
  The entire setup consists of two major components.
  1. The android application project files (To be opened with android studio)
  2. Flask server side files

### Dependencies
##### Python Modules
  * Flask
  * Selenium (By default configured for firefox)
  * Scikit Learn (For NB and SVM classsification)
  * pymongo
  * BeautifulSoup4
  * speech_recognition
  * VaderSentiment
  * NLTK (all corpus)

##### Software
  * Android Studio
  * Selenium WebDriver
  * MongoDB
  
#### Setup
<p>Install the application on to your phone using the project files opened on android studio. At the time of development we do not have a production server hence REST api calls were done with hard coding the IP Address and Port. Change its reference in strings.xml and you should be all set.<p>
    
Run the flask server files by
<code>python3 server.py</code>

<p>All the scraping and classification work is automated. The files to retrain the classifier is also provided.<p>
