import csv
import os
import sys

import requests

MATCHING_THRESHOLD = 0.80
TEST_LOGIN = 'myadmin321'
TEST_PASSWORD = 'myadmin321'


def main():
    if len(sys.argv) != 3:
        sys.stderr.write("usage: " + sys.argv[0] + " dirpath result_file_name\n")
        sys.stderr.write("dirpath - path with solidity files\n")
        sys.stderr.write("result_file_name - file name for csv file\n")
        sys.stderr.write("every solidity file should endswith with .sol\n")
        exit(1)

    session = requests.session()
    folder_path = sys.argv[1]
    result_file_name = sys.argv[2]

    results = {}
    bad_file_binaries = []
    for file_name in os.listdir(folder_path):
        if ".sol" not in file_name:
            continue

        with open(folder_path + "/" + file_name, "r", encoding="utf-8") as ff:
            source_code = ff.read()

        response = session.post("http://localhost:8080/api/solidityFiles", data=str(source_code).encode('utf-8'),
                                auth=(TEST_LOGIN, TEST_PASSWORD))
        if response.status_code == 200:
            upload_file_hash = response.json()['sourceCodeHash']

            folder_bin_path = folder_path + "/bin"
            file_bin_name = None
            for f_ame in os.listdir(folder_bin_path):
                if file_name[:-4] in f_ame:
                    file_bin_name = f_ame

            if file_bin_name is None:
                sys.stderr.write("cannot find compiled file for specified solidity file\n"
                                 "in folder: " + folder_bin_path)
                exit(1)

            with open(folder_bin_path + "/" + file_bin_name) as f:
                bytecode = f.read()

            payload = {'bytecode': bytecode, 'allFiles': 'true'}
            byte_response = session.post("http://localhost:8080/api/bytecode", data=payload,
                                         auth=('myadmin321', 'myadmin321'))
            bytecode_response_status_code = byte_response.status_code
            print("Post status code: " + str(bytecode_response_status_code))
            print("File bin name: " + file_bin_name)

            if bytecode_response_status_code == 404:
                results[file_bin_name] = {'percents': 0, 'passed': False, 'matched_impls': 0}

            elif bytecode_response_status_code == 200:
                list_of_impl = byte_response.json()
                list_of_impl.sort(key=lambda x: float(x['valueOfMatch']), reverse=True)

                max_value_of_match = float(list_of_impl[0]['valueOfMatch'])

                matched_implementations = 0
                uploaded_impl = None
                for row in list_of_impl:
                    if float(row['valueOfMatch']) > MATCHING_THRESHOLD:
                        matched_implementations = matched_implementations + 1

                    if upload_file_hash == row['fileHash']:
                        uploaded_impl = row

                if uploaded_impl:
                    result_value = float(uploaded_impl['valueOfMatch'])
                    results[file_bin_name] = {'percents': result_value, 'passed': result_value == max_value_of_match,
                                              'matched_impls': matched_implementations}
                else:
                    results[file_bin_name] = {'percents': 0, 'passed': False, 'matched_impls': matched_implementations}

            else:
                print("ERROR: Contract have wrong bytecode, please check this contract!")
                print("ERROR: Contract binary file name: " + file_bin_name)
                bad_file_binaries.append(file_bin_name)

        else:
            print("Failed uploading")

    print("Result: " + str(results))
    print("Bad contract bins: " + str(bad_file_binaries))
    print("Test length: " + str(len(results)))

    passed_count = 0
    all_percents = 0.0
    passed_percents = 0.0
    all_matched_impls = 0.0

    with open(result_file_name, mode='w', newline='') as f:
        writer = csv.writer(f, delimiter=',', quotechar='"', dialect='excel')
        writer.writerow(['filename', 'percents', 'passed', 'matched_impls'])

        for key, value in results.items():
            passed = value['passed']
            percents = value["percents"] * 100
            matched_impls = value['matched_impls']

            writer.writerow([key, percents, passed, matched_impls])

            all_percents = all_percents + percents
            all_matched_impls = all_matched_impls + matched_impls
            if passed:
                passed_count = passed_count + 1
                passed_percents = passed_percents + percents

    print("Passed: " + str(passed_count) + " Total: " + str(len(results)))
    print("Passed avg percent: " + str(passed_percents / passed_count))
    print("Total avg percent: " + str(all_percents / len(results)))
    print("Total avg percenr of matched impls: " + str(passed_count / all_matched_impls))
    print("All matched impls: " + str(all_matched_impls))


if __name__ == "__main__":
    main()
