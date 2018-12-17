import requests
import simplejson
from lxml import html


def main():
    session = requests.session()

    print("run script")
	
    for i in range(1,10):
        print("run script")
	
	
    for i in range(1,10):
        print("run script")
        response = session.get("https://etherscan.io/contractsVerified/" + str(i) + "?ps=100")
        tree = html.fromstring(response.text)
        print(len(tree.xpath("/html/body/div[1]/div[4]/div[3]/div/div/div/table/tbody/tr/td/a")))
        for address in tree.xpath("/html/body/div[1]/div[4]/div[3]/div/div/div/table/tbody/tr/td/a"):
            response = session.get("https://api.etherscan.io/api?module=contract&action=getsourcecode&address="+address.text)
            jsonresponse = simplejson.loads(response.content)
            sourceCode = jsonresponse["result"][0]["SourceCode"]

            response = session.post("http://localhost:8080/api/solidityFiles", data=str(sourceCode).encode("utf-8"), )
            if response.status_code == 200:
                print("ok")
            else:
                print("failed")



if __name__ == "__main__":
    main()
