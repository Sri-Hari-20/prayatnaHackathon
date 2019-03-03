from selenium import webdriver
from bs4 import BeautifulSoup
import sys
import re
import time
import os

pause_time = 0.5
non_bmp_map = dict.fromkeys(range(0x10000, sys.maxunicode + 1), 0xfffd)

def write_function(content, path):
    if not os.path.exists(path):
        os.makedirs(path)
    path = path + "/twitter_output"
    with open(path, "w+", encoding="utf-8") as f:
        for i in content:
            f.write("%s" % i)
            f.write("\n")
    f.close()

def user_scraping(url, path):
    driver = webdriver.Firefox()
    driver.get(url)
    count = 5
    while count > 0:
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(pause_time)
        count = count - 1
    soup = BeautifulSoup(driver.page_source, "lxml")
    tweetReplyList = soup.find(class_='stream')
    tweetReplyContent = []
    if tweetReplyList is not None:
        tweetReply = soup.findAll(attrs = {'class' : 'TweetTextSize TweetTextSize--normal js-tweet-text tweet-text'})
        for content in tweetReply:
            if content is not None:
                content = content.get_text().replace("\n", " ")
                content = re.sub(r'http\S+', '', content)
                tweetReplyContent.append(content)
    write_function(tweetReplyContent, path)
    driver.quit()
if __name__ == "__main__":
    user_scraping("https://twitter.com/VanossGaming", "boop")
