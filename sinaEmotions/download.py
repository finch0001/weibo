import urllib
import urllib2
import sqlite3
import sys
reload(sys)
sys.setdefaultencoding("utf-8")
con = sqlite3.connect("sina_emotions.db")
cursor = con.execute("SELECT * FROM t_emotion")

for row in cursor:
    url = row[2]
    path = "sina_emotions/" + row[3]
    urllib.urlretrieve(url, path)
    print "download" + row[1]

cursor.close()