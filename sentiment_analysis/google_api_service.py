import os

from flask import Flask, request, jsonify 
import google_api.helper_functions as helper_functions
from google_api.helper_functions import DEFAULT_LIMIT

app = Flask(__name__)

resizer_url = "http://image-resizing-service.apps.pcfongcp.com/"

def logMsg(args):
    print "[Instance: %s] %s" % (str(os.getenv("CF_INSTANCE_INDEX", 0)), args)

@app.route("/")
def main():
    return "Hello World!"

# Handle JSON
# Simulate adding a "sentiment" attribute
@app.route('/template', methods = ['POST', 'GET'])
def jsonHandler():
  obj = request.get_json(force=True)
  obj['sentiment'] = { 'score': 0.4, 'magnitude': 0.9 }
  logMsg(json.dumps(obj))

  return jsonify(obj)

@app.route('/status')
def test():
    return "STATUS_OK"

@app.route('/api', methods=['POST','OPTIONS'])
#@helper_functions.crossdomain(origin='*')
def handle_google_api_request():
    req = request.get_json(force=True)
    return jsonify(req)

@app.route('/nlp', methods=['POST','OPTIONS'])
#@helper_functions.crossdomain(origin='*')
def handle_nlp_request():
    req = request.get_json(force=True)
    results = helper_functions.get_text_entities(req['content'])

    req['nlp'] = results;
    return jsonify(req)

@app.route('/vision/ocr', methods=['POST','OPTIONS'])
#@helper_functions.crossdomain(origin='*')
def handle_vision_text_request():
    
    req = request.get_json(force=False, silent=True)
    
    text_list = []
    if req is not None:
        if "http" not in req['url']:
            return jsonify( { "Unsupported url parameter" : req['url']  } )

        # URL in -> Bytes out
        resizer_url = "http://image-resizing-service.apps.pcfongcp.com/"
        resized_bytes = helper_functions.resize_image_using_service(resizer_url, req['url'])

        text_list = helper_functions.get_image_text(resized_bytes)

        req['ocr'] = text_list
    else:
        text_list = helper_functions.get_image_text(request.data)

        req = { 'ocr' : text_list }

    return jsonify(req)

@app.route('/vision/logos', methods=['POST','OPTIONS'])
#@helper_functions.crossdomain(origin='*')
def handle_vision_logo_request():

    # TODO - call to image resizer, then push to Google bucket
    req = request.get_json(force=True, silent=True)
    logo_list = []
    if req is not None:
        if "http" not in req['url']:
            return jsonify( { "Unsupported url parameter" : req['url']  } )

        # URL in -> Bytes out
        resized_bytes = helper_functions.resize_image_using_service(resizer_url, req['url'])

        logo_list = helper_functions.get_image_logos(resized_bytes)

        req['logos'] = logo_list
    else:
        logo_list = helper_functions.get_image_logos(request.data)

        req = { 'logos' : logo_list }

    return jsonify(req)

@app.route('/storage/<bucket>/<blob>', methods=['GET', 'POST', 'OPTIONS'])
#@helper_functions.crossdomain(origin='*')
def handle_storage_request(bucket, blob):
    if request.method == 'POST':
        new_blob = helper_functions.create_blob(request.data, blob, bucket, 
                                                request.headers['content-type'], 
                                                create_bucket=True)
        return jsonify({
            'created': '{0} in bucket {1}'.format(blob, bucket)
        })
    elif request.method == 'GET':
        requested_blob = helper_functions.get_blob(bucket, blob)
        return jsonify(requested_blob)
    else:
        return jsonify({
            'reponse' : "{0} method not supported".format(request.method)
        })

if __name__ == "__main__":
    # running locally
    if os.environ.get('VCAP_SERVICES') is None: 
        PORT = 5000
        DEBUG = True
        app.run(debug=DEBUG)
    # running on CF
    else:                                       
        PORT = int(os.getenv("PORT"))
        DEBUG = False
        app.run(host='0.0.0.0', port=PORT, debug=DEBUG)

