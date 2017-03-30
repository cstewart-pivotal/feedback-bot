#!/usr/bin/env python

import os
import json
from flask import (Flask, request, jsonify)
from pcfgcp import PcfGcp
from google.cloud import language


app = Flask(__name__)
port = int(os.getenv("PORT", 18080))

def logMsg(args):
    print "[Instance: %s] %s" % (str(os.getenv("CF_INSTANCE_INDEX", 0)), args)

# def print_result(annotations):
#   score = annotations.sentiment.score
#   magnitude = annotations.sentiment.magnitude
#
#   for index, sentence in enumerate(annotations.sentences):
#     sentence_sentiment = sentence.sentiment.score
#     print('Sentence {} has a sentiment score of {}'.format(index, sentence_sentiment))
#     print('Overall Sentiment: score of {} with magnitude of {}'.format(score, magnitude))
#     return 0
#
#     print('Sentiment: score of {} with magnitude of {}'.format(
#         score, magnitude))
#     return 0

# def analyze(client, text):
  # document = client.document_from_text(text)
  # annotations = document.annotate_text(include_sentiment=True,
  #                                      include_syntax=False,
  #                                      include_entities=False)
  # # Print the results
  #print_result(annotations)


# Handle JSON
@app.route('/', methods = ['POST', 'GET'])
def jsonHandler():
  #get incoming JSON
  obj = request.get_json(force=True)
  text = obj['request']

  #analyze
  pg = PcfGcp()
  client = pg.getLanguage()
  document = client.document_from_text(text)
  annotations = document.annotate_text(include_sentiment=True,
                                       include_syntax=False,
                                       include_entities=False)

  score = annotations.sentiment.score
  magnitude = annotations.sentiment.magnitude

  #add sentiment, score and magnitude to obj
  obj['sentiment'] = { 'score': score, 'magnitude': magnitude }
  logMsg(json.dumps(obj))
  return json.dumps(obj)

@app.route('/status')
def test():
    return "STATUS_OK"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=port, threaded=True, debug=False)
