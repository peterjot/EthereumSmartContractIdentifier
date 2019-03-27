import requests
import sys
import os
import csv

def main():

    if len(sys.argv) != 3:
        sys.stderr.write("usage: " + sys.argv[0] + " dirpath result_file_name\n")
        sys.stderr.write("dirpath - path with solidity files")
        sys.stderr.write("result_file_name - file name for csv file")
        sys.stderr.write("every solidity file should endswith with .sol")
        exit(1)

    session = requests.session()
    folder_path = sys.argv[1]
    result_file_name = sys.argv[2]

    print("run script")
    results = {}
    bad_file_binaries = []
    for file_name in os.listdir(folder_path):
        if ".sol" not in file_name:
            continue

        with open (folder_path+"/"+file_name, "r", encoding = "utf-8") as ff:
            source_code = ff.read()

        response = session.post("http://localhost:8080/api/solidityFiles", data=str(source_code).encode('utf-8'), auth=('myadmin321', 'myadmin321'))
        if response.status_code == 200:
            upload_file_hash = response.json()['sourceCodeHash']

            for f_ame in os.listdir(folder_path+"/bin"):
                if file_name[:-4] in f_ame:
                    file_bin_name = f_ame

            with open (folder_path+"/bin/"+file_bin_name) as f:
                bytecode = f.read()

            payload = {'bytecode': bytecode, 'allFiles': 'true'}
            byte_response = session.post("http://localhost:8080/api/bytecode", data=payload, auth=('myadmin321', 'myadmin321'))
            b_code = byte_response.status_code
            print("Post status code: "+str(b_code))
            print("File bin name: "+file_bin_name)

            if(b_code == 404):
                results[file_bin_name] = {'percents':0, 'passed':False}
            elif(b_code == 200):
                list_of_impl = byte_response.json()
                list_of_impl.sort(key=lambda x: float(x['valueOfMatch']), reverse=True)

                max_value_of_match = float(list_of_impl[0]['valueOfMatch'])

                for row in list_of_impl:
                    if upload_file_hash == row['fileHash']:
                        uploaded_impl = row
                        break

                result_value = float(uploaded_impl['valueOfMatch'])
                passed = result_value == max_value_of_match
                results[file_bin_name] = {'percents':result_value, 'passed':passed}
            else:
                print("ERROR: Contract have wrong bytecode, please check this contract!")
                print("ERROR: Contract binary file name: "+file_bin_name)
                bad_file_binaries.append(file_bin_name)

        else:
            print("Failed uploading")


    print("Result: "+str(results))
    print("Bad contract bins: "+str(bad_file_binaries))

    print("Test length: " + str(len(results)))
    passedCount = 0
    allPercents = 0.0
    passedPercents = 0.0
    with open(result_file_name, mode='w', newline='') as f:
        writer = csv.writer(f, delimiter=',', quotechar='"', dialect='excel')
        writer.writerow(['filename', 'percents', 'passed'])

        for key, value in results.items():
            passed = value['passed']
            percents = value["percents"] *100

            writer.writerow([key, percents, passed])

            allPercents = allPercents + percents
            if(passed):
                passedCount = passedCount + 1
                passedPercents = passedPercents + percents
        
    print("Passed: " + str(passedCount) + " Total: " + str(len(results)))
    print("Passed avg percent: "+ str(passedPercents/passedCount))
    print("Total avg percent: " + str(allPercents / len(results)))


if __name__ == "__main__":
    main()
