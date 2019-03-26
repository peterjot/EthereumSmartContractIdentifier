import solc
import os
import sys
from pathlib import Path
import re

PATH_HOME = str(Path.home())

COMPILER_BIN = {
    "0.4.22" : PATH_HOME + "/.py-solc/solc-v0.4.22/bin/solc", 
    "0.4.23" : PATH_HOME + "/.py-solc/solc-v0.4.23/bin/solc",
    "0.4.24" : PATH_HOME + "/.py-solc/solc-v0.4.24/bin/solc",
    "0.4.25" : PATH_HOME + "/.py-solc/solc-v0.4.25/bin/solc"
}

CONTRACT_BLACKLIST = ["SafeMath"]

def main():
    if len(sys.argv) != 2:
        sys.stderr.write("usage: " + sys.argv[0] + " dirpath\n")
        sys.stderr.write("dirpath - path with solidity files")
        sys.stderr.write("every solidity file should endswith with .sol")
        exit(1)

    folder_path = sys.argv[1]
    create_folder(folder_path + "/bin")

    for file_name in os.listdir(folder_path):
        if ".sol" not in file_name:
            continue
        log("\n\n\n")

        log("Compiling file: " + file_name)

        file_path = folder_path + '/'+file_name
        file_solc_version,_,_ = file_name.split("--")

        if file_solc_version in COMPILER_BIN.keys():
            current_version = file_solc_version
        else:
            sys.stderr.write("File has no supported compiler version: " + file_solc_version)
            exit(2)

        solc_path = COMPILER_BIN[current_version]

        log("Compiler version: " + file_solc_version)
        log("File path: " + file_path)
        log("Set env SOLC_BINARY path to " + solc_path)

        os.environ["SOLC_BINARY"] = solc_path

        contract_name = None
        log("Opening file: "+file_path)
        with open (file_path) as ff:
            for line in ff:
                match = re.match(r"\s*(\bcontract\b|\blibrary\b)\s*([a-zA-Z_$][a-zA-Z_$0-9]*).*",line)

                if match and match.group(2) not in CONTRACT_BLACKLIST:
                    contract_name = match.group(2)

        if contract_name is None:
            raise Exception("This file does not have contracts")
        log("Contract name: "+contract_name)

        bin = solc.compile_files([file_path])["{}:{}".format(file_path, contract_name)]["bin"]

        bin_file_path = "{}/bin/{}--{}--.bin".format(folder_path, file_name[:-6], contract_name)

        log("Saving bin to " + bin_file_path)
        with open (bin_file_path, "w") as file:
           file.write(bin)

def log(msg):
    print("- [LOGGING] - {}".format(msg))

def create_folder(path):
    if not os.path.exists(path):
        os.makedirs(path)

if __name__ == "__main__":
    main()