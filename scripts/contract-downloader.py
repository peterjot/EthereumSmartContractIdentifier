import requests
import simplejson
from lxml import html


def main():
    session = requests.session()

    for i in range(1, 10):
        print("run script")
        response = session.get("https://etherscan.io/contractsVerified/" + str(i) + "?ps=100")
        tree = html.fromstring(response.text)

        for row in tree.xpath("//*[@id=\"transfers\"]/div[2]/table/tbody/tr"):
            contract_id = row.xpath("td[1]/a")[0].text.strip()
            compiler_version = row.xpath("td[4]/text()")[0].strip()

            response = session.get(
                "https://api.etherscan.io/api?module=contract&action=getsourcecode&address=" + contract_id)

            json_response = simplejson.loads(response.content)

            source_code = json_response["result"][0]["SourceCode"]
            contract_name = json_response["result"][0]["ContractName"]

            with open('scraped_contracts/' + compiler_version + '--' + contract_id + '--' + contract_name + '--.sol',
                      "w", encoding="utf-8") as file:
                file.write(source_code)

            print("Contract id: " + contract_id)
            print("Compiler version: " + compiler_version)


if __name__ == "__main__":
    main()
