# -*- coding: utf-8 -*-
import os
import re
import time
import json

__author__ = 'Jayin Ton'

ignores = [
  r'.*~',
  r'.*json'
]

def file_filter(file_name):
    #global ignores
    for ig in ignores :
        if re.match(ig,file_name) :
            return False
    return True 



for x in range(1,5,1):
    files = filter(file_filter, os.listdir(('./{0}/'.format(x))))
    print(files);
    result = json.dumps({
      ' update_time' : long(time.time())
      ,'size' : len(files)

    })

    with open('./{0}/{1}.json'.format(x,x),mode='w') as f:
        f.write(result)

print('make finish!')



