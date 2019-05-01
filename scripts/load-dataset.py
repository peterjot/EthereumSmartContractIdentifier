import os
import requests
import sys


def main():
    if len(sys.argv) != 2:
        sys.stderr.write("usage: " + sys.argv[0] + " dirpath\n")
        sys.stderr.write("dirpath - path with solidity files\n")
        sys.stderr.write("every solidity file should endswith with .sol\n")
        exit(1)

    session = requests.session()
    folder_path = sys.argv[1]

    files_name = [name for name in os.listdir(folder_path) if os.path.isfile(folder_path+"/"+name) and ".sol" in name]
    print("Scaned sol files: " + str(len(files_name)))

    for file_name in files_name:
        with open(folder_path + "/" + file_name, "r", encoding="utf-8") as ff:
            source_code = ff.read()

        response = session.post("http://localhost:8080/api/solidityFiles", data=str(source_code).encode('utf-8'),
                                auth=('admin', 'admin'))
        if response.status_code == 200:
            print("Uploaded file: {}".format(file_name))
        else:
            print("Failed uploading file: {}".format(file_name))


if __name__ == "__main__":
    main()
