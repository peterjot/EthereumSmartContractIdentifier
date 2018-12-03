import requests
from lxml import html
import simplejson


def main():
    session = requests.session()


    for i in range(1,10):
        response = session.get("https://etherscan.io/contractsVerified/" + str(i) + "?ps=100")
        tree = html.fromstring(response.text)
        for address in tree.xpath("/html/body/div[1]/div[5]/div[3]/div/div/div/table/tbody/tr/td/a"):
            response = session.get("https://api.etherscan.io/api?module=contract&action=getsourcecode&address="+address.text)

            jsonresponse = simplejson.loads(response.content)
            sourceCode = jsonresponse["result"][0]["SourceCode"]

            response = session.post("http://localhost:8080/api/solidity/sourceCodes", json=sourceCode)
            if response.status_code == 200:
                print("ok")



if __name__ == "__main__":
    main()
