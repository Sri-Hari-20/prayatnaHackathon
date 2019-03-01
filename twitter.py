from selenium import webdriver
from bs4 import BeautifulSoup
import sys
import random

non_bmp_map = dict.fromkeys(range(0x10000, sys.maxunicode + 1), 0xfffd)

list_to_scrap_suicidal = ["suicidal","suicide","kill myself","my suicide note","my suicide letter","end my life","never wake up","can't go on","not worth living","ready to jump"
,"sleep forever","want to die","be dead","better off without me","better off dead","suicide plan","suicide pact","tired of living","don't want to be here","die alone","wanna die"
,"wanna suicide","commit suicide","die now","slit my wrist","cut my wrist","slash my wrist","do not want to be here","want it to be over","want to be dead","nothing to live for"
,"ready to die","thoughts of suicide","suicide ideation","why should i live","take my own life","depressed"]

list_to_scrap_non_suicidal = ["movie","play","enjoyed","party","fun","memorable","adventure","exciting","cricket","news","engaged","marriage","aeroplane","fruits","balloon","bible"
,"vegetables","temple","circus","clock","kitchen","library","liquid","map","office","radar","rainbow","sandwich","food","subway","space","dog","pet","panda","tv","superhero","school"]

def scrape_function(type, sterm):
    URL = "https://twitter.com/search?f=tweets&vertical=default&q="+sterm+"&l=en&src=typd"
    driver = webdriver.Firefox()
    driver.get(URL)
    soup = BeautifulSoup(driver.page_source, "lxml")
    tweetReplyList = soup.find(class_='stream')
    tweetReplyContent = []
    if tweetReplyList is not None:
        tweetReply = soup.findAll(attrs = {'class' : 'TweetTextSize js-tweet-text tweet-text'})
        tweetReplyList = soup.findAll(attrs = {'class' : 'tweet-timestamp js-permalink js-nav js-tooltip'})
        for content in tweetReply:
            if content is not None:
                content = content.get_text().replace("\n", " ")
                tweetReplyContent.append(content)
    write_function(type, tweetReplyContent)
    driver.quit()

def write_function(type, content):
    with open("output", "a+") as f:
        for i in content:
            f.write(type)
            f.write("\t\t")
            f.write("%s" % i)
            f.write("\n")
    f.close()

if __name__ == "__main__":
    for i, j in zip(list_to_scrap_suicidal, list_to_scrap_non_suicidal):
        scrape_function("suicidal", i)
        scrape_function("non suicidal", j)
