import json
import http.client
import uuid

class Services:

    def __init__(self, mock_type):
        self.api = None
        self.isFormatted = False
        if mock_type == "car_registration_formatted":
            self.api = "/api/187dcdc0?count=50&key=a9d7e570"
            self.isFormatted = True
        elif mock_type == "car_registration":
            self.api = "/api/187dcdc0?count=50&key=a9d7e570"
            self.isFormatted = False

    def get_data_from_provider(self):

        conn = http.client.HTTPSConnection("api.mockaroo.com")
        payload = ''
        headers = {
            'Content-Type': 'application/json'
        }
        conn.request("GET", self.api, payload, headers)
        res = conn.getresponse()
        data = res.read()
        print(data.decode("utf-8"))
        return data.decode("utf-8")

    def format_payload(self, data):
        title = "No title will be send"
        if(self.isFormatted):
            title = "testData-" + str(uuid.uuid4())
            payload = {
                "micromorphMetaData": {"name": title, "labels": ["cars", "test"]},
                "fileContent": data,
            }
            payload = json.dumps(payload)
        else:
            payload = json.dumps(data)

        print("Payload created, tittle name = " + title)
        return payload


    def send_data_to_micromorph(self, payload):

        conn = http.client.HTTPConnection("localhost", 8001)
        payload = payload
        headers = {
            'Content-Type': 'application/json'
        }
        conn.request("POST", "/micromorph/data", payload, headers)
        res = conn.getresponse()
        data = res.read()
        print(data.decode("utf-8"))



